package com.rootrip.platform.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class ValidateUtil {
	
	public static final String EMAIL_PATTERN = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

	/**
	 * 判断传来的数字对象是否为null或0
	 * @param n
	 * @return
	 */
	public static boolean isNullOrZero(Number n) {
		if(n == null) return true;
		return n.doubleValue() == 0d;
	}
	
	/**
	 * 判断字符串的长度是否在指定范围
	 * @param str
	 * @param min
	 * @param max
	 * @return
	 */
	public static boolean isLengthBetween(String str, Integer min, Integer max) {
		if(str == null) return false;
		int size = str.length();
		if(min != null) {
			if(size < min) return false;
		}
		if(max != null) {
			if(size > max) return false;
		}
		return true;
	}
	
	/**
	 * 是否符合邮箱格式
	 * @param str
	 * @return
	 */
	public static boolean isEmail(String str) {
		if (StringUtils.isBlank(str))
			return false;
		Pattern regex = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = regex.matcher(str);
		return matcher.matches();
	}
	
}
