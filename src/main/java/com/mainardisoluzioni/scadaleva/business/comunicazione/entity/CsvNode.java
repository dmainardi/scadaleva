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
package com.mainardisoluzioni.scadaleva.business.comunicazione.entity;

import com.mainardisoluzioni.scadaleva.business.comunicazione.control.CategoriaVariabileProduzione;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

/**
 *
 * @author maina
 */
@Entity
public class CsvNode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(optional = false)
    private @NotNull CsvDevice csvDevice;
    
    @Enumerated
    @Column(nullable = false, columnDefinition = "smallint")
    private @NotNull CategoriaVariabileProduzione categoriaVariabileProduzione;
    
    /**
     * Indice è 0-based, ovvero inizia da zero.
     * La prima colonna del CVS avrà columnIndex = 0
     */
    @Column(nullable = false)
    private @NotNull @DecimalMin(value = "0", inclusive = false) Integer columnIndex;

    public CsvNode() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CsvDevice getCsvDevice() {
        return csvDevice;
    }

    public void setCsvDevice(CsvDevice csvDevice) {
        this.csvDevice = csvDevice;
    }

    public CategoriaVariabileProduzione getCategoriaVariabileProduzione() {
        return categoriaVariabileProduzione;
    }

    public void setCategoriaVariabileProduzione(CategoriaVariabileProduzione categoriaVariabileProduzione) {
        this.categoriaVariabileProduzione = categoriaVariabileProduzione;
    }

    public Integer getColumnIndex() {
        return columnIndex;
    }

    public void setColumnIndex(Integer columnIndex) {
        this.columnIndex = columnIndex;
    }
    
}
