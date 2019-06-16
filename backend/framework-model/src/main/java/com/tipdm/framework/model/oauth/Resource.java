package com.tipdm.framework.model.oauth;

import com.tipdm.framework.model.IdEntity;
import org.springframework.util.Assert;

import javax.persistence.*;

@Entity
@Table(name = "sys_resource", uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "parent_id", "resource_scope_id"})})
public class Resource extends IdEntity<Long> {

	private static final long serialVersionUID = 7751998348083876959L;

	@Column(name = "name", length = 100)
	private String name;
	@Column(name = "type", length = 30)
	private String type = "button";
	@Column(name = "priority")
	private Integer priority = 1;
	@Column(name = "parent_id")
	private Long parentId = 0L;
	@Column(name = "permission", length = 100)
	private String permission;
	@Column(name = "available")
	private Boolean available = Boolean.TRUE;
	@Column(name = "url", length = 200)
	private String url;
	@Column(name = "scope", length = 30)
	private String scope;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "resource_scope_id")
	private ResourceScope resourceScope;

//	@ManyToMany(mappedBy = "resources", targetEntity = Role.class)
//	private Set<Role> roles = new HashSet<Role>();

	public Resource() {

	}

	public Resource(String name, String type, String permission) {
		this.name = name;
		this.type = type;
		this.permission = permission;
	}

//	public Set<Role> getRoles() {
//		return roles;
//	}
//
//	public void setRoles(Set<Role> roles) {
//		this.roles = roles;
//	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		Assert.hasText(name, "资源名称不能为空");
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		Assert.hasText(permission, "权限字符串不能为空");
		this.permission = permission;
	}

	public Boolean getAvailable() {
		return available;
	}

	public void setAvailable(Boolean available) {
		this.available = available;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public ResourceScope getResourceScope() {
		return resourceScope;
	}

	public void setResourceScope(ResourceScope resourceScope) {
		this.resourceScope = resourceScope;
	}

	@Override
	public boolean equals(Object obj){
		
		boolean isSame = false;
		if(null != obj && obj instanceof Resource){
			Resource resource = (Resource)obj;
			isSame = this.name == resource.getName() && this.url == resource.getUrl() && this.resourceScope.equals(resource.getResourceScope());
		}
		return isSame;
	}
}
