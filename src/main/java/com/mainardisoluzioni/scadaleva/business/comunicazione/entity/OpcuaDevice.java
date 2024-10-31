/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
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
