package com.fastdev.core.test.scan;

import javax.inject.Inject;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.fastdev.core.injector.Scanner;
import com.fastdev.core.injector.impl.ScannerImpl;

public class ScannerTest {

	@Test
	public void testClassScan(){
		
		Scanner scanner = new ScannerImpl();
		Assert.assertTrue(
				scanner.scan("com.fastdev.core.test.scan").contains(ScannerTest.class)
		);
	}
	
	@Test
	public void testJarScan(){
		
		Scanner scanner = new ScannerImpl();
		Assert.assertTrue(
				scanner.scan("javax.inject").contains(Inject.class)
		);
	}
}
