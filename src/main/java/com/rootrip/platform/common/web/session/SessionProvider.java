package com.rootrip.platform.common.web.session;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Session提供者
 * @author liubin
 * @date 2012-11-14
 *
 */
public interface SessionProvider {
	public Serializable getAttr(HttpServletRequest request, String name);

	public void setAttr(HttpServletRequest request,
			HttpServletResponse response, String name, Serializable value);

	public String getSessionId(HttpServletRequest request,
			HttpServletResponse response);

	public void logout(HttpServletRequest request, HttpServletResponse response);
	
	public void removeAttr(HttpServletRequest request, String name);
}
