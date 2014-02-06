package com.rootrip.platform.common.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author liubin
 *
 */
@SuppressWarnings("unchecked")
public class Page {
	
	protected final Logger logger = LoggerFactory.getLogger(Page.class); 
	
	/**
	 * 构造方法
	 * @param pageSize 每页记录数
	 * @param pageIndex 当前页数
	 */
	public Page(Integer pageSize, Integer pageIndex, boolean countTotalPage){
		this.pageSize = pageSize;
		this.pageIndex = pageIndex;
		this.countTotalPage = countTotalPage;
	}

	// 每页记录数
	private Integer pageSize;
	
	// 当前页数
	private Integer pageIndex;
	
	// 总页数
	private Integer pageCount;
	
	// 总记录数
	private Integer totalRecordCount;
	
	// 是否需要计算总页数
	private boolean countTotalPage;
	
	// 本页记录
	private List record;

	/**
	 * @return the totalRecordCount
	 */
	public Integer getTotalRecordCount() {
		return totalRecordCount;
	}

	/**
	 * @param totalRecordCount the totalRecordCount to set
	 */
	public void setTotalRecordCount(Integer totalRecordCount) {
		this.pageCount = totalRecordCount % this.pageSize > 0 ? totalRecordCount / this.pageSize + 1 : totalRecordCount / this.pageSize;
		if(this.pageIndex > this.pageCount) {
			this.pageIndex = this.pageCount;
		} else if(this.pageIndex < 1) {
			this.pageIndex = 1;
		}
		this.totalRecordCount = totalRecordCount;
	}

	/**
	 * @return the pageIndex
	 */
	public Integer getPageIndex() {
		return pageIndex;
	}

	/**
	 * @return the pageSize
	 */
	public Integer getPageSize() {
		return pageSize;
	}

	/**
	 * @return the totalPageCount
	 */
	public Integer getPageCount() {
		return pageCount;
	}

	/**
	 * @return the countTotalPage
	 */
	public boolean isCountTotalPage() {
		return countTotalPage;
	}

	/**
	 * @return the record
	 */
	public List getRecord() {
		return record;
	}

	/**
	 * @param record the record to set
	 */
	public void setRecord(List record) {
		this.record = record;
	}
	
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
}
