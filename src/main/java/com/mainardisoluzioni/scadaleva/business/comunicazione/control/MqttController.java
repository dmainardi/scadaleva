/*
 * Copyright (C) 2024 adminavvimpa
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mainardisoluzioni.scadaleva.business.comunicazione.control;

import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;
import com.hivemq.client.mqtt.mqtt5.message.subscribe.Mqtt5RetainHandling;
import com.hivemq.client.mqtt.mqtt5.message.subscribe.suback.Mqtt5SubAck;
import com.mainardisoluzioni.scadaleva.business.comunicazione.boundary.MqttDeviceService;
import com.mainardisoluzioni.scadaleva.business.comunicazione.entity.MqttDevice;
import com.mainardisoluzioni.scadaleva.business.energia.boundary.EventoEnergiaService;
import com.mainardisoluzioni.scadaleva.business.energia.entity.PayloadTelemetryTraceAndFollow;
import com.mainardisoluzioni.scadaleva.business.produzione.boundary.EventoProduzioneService;
import com.mainardisoluzioni.scadaleva.business.produzione.control.EventoProduzioneController;
import com.mainardisoluzioni.scadaleva.business.reparto.entity.Macchina;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adminavvimpa
 */
@Startup
@Singleton
public class MqttController {
    private static final Logger LOGGER = Logger.getLogger(MqttController.class.getName());
    
    private final String BROKER_IP_ADDRESS = "192.168.220.125";
    
    private Mqtt5AsyncClient client;
    private Map<Mqtt5SubAck, String> mqtt5SubAcks;
    
    @Inject
    MqttDeviceService mqttDeviceService;
    
    @Inject
    EventoEnergiaService eventoEnergiaService;
    
    @Inject
    EventoProduzioneService eventoProduzioneService;
    
    @PostConstruct
    public void init() {
        client = Mqtt5Client.builder()
                .identifier(UUID.randomUUID().toString())
                .serverHost(BROKER_IP_ADDRESS)
                .buildAsync();
        
        mqtt5SubAcks = new HashMap<>();
        client.connect()
                .thenAccept(connAck -> System.out.println("Client connected with message: " + connAck));
        sottoscrivitiEAspetta();
    }
    
    public void sottoscrivitiEAspetta() {
        List<MqttDevice> mqttDevices = mqttDeviceService.list();
        if (mqttDevices != null)
            for (MqttDevice mqttDevice : mqttDevices) {
                mqtt5SubAcks.put(
                        client.subscribeWith()
                                .topicFilter(mqttDevice.getTopic())
                                .noLocal(true)                                      // we do not want to receive our own message
                                .retainHandling(Mqtt5RetainHandling.DO_NOT_SEND)    // do not send retained messages
                                .retainAsPublished(true)                            // keep the retained flag as it was published
                                .callback(
                                        publish -> estraiDati(mqttDevice.getMacchina(), new String(publish.getPayloadAsBytes()))
                                )
                                .send()
                                .join(),
                        mqttDevice.getTopic()
                );
            }
    }
    
    private void estraiDati(Macchina macchina, String payload) {
        try {
            Jsonb jsonb = JsonbBuilder.create();
            PayloadTelemetryTraceAndFollow payloadTaF = jsonb.fromJson(payload, PayloadTelemetryTraceAndFollow.class);
            LOGGER.log(
                    Level.FINE,
                    "MqttController::estraiDati - macchina: {0}, ts: {1}, consumo: {2}",
                    new Object[]{
                        macchina.getCodice(),
                        payloadTaF.getTimestamp(),
                        DecimalFormat.getNumberInstance(Locale.ITALY).format(payloadTaF.getContenuto().getConsumoWh())
                    }
            );
            LocalDateTime timestamp = LocalDateTime.now();
            eventoEnergiaService.createAndSave(
                    macchina,
                    //payloadTaF.getTimestamp(), // non usiamo il timestamp del T&F perché non è preciso
                    Long.toString(timestamp.toInstant(ZoneOffset.UTC).toEpochMilli()),
                    payloadTaF.getContenuto().getConsumoWh()
            );
            Integer input1 = payloadTaF.getContenuto().getInput1();
            Integer input2 = payloadTaF.getContenuto().getInput2();
            Integer input3 = payloadTaF.getContenuto().getInput3();
            Integer input4 = payloadTaF.getContenuto().getInput4();
            int totalInput = 0;
            totalInput +=
                    (input1 != null ? input1 : 0)
                    +
                    (input2 != null ? input2 : 0)
                    +
                    (input3 != null ? input3 : 0)
                    +
                    (input4 != null ? input4 : 0);
            if (totalInput > 0)
                eventoProduzioneService.save(EventoProduzioneController.createEventoProduzione(macchina, timestamp.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime(), totalInput, null));
        } catch (JsonbException e) {
            LOGGER.log(Level.WARNING, "MqttController:estraiDati - Errore: {0}", new Object[]{e.getLocalizedMessage()});
        }
    }
    
    @PreDestroy
    public void destroy() {
        if (!mqtt5SubAcks.isEmpty()) {
            for (String topic : mqtt5SubAcks.values())
                client.unsubscribeWith().topicFilter(topic).send();
            LOGGER.log(Level.INFO, "MqttController::destroy - mi sono tolto dalla sottoscrizione");
        }
        client.disconnect()
                .thenAccept(v -> System.out.println("Client disconnected"));
    }
}
