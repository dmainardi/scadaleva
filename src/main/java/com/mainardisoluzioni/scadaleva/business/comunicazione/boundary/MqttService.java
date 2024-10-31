/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mainardisoluzioni.scadaleva.business.comunicazione.boundary;

import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;
import com.hivemq.client.mqtt.mqtt5.message.subscribe.Mqtt5RetainHandling;
import com.hivemq.client.mqtt.mqtt5.message.subscribe.suback.Mqtt5SubAck;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.ejb.Startup;
import jakarta.ejb.Singleton;
import java.util.UUID;

/**
 *
 * @author adminavvimpa
 */
@Startup
@Singleton
public class MqttService {
    private final String BROKER_IP_ADDRESS = "192.168.2.125";
    private final String TELEMETRY_TOPIC = "v1/devices/me/telemetry";
    
    private Mqtt5AsyncClient client;
    private Mqtt5SubAck mqtt5SubAck = null;
    
    @PostConstruct
    public void init() {
        client = Mqtt5Client.builder()
                .identifier(UUID.randomUUID().toString())
                .serverHost(BROKER_IP_ADDRESS)
                .buildAsync();
        
        client.connect()
                .thenAccept(connAck -> System.out.println("Client connected with message: " + connAck));
        sottoscrivitiEAspetta();
    }
    
    public void sottoscrivitiEAspetta() {
        mqtt5SubAck = client.subscribeWith()
                .topicFilter(TELEMETRY_TOPIC)
                .noLocal(true)                                      // we do not want to receive our own message
                .retainHandling(Mqtt5RetainHandling.DO_NOT_SEND)    // do not send retained messages
                .retainAsPublished(true)                            // keep the retained flag as it was published
                .callback(
                        publish -> maina(new String(publish.getPayloadAsBytes()))
                )
                .send()
                .join();
    }
    
    private void maina(String payload) {
        System.out.println("Message: " + payload);
    }
    
    @PreDestroy
    public void destroy() {
        if (mqtt5SubAck != null) {
            client.unsubscribeWith().topicFilter(TELEMETRY_TOPIC).send();
            System.out.println("Mi sono tolto dalla sottoscrizione");
        }
        client.disconnect()
                .thenAccept(v -> System.out.println("Client disconnected"));
    }
}
