package com.rootrip.platform.common.web.page;

import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.rootrip.platform.util.TemplateHelper;

/**
 * 
 * @author liubin
 *
 */
@SuppressWarnings("unchecked")
public class FreemarkerPageEngine extends PageEngine {

	// 模板文件名
	private String templateName;
	
	private HttpServletRequest request;

	public FreemarkerPageEngine(String templateName,Integer pageSize, Integer pageIndex, HttpServletRequest request, boolean countTotalPage) {
		super(pageSize, pageIndex, countTotalPage);
		this.templateName = templateName;
		this.request = request;
	}
	
	@Override
	public String toString() {
		try {
			// 取得freemarker的模版
			TemplateHelper template = TemplateHelper.create(templateName);

			// 把数据填入上下文
			template.put("page", this);
			Map params = request.getParameterMap();
			String oldUrl = request.getRequestURI();
			if(oldUrl.startsWith("//")) {
				//判断不是双斜杠开头
				logger.warn("page url starts with '//' found:" + oldUrl);
				oldUrl = oldUrl.replaceFirst("//", "/");
			}
			StringBuffer url = new StringBuffer(oldUrl);
			String key = null;
			String[] values = null;
			url.append("?");
			for(Iterator<Map.Entry> iter = params.entrySet().iterator(); iter.hasNext();){
				Map.Entry entry = iter.next();
				key = (String) entry.getKey();
				values = (String[]) entry.getValue();
				if(key.equals("pageIndex")){
					continue;
				}
				for(int i = 0; i < values.length; i++){
					url.append(key);
					url.append("=");
					try {
						url.append(java.net.URLEncoder.encode(values[i], "UTF-8"));
					} catch (UnsupportedEncodingException e) {
						logger.warn(e.getMessage(), e);
					}
					url.append("&");
				}
			}
			url.deleteCharAt(url.length() - 1);
			template.put("url", url);
			// 判断url后是否有参数
			if(url.lastIndexOf("?") == -1){
				template.put("hasPara", false);
			} else {
				template.put("hasPara", true);
			}
//			template.put("contextPath", request.getContextPath());
			return template.getResult();
		} catch (Exception e) {
			logger.error("", e);
			return "";
		}
	}

}
