package com.rootrip.platform.common.web.session;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * HttpSession实现者,实现SessionProvider接口
 * @author liubin
 * @date 2012-11-14
 *
 */
public class HttpSessionProvider implements SessionProvider {

	@Override
	public Serializable getAttr(HttpServletRequest request, String name) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			return (Serializable) session.getAttribute(name);
		} else {
			return null;
		}
	}

	@Override
	public void setAttr(HttpServletRequest request,
			HttpServletResponse response, String name, Serializable value) {
		HttpSession session = request.getSession();
		session.setAttribute(name, value);
	}

	@Override
	public String getSessionId(HttpServletRequest request,
			HttpServletResponse response) {
		return request.getSession().getId();
	}

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}
	}

	@Override
	public void removeAttr(HttpServletRequest request, String name) {
		HttpSession session = request.getSession(false);
		if(session != null) {
			session.removeAttribute(name);
		}
	}
}
