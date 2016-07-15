package com.fastdev.rapidoid.session;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.fastdev.core.session.Session;

@Entity
public class SessionEntity extends Session implements Serializable{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -5065079121591212292L;
	
	@Id
	private String token;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	
}
