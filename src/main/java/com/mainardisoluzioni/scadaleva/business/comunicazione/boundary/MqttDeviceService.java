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

import com.mainardisoluzioni.scadaleva.business.comunicazione.entity.MqttDevice;
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
public class MqttDeviceService {
    @PersistenceContext(unitName = "scadaleva_PU")
    EntityManager em;
    
    public List<MqttDevice> list() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<MqttDevice> query = cb.createQuery(MqttDevice.class);
        Root<MqttDevice> root = query.from(MqttDevice.class);
        CriteriaQuery<MqttDevice> select = query.select(root).distinct(true);
        
        return em.createQuery(select).getResultList();
    }
}
