/* Copyright 2009 The Revere Group
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.rootrip.platform.genericdao.dao.hibernate;

import java.io.Serializable;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.LockMode;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import com.rootrip.platform.common.dao.Entity;
import com.rootrip.platform.common.dao.Ordered;
import com.rootrip.platform.common.dao.Page;
import com.rootrip.platform.genericdao.dao.DAOUtil;
import com.rootrip.platform.genericdao.search.ExampleOptions;
import com.rootrip.platform.genericdao.search.Filter;
import com.rootrip.platform.genericdao.search.IMutableSearch;
import com.rootrip.platform.genericdao.search.ISearch;
import com.rootrip.platform.genericdao.search.Search;
import com.rootrip.platform.genericdao.search.SearchResult;
import com.rootrip.platform.id.IdGenerator;

/**
 * Implementation of <code>GenericDAO</code> using Hibernate.
 * The SessionFactory property is annotated for automatic resource injection.
 * 
 * @author dwolverton
 * 
 * @param <T>
 *            The type of the domain object for which this instance is to be
 *            used.
 * @param <ID>
 *            The type of the id of the domain object for which this instance is
 *            to be used.
 */
@SuppressWarnings("unchecked")
public class GenericDAOImpl<T extends Entity<ID>, ID extends Serializable> extends HibernateBaseDAO {

	protected Class<T> persistentClass = (Class<T>) DAOUtil.getTypeArguments(GenericDAOImpl.class, this.getClass()).get(0);
	
	@Autowired
	private IdGenerator idGenerator;
	
	// 删除order by字句使用的正则表达式
	private static Pattern removeOrderByPattern = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*", Pattern.CASE_INSENSITIVE);


	public int count(ISearch search) {
		if (search == null)
			search = new Search();
		return _count(persistentClass, search);
	}

	public T get(Serializable id) {
		return _get(persistentClass, id);
	}
	
	public T get(Serializable id, LockMode lockMode) {
		return _get(persistentClass, id, lockMode);
	}

	public T[] get(Serializable... ids) {
		return _get(persistentClass, ids);
	}

	public List<T> findAll() {
		return _all(persistentClass);
	}

	public void flush() {
		_flush();
	}
	
	public boolean isAttached(T entity) {
		return _sessionContains(entity);
	}

	public void refresh(T... entities) {
		_refresh(entities);
	}
	
	public boolean delete(T entity) {
		return _deleteEntity(entity);
	}
	
	public void delete(T... entities) {
		_deleteEntities(entities);
	}

	public boolean delete(Serializable id) {
		return _deleteById(persistentClass, id);
	}

	public void delete(Serializable... ids) {
		_deleteById(persistentClass, ids);
	}

	public void save(T entity) {
		//如果id为空,则生成一个新id
		if(entity.getId() == null) {
			Class<?> clazz = ClassUtils.getUserClass(entity);
			while(!clazz.getSuperclass().equals(Object.class)) {
				clazz = clazz.getSuperclass();
			}
			entity.setId((ID)Long.valueOf(idGenerator.nextId(clazz)));
		}
		//如果可排序,默认按照id的顺序排
		if(entity instanceof Ordered) {
			Ordered orderedEntity = (Ordered) entity;
			if(orderedEntity.getBySort() < 0) {
				orderedEntity.setBySort(0);
			} else if(orderedEntity.getBySort() > 255) {
				orderedEntity.setBySort(255);
			}
		}
		_saveOrUpdate(entity);
	}

	public void save(T... entities) {
		for(T entity : entities) {
			save(entity);
		}
	}

	public <RT> List<RT> search(IMutableSearch search) {
		if (search == null)
			return (List<RT>) findAll();
		if(search.getPagination() == null) {
			return _search(persistentClass, search);
		} else {
			SearchResult<RT> result = searchAndCount(search);
			return result.getResult();
		}
	}

	private <RT> SearchResult<RT> searchAndCount(IMutableSearch search) {
		if (search == null) {
			SearchResult<RT> result = new SearchResult<RT>();
			result.setResult((List<RT>) findAll());
			result.setTotalCount(result.getResult().size());
			return result;
		}
		Page page = search.getPagination();
		search.setPage(page.getPageIndex() == null ? 0 : page.getPageIndex() - 1);
		search.setMaxResults(page.getPageSize() == null ? 0 : page.getPageSize());
		
		SearchResult<RT> result = _searchAndCount(persistentClass, search);
		
		page.setTotalRecordCount(result.getTotalCount());
		page.setRecord(result.getResult());
		
		return result;
	}

	public <RT> RT searchUnique(IMutableSearch search) {
		return (RT) _searchUnique(persistentClass, search);
	}

	public Filter getFilterFromExample(T example) {
		return _getFilterFromExample(example);
	}

	public Filter getFilterFromExample(T example, ExampleOptions options) {
		return _getFilterFromExample(example, options);
	}

	public int update(String ql, Object... args) {
		Query query = super.getSession().createQuery(ql);
		for(int i = 0; i < args.length; i++){
			query.setParameter(i, args[i]);
		}
		return query.executeUpdate();
	}
	
	public <RT> List<RT> query(String ql, Page page, Object... args) {
		if(this.hasGroupBy(ql)) {
			StringBuilder hql = new StringBuilder(ql);
			StringBuilder countQL = new StringBuilder("select count(*) ");
			countQL.append(hql.substring(hql.indexOf("from"), hql.lastIndexOf("order")));
			List count = super.getHibernateTemplate().find(countQL.toString(), args);
			Query query = super.getSession().createQuery(hql.toString());
			for(int i = 0; i < args.length; i++) { 
				query.setParameter(i, args[i]);
			}
			if(page == null) {
				return query.list();
			} else {
				query.setFirstResult((page.getPageIndex() - 1) * page.getPageSize());
				query.setMaxResults(page.getPageSize());
				page.setRecord(query.list());
				page.setTotalRecordCount(count.size());
				return page.getRecord();
			}
		} else {
			Query query = getSession().createQuery(ql);
			for (int i = 0; i < args.length; i++) {
				query.setParameter(i, args[i]);
			}
			if(page == null) {
				return query.list();
			} else {
				this.analyse(query, page, args);
				page.setRecord(query.list());
				return page.getRecord();
			}
		}
	}
	
	private void analyse(Query query, Page page, Object... args) {
		if(page.isCountTotalPage()){
			page.setTotalRecordCount(this.count(query.getQueryString(), args));
		}
		this.analyse(query, page);
	}
	
	private void analyse(Query q, Page page) {
		if(page.getPageSize() > 0) {
			Query query = (Query)q;
			query.setFirstResult((page.getPageIndex()  - 1) * page.getPageSize());
			query.setMaxResults(page.getPageSize());
		}
	}
	
	public int count(String ql, Object... args) {
		Query query = getSession().createQuery(ql);
	    for (int i = 0; i < args.length; i++) {
	      query.setParameter(i, args[i]);
	    }
	    String countQueryString = " select count (*) " + this.removeSelect(this.removeOrderBy(ql));
	    List countlist = getHibernateTemplate().find(countQueryString, args);
	    Long totalCount = 0l;
	    if(countlist != null && countlist.size() >= 1){
	    	if(this.hasGroupBy(ql)){
	    		totalCount = Long.parseLong(countlist.size()+"");
	    	}else{
	    		totalCount = (Long) countlist.get(0);
	    	}
	    }

	    return totalCount.intValue();
	}
	
	private boolean hasGroupBy(String ql) {
		if(ql != null && !"".equals(ql)){
		    if(ql.indexOf("group by") > -1){
		    	return true;
		    }
		}
		return false;
	}
	
	/**
	 * 去除ql语句中的select子句
	 * @param ql 查询语句
	 * @return 删除后的语句
	 */
	private String removeSelect(String hql) {
		Assert.hasText(hql);
		int beginPos = hql.toLowerCase().indexOf("from");
		Assert.isTrue(beginPos != -1, " hql : " + hql + " must has a keyword 'from'");
		return hql.substring(beginPos);
	}
	
	/**
	 * 删除ql语句中的order by字句
	 * @param ql 查询语句
	 * @return 删除后的查询语句
	 */
	private String removeOrderBy(String ql){
		if(ql != null && !"".equals(ql)){
		    Matcher m = removeOrderByPattern.matcher(ql);
		    StringBuffer sb = new StringBuffer();
		    while (m.find()) {
		      m.appendReplacement(sb, "");
		    }
		    m.appendTail(sb);
		    return sb.toString();
		}
		return "";
	}
}