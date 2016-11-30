package com.fastdev.balancer.handler;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import com.fastdev.balancer.Constant;
import com.fastdev.balancer.Result;
import com.fastdev.balancer.entity.UserPartition;
import com.fastdev.balancer.service.AllocateService;
import com.fastdev.core.handler.Handler;
import com.fastdev.core.handler.OnCommand;
import com.fastdev.core.transaction.TransactionManager;

@OnCommand(name="register")
public class AllocateHandler implements Handler{
	
	@Inject
	@Named(Constant.DATASOURCE_BALANCER)
	private TransactionManager transactionManager;
	
	@Inject
	private AllocateService registerService;

	@Override
	public Object call(Map<String, Object> params) {
		
		
		Result result = new Result();
		
		transactionManager.autoConnection(()->{
			
			try {
				
				UserPartition userPartition = registerService.allocate((String)params.get("userName"), (String)params.get("merchant"));
				result.setStatus(1);
				result.setData(userPartition);
			
			} catch (Exception e) {
				result.setStatus(0);
				result.setMessage(e.getMessage());
				
			}
			
		});
		
		return result;
		
	}

}
