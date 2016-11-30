package com.ssc.auth.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import com.fastdev.core.transaction.TransactionManager;
import com.ssc.auth.dao.PartitionInfoDao;
import com.ssc.auth.entity.PartitionInfo;

@Singleton
public class PartitionInfoDaoImpl implements PartitionInfoDao{
	
	@Inject
	@Named("auth")
	private TransactionManager transactionManager;

	@Override
	public Integer save(PartitionInfo partitionInfo) {
		
		String sql = "INSERT INTO PartitionInfo ( merchant ) VALUES (?)";
		
		//自动关闭资源
		try ( 
				PreparedStatement statement = transactionManager
											.getConnection()
											.prepareStatement(sql,
											PreparedStatement.RETURN_GENERATED_KEYS); 
		){
			
			//设置参数
			statement.setString(1, partitionInfo.getMerchant());
			
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
	public List<PartitionInfo> findByMerchant( String merchant ) {
		
		String sql = "SELECT partitionId,userCount,merchant FROM PartitionInfo WHERE merchant=?";
		
		//自动关闭资源
		try ( 
				PreparedStatement statement = transactionManager
											.getConnection()
											.prepareStatement(sql); 
		){
			
			//设置参数
			statement.setString(1, merchant);
			
			List<PartitionInfo> partitionInfos = new ArrayList<>();
			
			//自动关闭资源
			try(ResultSet rs = statement.executeQuery()){
				
				while(rs.next()){
					PartitionInfo partitionInfo = new PartitionInfo();
					partitionInfo.setPartitionId(rs.getInt(1));
					partitionInfo.setUserCount(rs.getInt(2));
					partitionInfo.setMerchant(rs.getString(3));
					partitionInfos.add(partitionInfo);
				}
				
			}
			
			return partitionInfos;
		
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

}
