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
import java.time.LocalDateTime;

/**
 *
 * @author maina
 */
@Entity
@Table(name = "Ingressi")
public class EventoAccess {
    @Id
    @Column(name = "`Ora Inizio`", columnDefinition = "timestamp")
    private LocalDateTime creazione;
    
    @Column(name = "`Ora Fine`", columnDefinition = "timestamp")
    private LocalDateTime termine;
    
    private String tipo;
    
    @Column(name = "`Nome programma`")
    private String codiceRicettaAccess;
    
    private Integer colpi;

    public EventoAccess() {
    }

    public LocalDateTime getCreazione() {
        return creazione;
    }

    public void setCreazione(LocalDateTime creazione) {
        this.creazione = creazione;
    }

    public LocalDateTime getTermine() {
        return termine;
    }

    public void setTermine(LocalDateTime termine) {
        this.termine = termine;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCodiceRicettaAccess() {
        return codiceRicettaAccess;
    }

    public void setCodiceRicettaAccess(String codiceRicettaAccess) {
        this.codiceRicettaAccess = codiceRicettaAccess;
    }

    public Integer getColpi() {
        return colpi;
    }

    public void setColpi(Integer colpi) {
        this.colpi = colpi;
    }
    
}
