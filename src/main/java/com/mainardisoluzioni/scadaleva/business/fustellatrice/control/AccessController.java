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
package com.mainardisoluzioni.scadaleva.business.fustellatrice.control;

import com.mainardisoluzioni.scadaleva.business.comunicazione.control.CategoriaVariabileProduzione;
import com.mainardisoluzioni.scadaleva.business.fustellatrice.boundary.AccessDeviceService;
import com.mainardisoluzioni.scadaleva.business.fustellatrice.boundary.EventoAccessService;
import com.mainardisoluzioni.scadaleva.business.fustellatrice.boundary.RicettaAccessService;
import com.mainardisoluzioni.scadaleva.business.fustellatrice.entity.AccessDevice;
import com.mainardisoluzioni.scadaleva.business.fustellatrice.entity.EventoAccess;
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
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.inject.Inject;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author maina
 */
//@Startup
@Singleton
public class AccessController {
    
    private Map<Macchina, LocalDateTime> ultimoTimestampProduzione;
    
    private List<Macchina> macchine;
    
    @Inject
    AccessDeviceService accessDeviceService;
    
    @Inject
    EventoProduzioneService eventoProduzioneService;
    
    @Inject
    EventoAccessService eventoAccessService;
    
    @Inject
    OrdineDiProduzioneService ordineDiProduzioneService;
    
    @Inject
    RicettaAccessService ricettaAccessService;
    
    @Inject
    ProduzioneGestionaleService produzioneGestionaleService;
    
    @PostConstruct
    public void init() {
        macchine = new ArrayList<>();
        List<AccessDevice> accessDevices = accessDeviceService.list();
        if (accessDevices != null && !accessDevices.isEmpty())
            macchine.addAll(accessDevices.stream().map(AccessDevice::getMacchina).collect(Collectors.toList()));
        
        ultimoTimestampProduzione = new HashMap<>();
        for (Macchina macchina : macchine) {
            LocalDateTime lastTimestampProduzione = eventoProduzioneService.getLastTimestampProduzione(macchina);
            if (lastTimestampProduzione != null)
                ultimoTimestampProduzione.put(macchina, lastTimestampProduzione);
        }
        controllaProduzioneFustellatrice();
    }
    
    //@Schedule(hour = "12", dayOfWeek = "Tue", persistent = false)
    public void controllaProduzioneFustellatrice() {
        if (macchine != null) {
            for (Macchina macchina : macchine) {
                List<EventoAccess> eventiAccess = eventoAccessService.list(ultimoTimestampProduzione.get(macchina));
                for (EventoAccess eventoAccess : eventiAccess) {
                    OrdineDiProduzione ordineDiProduzione = ordineDiProduzioneService.getLastOrdineDiProduzione(macchina.getCodice());
                    EventoProduzione eventoProduzione = EventoProduzioneController.createEventoProduzione(
                            macchina,
                            eventoAccess.getTermine().atZone(ZoneId.of("ECT", ZoneId.SHORT_IDS)).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime(),
                            eventoAccess.getColpi(),
                            ordineDiProduzione
                    );
                    RicettaAccess ricettaAccess = ricettaAccessService.find(eventoAccess.getCodiceRicettaAccess());
                    if (ricettaAccess != null) {
                        eventoProduzione.addParametroMacchinaProduzione(new ParametroMacchinaProduzione(CategoriaVariabileProduzione.RICETTA_IMPOSTATA_IN_LETTURA_CODICE, ricettaAccess.getCodice()));
                        eventoProduzione.addParametroMacchinaProduzione(new ParametroMacchinaProduzione(CategoriaVariabileProduzione.FUSTELLATRICE_TIPO, ricettaAccess.getTipo().toString()));
                        eventoProduzione.addParametroMacchinaProduzione(new ParametroMacchinaProduzione(CategoriaVariabileProduzione.FUSTELLATRICE_ALTEZZA, ricettaAccess.getAltezza().toString()));
                        eventoProduzione.addParametroMacchinaProduzione(new ParametroMacchinaProduzione(CategoriaVariabileProduzione.FUSTELLATRICE_PASSO, ricettaAccess.getPasso().toString()));
                        eventoProduzione.addParametroMacchinaProduzione(new ParametroMacchinaProduzione(CategoriaVariabileProduzione.FUSTELLATRICE_AVANZAMENTO, ricettaAccess.getAvanzamento().toString()));
                        eventoProduzione.addParametroMacchinaProduzione(new ParametroMacchinaProduzione(CategoriaVariabileProduzione.FUSTELLATRICE_AVANZAMENTO_2, ricettaAccess.getAvanzamento2().toString()));
                        eventoProduzione.addParametroMacchinaProduzione(new ParametroMacchinaProduzione(CategoriaVariabileProduzione.FUSTELLATRICE_STRATI, ricettaAccess.getStrati().toString()));
                        eventoProduzione.addParametroMacchinaProduzione(new ParametroMacchinaProduzione(CategoriaVariabileProduzione.FUSTELLATRICE_DELTA_1, ricettaAccess.getDelta1().toString()));
                        eventoProduzione.addParametroMacchinaProduzione(new ParametroMacchinaProduzione(CategoriaVariabileProduzione.FUSTELLATRICE_CORREZIONE, ricettaAccess.getCorrezione().toString()));
                        eventoProduzione.addParametroMacchinaProduzione(new ParametroMacchinaProduzione(CategoriaVariabileProduzione.FUSTELLATRICE_OFFSET_Z, ricettaAccess.getOffsetZ().toString()));
                    }
                    ultimoTimestampProduzione.put(macchina, eventoAccess.getCreazione());
                    eventoProduzioneService.save(eventoProduzione);
                    produzioneGestionaleService.save(
                            ProduzioneGestionaleController.createAndSave(
                                    macchina.getCodice(),
                                    eventoAccess.getCodiceRicettaAccess(),
                                    ordineDiProduzione,
                                    1,
                                    0,
                                    eventoAccess.getTermine(),
                                    eventoAccess.getColpi()
                            )
                    );
                }
            }
        }
    }
}
