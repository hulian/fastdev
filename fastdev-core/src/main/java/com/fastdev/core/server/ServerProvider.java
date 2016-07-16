package com.fastdev.core.server;

import com.fastdev.core.config.Config;
import com.fastdev.core.config.Server;
import com.fastdev.core.dispatcher.Dispatcher;

public interface ServerProvider{
	
	Server getServerName();
	
	void start( Config config);
	
	void stop();
	
	public void setDispatcher(final Dispatcher dispatcher );
}
