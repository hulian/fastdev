package com.fastdev.balancer.service.impl;

import java.util.Comparator;
import java.util.List;
import java.util.function.ToIntFunction;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import com.fastdev.balancer.Constant;
import com.fastdev.balancer.dao.PartitionInfoDao;
import com.fastdev.balancer.dao.UserPartitionDao;
import com.fastdev.balancer.entity.PartitionInfo;
import com.fastdev.balancer.entity.UserPartition;
import com.fastdev.balancer.service.RegisterService;
import com.fastdev.core.transaction.TransactionManager;
import com.fastdev.core.util.StringUtil;

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
		
		UserPartition userPartitionCheck = userPartitionDao.findByUserName(userName);
		if(userPartitionCheck!=null){
			return userPartitionCheck;
		}

		
		//根据代理名查询分区信息
		if(StringUtil.isEmpty(merchant)){
			throw new RuntimeException("parameter error:merchant is empty");
		}
		
		List<PartitionInfo> partitionInfos = partitionInfoDao.findByMerchant(merchant);
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
