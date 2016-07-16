package com.fastdev.core.config;

public class DefaultConfig extends Config{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2655658905276946785L;

	public DefaultConfig() {
		
		this.put(Config.SERVER, "UNDERTOW");
		this.put(Config.SERVER_HOST, "0.0.0.0");
		this.put(Config.SERVER_PORT, "8888");
		this.put(Config.SERVER_IOTHREADS, String.valueOf(Runtime.getRuntime().availableProcessors()+1));
		this.put(Config.SERVER_WORKERS, String.valueOf(Runtime.getRuntime().availableProcessors()*100));
		
	}
	
}
