package com.rootrip.platform;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;

/**
 * 
 * @author liubin
 * @date 2012-11-9
 *
 */
@SuppressWarnings("unchecked")
public class Platform {
	
	// Spring上下文
	private ApplicationContext context;
	
	private ServletContext servletContext;
	
	// 平台实例
	private static Platform instance;
	
	// Spring配置文件
	private List springCfgFiles = new ArrayList();
	
	// Hibernate配置文件
	private Resource[] hibernateCfgFiles;
	
	// web根目录
	private String webRootPath;
	
	// 所有改平台下的应用
	private Application app;
	
	// 是否初始化结束
	private AtomicBoolean initFinish = new AtomicBoolean(false);
	
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * @return the webRootPath
	 */
	public String getWebRootPath() {
		return webRootPath;
	}

	/**
	 * @param webRootPath the webRootPath to set
	 */
	public void setWebRootPath(String webRootPath) {
		this.webRootPath = webRootPath;
	}

	private Platform(){};
	
	/**
	 * 获得实例
	 * @return 实例
	 */
	public static Platform getInstance(){
		return instance;
	}
	
	/**
	 * 初始化Platform
	 * @return 是否成功
	 */
	public synchronized void init(){
		if(instance != null){
			return;
		}
		
		if(app.getHibernateMappingFiles() != null){
			this.setHibernateCfgFiles(app.getHibernateMappingFiles());
		}
		if(app.getSpringCfgFiles() != null){
			this.setSpringCfgFiles(app.getSpringCfgFiles());
		}
		instance = this;
	}
	
	/**
	 * 初始化应用
	 * @return 是否成功
	 */
	public void initApp(){
		if(!app.init()){
			logger.error("应用[" + app.getName() + "]初始化失败!");
		} else {
			logger.info("应用[" + app.getName() + "]初始化成功!");
		}
		//设置初始化结束
		this.finish();
	}
	
	/**
	 * 添加应用
	 * @param app 应用对象
	 */
	public void setApp(Application app){
		this.app = app;
	}
	
	/**
	 * 通过应用名取得应用
	 * @return 应用
	 */
	public Application getApp(){
		return this.app;
	}
	
	/**
	 * 取得Bean
	 * @param name Bean名称
	 * @return Bean对象
	 */
	public Object getBean(String name){
		return this.context.getBean(name);
	}

	/**
	 * @return the context
	 */
	public ApplicationContext getContext() {
		return context;
	}

	/**
	 * @param context the context to set
	 */
	public void setContext(ApplicationContext context) {
		this.context = context;
	}

	/**
	 * @return the springCfgFiles
	 */
	public List getSpringCfgFiles() {
		return springCfgFiles;
	}

	/**
	 * @param springCfgFiles the springCfgFiles to set
	 */
	public void setSpringCfgFiles(List springCfgFiles) {
		this.springCfgFiles.addAll(springCfgFiles);
	}

	/**
	 * @return the servletContext
	 */
	public ServletContext getServletContext() {
		return servletContext;
	}
	
	public String getContextPath() {
		if(servletContext == null) return null;
		return "/".equals(servletContext.getContextPath()) ? "" : servletContext.getContextPath();
	}

	/**
	 * @param servletContext the servletContext to set
	 */
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}
	
	/**
	 * 获得Bean
	 * @param <T>
	 * @param requiredType Bean的类型
	 * @return
	 */
	public <T> T getBean(Class<T> requiredType) {
		return this.context.getBean(requiredType);
	}
	
	/**
	 * 获得Bean
	 * @param <T>
	 * @param name Bean名称
	 * @param requiredType Bean的类型
	 * @return
	 */
	public <T> T getBean(String name, Class<T> requiredType) {
		return this.context.getBean(name, requiredType);
	}

	public Resource[] getHibernateCfgFiles() {
		return hibernateCfgFiles;
	}

	public void setHibernateCfgFiles(Resource[] hibernateCfgFiles) {
		this.hibernateCfgFiles = hibernateCfgFiles;
	}
	
	/**
	 * 设置初始化结束
	 */
	public void finish() {
		this.initFinish.set(true);
	}
	
	/**
	 * 是否初始化结束
	 * @return
	 */
	public boolean isInitFinish() {
		return this.initFinish.get();
	}
	
}
