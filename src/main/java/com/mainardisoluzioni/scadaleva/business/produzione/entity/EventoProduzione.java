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
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 *
 * @author adminavvimpa
 */
@Entity
@Table(name = "produzione")
public class EventoProduzione {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "NumeroOdL", length = 15)
    private String numeroOrdineDiLavoro;
    
    @Column(name = "DataOdL")
    private LocalDate dataOrdineDiLavoro;
    
    @ManyToOne
    @JoinColumn(name = "Macchina", referencedColumnName = "codice")
    private Macchina macchina;
    
    @Column(name = "DataProd")
    private LocalDate dataProduzione;
    
    @Column(name = "OraProd")
    private LocalTime oraProduzione;
    
    @Enumerated
    @Column(name = "StatoLav")
    private StatoLavorazione statoLavorazione;
    
    private Integer quantita;

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

    public LocalDate getDataOrdineDiLavoro() {
        return dataOrdineDiLavoro;
    }

    public void setDataOrdineDiLavoro(LocalDate dataOrdineDiLavoro) {
        this.dataOrdineDiLavoro = dataOrdineDiLavoro;
    }

    public Macchina getMacchina() {
        return macchina;
    }

    public void setMacchina(Macchina macchina) {
        this.macchina = macchina;
    }

    public LocalDate getDataProduzione() {
        return dataProduzione;
    }

    public void setDataProduzione(LocalDate dataProduzione) {
        this.dataProduzione = dataProduzione;
    }

    public LocalTime getOraProduzione() {
        return oraProduzione;
    }

    public void setOraProduzione(LocalTime oraProduzione) {
        this.oraProduzione = oraProduzione;
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
    
}
