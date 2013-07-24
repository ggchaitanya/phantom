/*
 * Copyright 2012-2015, Flipkart Internet Pvt Ltd. All rights reserved.
 * 
 * This software is the confidential and proprietary information of Flipkart Internet Pvt Ltd. ("Confidential Information").  
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the license 
 * agreement you entered into with Flipkart.    
 * 
 */
package com.flipkart.phantom.runtime.impl.jetty;

import org.eclipse.jetty.webapp.WebAppContext;
import org.springframework.beans.factory.FactoryBean;
import org.trpr.platform.runtime.impl.config.FileLocator;

import java.io.File;

/**
 * The Spring factory bean for creating the Jetty WebAppContext using resources found on the classpath
 * 
 * @author Regunath B
 * @version 1.0, 14 Mar 2013
 */
public class JettyWebAppContextFactory  implements FactoryBean<WebAppContext> {
	
	/** The default max form contents size*/
	public static final int DEFAULT_MAX_FORM_SIZE = 2000000;
	
	/** The name of the web app context */
	private String contextName;
	
	/** The web app context path i.e. where WEB-INF is located*/
	private String contextPath;
	
	/** The max form size property*/
	private int maxFormContentSize = DEFAULT_MAX_FORM_SIZE;

	/**
	 * Interface method implementation. Returns the Jetty WebAppContext type
	 * @see org.springframework.beans.factory.FactoryBean#getObjectType()
	 */
	public Class<WebAppContext> getObjectType() {
		return WebAppContext.class;
	}

	/**
	 * Interface method implementation. Returns true
	 * @see org.springframework.beans.factory.FactoryBean#isSingleton()
	 */
	public boolean isSingleton() {
		return true;
	}

	/**
	 * Interface method implementation. Creates and returns a WebAppContext instance
	 * @see org.springframework.beans.factory.FactoryBean#getObject()
	 */
	public WebAppContext getObject() throws Exception {
		String path = null;
		File[] files = FileLocator.findDirectories(this.getContextPath(), null);
		for (File file : files) {
			// we need only WEB-INF from runtime project and none else even by mistake
			String fileToString = file.toString();
			if (fileToString.contains("runtime")) {
				path = fileToString;
				break;
			}
		}
		if (path.contains(".jar!") && path.startsWith("file:/")) {
			path = path.replace("file:/","jar:file:/");
		}
		// trim off the "WEB-INF" part as the WebAppContext path should refer to the parent directory
		if (path.endsWith("WEB-INF")) {
			path = path.replace("WEB-INF", "");
		}
		WebAppContext webAppContext = new WebAppContext(path, this.getContextName());
		webAppContext.setMaxFormContentSize(this.getMaxFormContentSize());
		return webAppContext;
	}

	/** Getter/Setter methods */
	public String getContextName() {
		return this.contextName;
	}

	public void setContextName(String contextName) {
		this.contextName = contextName;
	}

	public String getContextPath() {
		return this.contextPath;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}	
	public int getMaxFormContentSize() {
		return this.maxFormContentSize;
	}
	public void setMaxFormContentSize(int maxFormContentSize) {
		this.maxFormContentSize = maxFormContentSize;
	}
	/** End Getter/Setter methods */	

}
