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
package com.mainardisoluzioni.scadaleva.business.produzione.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;

/**
 *
 * @author adminavvimpa
 */
@Entity
@Table(name = "dfi4godl")
public class OrdineDiProduzione {
    
    @Id
    @Column(name = "numodl_0", length = 15)
    private String numeroOrdineDiLavoro;
    
    @Column(name = "dataodl_0")
    private LocalDate dataOrdineDiLavoro;
    
    @Column(name = "codmac_0", length = 7)
    private String codiceMacchina;
    
    @Column(name = "codart_0", length = 20)
    private String codiceArticolo;
    
    @Column(name = "program_0", length = 25)
    private String codiceRicettaRichiesta;
    
    @Column(name = "statoodl_0", length = 1)
    private String statoOdl;
    
    @Column(name = "lotpro_0", length = 15)
    private String lotto;
    
    @Column(name = "qtadapro_0")
    private Double quantitaDaRealizzare;

    public OrdineDiProduzione() {
    }

    public String getNumeroOrdineDiLavoro() {
        return numeroOrdineDiLavoro;
    }

    public void setNumeroOrdineDiLavoro(String numeroOrdineDiLavoro) {
        this.numeroOrdineDiLavoro = numeroOrdineDiLavoro;
    }

    public LocalDate getDataOrdineDiLavoro() {
        return dataOrdineDiLavoro;
    }

    public void setDataOrdineDiLavoro(LocalDate dataOrdineDiLavoro) {
        this.dataOrdineDiLavoro = dataOrdineDiLavoro;
    }

    public String getCodiceMacchina() {
        return codiceMacchina;
    }

    public void setCodiceMacchina(String codiceMacchina) {
        this.codiceMacchina = codiceMacchina;
    }

    public String getCodiceArticolo() {
        return codiceArticolo;
    }

    public void setCodiceArticolo(String codiceArticolo) {
        this.codiceArticolo = codiceArticolo;
    }

    public String getCodiceRicettaRichiesta() {
        return codiceRicettaRichiesta;
    }

    public void setCodiceRicettaRichiesta(String codiceRicettaRichiesta) {
        this.codiceRicettaRichiesta = codiceRicettaRichiesta;
    }

    public String getStatoOdl() {
        return statoOdl;
    }

    public void setStatoOdl(String statoOdl) {
        this.statoOdl = statoOdl;
    }

    public String getLotto() {
        return lotto;
    }

    public void setLotto(String lotto) {
        this.lotto = lotto;
    }

    public Double getQuantitaDaRealizzare() {
        return quantitaDaRealizzare;
    }

    public void setQuantitaDaRealizzare(Double quantitaDaRealizzare) {
        this.quantitaDaRealizzare = quantitaDaRealizzare;
    }
    
}
