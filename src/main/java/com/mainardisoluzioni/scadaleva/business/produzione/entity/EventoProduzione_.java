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

import com.mainardisoluzioni.scadaleva.business.reparto.entity.Macchina;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 *
 * @author adminavvimpa
 */
@StaticMetamodel(EventoProduzione.class)
public class EventoProduzione_ {
    public static volatile SingularAttribute<EventoProduzione, Macchina> macchina;
    public static volatile SingularAttribute<EventoProduzione, LocalDate> dataProduzione;
    public static volatile SingularAttribute<EventoProduzione, LocalTime> oraProduzione;
    public static volatile SingularAttribute<EventoProduzione, Integer> quantita;
}
