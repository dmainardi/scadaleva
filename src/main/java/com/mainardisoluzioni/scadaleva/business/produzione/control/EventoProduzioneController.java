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
package com.mainardisoluzioni.scadaleva.business.produzione.control;

import com.mainardisoluzioni.scadaleva.business.produzione.entity.EventoProduzione;
import com.mainardisoluzioni.scadaleva.business.produzione.entity.OrdineDiProduzione;
import com.mainardisoluzioni.scadaleva.business.reparto.entity.Macchina;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 *
 * @author maina
 */
public class EventoProduzioneController {
    
    public static EventoProduzione createEventoProduzione(@NotNull Macchina macchina, @NotNull LocalDateTime timestampProduzione, @NotNull Integer quantitaProdotta, OrdineDiProduzione ordineDiProduzione) {
        EventoProduzione eventoProduzione = new EventoProduzione();
        if (ordineDiProduzione != null)
            eventoProduzione.setNumeroOrdineDiProduzione(ordineDiProduzione.getNumeroOrdineDiProduzione());
        eventoProduzione.setMacchina(macchina);
        eventoProduzione.setTimestampProduzione(timestampProduzione);
        eventoProduzione.setQuantita(quantitaProdotta);
        
        return eventoProduzione;
    }
}
