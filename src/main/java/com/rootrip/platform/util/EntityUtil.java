package com.rootrip.platform.util;

import java.io.Serializable;

import org.apache.commons.lang3.Validate;

import com.rootrip.platform.common.dao.Entity;

public class EntityUtil {

	public static boolean isCreate(Entity<? extends Serializable> entity) {
		Validate.notNull(entity);
		Serializable id = entity.getId();
		if (id == null) return true;
		if(id instanceof Long) {
			if(((Long)id).equals(0L)) return true;
		}
		return false;
	}

	public static boolean isEmpty(Entity<? extends Serializable> entity) {
		if(entity == null || entity.getId() == null) return true;
		return false;
	}
	
}
