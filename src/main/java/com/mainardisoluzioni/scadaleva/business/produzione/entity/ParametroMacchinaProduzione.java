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
package com.mainardisoluzioni.scadaleva.business.produzione.entity;

import com.mainardisoluzioni.scadaleva.business.comunicazione.entity.OpcuaNode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 *
 * @author adminavvimpa
 */
@Entity
public class ParametroMacchinaProduzione {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private @NotNull EventoProduzione eventoProduzione;
    
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private @NotNull OpcuaNode opcuaNode;
    
    @Column(nullable = false)
    private @NotBlank String valore;

    public ParametroMacchinaProduzione() {
    }

    public ParametroMacchinaProduzione(OpcuaNode opcuaNode, String valore) {
        this();
        this.opcuaNode = opcuaNode;
        this.valore = valore;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EventoProduzione getEventoProduzione() {
        return eventoProduzione;
    }

    public void setEventoProduzione(EventoProduzione eventoProduzione) {
        this.eventoProduzione = eventoProduzione;
    }

    public OpcuaNode getOpcuaNode() {
        return opcuaNode;
    }

    public void setOpcuaNode(OpcuaNode opcuaNode) {
        this.opcuaNode = opcuaNode;
    }

    public String getValore() {
        return valore;
    }

    public void setValore(String valore) {
        this.valore = valore;
    }
    
}
