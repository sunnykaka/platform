package com.rootrip.platform.common.dao;

/**
 * 
 * @author liubin
 *
 * @param <IDClass>
 */
public interface Entity<IDClass extends java.io.Serializable> {

	public IDClass getId();
	
	public void setId(IDClass id);
}
