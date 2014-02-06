package com.rootrip.platform.common.dao.pagination;

import com.rootrip.platform.common.dao.GenericDao;
import com.rootrip.platform.common.dao.Page;

/**
 * 
 * @author liubin
 *
 */
@SuppressWarnings("unchecked")
public class OraclePaginationAnalyzer extends PaginationAnalyzer{

	/*
	 * (non-Javadoc)
	 * @see com.rootrip.platform.common.dao.pagination.PageinationAnalyzer#analyse(java.lang.String, com.rootrip.platform.common.dao.Page)
	 */
	public String analyse(Object ql, Page page) {
		Integer start = 0;
		Integer end = 0;
		if(page.getPageSize() > 0){
			start = (page.getPageIndex() - 1) * page.getPageSize() + 1;
			end = page.isCountTotalPage() ? start + page.getPageSize() : start + page.getPageSize() + 1;
		}
		String sql = ((String)ql).trim().toLowerCase();
		String orderBy = null;
		if(sql.lastIndexOf(" order ") != -1){
			orderBy = sql.substring(sql.lastIndexOf(" order "), sql.length());
		}
		StringBuffer sb = new StringBuffer("select t598256180.* from (select row_number() over(");
		if(orderBy != null && orderBy.length() > 0){
			sb.append(orderBy);
		}else{
			sb.append("order by rownum desc");
		}
		sb.append(") TMP_NUM110718, ");
		sb.append(sql.substring(6, orderBy == null ? sql.length() : sql.length() - orderBy.length()));
		sb.append(") t598256180");
		if(start < end){
			sb.append(" where t598256180.TMP_NUM110718 >= ");
			sb.append(start);
			sb.append(" and t598256180.TMP_NUM110718 < ");
			sb.append(end);
		}
		return sb.toString();
	}

	@Override
	public Object analyse(Object query, Page page, GenericDao dao, Object... args) {
		if(page.isCountTotalPage()){
			page.setTotalRecordCount(dao.count((String)query, args));
		}
		return this.analyse(query, page);
	}

}
