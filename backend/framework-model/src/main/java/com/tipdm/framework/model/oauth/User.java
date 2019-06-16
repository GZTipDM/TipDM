package com.tipdm.framework.model.oauth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tipdm.framework.model.IdEntity;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "sys_user", uniqueConstraints = {@UniqueConstraint(columnNames = "username"), @UniqueConstraint(columnNames = "email")})
public class User extends IdEntity<Long> {

	private static final long serialVersionUID = 9061080809787690595L;

	@Column(name = "username", length = 100, nullable = false)
	private String username;

	@Column(name = "password", length = 100, nullable = false)
	private String password;

	@Column(name = "user_alias", length = 50)
	private String userAlias;

	@Column(name = "id_card", length = 20)
	private String idCard;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "last_login_time")
	private Date lastLoginTime;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "login_time")
	private Date loginTime;

	@Column(name = "salt", length = 50)
	private String salt;

	@Column(name = "locked")
	private Boolean locked = Boolean.FALSE;

	@Column(name = "activated")
	private Boolean activated = Boolean.FALSE;

	@Column()
	private String email;

	@Transient
	private String organizationId;

	@Transient
	private List<Resource> resources = new ArrayList<>();

	@Transient
	private Integer count;//批量生成的用户数

	@Transient
	private Integer index;//起始索引

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	@ManyToMany(targetEntity = Organization.class, fetch = FetchType.LAZY)
	@JoinTable(name = "sys_user_organization", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "organization_id"))
	private Set<Organization> organization = new HashSet<Organization>();

	@ManyToMany(targetEntity = Role.class, fetch = FetchType.LAZY)
	@JoinTable(name = "sys_user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<Role>();
	public User() {

	}

	public User(String username, String password, String alias,String idcard) {
		this.username = username;
		this.password = password;
		this.userAlias = alias;
		this.idCard = idcard;
	}

	@JsonIgnore
	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserAlias() {
		if(StringUtils.isBlank(userAlias)){
			return this.username;
		}
		return userAlias;
	}

	public void setUserAlias(String userAlias) {
		this.userAlias = userAlias;
	}

	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public Boolean getLocked() {
		return locked;
	}

	public void setLocked(Boolean locked) {
		this.locked = locked;
	}

	public Boolean getActivated() {
		return activated;
	}

	public void setActivated(Boolean activated) {
		this.activated = activated;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@JsonIgnore
	public Set<Organization> getOrganization() {
		return organization;
	}

	public void setOrganization(Set<Organization> organization) {
		this.organization = organization;
	}

	public void setResources(List<Resource> resources){
		this.resources = resources;
	}

	public List<Resource> getResources() {
		return resources;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
}
