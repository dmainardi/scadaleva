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
package com.mainardisoluzioni.scadaleva.business.fustellatrice.boundary;

import com.mainardisoluzioni.scadaleva.business.fustellatrice.entity.RicettaAccess;
import com.mainardisoluzioni.scadaleva.business.fustellatrice.entity.RicettaAccess_;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

/**
 *
 * @author maina
 */
@Stateless
public class RicettaAccessService {
    @PersistenceContext(unitName = "scadaleva_access_readOnly_archivioLavoro_PU")
    EntityManager em;
    
    public RicettaAccess find(@NotBlank String codice) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<RicettaAccess> query = cb.createQuery(RicettaAccess.class);
        Root<RicettaAccess> root = query.from(RicettaAccess.class);
        CriteriaQuery<RicettaAccess> select = query.select(root).distinct(true);
        query.where(cb.like(cb.lower(root.get(RicettaAccess_.codice)), codice.toLowerCase()));

        TypedQuery<RicettaAccess> typedQuery = em.createQuery(select);
        try {
            return typedQuery.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            return typedQuery.getResultList().get(typedQuery.getResultList().size() - 1);
        }
    }
        
    public List<RicettaAccess> list() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<RicettaAccess> query = cb.createQuery(RicettaAccess.class);
        Root<RicettaAccess> root = query.from(RicettaAccess.class);
        CriteriaQuery<RicettaAccess> select = query.select(root).distinct(true);
        
        return em.createQuery(select).getResultList();
    }
}
