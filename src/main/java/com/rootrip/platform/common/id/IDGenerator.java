package com.rootrip.platform.common.id;

import java.io.Serializable;

/**
 * 
 * @author liubin
 *
 */
public interface IDGenerator {

	public Serializable generate(Object obj);
}
