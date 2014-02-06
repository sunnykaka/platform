package com.rootrip.platform.tag.page;

import java.io.Writer;

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
public class Cursors extends TagSupport {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	// 页标个数
	private Integer count = 9;
	
	// 模板
	private String template;
	
	// 当前页标模板
	private String currentTemplate;

	/**
	 * 
	 */
	private static final long serialVersionUID = 4293911497560646198L;
	
	@Override
	public int doStartTag() throws JspException {
		if((Boolean)super.pageContext.getAttribute(Pagination.COUNT_TOTAL_PAGE)){
			Writer out = super.pageContext.getOut();
			String url = (String) this.pageContext.getAttribute(Pagination.ACTION_URL);
			int index = (Integer) this.pageContext.getAttribute(Pagination.PAGE_INDEX);
			int total = (Integer) this.pageContext.getAttribute(Pagination.TOTAL_PAGE);
			int start = index - count / 2;
			if(start <= 0){
				start = 1;
			}
			int end = start + count;
			if(end > total){
				end = total;
			}
			try{
				if(!"".equals(template) && null != template){
					if(currentTemplate == null || "".equals(currentTemplate)){
						currentTemplate = template;
					}
					for(int i = start; i < end; i++){
						if(i == index){
							out.write(currentTemplate.replaceAll("\\{cursor\\}", i + ""));
						}else{
							out.write(template.replaceAll("\\{url\\}", url + "&pageIndex=" + index).replaceAll("\\{cursor\\}", i + ""));
						}
					}
				} else {
					for(int i = start; i < end; i++){
						out.write("<span>");
						if(i == index){
							out.write(i);
						} else {
							out.append("<a href=\"");
							out.append(url);
							out.append("&pageIndex=");
							out.append(index + "");
							out.append("\">");
							out.append(i + "");
							out.append("</a>");
						}
						out.write("</span>");
					}
				}
			}catch(Exception ex){
				logger.error(ex.getMessage(), ex);
			}
		}
		return SKIP_BODY;
	}

	/**
	 * @return the count
	 */
	public Integer getCount() {
		return count;
	}

	/**
	 * @param count the count to set
	 */
	public void setCount(Integer count) {
		this.count = count;
	}

	/**
	 * @return the currentTemplate
	 */
	public String getCurrentTemplate() {
		return currentTemplate;
	}

	/**
	 * @param currentTemplate the currentTemplate to set
	 */
	public void setCurrentTemplate(String currentTemplate) {
		this.currentTemplate = currentTemplate;
	}

	/**
	 * @return the template
	 */
	public String getTemplate() {
		return template;
	}

	/**
	 * @param template the template to set
	 */
	public void setTemplate(String template) {
		this.template = template;
	}

}
