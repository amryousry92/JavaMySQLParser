/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 *
 * @author user
 */
public class Configurations extends Properties{
    
    	private static final long serialVersionUID = 1L;
	private static Map<String, Configurations> cache = new HashMap<String, Configurations>();

	/**
	 * Private to disallow instansiation, use getInstance instead
	 * 
	 * @param fileName
	 *            Name of the properties file, must be in the classpath
	 * @throws IOException
	 */
	private Configurations(String fileName) throws IOException {
		InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
		if (in == null) {
			throw new IOException("File not found: " + fileName);
		}
		super.load(in);
	}

	/**
	 * Gets a Configurations instance for the given fileName
	 * 
	 * @param fileName
	 *            must be in the classpath
	 * @return The Configurations for the given properties file
	 */
	public static synchronized Configurations getInstance(String fileName) {
		try {
			Configurations instance = (Configurations) cache.get(fileName);
			if (instance == null) {
				// create and store
				instance = new Configurations(fileName);
				cache.put(fileName, instance);
			}
			return instance;
		} catch (IOException e) {
			// this is a deployment or programming error, so we use unchecked exception
			throw new RuntimeException("Unable to load properties file " + fileName);
		}
	}

	public static synchronized String test() {
		return "TEST OPTIONAL PACKAGES WITH HOT DEPLOYMENT";
	}

}
