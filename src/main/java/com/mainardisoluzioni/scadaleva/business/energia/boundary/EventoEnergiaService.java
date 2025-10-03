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
package com.mainardisoluzioni.scadaleva.business.energia.boundary;

import com.mainardisoluzioni.scadaleva.business.energia.entity.EventoEnergia;
import com.mainardisoluzioni.scadaleva.business.reparto.entity.Macchina;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adminavvimpa
 */
@Stateless
public class EventoEnergiaService {
    private static final Logger LOGGER = Logger.getLogger(EventoEnergiaService.class.getName());
    
    @PersistenceContext(unitName = "scadaleva_PU")
    EntityManager em;
    
    public void createAndSave(@NotNull Macchina macchina, @NotBlank String timestamp, @NotNull BigDecimal consumoWh) {
        try {
            EventoEnergia evento = new EventoEnergia();
            evento.setMacchina(macchina);
            evento.setConsumo(consumoWh);
            evento.setDataOra(LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.parseLong(timestamp)), ZoneId.of("UTC")));
            
            em.persist(evento);
            LOGGER.log(Level.FINE, "EventoEnergiaService::createAndSave - Evento energia scritto sul database");
        } catch (NumberFormatException | DateTimeException e) {
            LOGGER.log(Level.WARNING, "EventoEnergiaService:createAndSave - Errore: {0}", new Object[]{e.getLocalizedMessage()});
        }
    }
    
    /*public void createAndSave(@NotNull Macchina macchina, @NotNull BigDecimal consumoWh, BigDecimal potenzaIstantanea) {
        try {
            EventoEnergia evento = new EventoEnergia();
            evento.setMacchina(macchina);
            evento.setConsumo(consumoWh);
            evento.setPotenzaIstantanea(potenzaIstantanea);
            evento.setDataOra(LocalDateTime.now(ZoneId.of("UTC")));
            
            em.persist(evento);
        } catch (NumberFormatException | DateTimeException e) {
            LOGGER.log(Level.WARNING, "EventoEnergiaService:createAndSave - Errore: {0}", new Object[]{e.getLocalizedMessage()});
        }
    }*/
    
    public static EventoEnergia create(@NotNull Macchina macchina, BigDecimal potenzaIstantanea) {
        EventoEnergia evento = new EventoEnergia();
        evento.setMacchina(macchina);
        evento.setPotenzaIstantanea(potenzaIstantanea);
        evento.setDataOra(LocalDateTime.now(ZoneId.of("UTC")));
        
        return evento;
    }
    
    public void save(@NotNull EventoEnergia eventoEnergia) {
        em.persist(eventoEnergia);
    }
}
