package com.rootrip.platform.common.dao.pagination;

import org.hibernate.Criteria;
import org.hibernate.Query;

import com.rootrip.platform.common.dao.GenericDao;
import com.rootrip.platform.common.dao.Page;

/**
 * 
 * @author liubin
 *
 */
@SuppressWarnings("unchecked")
public class HibernatePaginationAnalyzer extends PaginationAnalyzer {

	/* (non-Javadoc)
	 * @see com.rootrip.platform.common.dao.pagination.PageinationAnalyzer#analyse(java.lang.Object, com.rootrip.platform.common.dao.Page)
	 */
	@Override
	public Object analyse(Object q, Page page) {
		if(page.getPageSize() > 0) {
			if(q instanceof Query){
				Query query = (Query)q;
				query.setFirstResult((page.getPageIndex()  - 1) * page.getPageSize());
//				query.setMaxResults(page.isCountTotalPage() ? page.getPageSize() : page.getPageSize() + 1);
				query.setMaxResults(page.getPageSize());
			} else if (q instanceof Criteria){
				Criteria c = (Criteria) q;
				c.setFirstResult((page.getPageIndex()  - 1) * page.getPageSize());
//				c.setMaxResults(page.isCountTotalPage() ? page.getPageSize() : page.getPageSize() + 1);
				c.setMaxResults(page.getPageSize());
			}
		}
		return q;
	}

	/*
	 * (non-Javadoc)
	 * @see com.rootrip.platform.common.dao.pagination.PaginationAnalyzer#analyse(java.lang.Object, com.rootrip.platform.common.dao.Page, com.rootrip.platform.common.dao.GenericDao, java.lang.Object[])
	 */
	@Override
	public Object analyse(Object query, Page page, GenericDao dao, Object... args) {
		if(page.isCountTotalPage()){
			page.setTotalRecordCount(dao.count(((Query)query).getQueryString(), args));
		}
		return this.analyse(query, page);
	}

}
