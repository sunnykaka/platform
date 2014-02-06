package com.rootrip.platform.tag.page;

import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rootrip.platform.common.web.page.PageEngine;
import com.rootrip.platform.common.web.page.PageEngineFactory;
import com.rootrip.platform.tag.TagUtils;

/**
 * 
 * @author liubin
 * @date 2012-11-9
 *
 */
@SuppressWarnings("unchecked")
public class Pagination extends BodyTagSupport {
	
	public static final String PAGE_NAME = PageEngineFactory.PAGE_NAME;
	
	// Page对象在request中的名称
	private String name = PAGE_NAME;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 
	 */
	private static final long serialVersionUID = -182806881308230382L;
	
	/**
	 * 是否有下一页在PageContext中的名称
	 */
	public static final String HAS_NEXT = "com.rootrip.platform.tag.page.HAS_NEXT";
	
	/**
	 * 是否有上一页在PageContext中的名称
	 */
	public static final String HAS_PREVIOUS = "com.rootrip.platform.tag.page.HAS_PREVIOUS";
	
	/**
	 * 是否计算过总页数在PageContext中的名称
	 */
	public static final String COUNT_TOTAL_PAGE = "com.rootrip.platform.tag.page.COUNT_TOTAL_PAGE";
	
	/**
	 * 总页数在PageContext中的名称
	 */
	public static final String TOTAL_PAGE = "com.rootrip.platform.tag.page.TOTAL_PAGE";
	
	/**
	 * 总记录数在PageContext中的名称
	 */
	public static final String TOTAL_RECORD = "com.rootrip.platform.tag.page.TOTAL_RECORD";
	
	/**
	 * 当前页码在PageContext中的名称
	 */
	public static final String PAGE_INDEX = "com.rootrip.platform.tag.page.PAGE_INDEX";
	
	/**
	 * 分页操作访问的URL在PageContext中的名称
	 */
	public static final String ACTION_URL = "com.rootrip.platform.tag.page.ACTION_URL";

	public int doStartTag(){
		HttpServletRequest request = (HttpServletRequest)super.pageContext.getRequest();
		PageEngine page = (PageEngine)request.getAttribute(name);
		if(page == null){
			throw new IllegalArgumentException("在request中未找到健为" + this.name + "的page对象!");
		}
		Map params = request.getParameterMap();
		StringBuffer url = new StringBuffer((String)request.getAttribute(PageEngineFactory.URL_NAME));
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
		super.pageContext.setAttribute(ACTION_URL, url.toString());
		super.pageContext.setAttribute(HAS_NEXT, page.hasNextPage());
		super.pageContext.setAttribute(HAS_PREVIOUS, page.hasPreviousPage());
		super.pageContext.setAttribute(COUNT_TOTAL_PAGE, page.isCountTotalPage());
		super.pageContext.setAttribute(PAGE_INDEX, page.getPageIndex());
		if(page.isCountTotalPage()){
			super.pageContext.setAttribute(TOTAL_PAGE, page.getPageCount());
			super.pageContext.setAttribute(TOTAL_RECORD, page.getTotalRecordCount());
		}
		return EVAL_BODY_AGAIN;
	}
	
	public int doAfterBody(){
		try{
			TagUtils.writePrevious(bodyContent, pageContext);
		}catch(Exception ex){
			logger.error(ex.getMessage(), ex);
		}
		return SKIP_BODY;
	}
	
	public int doEndTag(){
		super.pageContext.removeAttribute(HAS_NEXT);
		super.pageContext.removeAttribute(HAS_PREVIOUS);
		super.pageContext.removeAttribute(COUNT_TOTAL_PAGE);
		super.pageContext.removeAttribute(TOTAL_PAGE);
		super.pageContext.removeAttribute(TOTAL_RECORD);
		super.pageContext.removeAttribute(PAGE_INDEX);
		super.pageContext.removeAttribute(ACTION_URL);
		return SKIP_BODY;
	}

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

}
