package com.fastdev.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fastdev.core.Application;

public class FileUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

	public static InputStream loadFileInputStream( String path ) {
		InputStream in = null;
		File file = new File(path);
		if( file.exists() ){
			
			logger.debug("load file from file system path:"+file.getAbsolutePath());
			try {
				in = new FileInputStream(file);
			} catch (FileNotFoundException e) {
				logger.error("load file from file system path failed",e);
			}
		
		}else{
		
			logger.debug("load file from class path:"+path);
			in = Application.class.getClassLoader().getResourceAsStream(path);
			if( in == null ){
				logger.error("cannot find configuration file at path:"+path);
				throw new RuntimeException();
			}
		}
		return in;
	}

}
