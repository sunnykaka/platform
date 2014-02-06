package com.rootrip.platform.springmvc;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.support.RequestDataValueProcessor;

import com.rootrip.platform.util.Servlets;

public class PageRequestDataValueProcessor implements RequestDataValueProcessor {
	
	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	public String processAction(HttpServletRequest request, String action) {
		return action;
	}

	@Override
	public String processFormFieldValue(HttpServletRequest request,
			String name, String value, String type) {
		return value;
	}

	@Override
	public Map<String, String> getExtraHiddenFields(HttpServletRequest request) {
//		String pageIndex = request.getParameter("pageIndex");
//		String pageSize = request.getParameter("pageSize");
//		Map<String, String> map = Maps.newHashMap();
//		if(NumberUtils.isNumber(pageIndex)) {
//			map.put("pageIndex", pageIndex);
//		}
//		if(NumberUtils.isNumber(pageSize)) {
//			map.put("pageSize", pageSize);
//		}
//		return map;
		return null;
	}

	@Override
	public String processUrl(HttpServletRequest request, String url) {
		String pageIndex = request.getParameter("pageIndex");
		String pageSize = request.getParameter("pageSize");
		StringBuilder urlBuilder = new StringBuilder(url);
		if(NumberUtils.isNumber(pageIndex) && url.indexOf("pageIndex") == -1) {
			appendUrl(urlBuilder, "pageIndex=" + pageIndex);
		}
		if(NumberUtils.isNumber(pageSize) && url.indexOf("pageSize") == -1) {
			appendUrl(urlBuilder, "pageSize=" + pageSize);
		}
		Map<String, String[]> params = Servlets.getParametersStartingWith(request, "sParam_");
		for(Map.Entry<String, String[]> entry : params.entrySet()) {
			String key = entry.getKey();
			String[] values = entry.getValue();
			if(values != null) {
				for(String value : values) {
					try {
						appendUrl(urlBuilder, new StringBuilder("sParam_").append(key).append("=").append(URLEncoder.encode(value, "UTF-8")).toString());
					} catch (UnsupportedEncodingException e) {
						log.error("", e);
					}
				}
			}
		}
		return urlBuilder.toString();
	}

	private void appendUrl(StringBuilder url, String str) {
		int index = url.indexOf("?");
		if(index == -1) {
			url.append("?").append(str);
		} else if(index == url.length() - 1) {
			url.append(str);
		} else {
			url.append("&").append(str);
		}
	}
}

