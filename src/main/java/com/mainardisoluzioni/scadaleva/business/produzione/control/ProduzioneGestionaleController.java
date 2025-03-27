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

import com.mainardisoluzioni.scadaleva.business.produzione.entity.OrdineDiProduzione;
import com.mainardisoluzioni.scadaleva.business.produzione.entity.ProduzioneGestionale;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 *
 * @author maina
 */
public class ProduzioneGestionaleController {
    
    public static ProduzioneGestionale createAndSave(
            @NotBlank String codiceMacchina,
            @NotNull String codiceRicettaImpostata,
            OrdineDiProduzione ordineDiProduzione,
            @NotNull Integer funzionamentoCicloInAutomatico,
            @NotNull Integer presenzaAllarme,
            @NotNull LocalDateTime timestampProduzione,
            @NotNull Integer quantitaProdotta
    ) {
        ProduzioneGestionale produzioneGestionale = new ProduzioneGestionale();
        produzioneGestionale.setCodiceMacchina(codiceMacchina);
        produzioneGestionale.setCodiceRicettaImpostata(codiceRicettaImpostata);
        if (ordineDiProduzione != null) {
            produzioneGestionale.setDataOrdineDiProduzione(ordineDiProduzione.getDataOrdineDiProduzione());
            produzioneGestionale.setNumeroOrdineDiProduzione(ordineDiProduzione.getNumeroOrdineDiProduzione());
            produzioneGestionale.setQuantita(ordineDiProduzione.getQuantitaDaRealizzare().intValue());
            produzioneGestionale.setStatoLavorazione(ordineDiProduzione.getStatoOdl() != null && !ordineDiProduzione.getStatoOdl().isBlank() && ordineDiProduzione.getStatoOdl().equalsIgnoreCase("k") ? "T" : "L");
        }
        produzioneGestionale.setFunzionamentoCicloInAutomatico(funzionamentoCicloInAutomatico);
        produzioneGestionale.setOrarioProduzione(timestampProduzione.toLocalTime());
        produzioneGestionale.setDataProduzione(timestampProduzione.toLocalDate());
        produzioneGestionale.setPezziProdotti(quantitaProdotta);
        produzioneGestionale.setPresenzaAllarme(presenzaAllarme);
        
        return produzioneGestionale;
    }
}
