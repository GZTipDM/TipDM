package com.tipdm.framework.model.oauth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tipdm.framework.model.IdEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "sys_role")
@JsonIgnoreProperties(value = { "resources","users" })
public class Role extends IdEntity<Long> {

	@Column(name = "name", length = 100)
	private String name;

	@Column(name = "description", length = 200)
	private String description;

	@Column(name = "available")
	private Boolean available = Boolean.TRUE;

	@ManyToMany(targetEntity = Resource.class, fetch = FetchType.LAZY)
	@JoinTable(name = "sys_role_resource", joinColumns = @JoinColumn(name = "role_id"), inverseJoinColumns = @JoinColumn(name = "resource_id"))
	@JsonIgnoreProperties
	private Set<Resource> resources = new HashSet<Resource>();

	@ManyToMany(targetEntity = User.class, mappedBy = "roles")
	@JsonIgnoreProperties
	private Set<User> users = new HashSet<User>();


	@ManyToMany(targetEntity = UserResource.class)
	@JoinColumn(name="role_id")
	@JsonIgnoreProperties
	private Set<UserResource> userResources = new HashSet<>();

	@Column(name = "is_default_role")
	private Boolean defaultRole = Boolean.FALSE;//新用户默认的角色

	public Role() {
	}

	public Role(String mark, String name) {
		// this.mark = mark;
		this.name = name;
	}

	@JsonIgnore
	public Set<User> getUsers() {
		return users;
	}
	@JsonIgnore
	public void setUsers(Set<User> users) {
		this.users = users;
	}
	@JsonIgnore
	public Set<Resource> getResources() {
		return resources;
	}
	@JsonIgnore
	public void setResources(Set<Resource> resources) {
		this.resources = resources;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getAvailable() {
		return available;
	}

	public void setAvailable(Boolean available) {
		this.available = available;
	}

	public Boolean getDefaultRole() {
		return defaultRole;
	}

	public void setDefaultRole(Boolean defaultRole) {
		this.defaultRole = defaultRole;
	}
}
