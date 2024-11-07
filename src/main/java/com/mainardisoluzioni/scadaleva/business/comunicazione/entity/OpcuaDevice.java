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
package com.mainardisoluzioni.scadaleva.business.comunicazione.entity;

import com.mainardisoluzioni.scadaleva.business.reparto.entity.Macchina;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderColumn;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author adminavvimpa
 */
@Entity
public class OpcuaDevice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(optional = false)
    private @NotNull Macchina macchina;
    
    private String ipAddress;
    private String tcpPort;
    
    @OrderColumn
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "opcuaDevice", orphanRemoval = true)
    private List<OpcuaNode> opcuaNodes = new ArrayList<>();

    public OpcuaDevice() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Macchina getMacchina() {
        return macchina;
    }

    public void setMacchina(Macchina macchina) {
        this.macchina = macchina;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getTcpPort() {
        return tcpPort;
    }

    public void setTcpPort(String tcpPort) {
        this.tcpPort = tcpPort;
    }
    
    public void addOpcuaNode(OpcuaNode opcuaNode) {
        if (!opcuaNodes.contains(opcuaNode)) {
            opcuaNodes.add(opcuaNode);
            opcuaNode.setOpcuaDevice(this);
        }
    }
    
    public void removeOpcuaNode(OpcuaNode opcuaNode) {
        if (opcuaNodes.contains(opcuaNode)) {
            opcuaNodes.remove(opcuaNode);
            opcuaNode.setOpcuaDevice(null);
        }
    }

    public List<OpcuaNode> getOpcuaNodes() {
        return opcuaNodes;
    }
    
}
