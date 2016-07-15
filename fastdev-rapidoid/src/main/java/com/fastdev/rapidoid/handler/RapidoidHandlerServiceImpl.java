package com.fastdev.rapidoid.handler;

import org.rapidoid.setup.On;

import com.fastdev.core.handler.Handler;
import com.fastdev.core.handler.HandlerService;

public class RapidoidHandlerServiceImpl implements HandlerService{

	@Override
	public String getFrameworkName() {
		return "rapidoid";
	}

	@Override
	public void addHandler(Handler handler) {
		On.get(handler.getPath()).roles(handler.getRolesAllowed()).json(handler.getHandler());
	}

}
