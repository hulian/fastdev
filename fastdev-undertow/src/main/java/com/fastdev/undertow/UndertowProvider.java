package com.fastdev.undertow;

import java.io.File;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fastdev.core.config.Config;
import com.fastdev.core.config.Server;
import com.fastdev.core.dispatcher.Dispatcher;
import com.fastdev.core.server.ServerProvider;
import io.undertow.Undertow;
import io.undertow.server.handlers.PathHandler;
import io.undertow.server.handlers.encoding.ContentEncodedResourceManager;
import io.undertow.server.handlers.encoding.ContentEncodingRepository;
import io.undertow.server.handlers.encoding.GzipEncodingProvider;
import io.undertow.server.handlers.resource.CachingResourceManager;
import io.undertow.server.handlers.resource.FileResourceManager;
import io.undertow.server.handlers.resource.ResourceHandler;
import io.undertow.server.handlers.resource.ResourceManager;

public class UndertowProvider implements ServerProvider{
	
	private static final Logger logger = LoggerFactory.getLogger(UndertowProvider.class);
	
	private Undertow undertow;
	private Dispatcher dispatcher;

	@Override
	public Server getServerName() {
		return Server.UNDERTOW;
	}
	
	@Override
	public void setDispatcher(Dispatcher dispatcher) {
		this.dispatcher=dispatcher;
	}


	@Override
	public void start(Config config) {
		
		int ioThread = Integer.parseInt(config.getProperty(Config.SERVER_IOTHREADS));
	    int workerThread = Integer.parseInt(config.getProperty(Config.SERVER_WORKERS));
		String httpHost = config.getProperty(Config.SERVER_HOST);
        int httpPort=Integer.parseInt( config.getProperty(Config.SERVER_PORT));
        String staticResourcePath = config.getProperty("server.context.path.static","static");
        
        PathHandler pathHandler = new PathHandler();
        
        //静态资源，压缩
        ResourceManager resourceManager = new FileResourceManager(new File(staticResourcePath), 10240);
        ContentEncodedResourceManager encoder =  new ContentEncodedResourceManager(Paths.get(staticResourcePath), new CachingResourceManager(100, 10000, null, resourceManager, -1), new ContentEncodingRepository()
                .addEncodingHandler("gzip", new GzipEncodingProvider(), 50, null),1024, 1024*500, null);
        ResourceHandler resourceHandler = new ResourceHandler(resourceManager);
        
        if(config.getProperty("http.gzip", "false").equals("true")){
        	resourceHandler.setContentEncodedResourceManager(encoder);
        }
        pathHandler.addPrefixPath("/", resourceHandler);
        
		//API
        pathHandler.addPrefixPath(dispatcher.getName(), new DispatcherHandler(dispatcher) );
        
		undertow = Undertow.builder()
                .addHttpListener(httpPort,httpHost)
                .setIoThreads(ioThread)
                .setWorkerThreads(workerThread)
                .setHandler(pathHandler).build();
        logger.info("Setup Undertow server static resources at path:"+staticResourcePath+" at location:"+staticResourcePath);
		
		if(undertow!=null){
			logger.info("Start Undertow server......");
			undertow.start();
			logger.info("Start Undertow OK!");
			 //kill消息钩子函数，关闭系统使用的资源
	      	Runtime.getRuntime().addShutdownHook(new Thread( new Runnable() {
	      			public void run() {
	      				logger.info("Stop Undertow......");
	      				undertow.stop();
	      				logger.info("Stop Undertow OK!");
	      			}
	      	}));
		}
		
	}


	@Override
	public void stop(){
		if(undertow!=null){
			logger.info("Stop Undertow......");
			undertow.stop();
			logger.info("Stop Undertow OK!");
		}
	}

}
