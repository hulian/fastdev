package com.fastdev.core.injector.impl;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fastdev.core.injector.Scanner;

public class ScannerImpl implements Scanner{
	
	private static final Logger logger = LoggerFactory.getLogger(ScannerImpl.class);

	@Override
	public List<Class<?>> scan(String packageName) {
		try {
			List<Class<?>> classes;
			
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			String path = packageName.replace('.', '/');
			Enumeration<URL> resources = classLoader.getResources(path);
			List<File> dirs = new ArrayList<File>();
			while (resources.hasMoreElements())
			{
			    URL resource = resources.nextElement();
			    dirs.add(new File(resource.getFile()));
			}
			classes = new ArrayList<Class<?>>();
			for (File directory : dirs)
			{
				if (directory.getPath().contains(".jar"))
			    {
			    	String jarpath = directory.getPath().split("!")[0];
			    	classes.addAll(findClassesFromJar(jarpath));
			    }else{
			    	classes.addAll(findClasses(directory,packageName));
			    }
			}
			return classes;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private List<Class<?>> findClasses(File directory, String packageName ) throws ClassNotFoundException
	{
	    List<Class<?>> classes = new ArrayList<Class<?>>();
	    
	    File[] files = directory.listFiles();
	    for (File file : files)
	    {
	        if (file.isDirectory())
	        {
	            classes.addAll(findClasses(file, packageName + "." + file.getName() ));
	        }
	        else if (file.getName().endsWith(".class"))
	        {
	        	logger.debug("find class:"+file.getName());
	        	Class<?> clazz = Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6));
	        	classes.add(clazz);
	        }
	    }
	    return classes;
	}
	
	public  List<Class<?>>  findClassesFromJar(String jarpath ){
		List<Class<?>> classes = new ArrayList<Class<?>>();
		
		Path jarPath2=null;
		try {
			jarPath2 = Paths.get(new URL(jarpath).toURI());
		} catch (MalformedURLException | URISyntaxException e) {
			logger.error("path error",e);
		}
		
		try(ZipFile zipFile=new ZipFile(jarPath2.toFile());){
			   
			Enumeration<? extends ZipEntry> zipEntries = zipFile.entries();

			    while (zipEntries.hasMoreElements())
			    {
			    	ZipEntry file = (ZipEntry)zipEntries.nextElement();
			    	if (file.getName().endsWith(".class"))
			        {
			    		logger.debug("find class:"+file.getName());
			            classes.add(Class.forName(file.getName().substring(0, file.getName().length() - 6).replace("/", ".")));
			        }
			    }
			return classes;
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return classes;
	}

}
