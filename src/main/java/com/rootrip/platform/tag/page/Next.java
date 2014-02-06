package com.rootrip.platform.tag.page;

/**
 * 
 * @author liubin
 * @date 2012-11-9
 *
 */
public class Next extends Label {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Next(){
		super();
		super.setText("下一页");
	}

	/* (non-Javadoc)
	 * @see com.rootrip.platform.tag.page.Label#hasAction()
	 */
	@Override
	public boolean hasAction() {
		return (Boolean)super.pageContext.getAttribute(Pagination.HAS_NEXT);
	}

	/*
	 * (non-Javadoc)
	 * @see com.rootrip.platform.tag.page.Label#getTargetPageIndex()
	 */
	@Override
	public Integer getTargetPageIndex() {
		return ((Integer)super.pageContext.getAttribute(Pagination.PAGE_INDEX)) + 1;
	}

}
