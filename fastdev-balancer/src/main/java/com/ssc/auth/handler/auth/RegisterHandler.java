package com.ssc.auth.handler.auth;

import java.util.Map;

import javax.inject.Inject;

import com.fastdev.core.handler.Handler;
import com.fastdev.core.handler.OnCommand;
import com.ssc.auth.Result;
import com.ssc.auth.entity.UserPartition;
import com.ssc.auth.service.RegisterService;

@OnCommand(name="register")
public class RegisterHandler implements Handler{
	
	@Inject
	private RegisterService registerService;

	@Override
	public Object call(Map<String, Object> params) {
		
		Result result = new Result();
		
		try {
			
			UserPartition userPartition = registerService.allocate((String)params.get("userName"), (String)params.get("merchant"));
			result.setStatus(1);
			result.setData(userPartition);
		
		} catch (Exception e) {
			result.setStatus(1);
			result.setMessage(e.getMessage());
			
		}
		
		return result;
		
	}

}
