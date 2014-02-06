package com.rootrip.platform.common.web.wro;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ro.isdc.wro.model.resource.support.naming.TimestampNamingStrategy;

import com.rootrip.platform.util.DateUtil;

public class DailyNamingStrategy extends TimestampNamingStrategy {
	
	protected final Logger log = LoggerFactory.getLogger(DailyNamingStrategy.class);

	@Override
	protected long getTimestamp() {
		String dateStr = DateUtil.formatDate(new Date(), "yyyyMMddHH");
		return Long.valueOf(dateStr);
	}

	

}
