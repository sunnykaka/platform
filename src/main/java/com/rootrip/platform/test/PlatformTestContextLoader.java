package com.rootrip.platform.test;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.test.context.MergedContextConfiguration;
import org.springframework.test.context.support.AbstractContextLoader;

import com.rootrip.platform.Platform;


/**
 * 
 * @author liubin
 * @date 2012-11-12
 *
 */
public class PlatformTestContextLoader extends AbstractContextLoader{
	
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	protected String getResourceSuffix() {
		return "-context.xml";
	}
	
	@Override
	public ApplicationContext loadContext(
			MergedContextConfiguration mergedConfig) throws Exception {
		ApplicationContext context = initContext();
		return context;
	}

	@SuppressWarnings("unchecked")
	private ApplicationContext initContext() {
		ApplicationContext appContext = new ClassPathXmlApplicationContext("Platform.xml");
		Platform platform = (Platform) appContext.getBean("platform");
		platform.init();
		List configFiles = platform.getSpringCfgFiles();
		int count = configFiles.size();
		String[] cfgs = new String[count];
		for (int i = 0; i < count; i++) {
			cfgs[i] = (String) configFiles.get(i);
		}
		FileSystemXmlApplicationContext ac = new FileSystemXmlApplicationContext(cfgs);
		platform.setContext(ac);
		platform.setWebRootPath(System.getProperty("user.dir") + "/src/main/webapp");
		ac.refresh();
		platform.initApp();
		return ac;
	}

	@Override
	public ApplicationContext loadContext(String... locations) throws Exception {
		ApplicationContext context = initContext();
		return context;
	}

}
