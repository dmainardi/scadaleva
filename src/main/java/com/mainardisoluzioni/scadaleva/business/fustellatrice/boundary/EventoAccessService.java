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

import com.mainardisoluzioni.scadaleva.business.fustellatrice.entity.EventoAccess;
import com.mainardisoluzioni.scadaleva.business.fustellatrice.entity.EventoAccess_;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author maina
 */
@Stateless
public class EventoAccessService {
    @PersistenceContext(unitName = "scadaleva_access_readOnly_archivioStorico_PU")
    EntityManager em;
    
    /**
     * Elenco degli eventi di Access
     * @param limiteInferioreCreazione se presente applica un filtro in modo che vengano restituiti tutti gli eventi la cui data di produzione è successiva (maggiore) a quanto indicato
     * @return Una lista degli eventi Access in cui il campo 'colpi' non è nullo ed è maggiore di zero
     */
    public List<EventoAccess> list(LocalDateTime limiteInferioreCreazione) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<EventoAccess> query = cb.createQuery(EventoAccess.class);
        Root<EventoAccess> root = query.from(EventoAccess.class);
        CriteriaQuery<EventoAccess> select = query.select(root).distinct(true);
        
        List<Predicate> conditions = new ArrayList<>();        
        conditions.add(cb.isNotNull(root.get(EventoAccess_.colpi)));
        conditions.add(cb.greaterThan(root.get(EventoAccess_.colpi), 0));
        if (limiteInferioreCreazione != null)
            conditions.add(cb.greaterThan(root.get(EventoAccess_.creazione), limiteInferioreCreazione));
        if (!conditions.isEmpty())
            query.where(conditions.toArray(Predicate[]::new));
        
        return em.createQuery(select).getResultList();
    }
    
}
