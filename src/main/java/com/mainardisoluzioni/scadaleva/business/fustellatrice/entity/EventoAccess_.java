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
import java.time.LocalDateTime;

/**
 *
 * @author maina
 */
@StaticMetamodel(EventoAccess.class)
public class EventoAccess_ {
    public static volatile SingularAttribute<EventoAccess, LocalDateTime> creazione;
    public static volatile SingularAttribute<EventoAccess, LocalDateTime> termine;
    public static volatile SingularAttribute<EventoAccess, String> tipo;
    public static volatile SingularAttribute<EventoAccess, String> codiceRicettaAccess;
    public static volatile SingularAttribute<EventoAccess, Integer> colpi;
}
