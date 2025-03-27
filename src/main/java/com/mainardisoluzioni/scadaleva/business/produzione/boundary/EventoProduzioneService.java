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
package com.mainardisoluzioni.scadaleva.business.produzione.boundary;

import com.mainardisoluzioni.scadaleva.business.produzione.entity.EventoProduzione;
import com.mainardisoluzioni.scadaleva.business.produzione.entity.EventoProduzione_;
import com.mainardisoluzioni.scadaleva.business.reparto.entity.Macchina;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 *
 * @author adminavvimpa
 */
@Stateless
public class EventoProduzioneService {
    @PersistenceContext(unitName = "scadaleva_PU")
    EntityManager em;
    
    public EventoProduzione save(EventoProduzione eventoProduzione) {
        if (eventoProduzione.getId() == null)
            em.persist(eventoProduzione);
        else
            return em.merge(eventoProduzione);
        
        return eventoProduzione;
    }
    
    public LocalDateTime getLastTimestampProduzione(@NotNull Macchina macchina) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<LocalDateTime> query = cb.createQuery(LocalDateTime.class);
        Root<EventoProduzione> root = query.from(EventoProduzione.class);
        query.select(cb.greatest(root.get(EventoProduzione_.timestampProduzione)));
        query.where(cb.equal(root.get(EventoProduzione_.macchina), macchina));
        try {
            return em.createQuery(query).getSingleResult();
        } catch (NoResultException | NonUniqueResultException e) {
            return null;
        }
    }
}
