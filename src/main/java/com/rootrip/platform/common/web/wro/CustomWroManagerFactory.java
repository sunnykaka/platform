package com.rootrip.platform.common.web.wro;

import ro.isdc.wro.manager.factory.standalone.DefaultStandaloneContextAwareManagerFactory;

public class CustomWroManagerFactory extends
		DefaultStandaloneContextAwareManagerFactory {
	public CustomWroManagerFactory() {
		setNamingStrategy(new DailyNamingStrategy());
	}
}
