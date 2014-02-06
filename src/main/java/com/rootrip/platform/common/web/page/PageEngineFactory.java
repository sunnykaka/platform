package com.rootrip.platform.common.web.page;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rootrip.platform.common.dao.Page;

/**
 * 
 * @author liubin
 *
 */
public class PageEngineFactory {
	
	private static PageEngineFactory instance = new PageEngineFactory();
	
	// 记录日志
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	// 最大每页记录数
	protected static final int maxPageSize = 50;
	
	// 默认每页记录数
	protected static final int defaultPageSize = 10;
	
	public static final String URL_NAME = "real_url_name";
	
	public static final String PAGE_NAME = "page";
	
	public static final String DEFAULT_TAMPLATE_NAME = "page.ftl";
	
	private PageEngineFactory() {}
	
	/**
	 * 从request中获得Long型参数
	 * @param request 请求对象
	 * @param paramName 参数名称
	 * @return 参数值
	 */
	protected Long getLongParameter(HttpServletRequest request, String paramName){
		String str = request.getParameter(paramName);
		if(str == null || "".equals(str)){
			return null;
		}
		return Long.parseLong(str.trim());
	}
	
	/**
	 * 从request中获得Integer型参数
	 * @param request 请求对象
	 * @param paramName 参数名称
	 * @return 参数值
	 */
	protected Integer getIntegerParameter(HttpServletRequest request, String paramName){
		String str = request.getParameter(paramName);
		if(str == null || "".equals(str.trim())){
			return null;
		}
		return Integer.parseInt(str.trim());
	}
	
	/**
	 * 从request中获得Double型参数
	 * @param request 请求对象
	 * @param paramName 参数名称
	 * @return 参数值
	 */
	protected Double getDoubleParameter(HttpServletRequest request, String paramName) {
		String str = request.getParameter(paramName);
		if(str == null || "".equals(str.trim())){
			return null;
		}
		return Double.parseDouble(str.trim());
	}

	/**
	 * 从request中获得Float型参数
	 * @param request 请求对象
	 * @param paramName 参数名称
	 * @return 参数值
	 */
	protected Float getFloatParameter(HttpServletRequest request, String paramName) {
		String str = request.getParameter(paramName);
		if(str == null || "".equals(str.trim())){
			return null;
		}
		return Float.parseFloat(str.trim());
	}

	/**
	 * 获得Long参数数组
	 * @param request 请求对象
	 * @param paramName 参数名
	 * @return 参数值
	 */
	protected List<Long> getLongArrayParameter(HttpServletRequest request, String paramName){
		String[] values = request.getParameterValues(paramName);
		if(values != null && values.length > 0) {
			Long[] result = new Long[values.length];
			for(int i = 0; i < result.length; i++){
				result[i] = Long.parseLong(values[i]);
			}
			return Arrays.asList(result);
		}
		return null;
	}
	
	/**
	 * 取得分页对象并设置到Request中
	 * @param request 
	 * @return　PageEngine对象
	 */
	public static PageEngine getPageEngine(HttpServletRequest request) {
		return getPageEngine(request, PAGE_NAME, DEFAULT_TAMPLATE_NAME);
	}
	
	
	/**
	 * 取得分页对象并设置到Request中
	 * @param request 
	 * @param name request中的name
	 * @return　yPageEngine对象
	 */
	public static PageEngine getPageEngine(HttpServletRequest request, String name) {
		return getPageEngine(request, name, DEFAULT_TAMPLATE_NAME);
	}
	
	/**
	 * 取得分页对象并设置到Request中
	 * @param request 
	 * @param name request中的name
	 * @param templateName Velocity文件名
	 * @return　VelocityPageEngine对象
	 */
	public static PageEngine getPageEngine(HttpServletRequest request, String name, String templateName) {
		PageEngine page = instance.createPageEngine(request, templateName);
		request.setAttribute(name, page);
		return page;
	}
	
	/**
	 * 通过参数创建PageEngine
	 * @param request
	 * @param templateName　Velocity文件名
	 * @return VelocityPageEngine对像
	 */
	private FreemarkerPageEngine createPageEngine(HttpServletRequest request, String templateName) {
		Integer pageSize = this.getIntegerParameter(request, "pageSize");
		Integer pageIndex = this.getIntegerParameter(request, "pageIndex");
		if(pageSize == null){
			pageSize = defaultPageSize;
		}else if(pageSize > maxPageSize){
			pageSize = maxPageSize;
		}
		return new FreemarkerPageEngine(templateName, pageSize, pageIndex == null ? 1 : pageIndex, request, !"false".equals(request.getParameter("countPage")));
	}
	
	/**
	 * 创建简单分页对象
	 * @param pageSize 需要取多少条数据
	 * @param pageIndex 从哪一页开始,第一页从1开始.
	 * @return
	 */
	public static Page createSimplePage(Integer pageSize, Integer pageIndex) {
		return new Page(
				(pageSize == null ? defaultPageSize : pageSize), 
				(pageIndex == null ? 1 : pageIndex), 
				false);
	}
}
