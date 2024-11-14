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

import com.mainardisoluzioni.scadaleva.business.comunicazione.boundary.OpcuaDeviceService;
import com.mainardisoluzioni.scadaleva.business.comunicazione.entity.OpcuaDevice;
import com.mainardisoluzioni.scadaleva.business.comunicazione.entity.OpcuaNode;
import com.mainardisoluzioni.scadaleva.business.produzione.boundary.EventoProduzioneService;
import com.mainardisoluzioni.scadaleva.business.produzione.entity.EventoProduzione;
import com.mainardisoluzioni.scadaleva.business.produzione.entity.ParametroMacchinaProduzione;
import com.mainardisoluzioni.scadaleva.business.reparto.entity.Macchina;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.ejb.Schedule;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotNull;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.api.config.OpcUaClientConfigBuilder;
import org.eclipse.milo.opcua.sdk.client.api.subscriptions.UaSubscription;
import org.eclipse.milo.opcua.sdk.client.api.subscriptions.UaSubscriptionManager;
import org.eclipse.milo.opcua.sdk.client.subscriptions.ManagedDataItem;
import org.eclipse.milo.opcua.sdk.client.subscriptions.ManagedSubscription;
import org.eclipse.milo.opcua.stack.client.DiscoveryClient;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.util.EndpointUtil;

/**
 *
 * @author adminavvimpa
 */
@Startup
@Singleton
public class OpcuaController {
    
    @Inject
    OpcuaDeviceService opcuaDeviceService;
    
    @Inject
    EventoProduzioneService eventoProduzioneService;
    
    private Map<OpcUaClient, OpcuaDevice> clients;
    private Map<Macchina, Integer> lastCycleCounters;   // ultimo valore del contapezzi
    
    private List<ManagedDataItem> dataItems;
    
    @PostConstruct
    public void init() {
        createAndConnectToClientsFromDatabase();
    }
    
    private void createAndConnectToClientsFromDatabase() {
        clients = new HashMap<>();
        lastCycleCounters = new HashMap<>();
        List<OpcuaDevice> opcuaDevices = opcuaDeviceService.list();
        if (opcuaDevices != null)
            createAndConnectToClients(opcuaDevices);
    }
    
    private void createAndConnectToClients(@NotNull List<OpcuaDevice> opcuaDevices) {
        for (OpcuaDevice opcuaDevice : opcuaDevices) {
            try {
                List<EndpointDescription> endpoints = DiscoveryClient.getEndpoints("opc.tcp://" + opcuaDevice.getIpAddress() +":" + opcuaDevice.getTcpPort()).get();
                EndpointDescription configPoint = EndpointUtil.updateUrl(endpoints.get(0), opcuaDevice.getIpAddress(), Integer.parseInt(opcuaDevice.getTcpPort()));

                OpcUaClientConfigBuilder cfg = new OpcUaClientConfigBuilder();
                cfg.setEndpoint(configPoint)
                        .setApplicationName(LocalizedText.english("scadaleva opc-ua client"))
                        .setApplicationUri("scadaleva:levaspa:com")
                        .setRequestTimeout(Unsigned.uint(5000));

                OpcUaClient client = OpcUaClient.create(cfg.build());
                client.connect().get(5, TimeUnit.SECONDS);
                listen(opcuaDevice, client);
                clients.put(client, opcuaDevice);
            } catch (InterruptedException | ExecutionException | NumberFormatException | UaException | TimeoutException ex) {
                System.err.println("OpcuaController:init - Errore: " + ex.getLocalizedMessage());
            }
        }
    }
    
    private void listen(@NotNull OpcuaDevice opcuaDevice, @NotNull OpcUaClient client) throws UaException {
        Optional<OpcuaNode> optionalOpcuaNode = opcuaDevice.getOpcuaNodes().stream().filter(n -> n.getCategoriaVariabileProduzione() == CategoriaVariabileProduzione.CONTAPEZZI).findAny();
        if (optionalOpcuaNode.isPresent()) {
            OpcuaNode opcuaNode = optionalOpcuaNode.get();
            try {
                lastCycleCounters.put(
                        opcuaDevice.getMacchina(),
                        Integer.valueOf(String.valueOf(client.getAddressSpace().getVariableNode(new NodeId(opcuaNode.getNameSpaceIndex(), opcuaNode.getNodeIdentifier())).readValue().getValue().getValue()))
                );
                dataItems = new ArrayList<>();
                client.getSubscriptionManager().addSubscriptionListener(
                        new UaSubscriptionManager.SubscriptionListener() {
                            @Override
                            public void onSubscriptionTransferFailed(UaSubscription subscription, StatusCode statusCode) {
                                System.err.println("OpcuaController:onSubscriptionTransferFailed - Errore: " + statusCode);
                                try {
                                    createSubscription(client, opcuaNode);
                                } catch (UaException e) {
                                    System.err.println("OpcuaController:onSubscriptionTransferFailed - Errore: " + e);
                                }
                            }

                        }
                );
                createSubscription(client, opcuaNode);
            } catch (NumberFormatException e) {
                System.err.println("OpcuaController:listen - Errore: " + e.getLocalizedMessage());
            }
        }
    }
    
    /**
     * Crea la sottoscrizione in modo che possa essere richiamata in caso di riconnessione.
     * Vedi https://gist.github.com/kevinherron/f2dfc6aaf62360de35dd595fc5930654 ed i commenti di https://stackoverflow.com/a/61774785
     * @param client
     * @param opcuaNode
     * @throws UaException 
     */
    private void createSubscription(@NotNull OpcUaClient client, @NotNull OpcuaNode opcuaNode) throws UaException {
        ManagedSubscription subscription = ManagedSubscription.create(client);
        subscription.addDataChangeListener((List<ManagedDataItem> items, List<DataValue> values) -> {
            for (int i = 0; i < items.size(); i++) {
                ManagedDataItem item = items.get(i);
                DataValue value = values.get(i);
                OpcuaDevice opcuaDeviceTemp = clients.get(item.getClient());
                Macchina macchina = opcuaDeviceTemp.getMacchina();

                String cycleCounterStr = String.valueOf(value.getValue().getValue());
                Integer currentCycleCounter = Integer.valueOf(cycleCounterStr);
                EventoProduzione eventoProduzione = new EventoProduzione();
                eventoProduzione.setMacchina(opcuaDeviceTemp.getMacchina());
                eventoProduzione.setDataProduzione(LocalDate.now(Clock.systemUTC()));
                eventoProduzione.setOraProduzione(LocalTime.now(Clock.systemUTC()));
                Integer previousCycleCounter = lastCycleCounters.getOrDefault(macchina, 0);
                Integer quantitaProdotta;
                if (currentCycleCounter.compareTo(previousCycleCounter) < 0)
                    quantitaProdotta = currentCycleCounter;
                else
                    quantitaProdotta = currentCycleCounter - previousCycleCounter;
                eventoProduzione.setQuantita(
                        quantitaProdotta
                );
                lastCycleCounters.put(
                        macchina,
                        currentCycleCounter
                );

                for (OpcuaNode opcuaNodeTemp : opcuaDeviceTemp.getOpcuaNodes()) {
                    try {
                        if (opcuaNodeTemp.getCategoriaVariabileProduzione() != CategoriaVariabileProduzione.CONTAPEZZI) {
                            String valore = String.valueOf(client.getAddressSpace().getVariableNode(new NodeId(opcuaNodeTemp.getNameSpaceIndex(), opcuaNodeTemp.getNodeIdentifier())).readValue().getValue().getValue());
                            if (!valore.isBlank())
                                eventoProduzione.addParametroMacchinaProduzione(
                                        new ParametroMacchinaProduzione(
                                                opcuaNodeTemp,
                                                valore
                                        )
                                );
                        }
                        else
                            eventoProduzione.addParametroMacchinaProduzione(
                                    new ParametroMacchinaProduzione(
                                            opcuaNodeTemp,
                                            cycleCounterStr
                                    )
                            );
                    } catch (UaException e) {
                    }
                }

                eventoProduzioneService.save(eventoProduzione);

                //System.out.println("Macchina: " + clients.get(item.getClient()).getMacchina().getCodice());
                //System.out.println(String.format("subscription value received: item={%s}, value={%s}", item.getNodeId(), value.getValue()));
            }
        });

        ManagedDataItem managedDataItem = subscription.createDataItem(new NodeId(opcuaNode.getNameSpaceIndex(), opcuaNode.getNodeIdentifier()));
        if (managedDataItem.getStatusCode().isGood()) {
            System.out.println(String.format("item created for nodeId={%s}", managedDataItem.getNodeId()));

            dataItems.add(managedDataItem);
        } else {
            System.err.println(String.format("failed to create item for nodeId={%s} (status={%s})", managedDataItem.getNodeId(), managedDataItem.getStatusCode()));
        }
    }
    
    @Schedule(minute = "*/1", hour = "*", persistent = false)
    protected void checkOpcuaDevicesLiveness() {
        System.out.println("Adesso provo a vedere se la macchina precedentemente spenta si è accesa");
        
        List<OpcuaDevice> opcuaDevices = opcuaDeviceService.list();
        if (opcuaDevices != null) {
            /*for (Iterator<OpcuaDevice> iterator = opcuaDevices.iterator(); iterator.hasNext();) {
                OpcuaDevice opcuaDevice = iterator.next();
                for (OpcuaDevice opcuaDeviceConnected : clients.values()) {
                    if (opcuaDeviceConnected.getId().equals(opcuaDevice.getId()))
                       iterator.remove();
                }
            }*/
            opcuaDevices.removeIf(device -> clients.values().stream().filter(d -> d.getId().equals(device.getId())).findAny().isPresent());
            createAndConnectToClients(opcuaDevices);
        }
    }
    
    @PreDestroy
    public void destroy() {
        if (dataItems != null)
            for (ManagedDataItem dataItem : dataItems) {
                try {
                    dataItem.delete();
                } catch (UaException ex) {
                    System.err.println("OpcuaController:destroy - Errore: " + ex.getLocalizedMessage());
                }
            }
        if (clients != null)
            for (OpcUaClient client : clients.keySet()) {
                if (client != null)
                    try {
                        client.disconnect().get(5, TimeUnit.SECONDS);
                    } catch (InterruptedException | ExecutionException | TimeoutException ex) {
                        System.err.println("OpcuaController:destroy - Errore: " + ex.getLocalizedMessage());
                    }
            }
    }
}
