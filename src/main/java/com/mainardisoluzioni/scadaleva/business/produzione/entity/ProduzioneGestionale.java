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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 *
 * @author maina
 */
@Entity
@Table(name = "MacchineStato")
public class ProduzioneGestionale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "macchina", length = 8)
    private String codiceMacchina;
    
    @Column(name = "commessa", length = 6)
    private String numeroOrdineDiProduzione;
    
    @Column(name = "data")
    private LocalDate dataOrdineDiProduzione;
    
    @Column(name = "articolo", length = 16)
    private String codiceArticolo;
    
    @Column(name = "descrizione", length = 40)
    private String nomeArticolo;
    
    @Column(name = "qta_commessa", scale = 4, precision = 16)
    private BigDecimal quantita;                       // quantità dell'ordine
    
    @Column(name = "fase", length = 6)
    private String fase;
    
    @Column(name = "operazione", length = 3)
    private String codiceOperazione;
    
    @Column(name = "descrizioneOperazione", length = 40)
    private String nomeOperazione;
    
    @Column(name = "Attrezzatura", length = 16)
    private String codiceAttrezzatura;
    
    @Column(name = "DescrizioneAttrezzatura", length = 40)
    private String nomeAttrezzatura;
    
    @Column(name = "impronte")
    private Integer impronte;
    
    @Column(name = "dal")
    private LocalDate dataProduzione;
    
    @Column(name = "dalle")
    private LocalTime orarioProduzione;
    
    @Column(name = "statoAttuale", length = 1)
    private String statoLavorazione;
    
    /**
     * Valore dell'etichetta che potrà essere applicata al pezzo oppure al
     * contenitore (ad esempio un cassone)
     */
    @Column(name = "ultimoUDXuscito", length = 20)
    private String serialeEtichetta;
    
    @Column(name = "ProgressivoBuoniCommessa", scale = 4, precision = 16)
    private BigDecimal pezziProdottiCorrettamente;
    
    @Column(name = "ProgressivoScartiCommessa", scale = 4, precision = 16)
    private BigDecimal pezziProdottiDiScarto;
    
    public ProduzioneGestionale() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodiceMacchina() {
        return codiceMacchina;
    }

    public void setCodiceMacchina(String codiceMacchina) {
        this.codiceMacchina = codiceMacchina;
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

    public String getCodiceArticolo() {
        return codiceArticolo;
    }

    public void setCodiceArticolo(String codiceArticolo) {
        this.codiceArticolo = codiceArticolo;
    }

    public String getNomeArticolo() {
        return nomeArticolo;
    }

    public void setNomeArticolo(String nomeArticolo) {
        this.nomeArticolo = nomeArticolo;
    }

    public BigDecimal getQuantita() {
        return quantita;
    }

    public void setQuantita(BigDecimal quantita) {
        this.quantita = quantita;
    }

    public String getFase() {
        return fase;
    }

    public void setFase(String fase) {
        this.fase = fase;
    }

    public String getCodiceOperazione() {
        return codiceOperazione;
    }

    public void setCodiceOperazione(String codiceOperazione) {
        this.codiceOperazione = codiceOperazione;
    }

    public String getNomeOperazione() {
        return nomeOperazione;
    }

    public void setNomeOperazione(String nomeOperazione) {
        this.nomeOperazione = nomeOperazione;
    }

    public String getCodiceAttrezzatura() {
        return codiceAttrezzatura;
    }

    public void setCodiceAttrezzatura(String codiceAttrezzatura) {
        this.codiceAttrezzatura = codiceAttrezzatura;
    }

    public String getNomeAttrezzatura() {
        return nomeAttrezzatura;
    }

    public void setNomeAttrezzatura(String nomeAttrezzatura) {
        this.nomeAttrezzatura = nomeAttrezzatura;
    }

    public Integer getImpronte() {
        return impronte;
    }

    public void setImpronte(Integer impronte) {
        this.impronte = impronte;
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

    public String getSerialeEtichetta() {
        return serialeEtichetta;
    }

    public void setSerialeEtichetta(String serialeEtichetta) {
        this.serialeEtichetta = serialeEtichetta;
    }

    public BigDecimal getPezziProdottiCorrettamente() {
        return pezziProdottiCorrettamente;
    }

    public void setPezziProdottiCorrettamente(BigDecimal pezziProdottiCorrettamente) {
        this.pezziProdottiCorrettamente = pezziProdottiCorrettamente;
    }

    public BigDecimal getPezziProdottiDiScarto() {
        return pezziProdottiDiScarto;
    }

    public void setPezziProdottiDiScarto(BigDecimal pezziProdottiDiScarto) {
        this.pezziProdottiDiScarto = pezziProdottiDiScarto;
    }

}
