package com.rootrip.platform.util;

import org.apache.commons.lang3.StringUtils;

/**
 * 匹配菜单的baseurl
 * @author liubin
 *
 */
public class UrlMatchUtil {
	
	
	
	public static String getBaseUrlFromRequestUrl(String requestUrl){
		if(StringUtils.isBlank(requestUrl)){
			return null;
		}
		String baseUrl = null;
		String[] arr = requestUrl.split("/");
		
		if(arr != null && arr.length > 2 && !arr[2].equals("ajax")){
			baseUrl = arr[1];
		}
		return baseUrl;
	}
	
}
