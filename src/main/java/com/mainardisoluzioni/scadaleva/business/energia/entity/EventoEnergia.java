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

import com.mainardisoluzioni.scadaleva.business.reparto.entity.Macchina;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 *
 * @author adminavvimpa
 */
@Entity
public class EventoEnergia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(optional = false)
    private @NotNull Macchina macchina;
    
    @Column(nullable = false, columnDefinition = "timestamp")
    private @NotNull LocalDateTime dataOra;
    
    /**
     * Energia attiva consumata nell’arco di tempo di un pacchetto in Wh
     */
    @Column(nullable = false, scale = 10, precision = 15)
    @DecimalMin("0")
    private @NotNull BigDecimal consumo;
    
    /**
     * Potenza istantanea, espressa in W
     */
    @Column(scale = 10, precision = 15)
    @DecimalMin("0")
    private BigDecimal potenzaIstantanea;

    public EventoEnergia() {
        consumo = BigDecimal.ZERO;
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

    public LocalDateTime getDataOra() {
        return dataOra;
    }

    public void setDataOra(LocalDateTime dataOra) {
        this.dataOra = dataOra;
    }

    public BigDecimal getConsumo() {
        return consumo;
    }

    public void setConsumo(BigDecimal consumo) {
        this.consumo = consumo;
    }

    public BigDecimal getPotenzaIstantanea() {
        return potenzaIstantanea;
    }

    public void setPotenzaIstantanea(BigDecimal potenzaIstantanea) {
        this.potenzaIstantanea = potenzaIstantanea;
    }
    
}
