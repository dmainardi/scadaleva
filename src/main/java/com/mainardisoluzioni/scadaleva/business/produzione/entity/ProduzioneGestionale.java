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
package com.mainardisoluzioni.scadaleva.business.produzione.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 *
 * @author maina
 */
@Entity
@Table(name = "produzione")
public class ProduzioneGestionale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;
    
    @Column(name = "NumeroOdL", length = 15)
    private String numeroOrdineDiProduzione;
    
    @Column(name = "DataOdL")
    private LocalDate dataOrdineDiProduzione;
    
    @Column(name = "Macchina")
    private String codiceMacchina;
    
    @Column(name = "DataProd")
    private LocalDate dataProduzione;
    
    @Column(name = "OraProd")
    private LocalTime orarioProduzione;
    
    @Column(name = "StatoLav", length = 1)
    private String statoLavorazione;
    
    @Column(name = "Quantita")
    private Integer quantita;                       // quantità dell'ordine
    
    @Column(name = "totPezzi")
    private Integer pezziProdotti;
    
    @Column(name = "allarme")
    private Integer presenzaAllarme;                // no allarme -> 0 / allarme presente -> 1
    
    @Column(name = "inAutomatico")
    private Integer funzionamentoCicloInAutomatico; // manuale -> 0 / automatico -> 1
    
    @Column(name = "ricetta")
    private String codiceRicettaImpostata;          // codice ricetta letta dal gestionale, scritta su HMI e letta da HMI

    public ProduzioneGestionale() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroOrdineDiProduzione() {
        return numeroOrdineDiProduzione;
    }

    public void setNumeroOrdineDiProduzione(String numeroOrdineDiProduzione) {
        this.numeroOrdineDiProduzione = numeroOrdineDiProduzione;
    }

    public LocalDate getDataOrdineDiProduzione() {
        return dataOrdineDiProduzione;
    }

    public void setDataOrdineDiProduzione(LocalDate dataOrdineDiProduzione) {
        this.dataOrdineDiProduzione = dataOrdineDiProduzione;
    }

    public String getCodiceMacchina() {
        return codiceMacchina;
    }

    public void setCodiceMacchina(String codiceMacchina) {
        this.codiceMacchina = codiceMacchina;
    }

    public LocalDate getDataProduzione() {
        return dataProduzione;
    }

    public void setDataProduzione(LocalDate dataProduzione) {
        this.dataProduzione = dataProduzione;
    }

    public LocalTime getOrarioProduzione() {
        return orarioProduzione;
    }

    public void setOrarioProduzione(LocalTime orarioProduzione) {
        this.orarioProduzione = orarioProduzione;
    }

    public String getStatoLavorazione() {
        return statoLavorazione;
    }

    public void setStatoLavorazione(String statoLavorazione) {
        this.statoLavorazione = statoLavorazione;
    }

    public Integer getQuantita() {
        return quantita;
    }

    public void setQuantita(Integer quantita) {
        this.quantita = quantita;
    }

    public Integer getPezziProdotti() {
        return pezziProdotti;
    }

    public void setPezziProdotti(Integer pezziProdotti) {
        this.pezziProdotti = pezziProdotti;
    }

    public Integer getPresenzaAllarme() {
        return presenzaAllarme;
    }

    public void setPresenzaAllarme(Integer presenzaAllarme) {
        this.presenzaAllarme = presenzaAllarme;
    }

    public Integer getFunzionamentoCicloInAutomatico() {
        return funzionamentoCicloInAutomatico;
    }

    public void setFunzionamentoCicloInAutomatico(Integer funzionamentoCicloInAutomatico) {
        this.funzionamentoCicloInAutomatico = funzionamentoCicloInAutomatico;
    }

    public String getCodiceRicettaImpostata() {
        return codiceRicettaImpostata;
    }

    public void setCodiceRicettaImpostata(String codiceRicettaImpostata) {
        this.codiceRicettaImpostata = codiceRicettaImpostata;
    }
    
}
