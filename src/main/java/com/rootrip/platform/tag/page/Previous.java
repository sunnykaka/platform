package com.rootrip.platform.tag.page;

/**
 * 
 * @author liubin
 * @date 2012-11-9
 *
 */
public class Previous extends Label {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6933792140573457884L;
	
	public Previous(){
		super();
		super.setText("上一页");
	}

	/* (non-Javadoc)
	 * @see com.rootrip.platform.tag.page.Label#hasAction()
	 */
	@Override
	public boolean hasAction() {
		return (Boolean)super.pageContext.getAttribute(Pagination.HAS_PREVIOUS);
	}

	/*
	 * (non-Javadoc)
	 * @see com.rootrip.platform.tag.page.Label#getTargetPageIndex()
	 */
	@Override
	public Integer getTargetPageIndex() {
		return ((Integer)super.pageContext.getAttribute(Pagination.PAGE_INDEX)) - 1;
	}

}
