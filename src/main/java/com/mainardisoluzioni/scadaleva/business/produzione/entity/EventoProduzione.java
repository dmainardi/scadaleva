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

import com.mainardisoluzioni.scadaleva.business.produzione.control.StatoLavorazione;
import com.mainardisoluzioni.scadaleva.business.reparto.entity.Macchina;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderColumn;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author adminavvimpa
 */
@Entity
public class EventoProduzione {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(length = 15)
    private String numeroOrdineDiLavoro;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "Macchina", referencedColumnName = "codice")
    private @NotNull Macchina macchina;
    
    @Column(nullable = false, columnDefinition = "timestamp")
    private @NotNull LocalDateTime timestampProduzione;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "StatoLav")
    private StatoLavorazione statoLavorazione;
    
    @Column(nullable = false)
    private @NotNull Integer quantita;
    
    @OrderColumn
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "eventoProduzione", orphanRemoval = true)
    private List<ParametroMacchinaProduzione> parametriMacchinaProduzione = new ArrayList<>();

    public EventoProduzione() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroOrdineDiLavoro() {
        return numeroOrdineDiLavoro;
    }

    public void setNumeroOrdineDiLavoro(String numeroOrdineDiLavoro) {
        this.numeroOrdineDiLavoro = numeroOrdineDiLavoro;
    }

    public Macchina getMacchina() {
        return macchina;
    }

    public void setMacchina(Macchina macchina) {
        this.macchina = macchina;
    }

    public LocalDateTime getTimestampProduzione() {
        return timestampProduzione;
    }

    public void setTimestampProduzione(LocalDateTime timestampProduzione) {
        this.timestampProduzione = timestampProduzione;
    }

    public StatoLavorazione getStatoLavorazione() {
        return statoLavorazione;
    }

    public void setStatoLavorazione(StatoLavorazione statoLavorazione) {
        this.statoLavorazione = statoLavorazione;
    }

    public Integer getQuantita() {
        return quantita;
    }

    public void setQuantita(Integer quantita) {
        this.quantita = quantita;
    }
    
    public void addParametroMacchinaProduzione(ParametroMacchinaProduzione parametroMacchinaProduzione) {
        if (!parametriMacchinaProduzione.contains(parametroMacchinaProduzione)) {
            parametriMacchinaProduzione.add(parametroMacchinaProduzione);
            parametroMacchinaProduzione.setEventoProduzione(this);
        }
    }
    
    public void removeParametroMacchinaProduzione(ParametroMacchinaProduzione parametroMacchinaProduzione) {
        if (parametriMacchinaProduzione.contains(parametroMacchinaProduzione)) {
            parametriMacchinaProduzione.remove(parametroMacchinaProduzione);
            parametroMacchinaProduzione.setEventoProduzione(null);
        }
    }
    
    public List<ParametroMacchinaProduzione> getParametriMacchinaProduzione() {
        return parametriMacchinaProduzione;
    }
    
}
