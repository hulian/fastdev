package com.fastdev.core.test.injector.service;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TestService {

	
	@Inject
	private InjectedService injectedService;

	public InjectedService getInjectedService() {
		return injectedService;
	}

	public void setInjectedService(InjectedService injectedService) {
		this.injectedService = injectedService;
	}
	
	
	
}
