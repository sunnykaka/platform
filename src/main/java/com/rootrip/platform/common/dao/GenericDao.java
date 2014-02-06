package com.rootrip.platform.common.dao;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author liubin
 *
 * @param <E>
 */
@SuppressWarnings("unchecked")
public interface GenericDao<E extends Entity> {
	
	/**
	 * 保存一个实体
	 * @param entity 实体对象
	 */
	public void save(E entity);
	
	/**
	 * 删除一个实体
	 * @param id 主键
	 */
	public void delete(Serializable id);
	
	/**
	 * 删除一个实体
	 * @param entity 实体对象
	 */
	public void delete(E entity);
	
	/**
	 * 批量删除实体
	 * @param entitiesId 实体编号数组
	 */
	public void deleteBatchByEntitiesId(Object[] entitiesId);
	
	/**
	 * 批量删除实体
	 * @param entities
	 */
	public void deleteBatchByEntities(List<E> entities);
	
	/**
	 * 查询所有结果
	 * @return 查询结果
	 */
	public List findAll();
	
	/**
	 * 查询所有
	 * @param page 分页对象
	 * @param sorts 排序对象
	 * @return 查询结果
	 */
	public List findAll(Page page, Sort... sorts);
	
	/**
	 * 按页号和每页记录数查询所有
	 * @param pageIndex 页号
	 * @param pageSize 每页记录数
	 * @param sorts 排序
	 * @return 查询结果
	 */
	public List findAll(Integer pageSize, Integer pageIndex, Sort... sorts);
	
	/**
	 * 通过主键查询
	 * @param id 主键
	 * @return 实体对象
	 */
	public E get(Serializable id);
	
	/**
	 * 按页号和每页记录数查询结果
	 * @param ql 查询语句
	 * @param pageIndex 页号
	 * @param pageSize 每页记录数
	 * @param args 查询语句参数
	 * @return 查询结果
	 */
	public List query(String ql, Integer pageSize, Integer pageIndex, Object ... args);
	
	/**
	 * 通过查询语句查询
	 * @param ql 查询语句
	 * @param page 分页对象
	 * @param args 参数
	 * @return 查询结果
	 */
	public List query(String ql, Page page, Object... args);
	
	/**
	 * 通过查询语句查询，无分页操作
	 * @param ql 查询语句
	 * @param args 参数
	 * @return 查询结果
	 */
	public List query(String ql, Object... args);
	
	/**
	 * 通过查询语句查询,指定锁定策略
	 * @param ql
	 * @param lockOptions
	 * @param page
	 * @param args
	 * @return
	 */
	public List queryForLock(String ql, Page page, LockOptions lockOptions, Object... args);
	
	/**
	 * 通过查询语句查询,无分页操作,指定锁定策略
	 * @param ql
	 * @param lockOptions
	 * @param args
	 * @return
	 */
	public List queryForLock(String ql, LockOptions lockOptions, Object... args);
	
	/**
	 * 通过查询语句查询,使用查询缓存
	 * @param ql 查询语句
	 * @param page 分页对象
	 * @param args 参数
	 * @return 查询结果
	 */
	public List queryWithQueryCache(String ql, Page page, Object... args);
	
	/**
	 * 通过查询语句查询，无分页操作,使用查询缓存
	 * @param ql 查询语句
	 * @param args 参数
	 * @return 查询结果
	 */
	public List queryWithQueryCache(String ql, Object... args);
	
	/**
	 * 执行更新
	 * @param ql 执行更新
	 * @param args ql参数
	 * @return 更新所影响的行数
	 */
	public int update(String ql, Object... args);
	
	/**
	 * 计算记录数
	 * @param ql 查询语句
	 * @param args 查询条件参数
	 * @return 符合条件的记录数
	 */
	public int count(String ql, Object... args);
	
	/**
	 * 重新从数据库读取状态
	 * @param entity
	 * @param lockOptions
	 */
	public void refresh(Entity entity, LockOptions lockOptions);
	
	/**
	 * 重新从数据库读取状态
	 * @param entity
	 */
	public void refresh(Entity entity);

}
