package com.ssc.auth.service.impl;

import java.util.Comparator;
import java.util.List;
import java.util.function.ToIntFunction;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import com.fastdev.core.transaction.TransactionManager;
import com.fastdev.core.util.StringUtil;
import com.ssc.auth.Constant;
import com.ssc.auth.dao.PartitionInfoDao;
import com.ssc.auth.dao.UserPartitionDao;
import com.ssc.auth.entity.PartitionInfo;
import com.ssc.auth.entity.UserPartition;
import com.ssc.auth.service.RegisterService;

@Singleton
public class RgisterServiceImpl implements RegisterService{
	
	@Inject
	@Named(Constant.DATASOURCE_AUTH)
	private TransactionManager transactionManager;
	
	@Inject
	private UserPartitionDao userPartitionDao;
	
	@Inject
	private PartitionInfoDao partitionInfoDao;

	@Override
	public UserPartition allocate(String userName, String merchant) {
		
		//检查会员是否被分配
		if(StringUtil.isEmpty(userName)){
			throw new RuntimeException("parameter userName is empty");
		}
		
		UserPartition userPartitionCheck =transactionManager.doWithoutTransaction(()->{
			 return userPartitionDao.findByUserName(userName);
		});
		if(userPartitionCheck!=null){
			return userPartitionCheck;
		}

		
		//根据代理名查询分区信息
		if(StringUtil.isEmpty(merchant)){
			throw new RuntimeException("parameter error:merchant is empty");
		}
		List<PartitionInfo> partitionInfos = transactionManager.doWithoutTransaction(()->{
			return partitionInfoDao.findByMerchant(merchant);
		});
		if(partitionInfos.size()==0){
			throw new RuntimeException("partitioninfo not found for merchant:"+merchant);
		}
		
		//选择人数最少的分区
		PartitionInfo uPartitionInfo =  partitionInfos
				.stream()
				.min(Comparator.comparingInt(new ToIntFunction<PartitionInfo>() {
					@Override
					public int applyAsInt(PartitionInfo value) {
						return value.getUserCount();
					}
				})).get();

		//创建会员分区信息
		final UserPartition userPartition = new UserPartition();
		transactionManager.doInTransaction(()->{
			
			userPartition.setUserName(userName);
			userPartition.setMerchant(merchant);
			userPartition.setPartitionId(uPartitionInfo.getPartitionId());
			
			Integer id = userPartitionDao.save(userPartition);
			
			userPartition.setUserId(id);
			
		});
		
		return userPartition;
	}

	@Override
	public boolean isUserExisted(String userName) {
		// TODO Auto-generated method stub
		return false;
	}

	
}
