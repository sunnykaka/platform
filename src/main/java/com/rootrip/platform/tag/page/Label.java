package com.rootrip.platform.tag.page;

import java.io.Writer;

import javax.servlet.jsp.tagext.TagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author liubin
 * @date 2012-11-9
 *
 */
public abstract class Label extends TagSupport {
	
	//	 显示文本
	private String text;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 
	 */
	private static final long serialVersionUID = -8175193643041899179L;
	
	public int doStartTag(){
		Writer out = super.pageContext.getOut();
		String url = (String) super.pageContext.getAttribute(Pagination.ACTION_URL);
		try{
			if(this.hasAction()) {
				out.write("<a href=\"");
				out.write(url);
				if(url.lastIndexOf("?") == -1){
					out.write("?");
				} else {
					out.write("&");
				}
				out.write("pageIndex=");
				out.write(this.getTargetPageIndex().toString());
				out.write("\">");
				out.write(text);
				out.write("</a>");
			} else {
				out.write(text);
			}
		}catch(Exception ex){
			logger.error(ex.getMessage(), ex);
		}
		return SKIP_BODY;
	}
	
	/**
	 * 是否可点击
	 * @return 是/否
	 */
	public abstract boolean hasAction();
	
	/**
	 * 取得目标页号
	 * @return 目标页号
	 */
	public abstract Integer getTargetPageIndex();

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}
}
