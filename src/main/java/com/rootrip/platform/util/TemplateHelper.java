package com.rootrip.platform.util;

import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import com.rootrip.platform.Platform;
import com.rootrip.platform.exception.PlatformException;

import freemarker.template.Template;

/**
 * 
 * @author liubin
 * @date 2012-11-19
 *
 */
public class TemplateHelper {
	
	private Template template = null;
	
	private static final String defaultEncoding = "UTF-8";
	
	private Map<String, Object> root = new HashMap<String, Object>();
	
	private TemplateHelper() {}
	
	public static TemplateHelper create(String fileName) {
		try {
			TemplateHelper helper = new TemplateHelper();
			helper.setTemplate(Platform.getInstance().getApp().getCfg().getTemplate(fileName, defaultEncoding));
			return helper;
		} catch (Exception e) {
			throw new PlatformException("模板文件读取失败!", e);
		}
	}
	

	public void put(String key, Object value) {
		root.put(key, value);
	}

	public String getResult() {
		try {
			StringWriter writer = new StringWriter();
			template.process(root, writer);
			return writer.toString();
		} catch (Exception e) {
			throw new PlatformException("模板文件初始化失败!", e);
		}
	}

	/**
	 * 设置日期格式
	 * @param string
	 */
	public void setDateFormat(String string) {
		root.put("dateFormat", new SimpleDateFormat(string));
	}


	public void setTemplate(Template template) {
		this.template = template;
	}
}