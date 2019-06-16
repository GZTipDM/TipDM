package com.tipdm.framework.model.oauth;

import com.tipdm.framework.model.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Created by zhoulong on 2017/9/22.
 * E-mail:zhoulong8513@gmail.com
 * 资源所属域
 */
@Entity
@Table(name = "sys_resource_scope", uniqueConstraints = {@UniqueConstraint(columnNames = "name")})
public class ResourceScope extends IdEntity<Long>{

    private static final long serialVersionUID = 3755614193080764406L;

    @Column(nullable = false, length = 10)
    private String name;

    @Column(name = "can_remove")
    private Boolean canRemove = true;

    public ResourceScope(){

    }

    public ResourceScope(String name, Boolean canRemove){
        this.name = name;
        this.canRemove = canRemove;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getCanRemove() {
        return canRemove;
    }

    public void setCanRemove(Boolean canRemove) {
        this.canRemove = canRemove;
    }

    @Override
    public boolean equals(Object obj) {
        boolean isSame = false;
        if(null != obj && obj instanceof ResourceScope){
            ResourceScope scope = (ResourceScope) obj;
            isSame = this.name == scope.getName();
        }
        return isSame;
    }
}
