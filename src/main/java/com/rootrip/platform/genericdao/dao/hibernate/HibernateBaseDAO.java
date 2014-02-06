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
import java.lang.reflect.Array;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.NonUniqueResultException;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.rootrip.platform.common.dao.Entity;
import com.rootrip.platform.genericdao.search.ExampleOptions;
import com.rootrip.platform.genericdao.search.Filter;
import com.rootrip.platform.genericdao.search.ISearch;
import com.rootrip.platform.genericdao.search.SearchResult;
import com.rootrip.platform.genericdao.search.hibernate.HibernateMetadataUtil;
import com.rootrip.platform.genericdao.search.hibernate.HibernateSearchProcessor;

/**
 * Base class for DAOs that uses Hibernate SessionFactory and HQL for searches.
 * This is the heart of Hibernate Generic DAO.
 * 
 * @author dwolverton
 * 
 */
@SuppressWarnings("unchecked")
public class HibernateBaseDAO extends HibernateDaoSupport {

	private HibernateSearchProcessor searchProcessor;

	private HibernateMetadataUtil metadataUtil;


	@Override
	protected void initDao() throws Exception {
		super.initDao();
		searchProcessor = HibernateSearchProcessor.getInstanceForSessionFactory(getSessionFactory());
		metadataUtil = HibernateMetadataUtil.getInstanceForSessionFactory(getSessionFactory());
	}
	
	/**
	 * Get the instance of HibernateMetadataUtil associated with the session
	 * factory
	 */
	protected HibernateMetadataUtil getMetadataUtil() {
		return metadataUtil;
	}

	/**
	 * Get the instance of EJBSearchProcessor associated with the session
	 * factory
	 */
	protected HibernateSearchProcessor getSearchProcessor() {
		return searchProcessor;
	}


	/**
	 * <p>
	 * Calls Hibernate's <code>saveOrUpdate()</code>, which behaves as follows:
	 * 
	 * <p>
	 * Either <code>save()</code> or <code>update()</code> based on the
	 * following rules
	 * <ul>
	 * <li>if the object is already persistent in this session, do nothing
	 * <li>
	 * if another object associated with the session has the same identifier,
	 * throw an exception
	 * <li>if the object has no identifier property, save() it
	 * <li>if the object's identifier has the value assigned to a newly
	 * instantiated object, save() it
	 * <li>if the object is versioned (by a &lt;version&gt; or
	 * &lt;timestamp&gt;), and the version property value is the same value
	 * assigned to a newly instantiated object, save() it
	 * <li>otherwise update() the object
	 * </ul>
	 */
	protected void _saveOrUpdate(Entity<? extends Serializable> entity) {
		super.getHibernateTemplate().saveOrUpdate(entity);
	}

	/**
	 * Remove the entity of the specified class with the specified id from the
	 * datastore.
	 * 
	 * @return <code>true</code> if the object is found in the datastore and
	 *         deleted, <code>false</code> if the item is not found.
	 */
	protected boolean _deleteById(Class<?> type, Serializable id) {
		if (id != null) {
			type = metadataUtil.getUnproxiedClass(type); //Get the real entity class
			Object entity = super.getHibernateTemplate().get(type, id);
			if (entity != null) {
				super.getHibernateTemplate().delete(entity);
				return true;
			}
		}
		return false;
	}

	/**
	 * Remove all the entities of the given type from the datastore that have
	 * one of these ids.
	 */
	protected void _deleteById(Class<?> type, Serializable... ids) {
		type = metadataUtil.getUnproxiedClass(type); //Get the real entity class
		Criteria c = getSession().createCriteria(type);
		c.add(Restrictions.in("id", ids));
		for (Object entity : c.list()) {
			super.getHibernateTemplate().delete(entity);
		}
	}

	/**
	 * Remove the specified entity from the datastore.
	 * 
	 * @return <code>true</code> if the object is found in the datastore and
	 *         removed, <code>false</code> if the item is not found.
	 */
	protected boolean _deleteEntity(Entity<? extends Serializable> entity) {
		if (entity != null) {
			super.getHibernateTemplate().delete(entity);
			return true;
		}
		return false;
	}

	/**
	 * Remove the specified entities from the datastore.
	 */
	protected void _deleteEntities(Entity<? extends Serializable>... entities) {
		for (Entity<? extends Serializable> entity : entities) {
			if (entity != null)
				super.getHibernateTemplate().delete(entity);
		}
	}

	/**
	 * Return the persistent instance of the given entity class with the given
	 * identifier, or null if there is no such persistent instance.
	 * <code>get()</code> always hits the database immediately.
	 */
	protected <T> T _get(Class<T> type, Serializable id) {
		type = metadataUtil.getUnproxiedClass(type); //Get the real entity class
		return (T) super.getHibernateTemplate().get(type, id);
	}
	
	protected <T> T _get(Class<T> type, Serializable id, LockMode lockMode) {
		type = metadataUtil.getUnproxiedClass(type); //Get the real entity class
		return (T) super.getHibernateTemplate().get(type, id, lockMode);
	}

	/**
	 * <p>
	 * Return the all the persistent instances of the given entity class with
	 * the given identifiers. An array of entities is returned that matches the
	 * same order of the ids listed in the call. For each entity that is not
	 * found in the datastore, a null will be inserted in its place in the
	 * return array.
	 * 
	 * <p>
	 * <code>get()</code> always hits the database immediately.
	 */
	protected <T> T[] _get(Class<T> type, Serializable... ids) {
		type = metadataUtil.getUnproxiedClass(type); //Get the real entity class
		Criteria c = getSession().createCriteria(type);
		c.add(Restrictions.in("id", ids));
		Object[] retVal = (Object[]) Array.newInstance(type, ids.length);

		for (Object entity : c.list()) {
			Serializable id = getMetadataUtil().getId(entity);
			for (int i = 0; i < ids.length; i++) {
				if (id.equals(ids[i])) {
					retVal[i] = entity;
					break;
				}
			}
		}

		return (T[]) retVal;
	}


	/**
	 * Get a list of all the objects of the specified class.
	 */
	protected <T> List<T> _all(Class<T> type) {
		type = metadataUtil.getUnproxiedClass(type); //Get the real entity class
		return getSession().createCriteria(type).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
	}

	/**
	 * Search for objects based on the search parameters in the specified
	 * <code>ISearch</code> object.
	 * 
	 * @see ISearch
	 */
	protected List _search(ISearch search) {
		if (search == null)
			throw new NullPointerException("Search is null.");
		if (search.getSearchClass() == null)
			throw new NullPointerException("Search class is null.");

		return getSearchProcessor().search(getSession(), search);
	}

	/**
	 * Same as <code>_search(ISearch)</code> except that it uses the specified
	 * search class instead of getting it from the search object. Also, if the search
	 * object has a different search class than what is specified, an exception
	 * is thrown.
	 */
	protected List _search(Class<?> searchClass, ISearch search) {
		if (search == null)
			throw new NullPointerException("Search is null.");
		if (searchClass == null)
			throw new NullPointerException("Search class is null.");
		if (search.getSearchClass() != null && !search.getSearchClass().equals(searchClass))
			throw new IllegalArgumentException("Search class does not match expected type: " + searchClass.getName());

		return getSearchProcessor().search(getSession(), searchClass, search);
	}

	/**
	 * Returns the total number of results that would be returned using the
	 * given <code>ISearch</code> if there were no paging or maxResult limits.
	 * 
	 * @see ISearch
	 */
	protected int _count(ISearch search) {
		if (search == null)
			throw new NullPointerException("Search is null.");
		if (search.getSearchClass() == null)
			throw new NullPointerException("Search class is null.");

		return getSearchProcessor().count(getSession(), search);
	}

	/**
	 * Same as <code>_count(ISearch)</code> except that it uses the specified
	 * search class instead of getting it from the search object. Also, if the search
	 * object has a different search class than what is specified, an exception
	 * is thrown.
	 */
	protected int _count(Class<?> searchClass, ISearch search) {
		if (search == null)
			throw new NullPointerException("Search is null.");
		if (searchClass == null)
			throw new NullPointerException("Search class is null.");
		if (search.getSearchClass() != null && !search.getSearchClass().equals(searchClass))
			throw new IllegalArgumentException("Search class does not match expected type: " + searchClass.getName());

		return getSearchProcessor().count(getSession(), searchClass, search);
	}

	/**
	 * Returns the number of instances of this class in the datastore.
	 */
	protected int _count(Class<?> type) {
		List counts = getSession().createQuery("select count(_it_) from " + getMetadataUtil().get(type).getEntityName() + " _it_").list();
		int sum = 0;
		for (Object count : counts) {
			sum += ((Long) count).intValue();
		}
		return sum;
	}

	/**
	 * Returns a <code>SearchResult</code> object that includes the list of
	 * results like <code>search()</code> and the total length like
	 * <code>searchLength</code>.
	 * 
	 * @see ISearch
	 */
	protected SearchResult _searchAndCount(ISearch search) {
		if (search == null)
			throw new NullPointerException("Search is null.");
		if (search.getSearchClass() == null)
			throw new NullPointerException("Search class is null.");

		return getSearchProcessor().searchAndCount(getSession(), search);
	}

	/**
	 * Same as <code>_searchAndCount(ISearch)</code> except that it uses the specified
	 * search class instead of getting it from the search object. Also, if the search
	 * object has a different search class than what is specified, an exception
	 * is thrown.
	 */
	protected SearchResult _searchAndCount(Class<?> searchClass, ISearch search) {
		if (search == null)
			throw new NullPointerException("Search is null.");
		if (searchClass == null)
			throw new NullPointerException("Search class is null.");
		if (search.getSearchClass() != null && !search.getSearchClass().equals(searchClass))
			throw new IllegalArgumentException("Search class does not match expected type: " + searchClass.getName());

		return getSearchProcessor().searchAndCount(getSession(), searchClass, search);
	}

	/**
	 * Search for a single result using the given parameters.
	 */
	protected Object _searchUnique(ISearch search) throws NonUniqueResultException {
		if (search == null)
			throw new NullPointerException("Search is null.");
		if (search.getSearchClass() == null)
			throw new NullPointerException("Search class is null.");

		return getSearchProcessor().searchUnique(getSession(), search);
	}

	/**
	 * Same as <code>_searchUnique(ISearch)</code> except that it uses the specified
	 * search class instead of getting it from the search object. Also, if the search
	 * object has a different search class than what is specified, an exception
	 * is thrown.
	 */
	protected Object _searchUnique(Class<?> searchClass, ISearch search) {
		if (search == null)
			throw new NullPointerException("Search is null.");
		if (searchClass == null)
			throw new NullPointerException("Search class is null.");
		if (search.getSearchClass() != null && !search.getSearchClass().equals(searchClass))
			throw new IllegalArgumentException("Search class does not match expected type: " + searchClass.getName());

		return getSearchProcessor().searchUnique(getSession(), searchClass, search);
	}

	/**
	 * Returns true if the object is connected to the current hibernate session.
	 */
	protected boolean _sessionContains(Entity<? extends Serializable> o) {
		return super.getHibernateTemplate().contains(o);
	}

	/**
	 * Flushes changes in the hibernate cache to the datastore.
	 */
	protected void _flush() {
		super.getHibernateTemplate().flush();
	}

	/**
	 * Refresh the content of the given entity from the current datastore state.
	 */
	protected void _refresh(Entity<? extends Serializable>... entities) {
		for (Entity<? extends Serializable> entity : entities)
			super.getHibernateTemplate().refresh(entity);
	}

	protected boolean _exists(Entity<? extends Serializable> entity) {
		if (super.getHibernateTemplate().contains(entity))
			return true;
		return _exists(entity.getClass(), entity.getId());
	}

	protected boolean _exists(Class<?> type, Serializable id) {
		if (type == null)
			throw new NullPointerException("Type is null.");
		if (id == null)
			return false;
		type = metadataUtil.getUnproxiedClass(type); //Get the real entity class

		Query query = getSession().createQuery("select id from " + getMetadataUtil().get(type).getEntityName() + " where id = :id");
		query.setParameter("id", id);
		return query.list().size() == 1;
	}

	protected boolean[] _exists(Class<?> type, Serializable... ids) {
		if (type == null)
			throw new NullPointerException("Type is null.");
		type = metadataUtil.getUnproxiedClass(type); //Get the real entity class

		boolean[] ret = new boolean[ids.length];

		// we can't use "id in (:ids)" because some databases do not support
		// this for compound ids.
		StringBuilder sb = new StringBuilder("select id from " + getMetadataUtil().get(type).getEntityName() + " where");
		boolean first = true;
		for (int i = 0; i < ids.length; i++) {
			if (first) {
				first = false;
				sb.append(" id = :id");
			} else {
				sb.append(" or id = :id");
			}
			sb.append(i);
		}

		Query query = getSession().createQuery(sb.toString());
		for (int i = 0; i < ids.length; i++) {
			query.setParameter("id" + i, ids[i]);
		}

		for (Serializable id : (List<Serializable>) query.list()) {
			for (int i = 0; i < ids.length; i++) {
				if (id.equals(ids[i])) {
					ret[i] = true;
					// don't break. the same id could be in the list twice.
				}
			}
		}

		return ret;
	}
	
	protected Filter _getFilterFromExample(Object example) {
		return searchProcessor.getFilterFromExample(example);
	}
	
	protected Filter _getFilterFromExample(Object example, ExampleOptions options) {
		return searchProcessor.getFilterFromExample(example, options);
	}
	
}
