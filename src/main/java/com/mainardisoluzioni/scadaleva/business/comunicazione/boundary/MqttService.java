/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mainardisoluzioni.scadaleva.business.comunicazione.boundary;

import static com.hivemq.client.internal.mqtt.util.MqttChecks.topic;
import com.hivemq.client.mqtt.MqttGlobalPublishFilter;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient.Mqtt5Publishes;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;
import com.hivemq.client.mqtt.mqtt5.message.connect.connack.Mqtt5ConnAck;
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5Publish;
import com.hivemq.client.mqtt.mqtt5.message.subscribe.suback.Mqtt5SubAck;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.ejb.Asynchronous;
import jakarta.ejb.Startup;
import jakarta.ejb.Singleton;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adminavvimpa
 */
@Startup
@Singleton
public class MqttService {
    private final String BROKER_IP_ADDRESS = "192.168.2.125";
    private final String TELEMETRY_TOPIC = "v1/devices/me/telemetry/#";
    
    private Mqtt5BlockingClient client;
    
    @PostConstruct
    public void init() {
        client = Mqtt5Client.builder()
                .identifier(UUID.randomUUID().toString())
                .serverHost(BROKER_IP_ADDRESS)
                .buildBlocking();
        
        Mqtt5ConnAck connect = client.connect();
        System.out.println("Client connected with message: " + connect.toString());
        sottoscrivitiEAspetta();
    }
    
    public void sottoscrivitiEAspetta() {
        Mqtt5SubAck mqtt5SubAck = null;
        try (final Mqtt5Publishes publishes = client.publishes(MqttGlobalPublishFilter.ALL)) {

            mqtt5SubAck = client.subscribeWith().topicFilter(TELEMETRY_TOPIC).qos(MqttQos.AT_LEAST_ONCE).send();

            System.out.println("Aspetto un minuto per vedere se arrivano messaggi");
            Optional<Mqtt5Publish> messaggioOptional = publishes.receive(1, TimeUnit.MINUTES);
            if (messaggioOptional.isPresent())
                System.out.println("Il valore del payload ricevuto è: " + new String(messaggioOptional.get().getPayloadAsBytes()));

        } catch (InterruptedException ex) {
            System.err.println(ex.getLocalizedMessage());
        } finally {
            if (mqtt5SubAck != null) {
                client.unsubscribeWith().topicFilter(TELEMETRY_TOPIC).send();
                System.out.println("Mi sono tolto dalla sottoscrizione");
            }
        }
    }
    
    @PreDestroy
    public void destroy() {
        client.disconnect();
        System.out.println("Client disconnected");
    }
}
