package com.fastdev.rapidoid.session;

import java.util.UUID;

import org.rapidoid.jpa.JPA;

import com.fastdev.core.session.Session;
import com.fastdev.core.session.SessionService;

public class SessionServiceImpl implements SessionService{

	@Override
	public boolean hasSession(String token) {

		if(JPA.getIfExists(SessionEntity.class	, token)!=null){
			return true;
		}

		return false;
	}

	@Override
	public Session createSession( String userName ) {
		SessionEntity sessionEntity = new SessionEntity();
		sessionEntity.setToken(UUID.randomUUID().toString());
		JPA.insert(sessionEntity);
		return sessionEntity;
	}

	@Override
	public void deleteSession(String token) {
		JPA.delete(SessionEntity.class, token);
	}

}
