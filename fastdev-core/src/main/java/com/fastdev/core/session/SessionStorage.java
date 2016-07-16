package com.fastdev.core.session;

public interface SessionStorage {

	void save(Session session);
	
	void delete( String token );
	
	Session find( String token );
	
	boolean existed(String token );
}
