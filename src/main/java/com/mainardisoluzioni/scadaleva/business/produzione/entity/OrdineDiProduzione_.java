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

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.time.LocalDate;

/**
 *
 * @author adminavvimpa
 */
@StaticMetamodel(OrdineDiProduzione.class)
public class OrdineDiProduzione_ {
    public static volatile SingularAttribute<OrdineDiProduzione, LocalDate> dataOrdineDiProduzione;
    public static volatile SingularAttribute<OrdineDiProduzione, String> codiceMacchina;
    public static volatile SingularAttribute<OrdineDiProduzione, String> statoOdl;
}
