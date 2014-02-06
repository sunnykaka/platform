package com.rootrip.platform;

import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import com.rootrip.platform.common.web.el.PlatformFunction;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;

/**
 * 
 * @author liubin
 * @date 2012-11-9
 *
 */
@SuppressWarnings("unchecked")
public abstract class Application {
	
	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	private List springCfgFiles;
	
	private Resource[] hibernateMappingFiles;
	
	private Properties params;
	
	private Platform platform;
	
	private String name;
	
	protected String attachPath;
	
	private Configuration cfg = null;
	
	//config.properties的键值对
	private final Map<String, String> configProperties = new HashMap<String, String>();
	
	//e.g. http://localhost:8080/rootrip
	private String rootripPath = null;
	//e.g. http://localhost:8080/rootripAdmin
	private String rootripAdminPath = null;
	
	//e.g. /rootrip
	private String rootripContextRoot = null;
	//e.g. /rootripAdmin
	private String rootripAdminContextRoot = null;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the platform
	 */
	public Platform getPlatform() {
		return platform;
	}

	/**
	 * @param platform the platform to set
	 */
	public void setPlatform(Platform platform) {
		this.platform = platform;
		this.platform.setApp(this);
	}

	/**
	 * @return the params
	 */
	public Properties getParams() {
		return params;
	}

	/**
	 * @param params the params to set
	 */
	public void setParams(Properties params) {
		this.params = params;
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
		this.springCfgFiles = springCfgFiles;
	}
	
	/**
	 * 该应用初始化时候所调用的方法
	 * @return 是否成功
	 */
	protected boolean init() {
		try {
			if(attachPath == null || "".equals(attachPath)) {
				attachPath = this.getPlatform().getWebRootPath();
			}
			
			System.setProperty("org.jboss.logging.provider", "slf4j");
			
			//初始化freemarker
			cfg = new Configuration();
			cfg.setTemplateLoader(new ClassTemplateLoader(Application.class,
					"/templates"));
			cfg.setObjectWrapper(new DefaultObjectWrapper());
			
			//初始化config.properties
			configProperties.clear();
			Properties props = new Properties();
			props.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties"));
			for(Iterator<Entry<Object,Object>> iter = props.entrySet().iterator(); iter.hasNext();) {
				Entry<Object,Object> entry = iter.next();
				configProperties.put((String)entry.getKey(), (String)entry.getValue());
			}
			
			//从配置文件中读取contextPath
			String rootripPath = getConfigValue("rootripPath");
			Validate.notNull(rootripPath, "config.properties缺少关键参数%s", rootripPath);
			this.rootripPath = rootripPath;
			URL url = new URL(rootripPath);
			rootripContextRoot = url.getPath();
			if("/".equals(rootripContextRoot)) {
				rootripContextRoot = "";
			}
			
			String rootripAdminPath = getConfigValue("rootripAdminPath");
			Validate.notNull(rootripAdminPath, "config.properties缺少关键参数%s", rootripAdminPath);
			this.rootripAdminPath = rootripAdminPath;
			url = new URL(rootripAdminPath);
			rootripAdminContextRoot = url.getPath();
			if("/".equals(rootripAdminContextRoot)) {
				rootripAdminContextRoot = "";
			}
			
			return true;
		} catch (Exception e) {
			logger.error("系统初始化错误！", e);
			return false;
		}
	}
	
	public Resource[] getHibernateMappingFiles() {
		return hibernateMappingFiles;
	}

	public void setHibernateMappingFiles(Resource[] hibernateMappingFiles) {
		this.hibernateMappingFiles = hibernateMappingFiles;
	}

	public String getAttachPath() {
		return attachPath;
	}

	public void setAttachPath(String attachPath) {
		this.attachPath = attachPath;
	}

	public Configuration getCfg() {
		return cfg;
	}

	public Map<String, String> getConfigProperties() {
		return configProperties;
	}
	
	public String getConfigValue(String key) {
		return configProperties.get(key);
	}

	public String getRootripContextRoot() {
		return rootripContextRoot;
	}

	public String getRootripAdminContextRoot() {
		return rootripAdminContextRoot;
	}

	public String getRootripPath() {
		return rootripPath;
	}

	public String getRootripAdminPath() {
		return rootripAdminPath;
	}
}
