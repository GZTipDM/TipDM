package com.tipdm.framework.common.token.model;

import java.io.Serializable;
import java.util.*;

/**
 * Created by zhoulong on 2016/12/6.
 * E-mail:zhoulong8513@gmail.com
 */
public class TokenModel implements Serializable{

    private static final long serialVersionUID = 1398183065753439041L;

    private String token;

    private Long userId;

    private String username;

    private Set<String> roles = new HashSet<>();

    private Set<String> permissions = new HashSet<>();

    private List<Map> userRoleOrg = new ArrayList<Map>();//用户角色架构

    private List<Map> resources = new ArrayList<>();

    private List<String> ownOrganization = new ArrayList<>();

    public List<Map> getUserRoleOrg() {
        return userRoleOrg;
    }

    public void setUserRoleOrg(List<Map> userRoleOrg) {
        this.userRoleOrg = userRoleOrg;
    }

    public TokenModel(){

    }

    public TokenModel(String token, Long userId, String username, Set<String> roles, Set<String> permissions,List<Map> userRoleOrg, List<Map> resources){
        this.token = token;
        this.userId = userId;
        this.username = username;
        this.roles = roles;
        this.permissions = permissions;
        this.userRoleOrg = userRoleOrg;
        this.resources = resources;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<String> permissions) {
        this.permissions = permissions;
    }

    public List<Map> getResources() {
        return resources;
    }

    public void setResources(List<Map> resources) {
        this.resources = resources;
    }

    public List<String> getOwnOrganization() {
        return ownOrganization;
    }

    public void setOwnOrganization(List<String> ownOrganization) {
        this.ownOrganization = ownOrganization;
    }
}
