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
import com.mainardisoluzioni.scadaleva.business.reparto.entity.Macchina;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.ejb.Startup;
import jakarta.ejb.Singleton;
import jakarta.inject.Inject;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 *
 * @author adminavvimpa
 */
//@Startup
@Singleton
public class MqttController {
    private final String BROKER_IP_ADDRESS = "192.168.2.125";
    
    private Mqtt5AsyncClient client;
    private Map<Mqtt5SubAck, String> mqtt5SubAcks;
    
    @Inject
    MqttDeviceService mqttDeviceService;
    
    @Inject
    EventoEnergiaService eventoEnergiaService;
    
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
            System.out.println("macchina: " + macchina.getCodice());
            System.out.println("ts: " + payloadTaF.getTimestamp());
            System.out.println("consumo: " + payloadTaF.getContenuto().getConsumoWh());
            eventoEnergiaService.createAndSave(macchina, payloadTaF.getTimestamp(), payloadTaF.getContenuto().getConsumoWh());
        } catch (JsonbException e) {
            System.err.println(e.getLocalizedMessage());
        }
    }
    
    @PreDestroy
    public void destroy() {
        if (!mqtt5SubAcks.isEmpty()) {
            for (String topic : mqtt5SubAcks.values())
                client.unsubscribeWith().topicFilter(topic).send();
            System.out.println("Mi sono tolto dalla sottoscrizione");
        }
        client.disconnect()
                .thenAccept(v -> System.out.println("Client disconnected"));
    }
}
