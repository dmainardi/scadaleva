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
package com.mainardisoluzioni.scadaleva.business.produzione.boundary;

import com.mainardisoluzioni.scadaleva.business.produzione.entity.ProduzioneGestionale;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

/**
 *
 * @author maina
 */
@Stateless
public class ProduzioneGestionaleService {
    @PersistenceContext(unitName = "scadaleva_PU")
    EntityManager em;
    
    public ProduzioneGestionale save(ProduzioneGestionale produzioneGestionale) {
        if (produzioneGestionale.getId() == null)
            em.persist(produzioneGestionale);
        else
            return em.merge(produzioneGestionale);
        
        return produzioneGestionale;
    }
}
