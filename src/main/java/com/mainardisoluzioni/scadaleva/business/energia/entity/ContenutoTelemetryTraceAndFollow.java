/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mainardisoluzioni.scadaleva.business.energia.entity;

import jakarta.json.bind.annotation.JsonbProperty;
import java.math.BigDecimal;

/**
 *
 * @author adminavvimpa
 */
public class ContenutoTelemetryTraceAndFollow {
    @JsonbProperty("energy_consumption")
    private BigDecimal consumoWh;
    
    @JsonbProperty("voltage1")
    private BigDecimal tensione1;
    
    @JsonbProperty("voltage2")
    private BigDecimal tensione2;
    
    @JsonbProperty("voltage3")
    private BigDecimal tensione3;
    
    @JsonbProperty("current1")
    private BigDecimal corrente1;
    
    @JsonbProperty("current2")
    private BigDecimal corrente2;
    
    @JsonbProperty("current3")
    private BigDecimal corrente3;
    
    @JsonbProperty("current_tot")
    private BigDecimal corrente;
    
    @JsonbProperty("active_power1")
    private BigDecimal potenza1;
    
    @JsonbProperty("active_power2")
    private BigDecimal potenza2;
    
    @JsonbProperty("active_power3")
    private BigDecimal potenza3;
    
    @JsonbProperty("active_power_tot")
    private BigDecimal potenza;
    
    @JsonbProperty("reactive_power1")
    private BigDecimal potenzaReattiva1;
    
    @JsonbProperty("reactive_power2")
    private BigDecimal potenzaReattiva2;
    
    @JsonbProperty("reactive_power3")
    private BigDecimal potenzaReattiva3;
    
    @JsonbProperty("reactive_power_tot")
    private BigDecimal potenzaReattiva;
    
    @JsonbProperty("apparent_power1")
    private BigDecimal potenzaApperente1;
    
    @JsonbProperty("apparent_power2")
    private BigDecimal potenzaApperente2;
    
    @JsonbProperty("apparent_power3")
    private BigDecimal potenzaApperente3;
    
    @JsonbProperty("apparent_power_tot")
    private BigDecimal potenzaApperente;
    
    @JsonbProperty("pf1")
    private BigDecimal fattoreDiPotenza1;
    
    @JsonbProperty("pf2")
    private BigDecimal fattoreDiPotenza2;
    
    @JsonbProperty("pf3")
    private BigDecimal fattoreDiPotenza3;
    
    @JsonbProperty("pf_tot")
    private BigDecimal fattoreDiPotenza;
    
    @JsonbProperty("phase1")
    private BigDecimal sfasamento1;
    
    @JsonbProperty("phase2")
    private BigDecimal sfasamento2;
    
    @JsonbProperty("phase3")
    private BigDecimal sfasamento3;
    
    @JsonbProperty("frequency")
    private BigDecimal frequenza;
    
    @JsonbProperty("reactive_energy_consumption")
    private BigDecimal energiaReattiva; // energia reattiva consumata nell’arco di tempo di un pacchetto in VARh
    
    @JsonbProperty("input1_value")
    private Integer input1;

    public ContenutoTelemetryTraceAndFollow() {
    }

    public BigDecimal getConsumoWh() {
        return consumoWh;
    }

    public void setConsumoWh(BigDecimal consumoWh) {
        this.consumoWh = consumoWh;
    }

    public BigDecimal getTensione1() {
        return tensione1;
    }

    public void setTensione1(BigDecimal tensione1) {
        this.tensione1 = tensione1;
    }

    public BigDecimal getTensione2() {
        return tensione2;
    }

    public void setTensione2(BigDecimal tensione2) {
        this.tensione2 = tensione2;
    }

    public BigDecimal getTensione3() {
        return tensione3;
    }

    public void setTensione3(BigDecimal tensione3) {
        this.tensione3 = tensione3;
    }

    public BigDecimal getCorrente1() {
        return corrente1;
    }

    public void setCorrente1(BigDecimal corrente1) {
        this.corrente1 = corrente1;
    }

    public BigDecimal getCorrente2() {
        return corrente2;
    }

    public void setCorrente2(BigDecimal corrente2) {
        this.corrente2 = corrente2;
    }

    public BigDecimal getCorrente3() {
        return corrente3;
    }

    public void setCorrente3(BigDecimal corrente3) {
        this.corrente3 = corrente3;
    }

    public BigDecimal getCorrente() {
        return corrente;
    }

    public void setCorrente(BigDecimal corrente) {
        this.corrente = corrente;
    }

    public BigDecimal getPotenza1() {
        return potenza1;
    }

    public void setPotenza1(BigDecimal potenza1) {
        this.potenza1 = potenza1;
    }

    public BigDecimal getPotenza2() {
        return potenza2;
    }

    public void setPotenza2(BigDecimal potenza2) {
        this.potenza2 = potenza2;
    }

    public BigDecimal getPotenza3() {
        return potenza3;
    }

    public void setPotenza3(BigDecimal potenza3) {
        this.potenza3 = potenza3;
    }

    public BigDecimal getPotenza() {
        return potenza;
    }

    public void setPotenza(BigDecimal potenza) {
        this.potenza = potenza;
    }

    public BigDecimal getPotenzaReattiva1() {
        return potenzaReattiva1;
    }

    public void setPotenzaReattiva1(BigDecimal potenzaReattiva1) {
        this.potenzaReattiva1 = potenzaReattiva1;
    }

    public BigDecimal getPotenzaReattiva2() {
        return potenzaReattiva2;
    }

    public void setPotenzaReattiva2(BigDecimal potenzaReattiva2) {
        this.potenzaReattiva2 = potenzaReattiva2;
    }

    public BigDecimal getPotenzaReattiva3() {
        return potenzaReattiva3;
    }

    public void setPotenzaReattiva3(BigDecimal potenzaReattiva3) {
        this.potenzaReattiva3 = potenzaReattiva3;
    }

    public BigDecimal getPotenzaReattiva() {
        return potenzaReattiva;
    }

    public void setPotenzaReattiva(BigDecimal potenzaReattiva) {
        this.potenzaReattiva = potenzaReattiva;
    }

    public BigDecimal getPotenzaApperente1() {
        return potenzaApperente1;
    }

    public void setPotenzaApperente1(BigDecimal potenzaApperente1) {
        this.potenzaApperente1 = potenzaApperente1;
    }

    public BigDecimal getPotenzaApperente2() {
        return potenzaApperente2;
    }

    public void setPotenzaApperente2(BigDecimal potenzaApperente2) {
        this.potenzaApperente2 = potenzaApperente2;
    }

    public BigDecimal getPotenzaApperente3() {
        return potenzaApperente3;
    }

    public void setPotenzaApperente3(BigDecimal potenzaApperente3) {
        this.potenzaApperente3 = potenzaApperente3;
    }

    public BigDecimal getPotenzaApperente() {
        return potenzaApperente;
    }

    public void setPotenzaApperente(BigDecimal potenzaApperente) {
        this.potenzaApperente = potenzaApperente;
    }

    public BigDecimal getFattoreDiPotenza1() {
        return fattoreDiPotenza1;
    }

    public void setFattoreDiPotenza1(BigDecimal fattoreDiPotenza1) {
        this.fattoreDiPotenza1 = fattoreDiPotenza1;
    }

    public BigDecimal getFattoreDiPotenza2() {
        return fattoreDiPotenza2;
    }

    public void setFattoreDiPotenza2(BigDecimal fattoreDiPotenza2) {
        this.fattoreDiPotenza2 = fattoreDiPotenza2;
    }

    public BigDecimal getFattoreDiPotenza3() {
        return fattoreDiPotenza3;
    }

    public void setFattoreDiPotenza3(BigDecimal fattoreDiPotenza3) {
        this.fattoreDiPotenza3 = fattoreDiPotenza3;
    }

    public BigDecimal getFattoreDiPotenza() {
        return fattoreDiPotenza;
    }

    public void setFattoreDiPotenza(BigDecimal fattoreDiPotenza) {
        this.fattoreDiPotenza = fattoreDiPotenza;
    }

    public BigDecimal getSfasamento1() {
        return sfasamento1;
    }

    public void setSfasamento1(BigDecimal sfasamento1) {
        this.sfasamento1 = sfasamento1;
    }

    public BigDecimal getSfasamento2() {
        return sfasamento2;
    }

    public void setSfasamento2(BigDecimal sfasamento2) {
        this.sfasamento2 = sfasamento2;
    }

    public BigDecimal getSfasamento3() {
        return sfasamento3;
    }

    public void setSfasamento3(BigDecimal sfasamento3) {
        this.sfasamento3 = sfasamento3;
    }

    public BigDecimal getFrequenza() {
        return frequenza;
    }

    public void setFrequenza(BigDecimal frequenza) {
        this.frequenza = frequenza;
    }

    public BigDecimal getEnergiaReattiva() {
        return energiaReattiva;
    }

    public void setEnergiaReattiva(BigDecimal energiaReattiva) {
        this.energiaReattiva = energiaReattiva;
    }

    public Integer getInput1() {
        return input1;
    }

    public void setInput1(Integer input1) {
        this.input1 = input1;
    }
    
}
