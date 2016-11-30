package com.fastdev.example;

import com.fastdev.core.Application;
import com.fastdev.core.config.Server;

public class Main {

	public static void main(String[] args) {
		
		Application.builder().loadConfig("config.properties")
		.server(Server.UNDERTOW)
		.build()
		.run();
		
	}
}
