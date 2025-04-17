/*
 * Copyright (C) 2025 maina
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

import com.digitalpetri.modbus.client.ModbusTcpClient;
import com.digitalpetri.modbus.exceptions.ModbusExecutionException;
import com.digitalpetri.modbus.exceptions.ModbusResponseException;
import com.digitalpetri.modbus.exceptions.ModbusTimeoutException;
import com.digitalpetri.modbus.pdu.ReadInputRegistersRequest;
import com.digitalpetri.modbus.pdu.ReadInputRegistersResponse;
import com.digitalpetri.modbus.tcp.client.NettyTcpClientTransport;
import com.mainardisoluzioni.scadaleva.business.comunicazione.boundary.TraceAndFollowModbusService;
import com.mainardisoluzioni.scadaleva.business.comunicazione.entity.TraceAndFollowModbusDevice;
import com.mainardisoluzioni.scadaleva.business.energia.boundary.EventoEnergiaService;
import com.mainardisoluzioni.scadaleva.business.reparto.entity.Macchina;
import io.netty.channel.ConnectTimeoutException;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.ejb.Schedule;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author maina
 */
@Startup
@Singleton
public class TraceAndFollowModbusController {
    @Inject
    TraceAndFollowModbusService traceAndFollowModbusService;
    
    @Inject
    EventoEnergiaService eventoEnergiaService;
    
    
    private Map<ModbusTcpClient, TraceAndFollowModbusDevice> clients;
    private Map<Macchina, BigDecimal> lastPotenzaIstantanea;   // ultimo valore della potenza istantanea
    
    @PostConstruct
    public void init() {
        createAndConnectToClientsFromDatabase();
    }
    
    private void createAndConnectToClientsFromDatabase() {
        clients = new HashMap<>();
        lastPotenzaIstantanea = new HashMap<>();
        List<TraceAndFollowModbusDevice> traceAndFollowModbusDevices = traceAndFollowModbusService.list();
        if (traceAndFollowModbusDevices != null)
            createAndConnectToClients(traceAndFollowModbusDevices);
    }
    
    private void createAndConnectToClients(@NotNull List<TraceAndFollowModbusDevice> traceAndFollowModbusDevices) {
        for (TraceAndFollowModbusDevice traceAndFollowModbusDevice : traceAndFollowModbusDevices) {
            try {
                NettyTcpClientTransport transport = NettyTcpClientTransport.create(cfg -> {
                    cfg.hostname = traceAndFollowModbusDevice.getIpAddress();
                    cfg.port = traceAndFollowModbusDevice.getTcpPort();
                });
                
                ModbusTcpClient client = ModbusTcpClient.create(transport);
                
                client.connect();
                listen(traceAndFollowModbusDevice, client);
                clients.put(client, traceAndFollowModbusDevice);
            } catch (NumberFormatException | ModbusExecutionException | ModbusResponseException | ModbusTimeoutException ex) {
                if (!getCause(ex).getClass().equals(ConnectTimeoutException.class)) // evita di 'sporcare' i log con ConnectTimeoutException quando le macchine sono spente
                    System.err.println("TraceAndFollowModbusController:init - Errore: " + ex.getLocalizedMessage());
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
    
    private BigDecimal convertRegisterValueFromUnsignedValue(@NotEmpty byte[] readInputRegisters) {
        BigDecimal result = BigDecimal.ZERO;
        if (readInputRegisters.length == 1)
            result = new BigDecimal(Integer.parseInt(Integer.toBinaryString(readInputRegisters[0]), 2));
        return result;
    }
    
    private void listen(@NotNull TraceAndFollowModbusDevice traceAndFollowModbusDevice, @NotNull ModbusTcpClient client) throws ModbusExecutionException, ModbusResponseException, ModbusTimeoutException {
        ReadInputRegistersResponse inputRegistersResponse = client.readInputRegisters(
                traceAndFollowModbusDevice.getModbusUnitId(),
                new ReadInputRegistersRequest(TraceAndFollowModbusDevice.INDIRIZZO_REGISTRO_POTENZA_ISTANTANEA, 1)
        );
        
        BigDecimal potenzaIstantanea = convertRegisterValueFromUnsignedValue(inputRegistersResponse.registers());

        try {
            lastPotenzaIstantanea.put(
                    traceAndFollowModbusDevice.getMacchina(),
                    potenzaIstantanea
            );
            eventoEnergiaService.createAndSave(traceAndFollowModbusDevice.getMacchina(), BigDecimal.ZERO, potenzaIstantanea);
        } catch (NumberFormatException e) {
            System.err.println("TraceAndFollowModbusController:listen - Errore: " + e.getLocalizedMessage());
        }
    }
    
    @Schedule(second = "*/10", minute = "*", hour = "*", persistent = false)
    public void leggiRegistro() {
        for (Map.Entry<ModbusTcpClient, TraceAndFollowModbusDevice> entry : clients.entrySet()) {
            try {
                listen(entry.getValue(), entry.getKey());
            } catch (ModbusExecutionException | ModbusResponseException | ModbusTimeoutException ex) {
                System.err.println("TraceAndFollowModbusController:leggiRegistro - Errore: " + ex.getLocalizedMessage());
            }
        }
    }
    
    @Schedule(minute = "*/5", hour = "*", persistent = false)
    protected void checkTraceAndFollowModbusDevicesLiveness() {
        //System.out.println("Adesso provo a vedere se la macchina precedentemente spenta si è accesa");
        
        List<TraceAndFollowModbusDevice> traceAndFollowModbusDevices = traceAndFollowModbusService.list();
        if (traceAndFollowModbusDevices != null) {
            traceAndFollowModbusDevices.removeIf(device -> clients.values().stream().filter(d -> d.getId().equals(device.getId())).findAny().isPresent());
            createAndConnectToClients(traceAndFollowModbusDevices);
        }
    }
    
    @PreDestroy
    public void destroy() {
        if (clients != null)
            for (ModbusTcpClient client : clients.keySet()) {
                if (client != null)
                    try {
                        client.disconnect();
                    } catch (ModbusExecutionException ex) {
                        System.err.println("TraceAndFollowModbusController:destroy - Errore: " + ex.getLocalizedMessage());
                    }
            }
    }
    
}
