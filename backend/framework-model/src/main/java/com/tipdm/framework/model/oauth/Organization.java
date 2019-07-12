package com.tipdm.framework.model.oauth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tipdm.framework.model.IdEntity;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "sys_organization", uniqueConstraints=@UniqueConstraint(columnNames = {"name", "parent_id"}))
@JsonIgnoreProperties(value = {"users" })
public class Organization extends IdEntity<Long> {
	
	@Column(name = "name", length = 100)
	private String name;
	@Column(name = "priority")
	private Integer priority;
	@Column(name = "parent_id")
	private Long parent_id;
	@Column(name = "available")
	private Boolean available = Boolean.TRUE;

	@Column(name = "description")
	@Type(type = "text")
	private String description;



	@ManyToMany(targetEntity = User.class, mappedBy = "organization")
	@JsonIgnoreProperties
	private Set<User> users = new HashSet<User>();

	@JsonIgnore
	public Set<User> getUsers() {
		return users;
	}
	@JsonIgnore
	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Long getParent_id() {
		return parent_id;
	}

	public void setParent_id(Long parent_id) {
		this.parent_id = parent_id;
	}

	public Boolean getAvailable() {
		return available;
	}

	public void setAvailable(Boolean available) {
		this.available = available;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
