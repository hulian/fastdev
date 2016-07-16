package com.fastdev.core.test.injector.service;

import javax.inject.Inject;
import javax.inject.Named;


@Named("injecedclassll2")
public class InjectedClassL2 {

	@Inject
	private TestService testService;

	public TestService getTestService() {
		return testService;
	}

	public void setTestService(TestService testService) {
		this.testService = testService;
	}
	
}
