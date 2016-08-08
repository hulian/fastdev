package com.fastdev.core.session;

import java.sql.Timestamp;
import java.util.Set;

public class Session {

	protected String token;
	
	protected String userName;
	
	protected Set<String> roles;
	
	protected Timestamp createTime;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Set<String> getRoles() {
		return roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return "Session [token=" + token + ", userName=" + userName + ", roles=" + roles + ", createTime=" + createTime
				+ "]";
	}
	
}
