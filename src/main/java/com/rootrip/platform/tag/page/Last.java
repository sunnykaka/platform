package com.rootrip.platform.tag.page;

/**
 * 
 * @author liubin
 * @date 2012-11-9
 *
 */
public class Last extends Label {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4769745557955837698L;
	
	public Last(){
		super();
		super.setText("末页");
	}

	/* (non-Javadoc)
	 * @see com.rootrip.platform.tag.page.Label#hasAction()
	 */
	@Override
	public boolean hasAction() {
		return (Boolean)super.pageContext.getAttribute(Pagination.HAS_NEXT);
	}
	
	public int doStartTag(){
		if((Boolean)super.pageContext.getAttribute(Pagination.COUNT_TOTAL_PAGE)){
			return super.doStartTag();
		}
		return SKIP_BODY;
	}

	/*
	 * (non-Javadoc)
	 * @see com.rootrip.platform.tag.page.Label#getTargetPageIndex()
	 */
	@Override
	public Integer getTargetPageIndex() {
		return (Integer)super.pageContext.getAttribute(Pagination.TOTAL_PAGE);
	}

}
