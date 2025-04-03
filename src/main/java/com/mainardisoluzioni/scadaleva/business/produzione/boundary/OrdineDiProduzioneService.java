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

import com.mainardisoluzioni.scadaleva.business.produzione.entity.OrdineDiProduzione;
import com.mainardisoluzioni.scadaleva.business.produzione.entity.OrdineDiProduzione_;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

/**
 *
 * @author adminavvimpa
 */
@Stateless
public class OrdineDiProduzioneService {
    @PersistenceContext(unitName = "scadaleva_readOnly_PU")
    EntityManager em;
    
    /**
     * Restituisce l'ultimo ordine di produzione (il più recente) in lavorazione della macchina specificata
     * @param codiceMacchina codice identificativo del macchinario
     * @return ordine di produzione più recente, opppure NULL se non ci sono ordini di produzione associati alla macchina
     */
    public OrdineDiProduzione getLastOrdineDiProduzione(@NotBlank String codiceMacchina) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<OrdineDiProduzione> query = cb.createQuery(OrdineDiProduzione.class);
        Root<OrdineDiProduzione> root = query.from(OrdineDiProduzione.class);
        CriteriaQuery<OrdineDiProduzione> select = query.select(root).distinct(true);
        query.where(
                cb.like(root.get(OrdineDiProduzione_.codiceMacchina), codiceMacchina),
                cb.like(cb.upper(root.get(OrdineDiProduzione_.statoOdl)), "L")
        );
        query.orderBy(cb.desc(root.get(OrdineDiProduzione_.dataOrdineDiProduzione)));
        TypedQuery<OrdineDiProduzione> typedQuery = em.createQuery(select);
        typedQuery.setMaxResults(1);
        List<OrdineDiProduzione> resultList = typedQuery.getResultList();
        if (resultList == null || resultList.isEmpty())
            return null;
        else
            return resultList.get(0);
    }
}
