package com.auth.test;

import com.fastdev.core.Application;
import com.fastdev.core.injector.Injector;

public class ApplicationInit {
	
	public static String MERCHANT = "testm";
	
	public static  Injector injector;

	synchronized public static Injector getInjector(){
		
		if( injector==null ){
			injector=Application.builder().loadConfig("config-test.properties").build().getInjector();
		}
		
		return injector;
	}
}
