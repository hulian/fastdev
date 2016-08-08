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
	
	
	/**
	 * 数据源相关配置
	 */
	public static final String DATASOURCE_NAMES = "datasource.names";
	
	public static final String DATASOURCE_JDBC_URL = "jdbc.url";
	
	public static final String DATASOURCE_JDBC_USERNAME  = "jdbc.username";
	
	public static final String DATASOURCE_JDBC_PASSWARD = "jdbc.password";
	
	public static final String DATASOURCE_INIT_SQL = "init.sql";

	
	/**
	 * TOKEN加密密钥
	 */
	public static final String TOKEN_KEY = "token.key";
}
