package com.fastdev.core;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.function.Consumer;

import com.fastdev.core.config.Config;
import com.fastdev.core.handler.Handler;
import com.fastdev.core.handler.HandlerService;
import com.fastdev.core.server.Server;

/**
 * 应用程序启动入口
 * @author admin
 *
 */
public class Application {
	
	private static Config config=new Config();
	private static Server server;
	private static HandlerService handlerService;
	private static List<Handler> handlers = new ArrayList<>();
	
	private Application(){
		
	}
	
	synchronized public void  run(){
		server.start(config);
	}
	
	public static Builder builder(){
		return new Builder();
	}
	
	public static class Builder{
		
		public Builder config( Config c){
			config=c;
			return this;
		}
		
		public Builder framework( final String serverName) {
			config.put(Config.FRAMEWORK, serverName);
			return this;
		}
		
		public Builder scan( final String[] path) {
			config.put(Config.SCAN_PACKAGES, path);
			return this;
		}
		
		public Builder addHandler( Handler handler ){
			handlers.add(handler);
			return this;
		}
		
		public Application build(){
			
			ServiceLoader.load(Server.class).forEach( new Consumer<Server>() {
				@Override
				public void accept(Server s) {
					if(s.getFrameworkName().equalsIgnoreCase(config.get(Config.FRAMEWORK,String.class))){
						server=s;
					}
				}
			});
			
			ServiceLoader.load(HandlerService.class).forEach( new Consumer<HandlerService>() {
				@Override
				public void accept(HandlerService s) {
					if(s.getFrameworkName().equalsIgnoreCase(config.get(Config.FRAMEWORK,String.class))){
						handlerService=s;
					}
				}
			});
			
			for( Handler handler : handlers ){
				System.out.println(handler.getPath());
				handlerService.addHandler(handler);
			}
			
			return new Application();
		}
		
	}
}
