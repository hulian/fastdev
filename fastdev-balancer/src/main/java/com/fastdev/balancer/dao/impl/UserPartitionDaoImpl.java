package com.fastdev.balancer.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import com.fastdev.balancer.dao.UserPartitionDao;
import com.fastdev.balancer.entity.UserPartition;
import com.fastdev.core.transaction.TransactionManager;

@Singleton
public class UserPartitionDaoImpl implements UserPartitionDao {
	
	@Inject
	@Named("auth")
	private TransactionManager transactionManager;

	@Override
	public Integer save(UserPartition userPartition) {
		
		String sql = "INSERT INTO UserPartition ( userName,merchant,partitionId ) VALUES (?,?,?)";
		
		//自动关闭资源
		try ( 
				PreparedStatement statement = transactionManager
											.getConnection()
											.prepareStatement(sql,
											PreparedStatement.RETURN_GENERATED_KEYS); 
		){
			
			//设置参数
			statement.setString(1, userPartition.getUserName());
			statement.setString(2, userPartition.getMerchant());
			statement.setInt(3, userPartition.getPartitionId());
			
			statement.executeUpdate();
			
			//自动关闭资源
			try(ResultSet rs = statement.getGeneratedKeys()){
				rs.next();
				return new Integer(rs.getInt(1));
			}
			
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public UserPartition findByUserName(String userName) {
		
		String sql = "SELECT userId,merchant,partitionId FROM UserPartition WHERE userName=?";
		
		//自动关闭资源
		try ( 
				PreparedStatement statement = transactionManager
											.getConnection()
											.prepareStatement(sql); 
		){
			
			//设置参数
			statement.setString(1, userName);
			
			UserPartition userPartition = null;
			
			//自动关闭资源
			try(ResultSet rs = statement.executeQuery()){
				
				while(rs.next()){
					
					userPartition = new UserPartition();
					userPartition.setUserId(rs.getInt(1));
					userPartition.setMerchant(rs.getString(2));
					userPartition.setPartitionId(rs.getInt(3));
					userPartition.setUserName(userName);
					
				}
				
			}
			
			return userPartition;
		
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

}
