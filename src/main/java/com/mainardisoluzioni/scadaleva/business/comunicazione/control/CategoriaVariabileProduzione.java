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
    RICETTA_RICHIESTA_CODICE(1),
    RICETTA_IMPOSTATA_CODICE(2),
    CODICE_ORDINE_DI_PRODUZIONE(3),
    DATA_ORDINE_DI_PRODUZIONE(4),
    CODICE_ARTICOLO(5),
    LOTTO(6),
    QUANTITA_RICHIESTA(7),
    QUANTITA_PRODOTTA_CORRETTAMENTE(8);
    
    private final int value;
    
    private CategoriaVariabileProduzione(int value) {
        this.value = value;
    }
}
