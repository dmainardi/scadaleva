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
package com.mainardisoluzioni.scadaleva.business.produzione.entity;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 *
 * @author maina
 */
@StaticMetamodel(ProduzioneGestionale.class)
public class ProduzioneGestionale_ {
    public static volatile SingularAttribute<ProduzioneGestionale, String> codiceMacchina;
    public static volatile SingularAttribute<ProduzioneGestionale, String> numeroOrdineDiProduzione;
    public static volatile SingularAttribute<ProduzioneGestionale, LocalDate> dataOrdineDiProduzione;
    public static volatile SingularAttribute<ProduzioneGestionale, String> codiceArticolo;
    public static volatile SingularAttribute<ProduzioneGestionale, String> nomeArticolo;
    public static volatile SingularAttribute<ProduzioneGestionale, BigDecimal> quantita;
    public static volatile SingularAttribute<ProduzioneGestionale, String> fase;
    public static volatile SingularAttribute<ProduzioneGestionale, String> codiceOperazione;
    public static volatile SingularAttribute<ProduzioneGestionale, String> nomeOperazione;
    public static volatile SingularAttribute<ProduzioneGestionale, String> codiceAttrezzatura;
    public static volatile SingularAttribute<ProduzioneGestionale, String> nomeAttrezzatura;
    public static volatile SingularAttribute<ProduzioneGestionale, Integer> impronte;
    public static volatile SingularAttribute<ProduzioneGestionale, LocalDate> dataProduzione;
    public static volatile SingularAttribute<ProduzioneGestionale, LocalTime> orarioProduzione;
    public static volatile SingularAttribute<ProduzioneGestionale, String> statoLavorazione;
    public static volatile SingularAttribute<ProduzioneGestionale, String> serialeEtichetta;
    public static volatile SingularAttribute<ProduzioneGestionale, BigDecimal> pezziProdottiCorrettamente;
    public static volatile SingularAttribute<ProduzioneGestionale, BigDecimal> pezziProdottiDiScarto;
}
