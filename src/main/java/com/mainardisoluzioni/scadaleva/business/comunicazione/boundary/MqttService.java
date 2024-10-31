/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mainardisoluzioni.scadaleva.business.comunicazione.boundary;

import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;
import com.hivemq.client.mqtt.mqtt5.message.subscribe.Mqtt5RetainHandling;
import com.hivemq.client.mqtt.mqtt5.message.subscribe.suback.Mqtt5SubAck;
import com.mainardisoluzioni.scadaleva.business.comunicazione.entity.MqttDevice;
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
@Startup
@Singleton
public class MqttService {
    private final String BROKER_IP_ADDRESS = "192.168.2.125";
    private final String TELEMETRY_TOPIC = "v1/devices/#/telemetry";
    
    private Mqtt5AsyncClient client;
    private Map<Mqtt5SubAck, String> mqtt5SubAcks;
    
    @Inject
    MqttDeviceService mqttDeviceService;
    
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
