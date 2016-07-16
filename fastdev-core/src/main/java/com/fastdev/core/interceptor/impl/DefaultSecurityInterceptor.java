package com.fastdev.core.interceptor.impl;

import java.util.Map;
import com.fastdev.core.code.Code;
import com.fastdev.core.code.Params;
import com.fastdev.core.handler.Handler;
import com.fastdev.core.interceptor.Interceptor;
import com.fastdev.core.session.Session;
import com.fastdev.core.session.SessionStorage;
import com.fastdev.core.util.StringUtil;

/**
 * 
 * 默认权限检查过滤器
 * @author fastdev
 *
 */
public class DefaultSecurityInterceptor implements Interceptor{
	
	private SessionStorage sessionStorage;

	public DefaultSecurityInterceptor(SessionStorage sessionStorage) {
		this.sessionStorage=sessionStorage;
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
		
		//检查Session是否存在
		Session session = sessionStorage.find(token);
		if(session==null){
			return Code.SESSION_EMPTY;
		}
		
		//检查是否有权限访问
		for( String roleAllowed : rolesAllowed ){
			if(session.getRoles().contains(roleAllowed)){
				return Code.SUCCESS;
			}
		}
		
		//无权限，返回错误码
		return Code.ACCESS_DINIED;
	}

}
