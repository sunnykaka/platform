package com.rootrip.platform.common.web.page;

import java.util.List;

import com.rootrip.platform.common.dao.Page;

/**
 * 
 * @author liubin
 *
 */
@SuppressWarnings("unchecked")
public class PageEngine extends Page {
	
	// 是否有下一页
	private boolean hasNextPage;

	/**
	 * 构造方法
	 * @param pageSize 每页记录数
	 * @param pageIndex 当前页数
	 */
	public PageEngine(Integer pageSize, Integer pageIndex, boolean countTotalPage) {
		super(pageSize, pageIndex, countTotalPage);
	}
	
	/**
	 * 是否有下一页
	 * @return 是/否
	 */
	public boolean hasNextPage() {
	    return this.hasNextPage;
	}
	
	/**
	 * 是否有上一页
	 * @return 是/否
	 */
	public boolean hasPreviousPage() {
		return super.getPageIndex() > 1;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.rootrip.platform.common.dao.Page#setTotalRecordCount(java.lang.Integer)
	 */
	public void setTotalRecordCount(Integer totalRecordCount){
		super.setTotalRecordCount(totalRecordCount);
		if(super.isCountTotalPage()){
			this.hasNextPage = super.getPageIndex() < super.getPageCount();
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.rootrip.platform.common.dao.Page#setRecord(java.util.List)
	 */
	public void setRecord(List record){
		if(!super.isCountTotalPage()){
			this.hasNextPage = super.getPageSize() < record.size();
			if(this.hasNextPage) {
				super.setRecord(record.subList(0, this.getPageSize()));
			} else {
				super.setRecord(record);
			}
		} else {
			super.setRecord(record);
		}
	}

}
