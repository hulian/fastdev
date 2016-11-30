package com.fastdev.balancer.handler.merchant;

import java.util.Map;

import com.fastdev.core.handler.Handler;
import com.fastdev.core.handler.OnCommand;

@OnCommand(name="createMerchant",rolesAllowed={"admin"})
public class CreateMerchantHandler implements Handler {

	@Override
	public Object call(Map<String, Object> params) {
		
		// TODO Auto-generated method stub
		return null;
	
	}
	
}
