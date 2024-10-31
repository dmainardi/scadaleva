/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
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
    @PersistenceContext
    EntityManager em;
    
    public List<MqttDevice> list() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<MqttDevice> query = cb.createQuery(MqttDevice.class);
        Root<MqttDevice> root = query.from(MqttDevice.class);
        CriteriaQuery<MqttDevice> select = query.select(root).distinct(true);
        
        return em.createQuery(select).getResultList();
    }
}
