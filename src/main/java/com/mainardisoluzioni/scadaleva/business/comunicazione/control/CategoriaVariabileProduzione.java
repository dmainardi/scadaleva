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
package com.mainardisoluzioni.scadaleva.business.comunicazione.control;

/**
 *
 * @author adminavvimpa
 */
public enum CategoriaVariabileProduzione {
    CONTAPEZZI(0),
    RICETTA_RICHIESTA_CODICE(1),                    // la ricetta indicata sull'ordine di produzione
    RICETTA_IMPOSTATA_IN_SCRITTURA_CODICE(2),       // la ricetta che compare sul HMI
    CODICE_ORDINE_DI_PRODUZIONE(3),
    DATA_ORDINE_DI_PRODUZIONE(4),
    CODICE_ARTICOLO(5),
    LOTTO(6),
    QUANTITA_RICHIESTA(7),
    QUANTITA_PRODOTTA_CORRETTAMENTE(8),             // la quantità prodotta che compare sul HMI
    FUNZIONAMENTO_CICLO_AUTOMATICO(9),
    PRESENZA_ALLARME(10),
    CODICE_ALLARME(11),
    RICETTA_IMPOSTATA_IN_LETTURA_CODICE(12),        // la ricetta impostata letta dalla macchina
    RISCALDO_INSERITO(13),
    TEMPO_CICLO(14),
    TEMPO_RISCALDO(15),
    TEMPO_PRESSATA(16),
    PERCENTUALE_RISCALDO_ZONA_1(17),
    PERCENTUALE_RISCALDO_ZONA_2(18),
    PERCENTUALE_RISCALDO_ZONA_3(19),
    PERCENTUALE_RISCALDO_ZONA_4(20),
    PERCENTUALE_RISCALDO_ZONA_5(21),
    SOGLIA_PIROMETRO_1(22),
    SOGLIA_PIROMETRO_2(23),
    SOGLIA_PIROMETRO_3(24),
    SOGLIA_PIROMETRO_4(25),
    TEMPERATURA_RASCHIATORE_DI_APPLICAZIONE_SINISTRA(26),
    TEMPERATURA_RASCHIATORE_DI_APPLICAZIONE_DESTRA(27),
    TEMPERATURA_RASCHIATORE_DI_RITORNO_SINISTRA(28),
    TEMPERATURA_RASCHIATORE_DI_RITORNO_DESTRA(29),
    TEMPERATURA_RULLO_DI_APPLICAZIONE_SINISTRA(30),
    TEMPERATURA_RULLO_DI_APPLICAZIONE_DESTRA(31),
    FUSTELLATRICE_TIPO(32),
    FUSTELLATRICE_ALTEZZA(33),
    FUSTELLATRICE_PASSO(34),
    FUSTELLATRICE_AVANZAMENTO(35),
    FUSTELLATRICE_AVANZAMENTO_2(36),
    FUSTELLATRICE_STRATI(37),
    FUSTELLATRICE_DELTA_1(38),
    FUSTELLATRICE_CORREZIONE(39),
    FUSTELLATRICE_OFFSET_Z(40),
    TIMESTAMP(41);
    
    private final int value;
    
    private CategoriaVariabileProduzione(int value) {
        this.value = value;
    }
}
