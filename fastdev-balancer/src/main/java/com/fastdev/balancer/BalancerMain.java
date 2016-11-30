package com.fastdev.balancer;

import com.fastdev.core.Application;
import com.fastdev.core.config.Server;

public class BalancerMain {

	public static void main(String[] args) {
		
		Application.builder().loadConfig("config.properties")
		.server(Server.UNDERTOW)
		.build()
		.run();
		
	}
}
