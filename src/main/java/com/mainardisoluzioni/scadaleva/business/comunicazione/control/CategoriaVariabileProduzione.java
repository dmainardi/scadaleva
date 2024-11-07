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
    STATO_MACCHINA(0),
    CONTAPEZZI(1),
    RICETTA_NOME(2),
    RICETTA_CODICE(3);
    
    private final int value;
    
    private CategoriaVariabileProduzione(int value) {
        this.value = value;
    }
}
