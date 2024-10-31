/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
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

    public EventoEnergia() {
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
    
}
