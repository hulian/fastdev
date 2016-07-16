package com.fastdev.core.interceptor;

import java.util.Map;

import com.fastdev.core.code.Code;
import com.fastdev.core.handler.Handler;

public interface Interceptor {

	Code intercept( Map<String, Object> params , Handler handler , String[] rolesAllowed );
	
}
