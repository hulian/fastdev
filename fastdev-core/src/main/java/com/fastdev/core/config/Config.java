package com.fastdev.core.config;

import java.util.Properties;

/**
 * 应用配置对象
 * @author 	fastdev
 * @since 	0.0.1
 */
public class Config extends Properties{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8802938048125097887L;
	
	/**
	 * 命令行参数
	 */
	public static final String ARGS = "args";

	/**
	 * 框架名称
	 */
	public static final String SERVER = "server";

	/**
	 * 包扫描
	 */
	public static final String SCAN_PACKAGES="scanPackages";
	
	/**
	 * Server host
	 */
	public static final String SERVER_HOST = "server.host";
	
	/**
	 * Server port
	 */
	public static final String SERVER_PORT = "server.port";
	
	/**
	 * IO Thread 数量
	 */
	public static final String SERVER_IOTHREADS = "server.iothreads";
	
	/**
	 * Worker Thread 数量
	 */
	public static final String SERVER_WORKERS = "server.workers";
}
