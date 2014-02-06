package com.rootrip.platform.tag;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyContent;

/**
 * 
 * @author liubin
 * @date 2012-11-9
 *
 */
public class TagUtils {

	public static void writePrevious(BodyContent bodyContent, PageContext pageContext) throws Exception {
		if (bodyContent != null) {
			JspWriter writer = pageContext.getOut();
			if (writer instanceof BodyContent){
				writer = ((BodyContent) writer).getEnclosingWriter();
			}
			writer.print(bodyContent.getString());
			bodyContent.clearBody();
		}
	}
}
