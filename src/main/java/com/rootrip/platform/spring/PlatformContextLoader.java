package com.rootrip.platform.spring;

import java.util.List;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.ContextLoader;

import com.rootrip.platform.Platform;

/**
 * 
 * @author liubin
 * @date 2012-11-9
 *
 */
public class PlatformContextLoader extends ContextLoader {
	
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	protected void configureAndRefreshWebApplicationContext(
			ConfigurableWebApplicationContext wac, ServletContext sc) {
		
		try {
			logger.info("平台初始化...");
			
			ConfigurableWebApplicationContext tempWac = (ConfigurableWebApplicationContext)createWebApplicationContext(sc);
			tempWac.setServletContext(sc);
			String[] tempLocations = new String[1];
			tempLocations[0] = "classpath:/Platform.xml";
			tempWac.setConfigLocations(tempLocations);
			tempWac.refresh();

			Platform platform = (Platform) tempWac.getBean("platform");
			
			platform.init();
			tempWac.close();
			
			if (ObjectUtils.identityToString(wac).equals(wac.getId())) {
				// The application context id is still set to its original default value
				// -> assign a more useful id based on available information
				String idParam = sc.getInitParameter(CONTEXT_ID_PARAM);
				if (idParam != null) {
					wac.setId(idParam);
				}
				else {
					// Generate default id...
					if (sc.getMajorVersion() == 2 && sc.getMinorVersion() < 5) {
						// Servlet <= 2.4: resort to name specified in web.xml, if any.
						wac.setId(ConfigurableWebApplicationContext.APPLICATION_CONTEXT_ID_PREFIX +
								ObjectUtils.getDisplayString(sc.getServletContextName()));
					}
					else {
						wac.setId(ConfigurableWebApplicationContext.APPLICATION_CONTEXT_ID_PREFIX +
								ObjectUtils.getDisplayString(sc.getContextPath()));
					}
				}
			}
			
			// Determine parent for root web application context, if any.
			ApplicationContext parent = loadParentContext(sc);

			wac.setParent(parent);
			wac.setServletContext(sc);
			List configFiles = platform.getSpringCfgFiles();
			int count = configFiles.size();
			String[] cfgs = new String[count];
			for (int i = 0; i < count; i++) {
				cfgs[i] = (String) configFiles.get(i);
			}
			wac.setConfigLocations(cfgs);
			
			customizeContext(sc, wac);
			platform.setContext(wac);
			platform.setServletContext(sc);
			wac.refresh();
			
			platform.setWebRootPath(sc.getRealPath("/"));
			
			logger.info("平台初始化成功,开始加载应用...");
			platform.initApp();
			logger.info("平台初始化完毕.");
			
		} catch (Exception e) {
			logger.error("平台初始化失败...", e);
		}
		
	}
}