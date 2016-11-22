package com.fastdev.core.interceptor.impl;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fastdev.core.code.Code;
import com.fastdev.core.code.Delimiter;
import com.fastdev.core.code.Params;
import com.fastdev.core.config.Config;
import com.fastdev.core.handler.Handler;
import com.fastdev.core.interceptor.Interceptor;
import com.fastdev.core.session.Session;
import com.fastdev.core.session.SessionStorage;
import com.fastdev.core.util.AESUtil;
import com.fastdev.core.util.StringUtil;

/**
 * 
 * 默认权限检查过滤器
 * @author fastdev
 *
 */
public class DistributeTokenInterceptor implements Interceptor{
	
	private static Map<String, Timestamp> roleCache = new HashMap<String, Timestamp>();
	
	private static final Logger logger = LoggerFactory.getLogger(DistributeTokenInterceptor.class);
	
	private SessionStorage sessionStorage;
	
	private Config config;
	
	private long lifetime;
	
	public DistributeTokenInterceptor(Config config,SessionStorage sessionStorage) {
		this.config=config;
		this.sessionStorage=sessionStorage;
		this.lifetime=1000*60*60L*Long.parseLong(config.getProperty(Config.SESSION_LIFE_TIME));
	}

	@Override
	public Code intercept(Map<String, Object> params, Handler handler , String[] rolesAllowed ) {
		
		//如果Handler不需要权限 检查通过
		if( rolesAllowed==null ||  rolesAllowed.length==0 ){
			return Code.SUCCESS;
		}
		
		//检查Token是否为空
		String token = (String) params.get(Params.TOKEN);
		if( StringUtil.isEmpty(token) ){
			return Code.TOKEN_EMPTY;
		}
		
		
		//获取Session
		Session session = sessionStorage.find(token);
		
		//如果session不存在，尝试解密token获取session
		if(session==null){
			
			session = decodeToSession(token);
			
			//如果session仍然不存在，返回错误信息
			if(session==null){
				return Code.SESSION_EMPTY;
			}
		}
		
		//检查session是否过期
		if(session.getCreateTime().getTime()+lifetime<System.currentTimeMillis()){
			return Code.SESSION_EXPIRED;
		}
		
		
		//检查权限cache
		Timestamp lifeTime = roleCache.get(token);
		
		if( lifeTime!=null ){
			//设置Session
			params.put(Params.SESSION, session);
			return Code.SUCCESS;
		}
		
		//检查是否有权限访问
		for( String roleAllowed : rolesAllowed ){
			if(session.getRoles().contains(roleAllowed)){
				roleCache.put(token,session.getCreateTime());
				//设置Session
				params.put(Params.SESSION, session);
				return Code.SUCCESS;
			}
		}
		
		//无权限，返回错误码
		return Code.ACCESS_DINIED;
	}

	private Session decodeToSession(String token) {
		
		try {
			
			//解密Token
			String key = config.getProperty(Config.TOKEN_KEY);
			if( StringUtil.isEmpty(key)){
				logger.warn("token key is empty");
				return null;
			}
			
			String userInfo= AESUtil.decrypt(token,key);
			
			String[] userInfos = userInfo.split(Delimiter.TOKEN);
			Session session = new Session();
			session.setUserName(userInfos[0]);
			
			String[] roles = userInfos[1].split(Delimiter.ROLES);
			session.setRoles(new HashSet<>(Arrays.asList(roles)));
			
			session.setToken(token);
			session.setCreateTime(new Timestamp(Long.parseLong(userInfos[2])));
			sessionStorage.save(session);
			
			return session;
			
		} catch (Exception e) {
			logger.warn("decode token error ",e);
		}
		
		return null;
	}

}
