package com.tipdm.framework.model.oauth;

import com.tipdm.framework.model.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author zhoulong E-mail:zhoulong8513@gmail.com
 * @version 创建时间：2016年11月29日 上午9:51:13
 * 类说明
 */
@Entity
@Table(name = "sys_oauth_client")
public class OAuthClient extends IdEntity<Long>{
	
	@Column(name="client_name", unique=true, nullable=true)
	private String clientName;
	
	@Column(name="client_id", nullable=true)
	private String clientId;
	
	@Column(name="client_secret", nullable=true)
	private String clientSecret;
	
	@Column(name="redirect_url")
	private String redirectUrl;

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}
	
}
