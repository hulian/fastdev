package com.fastdev.core.test.injector;

import org.testng.Assert;
import org.testng.annotations.Test;
import com.fastdev.core.injector.Injector;
import com.fastdev.core.injector.impl.InjectorImpl;
import com.fastdev.core.injector.impl.ScannerImpl;
import com.fastdev.core.test.injector.service.TestService;

public class InjectorTest {

	@Test
	public void testInjector(){
		
		Injector injector = new InjectorImpl(new ScannerImpl());
		injector.scan(new String[]{"com.fastdev.core.test.injector"});
		
		TestService testService = injector.getBean("TestService",TestService.class);
		Assert.assertNotNull(testService);
		Assert.assertNotNull(testService.getInjectedService());
		Assert.assertNotNull(testService.getInjectedService().getInjectedService());
		Assert.assertNotNull(testService.getInjectedService().getInjectedService().getInjectedServiceL2());
		Assert.assertNotNull(testService.getInjectedService().getInjectedService().getInjectedServiceL2().getTestService());
	}
}
