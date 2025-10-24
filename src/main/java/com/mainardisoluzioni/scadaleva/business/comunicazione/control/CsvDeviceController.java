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
package com.mainardisoluzioni.scadaleva.business.comunicazione.control;

import com.mainardisoluzioni.scadaleva.business.comunicazione.entity.CsvDevice;
import com.mainardisoluzioni.scadaleva.business.comunicazione.entity.CsvNode;
import com.mainardisoluzioni.scadaleva.business.fustellatrice.entity.RicettaAccess;
import com.mainardisoluzioni.scadaleva.business.produzione.boundary.EventoProduzioneService;
import com.mainardisoluzioni.scadaleva.business.produzione.boundary.OrdineDiProduzioneService;
import com.mainardisoluzioni.scadaleva.business.produzione.boundary.ProduzioneGestionaleService;
import com.mainardisoluzioni.scadaleva.business.produzione.control.EventoProduzioneController;
import com.mainardisoluzioni.scadaleva.business.produzione.control.ProduzioneGestionaleController;
import com.mainardisoluzioni.scadaleva.business.produzione.entity.EventoProduzione;
import com.mainardisoluzioni.scadaleva.business.produzione.entity.OrdineDiProduzione;
import com.mainardisoluzioni.scadaleva.business.produzione.entity.ParametroMacchinaProduzione;
import com.mainardisoluzioni.scadaleva.business.reparto.entity.Macchina;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotNull;
import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author maina
 */
@Stateless
public class CsvDeviceController {
    private static final Logger LOGGER = Logger.getLogger(CsvDeviceController.class.getName());
    
    @Inject
    EventoProduzioneService eventoProduzioneService;
    
    @Inject
    OrdineDiProduzioneService ordineDiProduzioneService;
    
    @Inject
    ProduzioneGestionaleService produzioneGestionaleService;
    
    public void leggiDatiDaCsvEPopolaEventoProduzione(@NotNull CsvDevice csvDevice) {
        try (Scanner scanner = new Scanner(new File(csvDevice.getPath()))) {
            boolean intestazioneScansita = false;
            Macchina macchina = csvDevice.getMacchina();
            LocalDateTime lastTimestampProduzione = eventoProduzioneService.getLastTimestampProduzione(macchina);
            Integer timestampColumnIndex = csvDevice.getColumnIndex(CategoriaVariabileProduzione.TIMESTAMP);
            Integer contapezziColumIndex = csvDevice.getColumnIndex(CategoriaVariabileProduzione.CONTAPEZZI);
            Integer funzionamentoCicloAutomaticoColumIndex = csvDevice.getColumnIndex(CategoriaVariabileProduzione.FUNZIONAMENTO_CICLO_AUTOMATICO);
            Integer presenzaAllarmeColumIndex = csvDevice.getColumnIndex(CategoriaVariabileProduzione.PRESENZA_ALLARME);
            while (timestampColumnIndex != null && scanner.hasNextLine()) {
                if (intestazioneScansita || !csvDevice.getIntestazionePresente()) {
                    String line = scanner.nextLine();
                    String[] columns = line.split(csvDevice.getDelimitatoreCsv());
                    try {
                        LocalDateTime readTimestamp = LocalDateTime.parse(columns[timestampColumnIndex]);
                        if (readTimestamp.isBefore(lastTimestampProduzione))
                            continue;
                        else {
                            OrdineDiProduzione ordineDiProduzione = ordineDiProduzioneService.getLastOrdineDiProduzione(macchina.getCodice());
                            
                            int quantita = ottieniValoreDatoIndice(contapezziColumIndex, columns);
                            int funzionamentoCicloAutomatico = ottieniValoreDatoIndice(funzionamentoCicloAutomaticoColumIndex, columns);
                            int presenzaAllarme = ottieniValoreDatoIndice(presenzaAllarmeColumIndex, columns);
                            if (presenzaAllarme == 0)
                                presenzaAllarme = 1;
                            else
                                presenzaAllarme = 0;
                            
                            EventoProduzione eventoProduzione = EventoProduzioneController.createEventoProduzione(
                                    macchina,
                                    readTimestamp,
                                    quantita,
                                    ordineDiProduzione
                            );
                            for (CsvNode csvNode : csvDevice.getCsvNodes()) {
                                if (
                                        !csvNode.getCategoriaVariabileProduzione().equals(CategoriaVariabileProduzione.TIMESTAMP)
                                        &&
                                        !csvNode.getCategoriaVariabileProduzione().equals(CategoriaVariabileProduzione.CONTAPEZZI)
                                        ) {
                                    Integer columnIndex = csvNode.getColumnIndex();
                                    if (columns.length > columnIndex) {
                                        String valueStr = columns[columnIndex];
                                        eventoProduzione.addParametroMacchinaProduzione(new ParametroMacchinaProduzione(csvNode.getCategoriaVariabileProduzione(), valueStr));
                                    }
                                }
                            }
                            
                            eventoProduzioneService.save(eventoProduzione);
                            
                            String ricetta = "---";
                            Integer ricettaColumnIndex = csvDevice.getColumnIndex(CategoriaVariabileProduzione.RICETTA_IMPOSTATA_IN_LETTURA_CODICE);
                            if (ricettaColumnIndex != null) {
                                ricetta = columns[ricettaColumnIndex];
                                if (ricetta == null || ricetta.isBlank()) {
                                    ricetta = "---";
                                }
                            }
                            produzioneGestionaleService.save(
                                    ProduzioneGestionaleController.createAndSave(
                                            macchina.getCodice(),
                                            ricetta,
                                            ordineDiProduzione,
                                            funzionamentoCicloAutomatico,
                                            presenzaAllarme,
                                            readTimestamp,
                                            quantita
                                    )
                            );
                        }
                    } catch (DateTimeParseException ex) {
                        LOGGER.log(Level.WARNING, "CsvDeviceController:leggiDatiDaCsvEPopolaEventoProduzione - Errore nella lettura del timestamp: {0}", new Object[]{ex.getLocalizedMessage()});
                    }
                }
                intestazioneScansita = true;
            }
        } catch (FileNotFoundException ex) {
            LOGGER.log(Level.WARNING, "CsvDeviceController:leggiDatiDaCsvEPopolaEventoProduzione - Errore: {0}", new Object[]{ex.getLocalizedMessage()});
        }
    }
    
    /**
     * La funzione esegue:
     * - controlla se index è nullo oppure no. Se è nullo il risultato della funzione è 1
     * - controlla il valore della colonna. Se tale valore è nullo, è vuoto, o ci sono errori la funzione restituisce 0
     * @param index
     * @param columns
     * @return il valore in formato numerico, 1 se non è stato "mappato", 0 se il valore non c'è o ci sono problemi
     */
    private int ottieniValoreDatoIndice(Integer index, String[] columns) {
        int result = 1;
        if (index != null) {
            String resultStr = columns[index];
            if (resultStr != null && !resultStr.isBlank()) {
                try {
                    result = Integer.parseInt(resultStr);
                } catch (NumberFormatException e) {
                    result = 0;
                }
            }
            else
                result = 0;
        }
        
        return result;
    }
}
