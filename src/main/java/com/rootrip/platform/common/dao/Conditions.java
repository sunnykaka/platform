package com.rootrip.platform.common.dao;

import java.util.HashMap;
import java.util.Map;

public class Conditions {
	
	Map<String, Object> map = new HashMap<String, Object>();
	
	private Page page;
	
	private Conditions() {}
	
	public Conditions put(String key, Object value) {
		map.put(key, value);
		return this;
	}
	
	public static Conditions create(String key, Object value) {
		return new Conditions().put(key, value);
	}
	
	public static Conditions create(Page page) {
		return new Conditions().setPage(page);
	}
	
	public boolean isEmpty() {
		return map.isEmpty();
	}

	public boolean containsKey(String key) {
		return map.containsKey(key);
	}

	public Object get(String key) {
		return map.get(key);
	}

	public Page getPage() {
		return page;
	}

	public Conditions setPage(Page page) {
		this.page = page;
		return this;
	}
}
