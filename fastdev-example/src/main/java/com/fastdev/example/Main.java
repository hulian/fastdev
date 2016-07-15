package com.fastdev.example;

import com.fastdev.core.Application;
import com.fastdev.core.handler.Handler;

public class Main {

	public static void main(String[] args) {
	
		Application.builder()
		.framework("rapidoid")
		.scan(new String[]{Main.class.getPackage().getName()})
		.addHandler(new Handler("/test",null,()->{
			return "hello";
		}))
		.addHandler(new Handler("/world",null,()->{
			return "world";
		}))
		.build()
		.run();
		
		
	}
}
