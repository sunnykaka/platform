package com.rootrip.platform.id;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.util.ClassUtils;

import com.rootrip.platform.exception.PlatformException;

public class IdGenerator {
	
	//TODO 放入memcached缓存
	private final ConcurrentHashMap<String, IdCreator> map = new ConcurrentHashMap<String, IdCreator>();
	
	private IdCreatorDao idCreatorDao;
	
	/**
	 * 得到下一个主键id
	 * @param clazz 对应实体类,为了兼容hibernate的继承映射,如果clazz有父类(父类不为object),则使用父类的表生成主键
	 * @return
	 * @throws PlatformException
	 */
	public long nextId(Class<?> clazz) throws PlatformException {
		if(clazz == null) {
			throw new NullPointerException("clazz for next id can not be null");
		}
		clazz = ClassUtils.getUserClass(clazz);
		while(!clazz.getSuperclass().equals(Object.class)) {
			clazz = clazz.getSuperclass();
		}
		
		IdCreator creator = getCreator(clazz.getSimpleName());
		return creator.getNext();
	}
	
	/**
	 * 得到对应数量的主键id
	 * @param clazz 对应实体类,为了兼容hibernate的继承映射,如果clazz有父类(父类不为object),则使用父类的表生成主键
	 * @param count 主键数量
	 * @return
	 * @throws PlatformException
	 */
	public List<Range> nextIds(Class<?> clazz, int count) throws PlatformException {
		if(clazz == null) {
			throw new NullPointerException("clazz for next id can not be null");
		}
		clazz = ClassUtils.getUserClass(clazz);
		while(!clazz.getSuperclass().equals(Object.class)) {
			clazz = clazz.getSuperclass();
		}
		
		IdCreator creator = getCreator(clazz.getSimpleName());
		return creator.getIdSet(count);
	}
	
	private IdCreator getCreator(String tableName) {
		IdCreator creator = map.get(tableName);
		if (creator == null) {
			creator = new IdCreator(tableName, idCreatorDao);
			IdCreator previous = map.putIfAbsent(tableName, creator);
			if (previous != null)
				creator = previous;
		}
		return creator;
	}

	public void setIdCreatorDao(IdCreatorDao idCreatorDao) {
		this.idCreatorDao = idCreatorDao;
	}

	/**
	 * 得到下一个线路产品编号
	 * @return
	 */
	public long nextPdtId() {
		IdCreator creator = getCreator("pdt_id");
		return creator.getNext();
	}
	
	/**
	 * 得到下一个订单编号
	 * @return
	 */
	public long nextOrderNo() {
		IdCreator creator = getCreator("order_base_no");
		return creator.getNext();
	}

}
