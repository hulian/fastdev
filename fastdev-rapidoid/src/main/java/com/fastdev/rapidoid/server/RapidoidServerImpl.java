package com.fastdev.rapidoid.server;

import org.rapidoid.setup.App;

import com.fastdev.core.config.Config;
import com.fastdev.core.server.Server;

public class RapidoidServerImpl implements Server{

	@Override
	public String getFrameworkName() {
		return "rapidoid";
	}

	@Override
	public void start( Config config) {
		App.path(config.get(Config.SCAN_PACKAGES,String[].class));
		App.bootstrap(config.get(Config.ARGS,String[].class));
		
	}

	@Override
	public void stop() {
	}


	
}
