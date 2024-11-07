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
package com.mainardisoluzioni.scadaleva.business.comunicazione.boundary;

import com.mainardisoluzioni.scadaleva.business.comunicazione.entity.OpcuaDevice;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;

/**
 *
 * @author adminavvimpa
 */
@Stateless
public class OpcuaDeviceService {
    @PersistenceContext
    EntityManager em;
    
    public List<OpcuaDevice> list() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<OpcuaDevice> query = cb.createQuery(OpcuaDevice.class);
        Root<OpcuaDevice> root = query.from(OpcuaDevice.class);
        CriteriaQuery<OpcuaDevice> select = query.select(root).distinct(true);
        
        return em.createQuery(select).getResultList();
    }
}
