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

import com.intelligt.modbus.jlibmodbus.exception.ModbusIOException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusNumberException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusProtocolException;
import com.intelligt.modbus.jlibmodbus.master.ModbusMaster;
import com.intelligt.modbus.jlibmodbus.master.ModbusMasterFactory;
import com.intelligt.modbus.jlibmodbus.tcp.TcpParameters;
import com.mainardisoluzioni.scadaleva.business.comunicazione.boundary.TraceAndFollowModbusService;
import com.mainardisoluzioni.scadaleva.business.comunicazione.entity.TraceAndFollowModbusDevice;
import com.mainardisoluzioni.scadaleva.business.energia.boundary.EventoEnergiaService;
import com.mainardisoluzioni.scadaleva.business.energia.entity.EventoEnergia;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.ejb.Schedule;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    
    
    private Map<ModbusMaster, TraceAndFollowModbusDevice> clients;
    private Map<TraceAndFollowModbusDevice, EventoEnergia> ultimiEventiEnergia;     // ancora da salvare sul database
    private Map<TraceAndFollowModbusDevice, EventoEnergia> penultimiEventiEnergia;  // ancora da salvare sul database
    
    private final BigDecimal PERCENTUALE = new BigDecimal(0.05);
    private final BigDecimal SOGLIA = new BigDecimal(2500);
    
    @PostConstruct
    public void init() {
        createAndConnectToClientsFromDatabase();
    }
    
    private void createAndConnectToClientsFromDatabase() {
        clients = new HashMap<>();
        ultimiEventiEnergia = new HashMap<>();
        penultimiEventiEnergia = new HashMap<>();
        List<TraceAndFollowModbusDevice> traceAndFollowModbusDevices = traceAndFollowModbusService.list();
        if (traceAndFollowModbusDevices != null)
            createAndConnectToClients(traceAndFollowModbusDevices);
    }
    
    private void createAndConnectToClients(@NotNull List<TraceAndFollowModbusDevice> traceAndFollowModbusDevices) {
        for (TraceAndFollowModbusDevice traceAndFollowModbusDevice : traceAndFollowModbusDevices) {
            try {
                TcpParameters tcpParameters = new TcpParameters();
                tcpParameters.setHost(InetAddress.getByName(traceAndFollowModbusDevice.getIpAddress()));
                tcpParameters.setKeepAlive(true);
                tcpParameters.setPort(traceAndFollowModbusDevice.getTcpPort());
                tcpParameters.setConnectionTimeout(5000);   // cinque secondi di timeout

                ModbusMaster client = ModbusMasterFactory.createModbusMasterTCP(tcpParameters);
                client.setResponseTimeout(1000);            // un secondo di timeout sulla risposta
                
                client.connect();
                listen(traceAndFollowModbusDevice, client);
                clients.put(client, traceAndFollowModbusDevice);
            } catch (NumberFormatException | UnknownHostException | ModbusIOException | ModbusProtocolException | ModbusNumberException ex) {
                if (!getCause(ex).getClass().equals(ConnectException.class) && !getCause(ex).getClass().equals(NoRouteToHostException.class) && !getCause(ex).getClass().equals(SocketTimeoutException.class)) // evita di 'sporcare' i log con ConnectTimeoutException quando le macchine sono spente
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
    
    private BigDecimal convertRegisterValueFromUnsignedValue(@NotEmpty int[] readInputRegisters) {
        BigDecimal result = BigDecimal.ZERO;
        if (readInputRegisters.length == 1)
            result = new BigDecimal(Integer.parseInt(Integer.toBinaryString(readInputRegisters[0]), 2));
        return result;
    }
    
    private void listen(@NotNull TraceAndFollowModbusDevice traceAndFollowModbusDevice, @NotNull ModbusMaster client) throws ModbusProtocolException, ModbusNumberException, ModbusIOException {
        
        int[] readInputRegisters = client.readInputRegisters(traceAndFollowModbusDevice.getModbusUnitId(),
                TraceAndFollowModbusDevice.INDIRIZZO_REGISTRO_POTENZA_ISTANTANEA,
                1
        );
        
        BigDecimal potenzaIstantanea = convertRegisterValueFromUnsignedValue(readInputRegisters);

        try {
            controllaSeSalvareEventoEnergia(traceAndFollowModbusDevice, potenzaIstantanea);
        } catch (NumberFormatException e) {
            System.err.println("TraceAndFollowModbusController:listen - Errore: " + e.getLocalizedMessage());
        }
    }
    
    private void controllaSeSalvareEventoEnergia(@NotNull TraceAndFollowModbusDevice traceAndFollowModbusDevice, @NotNull BigDecimal potenzaIstantanea) {
        boolean datoSalvato = false;
        EventoEnergia penultimoEventoEnergia = penultimiEventiEnergia.get(traceAndFollowModbusDevice);
        EventoEnergia ultimoEventoEnergia = ultimiEventiEnergia.get(traceAndFollowModbusDevice);
        if (penultimoEventoEnergia == null) {
            penultimiEventiEnergia.put(traceAndFollowModbusDevice, EventoEnergiaService.create(traceAndFollowModbusDevice.getMacchina(), potenzaIstantanea));
            datoSalvato = true;
        }
        if (!datoSalvato && ultimoEventoEnergia == null) {
            ultimiEventiEnergia.put(traceAndFollowModbusDevice, EventoEnergiaService.create(traceAndFollowModbusDevice.getMacchina(), potenzaIstantanea));
            datoSalvato = true;
        }
        if (!datoSalvato && ultimoEventoEnergia != null && ultimoEventoEnergia.getPotenzaIstantanea() != null) {
            if (
                    potenzaIstantanea.compareTo(SOGLIA) <= 0
                    &&
                    potenzaIstantanea.compareTo(ultimoEventoEnergia.getPotenzaIstantanea().multiply(BigDecimal.ONE.subtract(PERCENTUALE))) >= 0
                    &&
                    potenzaIstantanea.compareTo(ultimoEventoEnergia.getPotenzaIstantanea().multiply(BigDecimal.ONE.add(PERCENTUALE))) <= 0
                    )
                ultimiEventiEnergia.put(traceAndFollowModbusDevice, EventoEnergiaService.create(traceAndFollowModbusDevice.getMacchina(), potenzaIstantanea));
            else {
                if (penultimoEventoEnergia != null)
                    eventoEnergiaService.save(penultimoEventoEnergia);
                eventoEnergiaService.save(ultimoEventoEnergia);
                penultimiEventiEnergia.put(traceAndFollowModbusDevice, EventoEnergiaService.create(traceAndFollowModbusDevice.getMacchina(), potenzaIstantanea));
                ultimiEventiEnergia.remove(traceAndFollowModbusDevice);
            }
        }
    }
    
    @Schedule(second = "*/1", minute = "*", hour = "*", persistent = false)
    public void leggiRegistro() {
        for (Map.Entry<ModbusMaster, TraceAndFollowModbusDevice> entry : clients.entrySet()) {
            try {
                listen(entry.getValue(), entry.getKey());
            } catch (ModbusProtocolException | ModbusNumberException | ModbusIOException ex) {
                if (!getCause(ex).getClass().equals(ConnectException.class) && !getCause(ex).getClass().equals(NoRouteToHostException.class) && !getCause(ex).getClass().equals(SocketTimeoutException.class)) // evita di 'sporcare' i log con ConnectTimeoutException quando le macchine sono spente
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
            for (ModbusMaster client : clients.keySet()) {
                if (client != null)
                    try {
                        client.disconnect();
                    } catch (ModbusIOException ex) {
                        System.err.println("TraceAndFollowModbusController:destroy - Errore: " + ex.getLocalizedMessage());
                    }
            }
    }
    
}
