package com.fastdev.core.dispatcher.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fastdev.core.code.Code;
import com.fastdev.core.code.Params;
import com.fastdev.core.dispatcher.Dispatcher;
import com.fastdev.core.handler.Handler;
import com.fastdev.core.interceptor.Interceptor;
import com.fastdev.core.util.StringUtil;

public class DefaultHandlerDispatcher implements Dispatcher{
	
	private static final Logger logger = LoggerFactory.getLogger(DefaultHandlerDispatcher.class);
	
	private final List<Interceptor> interceptors;
	private final Map<String, Handler> handlers;
	private final Map<String, String[]> rolesAllowed;
	
	public DefaultHandlerDispatcher( 
			List<Interceptor> interceptors , 
			Map<String, Handler> handlers ,
			Map<String, String[]> rolesAllowed
			) {
		this.interceptors=interceptors;
		this.handlers=handlers;
		this.rolesAllowed=rolesAllowed;
	}
	
	@Override
	public String getName() {
		return "handlers";
	}

	@Override
	public Object dispatch(Map<String, Object> params) {
		
		try {
			String handlerName = (String) params.get(Params.HANDLER);
			
			if(StringUtil.isEmpty(handlerName)){
				return Code.HANDLER_EMPTY.toObject();
			}
			
			Handler handler = handlers.get(handlerName);
			
			if( handler==null ){
				return Code.HANDLER_NOT_FOUND.toObject(handlerName);
			}
			
			
			for( Interceptor interceptor : interceptors ){
				Code code = interceptor.intercept(params, handler,rolesAllowed.get(handlerName));
				if(!code.equals(Code.SUCCESS)){
					return code.toObject(); 
				}
			}
			
			return handler.call(params);
		
		} catch (Exception e) {
			logger.error("application error",e);
		}
		
		return Code.UNKNOWN_ERROR.toObject();
		
	}

}
