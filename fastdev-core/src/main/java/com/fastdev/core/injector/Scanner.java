package com.fastdev.core.injector;

import java.util.List;

public interface Scanner {

	List<Class<?>> scan(String packageName );
	
}
