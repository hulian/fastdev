package com.fastdev.core.injector.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fastdev.core.handler.Handler;
import com.fastdev.core.handler.OnCommand;
import com.fastdev.core.injector.Injector;
import com.fastdev.core.injector.Scanner;

public class InjectorImpl implements Injector{
	
	private static final Logger logger = LoggerFactory.getLogger(InjectorImpl.class);
	
	private static final List<Class<? extends Annotation>> annotations = Arrays.asList(Singleton.class,Named.class,OnCommand.class);
	
	//把被管理的所有对象储存在MAP里
	private static final Map<String, Object> beans = new ConcurrentHashMap<>();
	
	private static final Map<String, Handler> handlers = new HashMap<>();
	
	private static final Map<String, String[]> rolesAllowed = new HashMap<>();
	
	private Scanner scanner;
	
	public InjectorImpl(Scanner scanner) {
		this.scanner=scanner;
	}

	@Override
	public Map<String, Handler> getHandlers() {
		return handlers;
	}

	@Override
	public synchronized void scan( String[] packages ) {
	
		//扫描包，创建Bean
		for( String packageName : packages ){
			List<Class<?>> classes = scanner.scan(packageName);
			for( Class<?> clazz : classes ){
				for( Class<? extends Annotation> annotation : annotations ){
					if(clazz.isAnnotationPresent(annotation)){
							createBean(clazz);
					}
				}
			}
		}
		
		//遍历Beans，注入依赖，不支持构造函数注入
		Iterator<String> iterator = beans.keySet().iterator();
		while( iterator.hasNext() ){
			String beanName = iterator.next();
			InjectDependency(beans.get(beanName));
		}
		
		//遍历Handlers，注入依赖，不支持构造函数注入
		Iterator<String> iteratorh = handlers.keySet().iterator();
		while( iteratorh.hasNext() ){
			String beanName = iteratorh.next();
			InjectDependency(handlers.get(beanName));
		}
		
		logger.debug("beans-->"+beans.toString());
		logger.debug("handlers-->"+handlers.toString());
		
	}
	
	/**
	 * @param clazz 类
	 * @return 创建的对象
	 */
	private Object createBean(Class<?> clazz) {
		
		//如果是超级handler对象
		if(clazz.isAnnotationPresent(OnCommand.class)){
			
			return createInstanceForHandler(clazz);
			
		//如果是超级普通bean
		}else{
			
			return createInstanceForBean(clazz);
		}
		
	}
	
	//创建handler
	private Object createInstanceForHandler( Class<?> clazz) {
		
		String beanName=null;
		Object instance=null;
		try {
			
			OnCommand annotation = clazz.getAnnotation(OnCommand.class);
			beanName=annotation.name();
			
			//检查是否已经创建 
			instance= handlers.get(beanName);
			if( instance!=null ){
				throw new RuntimeException("handler:"+beanName+" is existed");
			}
				
			//创建
			instance = clazz.newInstance();
			handlers.put(beanName, (Handler)instance);
			rolesAllowed.put(beanName, annotation.rolesAllowed());
			
			logger.debug("create handler ["+beanName+"] for class:"+clazz.getName());
			
		} catch (InstantiationException | IllegalAccessException e) {
			logger.error("create handler ["+beanName+"] failed for class:"+clazz.getName());
		}
		return instance;
	}

	//创建bean
	private Object createInstanceForBean(Class<?> clazz) {
		
		String beanName=null;
		Object instance=null;
		try {
			
			//检查Class是否使用Named注解
			if(clazz.isAnnotationPresent(Named.class)){	
				beanName=clazz.getAnnotation(Named.class).value();
			}else{
				beanName=clazz.getSimpleName();
			}
			
			instance= beans.get(beanName);
			if( instance!=null){
				throw new RuntimeException("bean:"+beanName+" is existed");
			}
			
			//创建
			instance = clazz.newInstance();
			beans.put(beanName, instance);
			logger.debug("create bean ["+beanName+"] for class:"+clazz.getName());
			
			//遍历这个类实现的接口，使用接口名创建Bean
			Class<?>[] inferfaces = clazz.getInterfaces();
			for( Class<?> interfacz : inferfaces){
				
				//如果是命名Bean，做接口名映射
				if(clazz.isAnnotationPresent(Named.class)){
					continue;
				}
				
				//如果一个接口有多个类实现，并且又没有使用命名区别，默认抛出异常
				if(beans.get(interfacz.getSimpleName())!=null){
					throw new RuntimeException("duplicated bean:"+interfacz.getSimpleName());
				}
				
				beans.put(interfacz.getSimpleName(),instance);
				logger.debug("create bean ["+interfacz.getSimpleName()+"] for interface:"+interfacz.getName());
				
			}
			
		} catch (InstantiationException | IllegalAccessException e) {
			logger.error("create bean ["+beanName+"] failed for class:"+clazz.getName(),e);
		}
		return instance;
	}


	//注入依赖
	private void InjectDependency(Object instance) {
		
		Class<?> clazz = instance.getClass();
		Field[] fields = clazz.getDeclaredFields();
		
		for( Field field : fields ){
			
			field.setAccessible(true);
			
			//检测是否为空，不为空不注入
			try {
				if(field.get(instance)!=null){
					continue;
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				logger.error("error",e);
			}
			
			if(field.isAnnotationPresent(Inject.class)){
				
				Class<?> injectedClazz = field.getType();
				
				//检查Field是否使用了Named注解
				String injectedBeanName = null;
				if(field.isAnnotationPresent(Named.class)){
					injectedBeanName=field.getAnnotation(Named.class).value();
				}else{
					injectedBeanName = injectedClazz.getSimpleName();
				}
				
			
				try {
					
					Object injectedBean = beans.get(injectedBeanName);
					if(injectedBean==null){
						throw new RuntimeException("cannot find named bean:"+injectedBeanName);
					}
					
					//注入依赖
					field.set(instance,injectedBean);
					logger.debug("inject bean ["+injectedBeanName+"] for class:"+clazz.getName()+" injectedClass:"+injectedClazz.getName());
				
				} catch (IllegalArgumentException | IllegalAccessException e) {
					logger.error("inject bean ["+injectedBeanName+"] failed for class:"+clazz.getName()+" injectedClass:"+injectedClazz.getName(),e);
				}
				
			}
			
			field.setAccessible(false);
			
		}
	}

	@Override
	public <T> T getBean(String beanName, Class<T> T) {
		Object o = beans.get(beanName);
		if(o!=null){
			return T.cast(o);
		}
		return null;
	}

	@Override
	public void addBean(String name, Object object) {
		beans.put(name, object);
	}

	@Override
	public Map<String, String[]> getRolesAllowed() {
		return rolesAllowed;
	}

}
