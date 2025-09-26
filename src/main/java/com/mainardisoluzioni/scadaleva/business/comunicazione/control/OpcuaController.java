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
import com.mainardisoluzioni.scadaleva.business.produzione.boundary.ProduzioneGestionaleService;
import com.mainardisoluzioni.scadaleva.business.produzione.control.EventoProduzioneController;
import com.mainardisoluzioni.scadaleva.business.produzione.control.ProduzioneGestionaleController;
import com.mainardisoluzioni.scadaleva.business.produzione.entity.EventoProduzione;
import com.mainardisoluzioni.scadaleva.business.produzione.entity.OrdineDiProduzione;
import com.mainardisoluzioni.scadaleva.business.produzione.entity.ParametroMacchinaProduzione;
import com.mainardisoluzioni.scadaleva.business.reparto.entity.Macchina;
import io.netty.channel.ConnectTimeoutException;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.ejb.Schedule;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotNull;
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
    private static final Logger LOGGER = Logger.getLogger(OpcuaController.class.getName());
    
    @Inject
    OpcuaDeviceService opcuaDeviceService;
    
    @Inject
    EventoProduzioneService eventoProduzioneService;
    
    @Inject
    OrdineDiProduzioneService ordineDiProduzioneService;
    
    @Inject
    ProduzioneGestionaleService produzioneGestionaleService;
    
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
                if (!getCause(ex).getClass().equals(ConnectTimeoutException.class)) // evita di 'sporcare' i log con ConnectTimeoutException quando le macchine sono spente
                    LOGGER.log(Level.WARNING, "OpcuaController:createAndConnectToClients - Errore: {0}", new Object[]{ex.getLocalizedMessage()});
            }
        }
    }
    
    private Throwable getCause(Throwable e) {
        Throwable cause = null; 
        Throwable result = e;
        
        while(null != (cause = result.getCause())  && (result != cause) ) {
            result = cause;
        }
        
        return result;
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
                                LOGGER.log(Level.WARNING, "OpcuaController:listen - Errore: {0}", new Object[]{statusCode});
                                try {
                                    createSubscription(client, opcuaNode);
                                } catch (UaException e) {
                                    LOGGER.log(Level.WARNING, "OpcuaController:listen - Errore: {0}", new Object[]{e.getMessage()});
                                }
                            }

                        }
                );
                createSubscription(client, opcuaNode);
            } catch (NumberFormatException e) {
                LOGGER.log(Level.WARNING, "OpcuaController:listen - Errore: {0}", new Object[]{e.getLocalizedMessage()});
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
                Integer previousCycleCounter = lastCycleCounters.getOrDefault(macchina, 0);
                Integer quantitaProdotta;
                if (currentCycleCounter.compareTo(previousCycleCounter) < 0)
                    quantitaProdotta = currentCycleCounter;
                else
                    quantitaProdotta = currentCycleCounter - previousCycleCounter;
                OrdineDiProduzione ordineDiProduzione = ordineDiProduzioneService.getLastOrdineDiProduzione(macchina.getCodice());
                EventoProduzione eventoProduzione = EventoProduzioneController.createEventoProduzione(opcuaDeviceTemp.getMacchina(), LocalDateTime.now(Clock.systemUTC()), quantitaProdotta, ordineDiProduzione);
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
                            case RICETTA_IMPOSTATA_IN_SCRITTURA_CODICE:
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
                        LOGGER.log(Level.WARNING, "OpcuaController:createSubscription - Errore: {0}", new Object[]{e.getLocalizedMessage()});
                    }
                }
                
                for (OpcuaNode opcuaNodeTemp : opcuaDeviceTemp.getOpcuaNodes()) {
                    try {
                        if (opcuaNodeTemp.getCategoriaVariabileProduzione() != CategoriaVariabileProduzione.CONTAPEZZI) {
                            String valore = String.valueOf(client.getAddressSpace().getVariableNode(createNodeId(opcuaNodeTemp)).readValue().getValue().getValue());
                            if (!valore.isBlank())
                                eventoProduzione.addParametroMacchinaProduzione(
                                        new ParametroMacchinaProduzione(
                                                opcuaNodeTemp.getCategoriaVariabileProduzione(),
                                                valore
                                        )
                                );
                        }
                        else
                            eventoProduzione.addParametroMacchinaProduzione(
                                    new ParametroMacchinaProduzione(
                                            opcuaNodeTemp.getCategoriaVariabileProduzione(),
                                            cycleCounterStr
                                    )
                            );
                    } catch (UaException e) {
                    }
                }

                eventoProduzioneService.save(eventoProduzione);
                
                produzioneGestionaleService.save(
                        ProduzioneGestionaleController.createAndSave(
                                macchina.getCodice(),
                                eventoProduzione.getParametriMacchinaProduzione().stream().filter(pmp -> CategoriaVariabileProduzione.RICETTA_IMPOSTATA_IN_LETTURA_CODICE == pmp.getCategoriaVariabileProduzione()).map(ParametroMacchinaProduzione::getValore).findFirst().orElse(null),
                                ordineDiProduzione,
                                eventoProduzione.getParametriMacchinaProduzione().stream().filter(pmp -> CategoriaVariabileProduzione.FUNZIONAMENTO_CICLO_AUTOMATICO == pmp.getCategoriaVariabileProduzione()).map(pmp -> Boolean.valueOf(pmp.getValore()) ? 1 : 0).findFirst().orElse(null),
                                eventoProduzione.getParametriMacchinaProduzione().stream().filter(pmp -> CategoriaVariabileProduzione.PRESENZA_ALLARME == pmp.getCategoriaVariabileProduzione()).map(pmp -> Boolean.valueOf(pmp.getValore()) ? 1 : 0).findFirst().orElse(null),
                                LocalDateTime.now(),
                                quantitaProdotta
                        )
                );

                LOGGER.log(
                        Level.FINE,
                        "MqttController::createSubscription - macchina: {0}, subscription value received: item={1}, value={2}",
                        new Object[]{
                            clients.get(item.getClient()).getMacchina().getCodice(),
                            item.getNodeId(),
                            value.getValue()
                        }
                );
            }
        });

        ManagedDataItem managedDataItem = subscription.createDataItem(createNodeId(opcuaNode));
        if (managedDataItem.getStatusCode().isGood()) {
            LOGGER.log(Level.FINE, "MqttController::createSubscription - item created for nodeId={0}", new Object[]{managedDataItem.getNodeId()});

            dataItems.add(managedDataItem);
        } else {
            LOGGER.log(Level.WARNING, "OpcuaController:createSubscription - failed to create item for nodeId={0} (status={1})", new Object[]{managedDataItem.getNodeId(), managedDataItem.getStatusCode()});
        }
    }
    
    @Schedule(minute = "*/5", hour = "*", persistent = false)
    protected void checkOpcuaDevicesLiveness() {
        LOGGER.log(Level.FINE, "OpcuaController:checkOpcuaDevicesLiveness - Adesso provo a vedere se la macchina precedentemente spenta si è accesa");
        
        List<OpcuaDevice> opcuaDevices = opcuaDeviceService.list();
        if (opcuaDevices != null) {
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
                    LOGGER.log(Level.WARNING, "OpcuaController:destroy - Errore: {0}", new Object[]{ex.getLocalizedMessage()});
                }
            }
        if (clients != null)
            for (OpcUaClient client : clients.keySet()) {
                if (client != null)
                    try {
                        client.disconnect().get(5, TimeUnit.SECONDS);
                    } catch (InterruptedException | ExecutionException | TimeoutException ex) {
                        LOGGER.log(Level.WARNING, "OpcuaController:destroy - Errore: {0}", new Object[]{ex.getLocalizedMessage()});
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
