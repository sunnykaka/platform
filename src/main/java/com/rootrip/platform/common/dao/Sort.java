package com.rootrip.platform.common.dao;

/**
 * 该类描述数据库order by子句中的一段
 * @author liubin
 *
 */
public class Sort {
	
	// 排序字段
	private String field;
	
	// 排序方式
	private String style;
	
	/**
	 * 升序
	 */
	public static final String ASC = "ASC";
	
	/**
	 * 降序
	 */
	public static final String DESC = "DESC";
	
	/**
	 * 私有构造方法防止外部初始化
	 * @param field 参与排序的字段
	 * @param style 排序方式 ASC, DESC
	 */
	private Sort(String field, String style){
		this.field = field;
		this.style = style;
	}
	
	/**
	 * 创建升序对象
	 * @param field 排序字段
	 * @return 排序对象
	 */
	public static Sort asc(String field){
		return new Sort(field, ASC);
	}
	
	/**
	 * 创建降序对象
	 * @param field 排序字段
	 * @return 排序对象
	 */
	public static Sort desc(String field){
		return new Sort(field, DESC);
	}
	
	/**
	 * 本条件是否为升序
	 * @return 是否为升序
	 */
	public boolean isAsc(){
		return ASC.equals(this.style);
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return new StringBuffer(this.field).append(" ").append(this.style).toString();
	}

	/**
	 * @return the field
	 */
	public String getField() {
		return field;
	}

	/**
	 * @param field the field to set
	 */
	public void setField(String field) {
		this.field = field;
	}
}
