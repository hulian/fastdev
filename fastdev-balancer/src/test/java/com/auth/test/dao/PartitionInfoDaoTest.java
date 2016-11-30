package com.auth.test.dao;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.auth.test.ApplicationInit;
import com.fastdev.balancer.dao.PartitionInfoDao;
import com.fastdev.balancer.entity.PartitionInfo;
import com.fastdev.core.transaction.TransactionManager;

public class PartitionInfoDaoTest {

	TransactionManager transactionManager = ApplicationInit.getInjector().getBean("auth", TransactionManager.class);
	PartitionInfoDao partitionInfoDao = ApplicationInit.getInjector().getBean("PartitionInfoDao",PartitionInfoDao.class);

	@Test
	public void testSave() {
		//77
		transactionManager.autoConnection(()->{
			transactionManager.doInTransaction(() -> {

				PartitionInfo partitionInfo = new PartitionInfo();
				partitionInfo.setMerchant(ApplicationInit.MERCHANT);
				Integer id = partitionInfoDao.save(partitionInfo);

				Assert.assertEquals(id, new Integer(1));

			});
			
		});
	}

	@Test(dependsOnMethods={"testSave"})
	public void testFind() {

		transactionManager.autoConnection(()->{
			
			List<PartitionInfo> partitionInfos = partitionInfoDao.findByMerchant(ApplicationInit.MERCHANT);
			Assert.assertEquals(partitionInfos.size(), 1);
		
		});

	}
}
