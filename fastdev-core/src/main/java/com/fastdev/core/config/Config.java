package com.fastdev.core.config;

import java.util.HashMap;

public class Config extends HashMap<String, Object>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8802938048125097887L;
	
	public static final String ARGS = "args";

	public static final String FRAMEWORK = "framework";

	public static final String SCAN_PACKAGES="scanPackages";
	
	@SuppressWarnings("unchecked")
	public <T> T get(String key , Class<?> T) {
		
		Object o = super.get(key);
		if(o!=null){
			return (T)o;
		}
		
		return null;
	}

	
}
