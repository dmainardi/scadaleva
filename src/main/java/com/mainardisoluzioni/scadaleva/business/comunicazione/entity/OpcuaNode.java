/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mainardisoluzioni.scadaleva.business.comunicazione.entity;

import com.mainardisoluzioni.scadaleva.business.comunicazione.control.CategoriaVariabileProduzione;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 *
 * @author adminavvimpa
 */
@Entity
public class OpcuaNode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(optional = false)
    private @NotNull OpcuaDevice opcuaDevice;
    
    @Enumerated
    @Column(nullable = false, columnDefinition = "smallint")
    private @NotNull CategoriaVariabileProduzione categoriaVariabileProduzione;
    
    @Column(nullable = false)
    private @NotBlank String nameSpaceIndex;
    
    @Column(nullable = false)
    @DecimalMin(value = "0", inclusive = false)
    private @NotNull Integer nodeId;

    public OpcuaNode() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OpcuaDevice getOpcuaDevice() {
        return opcuaDevice;
    }

    public void setOpcuaDevice(OpcuaDevice opcuaDevice) {
        this.opcuaDevice = opcuaDevice;
    }

    public CategoriaVariabileProduzione getCategoriaVariabileProduzione() {
        return categoriaVariabileProduzione;
    }

    public void setCategoriaVariabileProduzione(CategoriaVariabileProduzione categoriaVariabileProduzione) {
        this.categoriaVariabileProduzione = categoriaVariabileProduzione;
    }

    public String getNameSpaceIndex() {
        return nameSpaceIndex;
    }

    public void setNameSpaceIndex(String nameSpaceIndex) {
        this.nameSpaceIndex = nameSpaceIndex;
    }

    public Integer getNodeId() {
        return nodeId;
    }

    public void setNodeId(Integer nodeId) {
        this.nodeId = nodeId;
    }
    
}
