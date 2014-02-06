package com.rootrip.platform.tag.page;

/**
 * 
 * @author liubin
 * @date 2012-11-9
 *
 */
public class First extends Label {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3642072334628664367L;
	
	public First(){
		super();
		super.setText("首页");
	}

	/*
	 * (non-Javadoc)
	 * @see com.rootrip.platform.tag.page.Label#hasAction()
	 */
	@Override
	public boolean hasAction() {
		return (Boolean) super.pageContext.getAttribute(Pagination.HAS_PREVIOUS);
	}

	/*
	 * (non-Javadoc)
	 * @see com.rootrip.platform.tag.page.Label#getTargetPageIndex()
	 */
	@Override
	public Integer getTargetPageIndex() {
		return 1;
	}
	
}
