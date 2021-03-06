package com.auth.test.dao;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.auth.test.ApplicationInit;
import com.fastdev.balancer.dao.UserPartitionDao;
import com.fastdev.balancer.entity.UserPartition;
import com.fastdev.core.transaction.TransactionManager;


public class UserPartitionDaoTest {
	
	TransactionManager transactionManager = ApplicationInit.getInjector().getBean("auth", TransactionManager.class);
	UserPartitionDao userPartitionDao = ApplicationInit.getInjector().getBean("UserPartitionDao", UserPartitionDao.class);

	@Test
	public void testSave(){
		
		transactionManager.autoConnection(()->{		
			transactionManager.doInTransaction(()->{
				
				UserPartition userPartition = new UserPartition();
				userPartition.setUserName("test");
				userPartition.setMerchant("testm");
				userPartition.setPartitionId(1);
				Integer id =  userPartitionDao.save(userPartition);
				
				Assert.assertEquals(id,new Integer(1));
				
			});
		});
		
	}
	
	@Test(dependsOnMethods={"testSave"})
	public void testFind(){
		
		
		transactionManager.autoConnection(()->{
			
			UserPartition userPartition = userPartitionDao.findByUserName("test");
			Assert.assertNotNull(userPartition);
			
		});
		
	}
}
