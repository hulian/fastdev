package com.fastdev.rapidoid.session.test;

import org.rapidoid.setup.App;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.fastdev.core.session.Session;
import com.fastdev.rapidoid.session.SessionServiceImpl;

@Test
public class SessionServiceTest {
	
	@BeforeTest
	void init(){
		App.bootstrap(new String[]{getClass().getPackage().getName()}).jpa();
	}

	void test(){
		
			SessionServiceImpl sessionServiceImpl = new SessionServiceImpl();
			
			Session session = sessionServiceImpl.createSession("user");
			Assert.assertNotNull(
					session
			);
			
			Assert.assertTrue(
					sessionServiceImpl.hasSession(session.getToken())
			);
			
			sessionServiceImpl.deleteSession(session.getToken());
			Assert.assertFalse(
					sessionServiceImpl.hasSession(session.getToken())
			);
		
	}
}
