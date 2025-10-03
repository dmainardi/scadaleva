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
package com.mainardisoluzioni.scadaleva.business.energia.entity;

import jakarta.json.bind.annotation.JsonbNillable;
import jakarta.json.bind.annotation.JsonbProperty;
import java.math.BigDecimal;

/**
 *
 * @author adminavvimpa
 */
public class ContenutoTelemetryTraceAndFollow {
    @JsonbProperty("energy_consumption")
    private BigDecimal consumoWh;
    
    @JsonbNillable
    @JsonbProperty("voltage1")
    private BigDecimal tensione1;
    
    @JsonbNillable
    @JsonbProperty("voltage2")
    private BigDecimal tensione2;
    
    @JsonbNillable
    @JsonbProperty("voltage3")
    private BigDecimal tensione3;
    
    @JsonbNillable
    @JsonbProperty("current1")
    private BigDecimal corrente1;
    
    @JsonbNillable
    @JsonbProperty("current2")
    private BigDecimal corrente2;
    
    @JsonbNillable
    @JsonbProperty("current3")
    private BigDecimal corrente3;
    
    @JsonbNillable
    @JsonbProperty("current_tot")
    private BigDecimal corrente;
    
    @JsonbNillable
    @JsonbProperty("active_power1")
    private BigDecimal potenza1;
    
    @JsonbNillable
    @JsonbProperty("active_power2")
    private BigDecimal potenza2;
    
    @JsonbNillable
    @JsonbProperty("active_power3")
    private BigDecimal potenza3;
    
    @JsonbNillable
    @JsonbProperty("active_power_tot")
    private BigDecimal potenza;
    
    @JsonbNillable
    @JsonbProperty("reactive_power1")
    private BigDecimal potenzaReattiva1;
    
    @JsonbNillable
    @JsonbProperty("reactive_power2")
    private BigDecimal potenzaReattiva2;
    
    @JsonbNillable
    @JsonbProperty("reactive_power3")
    private BigDecimal potenzaReattiva3;
    
    @JsonbNillable
    @JsonbProperty("reactive_power_tot")
    private BigDecimal potenzaReattiva;
    
    @JsonbNillable
    @JsonbProperty("apparent_power1")
    private BigDecimal potenzaApperente1;
    
    @JsonbNillable
    @JsonbProperty("apparent_power2")
    private BigDecimal potenzaApperente2;
    
    @JsonbNillable
    @JsonbProperty("apparent_power3")
    private BigDecimal potenzaApperente3;
    
    @JsonbNillable
    @JsonbProperty("apparent_power_tot")
    private BigDecimal potenzaApperente;
    
    @JsonbNillable
    @JsonbProperty("pf1")
    private BigDecimal fattoreDiPotenza1;
    
    @JsonbNillable
    @JsonbProperty("pf2")
    private BigDecimal fattoreDiPotenza2;
    
    @JsonbNillable
    @JsonbProperty("pf3")
    private BigDecimal fattoreDiPotenza3;
    
    @JsonbNillable
    @JsonbProperty("pf_tot")
    private BigDecimal fattoreDiPotenza;
    
    @JsonbNillable
    @JsonbProperty("phase1")
    private BigDecimal sfasamento1;
    
    @JsonbNillable
    @JsonbProperty("phase2")
    private BigDecimal sfasamento2;
    
    @JsonbNillable
    @JsonbProperty("phase3")
    private BigDecimal sfasamento3;
    
    @JsonbNillable
    @JsonbProperty("frequency")
    private BigDecimal frequenza;
    
    @JsonbNillable
    @JsonbProperty("reactive_energy_consumption")
    private BigDecimal energiaReattiva; // energia reattiva consumata nell’arco di tempo di un pacchetto in VARh
    
    @JsonbNillable
    @JsonbProperty("input1_value")
    private Integer input1;
    
    @JsonbNillable
    @JsonbProperty("input2_value")
    private Integer input2;
    
    @JsonbNillable
    @JsonbProperty("input3_value")
    private Integer input3;
    
    @JsonbNillable
    @JsonbProperty("input4_value")
    private Integer input4;

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

    public Integer getInput2() {
        return input2;
    }

    public void setInput2(Integer input2) {
        this.input2 = input2;
    }

    public Integer getInput3() {
        return input3;
    }

    public void setInput3(Integer input3) {
        this.input3 = input3;
    }

    public Integer getInput4() {
        return input4;
    }

    public void setInput4(Integer input4) {
        this.input4 = input4;
    }
    
}
