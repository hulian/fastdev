package com.fastdev.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.function.Consumer;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fastdev.core.config.Config;
import com.fastdev.core.config.DefaultConfig;
import com.fastdev.core.config.Server;
import com.fastdev.core.dispatcher.Dispatcher;
import com.fastdev.core.dispatcher.impl.DefaultHandlerDispatcher;
import com.fastdev.core.injector.Injector;
import com.fastdev.core.injector.impl.InjectorImpl;
import com.fastdev.core.injector.impl.ScannerImpl;
import com.fastdev.core.interceptor.Interceptor;
import com.fastdev.core.interceptor.impl.DistributeTokenInterceptor;
import com.fastdev.core.server.ServerProvider;
import com.fastdev.core.session.SessionStorage;
import com.fastdev.core.session.impl.DefaultSessionStorage;
import com.fastdev.core.sql.SqlRunner;
import com.fastdev.core.transaction.TransactionManager;
import com.fastdev.core.transaction.impl.TransactionManagerImpl;
import com.fastdev.core.util.FileUtil;
import com.fastdev.core.util.PackageNameUtil;
import com.fastdev.core.util.StringUtil;
import com.zaxxer.hikari.HikariDataSource;

/**
 * 应用程序启动入口
 * 
 * @author admin
 *
 */
public class Application {
	
	private static final Logger logger = LoggerFactory.getLogger(Application.class);

	private Builder builder;

	private Application(Builder builder) {  
		this.builder = builder;
	}

	synchronized public void run() {
		builder.server.start(builder.config);
	}
	
	public Injector getInjector(){
		return builder.injector;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {

		private Injector injector = new InjectorImpl(new ScannerImpl());
		private Config config = new DefaultConfig();
		private List<Interceptor> interceptors = new ArrayList<>();

		private ServerProvider server;
		private Dispatcher dispatcher;
		private SessionStorage sessionStorage;
		
		public Builder loadConfig( String path ){
			try {
				
				InputStream in = FileUtil.loadFileInputStream(path);
				config.load(in);
				logger.debug("load config:"+config);
			} catch (IOException e) {
				logger.error("load configuration failed");
				throw new RuntimeException(e);
			}
			return this;
		}

		public Builder config(Config c) {
			config = c;
			return this;
		}

		public Builder server(Server frameWork) {
			config.put(Config.SERVER, frameWork.toString());
			return this;
		}

		public Builder pakcages(String... packages) {
			StringBuffer sb = new StringBuffer();
			for (String packageName : packages) {
				sb.append(packageName);
				sb.append(",");
			}
			config.put(Config.SCAN_PACKAGES, sb.toString());
			return this;
		}

		public Builder setDispatcher(Dispatcher dispatcher) {
			this.dispatcher = dispatcher;
			return this;
		}

		public Builder setSessionStorage(SessionStorage sessionStorage) {
			this.sessionStorage = sessionStorage;
			return this;
		}
		
		public Builder addDataSource(String name , String jdbcUrl , String userName , String password , String initSql) {
			HikariDataSource hikariDataSource = new HikariDataSource();
			hikariDataSource.setJdbcUrl(jdbcUrl);
			hikariDataSource.setUsername(userName);
			hikariDataSource.setPassword(password);
			addDataSource(name,hikariDataSource,initSql);
			return this;
		}

		public Builder addDataSource(String name, DataSource dataSource) {
			TransactionManagerImpl transactionManager = new TransactionManagerImpl();
			transactionManager.setDataSource(dataSource);
			injector.addBean(name, transactionManager);
			return this;
		}

		public Builder addDataSource(String name, DataSource dataSource, String initSql) {
			
			//添加数据源
			addDataSource(name, dataSource);
			
			//执行初始化SQL
			if ( StringUtil.isNotEmpty(initSql) ) {
				
				TransactionManager transactionManager = injector.getBean(name,TransactionManager.class);
				
				InputStream in = FileUtil.loadFileInputStream(initSql);
				if( in==null ){
					logger.error("cannot find init sql script at path:"+initSql);
					throw new RuntimeException();
				}
				
				transactionManager.autoConnection(()->{
					transactionManager.doInTransaction(() -> {
						SqlRunner sqlRunner = new SqlRunner(transactionManager.getConnection(),
								new PrintWriter(System.out), new PrintWriter(System.err), true, true);
						try {
							sqlRunner.runScript(new InputStreamReader(in));
						} catch (SQLException e) {
							logger.error("excute init sql error",e);
						}
					});
				});
			}
			return this;
		}

		public Application build() {
			
			//加载配置文件数据源
			String datasourceNames = config.getProperty(Config.DATASOURCE_NAMES);
			if(StringUtil.isNotEmpty(datasourceNames)){
				String[] names = datasourceNames.split(",");
				for( String name : names ){
					addDataSource(
							name, 
							config.getProperty(name+"."+Config.DATASOURCE_JDBC_URL),
							config.getProperty(name+"."+Config.DATASOURCE_JDBC_USERNAME),
							config.getProperty(name+"."+Config.DATASOURCE_JDBC_PASSWARD),
							config.getProperty(name+"."+Config.DATASOURCE_INIT_SQL)
					);
				}
			}


			// 获取Server
			ServiceLoader.load(ServerProvider.class).forEach(new Consumer<ServerProvider>() {
				@Override
				public void accept(ServerProvider s) {
					if (s.getServerName().toString().equalsIgnoreCase(config.getProperty(Config.SERVER))) {
						server = s;
					}
				}
			});

			if (server == null) {
				throw new RuntimeException("Cannot find server provider!");
			}

			if (sessionStorage == null) {
				sessionStorage = new DefaultSessionStorage();
				injector.addBean("sessionStorage", sessionStorage);
			}

			interceptors.add(new DistributeTokenInterceptor(config,sessionStorage));

			if (dispatcher == null) {
				dispatcher = new DefaultHandlerDispatcher(interceptors, injector.getHandlers(),injector.getRolesAllowed());
			}
			server.setDispatcher(dispatcher);
			
			injector.addBean("config", config);
			
			// 根据配置路径，扫描包。如果没有配置默认区Main函数所在包扫描
			injector.scan(config.getProperty(Config.SCAN_PACKAGES, PackageNameUtil.getDefaultPackageName()).split(","));

			return new Application(this);
		}

	}
}
