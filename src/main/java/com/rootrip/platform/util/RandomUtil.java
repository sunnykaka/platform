package com.rootrip.platform.util;

import org.apache.commons.lang3.RandomStringUtils;

public class RandomUtil {

	/**
	 * 生成随机字符串
	 * @param count 字符串长度
	 * @return
	 */
	public static String randomString(int count) {
		return RandomStringUtils.randomAlphabetic(count);
	}
	
	/**
	 * 生成随机数字
	 * @param count 数字长度
	 * @return
	 */
	public static String randomNumber(int count) {
		return RandomStringUtils.randomNumeric(count);
	}
}
