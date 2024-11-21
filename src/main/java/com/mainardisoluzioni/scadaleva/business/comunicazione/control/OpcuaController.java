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
import com.mainardisoluzioni.scadaleva.business.produzione.boundary.OrdineDiProduzioneService;
import com.mainardisoluzioni.scadaleva.business.produzione.entity.EventoProduzione;
import com.mainardisoluzioni.scadaleva.business.produzione.entity.OrdineDiProduzione;
import com.mainardisoluzioni.scadaleva.business.produzione.entity.ParametroMacchinaProduzione;
import com.mainardisoluzioni.scadaleva.business.reparto.entity.Macchina;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.ejb.Schedule;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotNull;
import java.text.DateFormat;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.api.config.OpcUaClientConfigBuilder;
import org.eclipse.milo.opcua.sdk.client.api.subscriptions.UaSubscription;
import org.eclipse.milo.opcua.sdk.client.api.subscriptions.UaSubscriptionManager;
import org.eclipse.milo.opcua.sdk.client.subscriptions.ManagedDataItem;
import org.eclipse.milo.opcua.sdk.client.subscriptions.ManagedSubscription;
import org.eclipse.milo.opcua.stack.client.DiscoveryClient;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.WriteValue;
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
    
    @Inject
    OrdineDiProduzioneService ordineDiProduzioneService;
    
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
                        Integer.valueOf(String.valueOf(client.getAddressSpace().getVariableNode(createNodeId(opcuaNode)).readValue().getValue().getValue()))
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
                OrdineDiProduzione ordineDiProduzione = ordineDiProduzioneService.getLastOrdineDiProduzione(macchina.getCodice());
                EventoProduzione eventoProduzione = new EventoProduzione();
                if (ordineDiProduzione != null)
                    eventoProduzione.setNumeroOrdineDiProduzione(ordineDiProduzione.getNumeroOrdineDiProduzione());
                eventoProduzione.setMacchina(opcuaDeviceTemp.getMacchina());
                eventoProduzione.setTimestampProduzione(LocalDateTime.now(Clock.systemUTC()));
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

                List<WriteValue> writeValues = new ArrayList<>();
                for (OpcuaNode opcuaNodeTemp : opcuaDeviceTemp.getOpcuaNodes()) {
                    Variant variant = null;
                    if (ordineDiProduzione != null) {
                        switch (opcuaNodeTemp.getCategoriaVariabileProduzione()) {
                            case CODICE_ARTICOLO:
                                variant = new Variant(ordineDiProduzione.getCodiceArticolo());
                                break;
                            case CODICE_ORDINE_DI_PRODUZIONE:
                                variant = new Variant(ordineDiProduzione.getNumeroOrdineDiProduzione());
                                break;
                            case CONTAPEZZI:
                                break;
                            case DATA_ORDINE_DI_PRODUZIONE:
                                variant = new Variant(DateTimeFormatter.ISO_DATE.format(ordineDiProduzione.getDataOrdineDiProduzione()));
                                break;
                            case LOTTO:
                                variant = new Variant(ordineDiProduzione.getLotto());
                                break;
                            case QUANTITA_PRODOTTA_CORRETTAMENTE:
                                variant = new Variant(ordineDiProduzione.getQuantitaProdottaCorrettamente().intValue());
                                break;
                            case QUANTITA_RICHIESTA:
                                variant = new Variant(ordineDiProduzione.getQuantitaDaRealizzare().intValue());
                                break;
                            case RICETTA_IMPOSTATA_CODICE:
                                break;
                            case RICETTA_RICHIESTA_CODICE:
                                variant = new Variant(ordineDiProduzione.getCodiceRicettaRichiesta());
                                break;
                            default:
                                variant = new Variant("Categoria variabile produzione non impostata");
                        }
                    }
                    else
                        variant = new Variant(null);
                    if (variant != null)
                        writeValues.add(
                                new WriteValue(
                                        createNodeId(opcuaNodeTemp),
                                        AttributeId.Value.uid(),
                                        null, // indexRange
                                        DataValue.valueOnly(variant)
                                )
                        );
                }
                if (!writeValues.isEmpty()) {
                    try {
                        client.write(writeValues).get();
                    } catch (InterruptedException | ExecutionException e) {
                        System.err.println("OpcuaController:createSubscription(DataChangeListener) - Errore: " + e.getLocalizedMessage());
                    }
                }
                
                for (OpcuaNode opcuaNodeTemp : opcuaDeviceTemp.getOpcuaNodes()) {
                    try {
                        if (opcuaNodeTemp.getCategoriaVariabileProduzione() != CategoriaVariabileProduzione.CONTAPEZZI) {
                            String valore = String.valueOf(client.getAddressSpace().getVariableNode(createNodeId(opcuaNodeTemp)).readValue().getValue().getValue());
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

        ManagedDataItem managedDataItem = subscription.createDataItem(createNodeId(opcuaNode));
        if (managedDataItem.getStatusCode().isGood()) {
            System.out.println(String.format("item created for nodeId={%s}", managedDataItem.getNodeId()));

            dataItems.add(managedDataItem);
        } else {
            System.err.println(String.format("failed to create item for nodeId={%s} (status={%s})", managedDataItem.getNodeId(), managedDataItem.getStatusCode()));
        }
    }
    
    @Schedule(minute = "*/5", hour = "*", persistent = false)
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
    
    private NodeId createNodeId(@NotNull OpcuaNode opcuaNode) {
        Integer nodeIdentifierInt;
        
        try {
            nodeIdentifierInt = Integer.valueOf(opcuaNode.getNodeIdentifier());
        } catch (NumberFormatException e) {
            nodeIdentifierInt = null;
        }
        
        if (nodeIdentifierInt != null)
            return new NodeId(opcuaNode.getNameSpaceIndex(), nodeIdentifierInt);
        else
            return new NodeId(opcuaNode.getNameSpaceIndex(), opcuaNode.getNodeIdentifier());
    }
}
