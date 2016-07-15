package com.fastdev.core.server;

import com.fastdev.core.BaseService;
import com.fastdev.core.config.Config;

public interface Server extends BaseService{
	
	void start( Config config);
	
	void stop();
}
