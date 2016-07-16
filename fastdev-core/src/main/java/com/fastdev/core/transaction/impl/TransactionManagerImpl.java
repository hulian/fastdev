package com.fastdev.core.transaction.impl;

import java.sql.Connection;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fastdev.core.transaction.InTransaction;
import com.fastdev.core.transaction.TransactionManager;



public class TransactionManagerImpl implements TransactionManager{
	
	private static final Logger logger = LoggerFactory.getLogger(TransactionManagerImpl.class);
	
	private  DataSource dataSource;
	
	private ThreadLocal<Connection> threadLocal = new ThreadLocal<>();

	@Override
	public  Connection getConnection() {
		Connection connection = threadLocal.get();
		return connection;
	}
	
	@Override
	public void start() {
			Connection connection=getConnection();
			if (connection == null) {
				
				if(dataSource==null){
					throw new RuntimeException("cannot find any datasource");
				}
				
				try {
					connection = getDataSource().getConnection();
				} catch (Throwable e) {
					logger.error("Start trsanction failed!"+e);
					throw new RuntimeException(e);
				}
				
				
				if(connection==null){
					throw new RuntimeException("cannot obtain connection from datasource:"+dataSource.getClass());
				}
				threadLocal.set(connection);
				
			}
		
			try {
				connection.setAutoCommit(false);
			} catch (Throwable e) {
				logger.error("Start trsanction failed!"+e);
				throw new RuntimeException(e);
			}
	}
	
	@Override
	public void commit(){
		try {
		
			Connection connection = threadLocal.get();
			if( connection==null ){
				return;
			}
			
			connection.commit();
		
		} catch (Throwable e) {
			logger.error("Commit trsanction failed!"+e);
		}
	}
	
	@Override
	public void rollback(){
		try {
			
			Connection connection = threadLocal.get();
			if( connection==null ){
				return;
			}
			
			connection.rollback();
			
		} catch (Throwable e) {
			logger.error("Rollback trsanction failed!"+e);
		}
	}
	
	@Override
	public void stop() {
		
		Connection connection = threadLocal.get();
		if( connection==null ){
			return;
		}
		
		threadLocal.remove();
		try {
			connection.close();
		} catch (Throwable e) {
			logger.error("Close connection failed!"+e);
		}
	}

	public  DataSource getDataSource() {
		return dataSource;
	}

	public  void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public void doInTransaction( InTransaction runable ) {
		try {
			this.start();
			runable.call();
			this.commit();
		} catch (Throwable e) {
			this.rollback();
			logger.error("Transaction Error:"+e);
			throw new RuntimeException("Do in transaction failed! roll back this transaction...",e);
		}finally{
			this.stop();
		}
		return;
	}


}
