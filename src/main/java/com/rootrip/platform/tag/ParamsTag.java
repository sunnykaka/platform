package com.rootrip.platform.tag;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author liubin
 * @date 2012-11-9
 *
 */
@SuppressWarnings("unchecked")
public class ParamsTag extends TagSupport {

	private static final long serialVersionUID = 6916970144007071232L;
	
	// 输出格式
	private String format;
	
	// 需要的参数名称
	private Map names;
	
	private static final String FORMAT_FORM = "form";
	
	private static final String FORMAT_URL = "url";
	
	private static final Logger logger = LoggerFactory.getLogger(ParamsTag.class);
	
	@Override
	public int doStartTag() throws JspException {
		HttpServletRequest request = (HttpServletRequest) this.pageContext.getRequest();
		StringBuilder html = new StringBuilder();
		Map params = request.getParameterMap();
		if(FORMAT_FORM.equals(format)) {
			for(Iterator<Map.Entry> iter = params.entrySet().iterator(); iter.hasNext();){
				Map.Entry entry = iter.next();
				String key = (String) entry.getKey();
				String[] values = (String[]) entry.getValue();
				if(values == null) continue;
				for(int i = 0; i < values.length; i++) {
					if(isAllowParam(key)) {
						html.append("<input type=\"hidden\" name=\"");
						html.append(key);
						html.append("\" value=\"");
						html.append(values[i]);
						html.append("\" />");
					}
				}
			}
		} else if(FORMAT_URL.equals(format)) {
			for(Iterator<Map.Entry> iter = params.entrySet().iterator(); iter.hasNext();){
				Map.Entry entry = iter.next();
				String key = (String) entry.getKey();
				String[] values = (String[]) entry.getValue();
				if(values == null || values.length == 0) continue;
				for(int i = 0; i < values.length; i++) {
					if(isAllowParam(key)) {
						html.append(html.length() == 0 ? "?" : "&");
						html.append(key);
						html.append("=");
						html.append(values[i]);
					}
				}
			}
		}
		try {
			this.pageContext.getOut().print(html.toString());
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return SKIP_BODY;
	}
	
	/**
	 * 判断指定的name是否为允许输出
	 * @param name 指定的name
	 * @return 是/否
	 */
	private boolean isAllowParam(String name) {
		return this.names == null || this.names.get(name) != null;
	}

	/**
	 * @return the names
	 */
	public Map getNames() {
		return names;
	}

	/**
	 * @param names the names to set
	 */
	public void setNames(String names) {
		this.names = new HashMap();
		String[] tmp = names.split(",");
		for(int i = 0; i < tmp.length; i++) {
			this.names.put(tmp[i], "y");
		}
	}

	/**
	 * @return the type
	 */
	public String getFormat() {
		return format;
	}

	/**
	 * @param type the type to set
	 */
	public void setFormat(String type) {
		this.format = type;
	}

}
