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
package com.mainardisoluzioni.scadaleva.business.fustellatrice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 *
 * @author maina
 */
@Entity
@Table(name = "Programmi")
public class RicettaAccess {
    @Id
    private Integer id;
    
    @Column(name = "Descrizione")
    private String codice;
    
    private Integer tipo;
    private Double altezza;
    private Double passo;
    private Double avanzamento;
    private Double avanzamento2;
    private Integer strati;
    private Double delta1;
    private Double correzione;
    private Double offsetZ;

    public RicettaAccess() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodice() {
        return codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    public Double getAltezza() {
        return altezza;
    }

    public void setAltezza(Double altezza) {
        this.altezza = altezza;
    }

    public Double getPasso() {
        return passo;
    }

    public void setPasso(Double passo) {
        this.passo = passo;
    }

    public Double getAvanzamento() {
        return avanzamento;
    }

    public void setAvanzamento(Double avanzamento) {
        this.avanzamento = avanzamento;
    }

    public Double getAvanzamento2() {
        return avanzamento2;
    }

    public void setAvanzamento2(Double avanzamento2) {
        this.avanzamento2 = avanzamento2;
    }

    public Integer getStrati() {
        return strati;
    }

    public void setStrati(Integer strati) {
        this.strati = strati;
    }

    public Double getDelta1() {
        return delta1;
    }

    public void setDelta1(Double delta1) {
        this.delta1 = delta1;
    }

    public Double getCorrezione() {
        return correzione;
    }

    public void setCorrezione(Double correzione) {
        this.correzione = correzione;
    }

    public Double getOffsetZ() {
        return offsetZ;
    }

    public void setOffsetZ(Double offsetZ) {
        this.offsetZ = offsetZ;
    }
    
}
