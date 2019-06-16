package com.tipdm.framework.repository.dmserver.impl;

import com.tipdm.framework.model.dmserver.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by TipDM on 2017/1/3.
 * E-mail:devp@tipdm.com
 */
public class ComponentRepositoryImpl {

    @PersistenceContext
    private EntityManager em;

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public void merge(Component component){
        em.merge(component);
    }

}
