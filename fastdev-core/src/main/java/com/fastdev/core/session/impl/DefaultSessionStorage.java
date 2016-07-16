package com.fastdev.core.session.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fastdev.core.session.Session;
import com.fastdev.core.session.SessionStorage;

public class DefaultSessionStorage implements SessionStorage {
	
	private static final Map<String, Session> sessions = new ConcurrentHashMap<>();

	@Override
	public void save(Session session) {
		sessions.put(session.getToken(), session);
	}

	@Override
	public void delete(String token) {
		sessions.remove(token);
	}

	@Override
	public Session find(String token) {
		return sessions.get(token);
	}

	@Override
	public boolean existed(String token) {
		return sessions.get(token)==null?false:true;
	}

}
