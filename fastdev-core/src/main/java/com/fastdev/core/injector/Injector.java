package com.fastdev.core.injector;

import java.util.Map;

import com.fastdev.core.handler.Handler;

public interface Injector {

	void scan( String[] packages );
	
	Map<String, Handler> getHandlers();
	
	Map<String, String[]> getRolesAllowed();

	<T> T getBean(String string , Class<T> T);
	
	void addBean(String name, Object object);
}
