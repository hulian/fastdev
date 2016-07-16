package com.fastdev.core.dispatcher;

import java.util.Map;

public interface Dispatcher {

	String getName();
	
	Object dispatch( Map<String, Object> params);
	
}
