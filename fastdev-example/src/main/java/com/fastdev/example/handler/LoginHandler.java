package com.fastdev.example.handler;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Named;
import com.fastdev.core.config.Config;
import com.fastdev.core.handler.Handler;
import com.fastdev.core.handler.OnCommand;
import com.fastdev.core.util.TokenUtil;

@OnCommand(name="login")
public class LoginHandler implements Handler{


	@Inject
	@Named("config")
	private Config config;

	@Override
	public Object call(Map<String, Object> params) {
		
		String token = TokenUtil.createDistributeToken("test","1", Arrays.asList("user","superuser"), new Timestamp(System.currentTimeMillis()),(String)config.get(Config.TOKEN_KEY));
		
		return token;
	}
}
