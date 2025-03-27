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
package com.mainardisoluzioni.scadaleva.business.fustellatrice.entity;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;

/**
 *
 * @author maina
 */
@StaticMetamodel(RicettaAccess.class)
public class RicettaAccess_ {
    public static volatile SingularAttribute<RicettaAccess, String> codice;
    public static volatile SingularAttribute<RicettaAccess, Integer> tipo;
    public static volatile SingularAttribute<RicettaAccess, Double> altezza;
    public static volatile SingularAttribute<RicettaAccess, Double> passo;
    public static volatile SingularAttribute<RicettaAccess, Double> avanzamento;
    public static volatile SingularAttribute<RicettaAccess, Double> avanzamento2;
    public static volatile SingularAttribute<RicettaAccess, Integer> strati;
    public static volatile SingularAttribute<RicettaAccess, Double> delta1;
    public static volatile SingularAttribute<RicettaAccess, Double> correzione;
    public static volatile SingularAttribute<RicettaAccess, Double> offsetZ;
}
