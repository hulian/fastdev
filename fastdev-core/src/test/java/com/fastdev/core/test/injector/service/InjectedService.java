package com.fastdev.core.test.injector.service;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
public class InjectedService {

	@Inject
	private InjectedService injectedService;
	
	@Inject
	@Named("injecedclassll2")
	private InjectedClassL2 injectedServiceL2;

	public InjectedService getInjectedService() {
		return injectedService;
	}

	public void setInjectedService(InjectedService injectedService) {
		this.injectedService = injectedService;
	}

	public InjectedClassL2 getInjectedServiceL2() {
		return injectedServiceL2;
	}

	public void setInjectedServiceL2(InjectedClassL2 injectedServiceL2) {
		this.injectedServiceL2 = injectedServiceL2;
	}
	
	
}
