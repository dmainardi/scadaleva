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
import com.mainardisoluzioni.scadaleva.business.reparto.entity.Macchina;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderColumn;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author maina
 */
@Entity
public class CsvDevice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(optional = false)
    private @NotNull Macchina macchina;
    
    @Column(nullable = false)
    private @NotBlank String path;
    
    @Column(nullable = false)
    private @NotNull Boolean intestazionePresente = Boolean.TRUE;
    
    @Column(nullable = false, length = 3)
    private @NotBlank String delimitatoreCsv = ";";
    
    @OrderColumn
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "csvDevice", orphanRemoval = true)
    private List<CsvNode> csvNodes = new ArrayList<>();

    public CsvDevice() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Macchina getMacchina() {
        return macchina;
    }

    public void setMacchina(Macchina macchina) {
        this.macchina = macchina;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Boolean getIntestazionePresente() {
        return intestazionePresente;
    }

    public void setIntestazionePresente(Boolean intestazionePresente) {
        this.intestazionePresente = intestazionePresente;
    }

    public String getDelimitatoreCsv() {
        return delimitatoreCsv;
    }

    public void setDelimitatoreCsv(String delimitatoreCsv) {
        this.delimitatoreCsv = delimitatoreCsv;
    }
    
    public void addCsvNode(CsvNode csvNode) {
        if (!csvNodes.contains(csvNode)) {
            csvNodes.add(csvNode);
            csvNode.setCsvDevice(this);
        }
    }
    
    public void removeCsvNode(CsvNode csvNode) {
        if (csvNodes.contains(csvNode)) {
            csvNodes.remove(csvNode);
            csvNode.setCsvDevice(null);
        }
    }

    public List<CsvNode> getCsvNodes() {
        return csvNodes;
    }
    
    /**
     * Find column index associated to given categoriaVariabileProduzione.
     * It answer the question: which column is 'timestamp'?
     * @param categoriaVariabileProduzione the categoria variabile produzione; must not be null
     * @return the column index. Null when categoriaVariabileProduzione is not present in device nodes.
     */
    public Integer getColumnIndex(@NotNull CategoriaVariabileProduzione categoriaVariabileProduzione) {
        CsvNode foundNode = csvNodes.stream().filter(n -> categoriaVariabileProduzione.equals(n.getCategoriaVariabileProduzione())).findFirst().orElse(null);
        if (foundNode != null)
            return foundNode.getColumnIndex();
        else
            return null;
    }
}
