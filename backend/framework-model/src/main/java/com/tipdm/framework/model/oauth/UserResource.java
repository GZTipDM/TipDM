package com.tipdm.framework.model.oauth;

import com.tipdm.framework.model.IdEntity;

import javax.persistence.*;

/**
 * Created by zhoulong on 2017/3/1.
 * E-mail:zhoulong8513@gmail.com
 * 可查看用户资源
 */
@Entity
@Table(name = "sys_role_user_resource")
public class UserResource extends IdEntity<Long>{

    @Column(name = "role_id")
    private Long roleId;

    @Column(name = "org_id")
    private Long orgId;

    @Column(name = "user_id")
    private Long userId;

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}
