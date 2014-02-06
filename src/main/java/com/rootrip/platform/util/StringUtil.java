package com.rootrip.platform.util;

import java.io.UnsupportedEncodingException;
import java.math.RoundingMode;
import java.net.URLDecoder;
import java.text.NumberFormat;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

public class StringUtil {
	
	private static final Logger log = LoggerFactory.getLogger(StringUtil.class);
	
	private static final String ZERO_16 = "0000000000000000";

	/**
	 * 将str左侧补0到指定长度
	 * @param str
	 * @param length
	 * @return
	 */
	public static String fillZero(String str, int length) {
		//String.format("%0" + length + "d", str);
		int fillLength = length;
		if(str != null) {
			fillLength = length - str.length();
		}
		int segment = fillLength / ZERO_16.length();
		int subLength = fillLength % ZERO_16.length();
		StringBuilder sb = new StringBuilder();
		if(segment > 0) {
			for (int i = 0; i < segment; i++) {
				sb.append(ZERO_16);
			}
		}
		if(subLength > 0) {
			sb.append(ZERO_16.substring(0, subLength));
		}
		sb.append(str);
		return sb.toString();
	}
	
	/**
	 * 根据传入格式格式化数据
	 * 
	 * @param digit
	 *            小数点位数
	 * @param value
	 *            要格式化的数据
	 * @return
	 */
	public static String formatNumber(int digit, Object value) {
		if (value == null) {
			return null;
		}
		NumberFormat format = NumberFormat.getInstance();
		format.setMaximumFractionDigits(digit);
		format.setMinimumFractionDigits(digit);
		format.setGroupingUsed(false);
		format.setRoundingMode(RoundingMode.DOWN);

		Double temp = Double.valueOf(value.toString());
		return format.format(temp);
	}
	
	/**
	 * 解析url,返回里面的参数
	 * @param url
	 * @return
	 */
	public static Map<String, String> analysisUrl(String url) {
		Map<String, String> params = Maps.newHashMap();
		if(!StringUtils.isBlank(url)) {
			if(url.indexOf('?') != -1) {
				url = url.substring(url.indexOf('?') + 1);
			}
            String paramaters[] = url.split("&");
            for (String param : paramaters) {
                String values[] = param.split("=");
                try {
					params.put(values[0], URLDecoder.decode(values[1], "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					log.error("", e);
				}
            }
		}
		return params;
	}
	
	/** 
	 * 截取一段字符的长度(汉、日、韩文字符长度为2),不区分中英文,如果数字不正好，则少取一个字符位 
	 *  
	 * @param str 原始字符串 
	 * @param specialCharsLength 截取长度(汉、日、韩文字符长度为2) 
	 * @param more 超出部分的替代字符，如：...
	 * @return 
	 */  
	public static String abbreviate(String str, int specialCharsLength, String more) {  
	    if (str == null || "".equals(str) || specialCharsLength < 1) {  
	        return "";  
	    }  
	    char[] chars = str.toCharArray();  
	    int charsLength = getCharsLength(chars, specialCharsLength);  
	 
	    if(charsLength < str.length())
	        return new String(chars, 0, charsLength) + more;  
	    else
	        return str;
	}  
	 
	/** 
	 * 获取一段字符的长度，输入长度中汉、日、韩文字符长度为2，输出长度中所有字符均长度为1 
	 * @param chars 一段字符 
	 * @param specialCharsLength 输入长度，汉、日、韩文字符长度为2 
	 * @return 输出长度，所有字符均长度为1 
	 */  
	public static int getCharsLength(char[] chars, int specialCharsLength) {  
	    int count = 0;  
	    int normalCharsLength = 0;  
	    for (int i = 0; i < chars.length; i++) {  
	        int specialCharLength = getSpecialCharLength(chars[i]);  
	        if (count <= specialCharsLength - specialCharLength) {  
	            count += specialCharLength;  
	            normalCharsLength++;  
	        } else {  
	            break;  
	        }  
	    }  
	    return normalCharsLength;  
	}  
	 
	/** 
	 * 获取字符长度：汉、日、韩文字符长度为2，ASCII码等字符长度为1 
	 * @param c 字符 
	 * @return 字符长度 
	 */  
	public static int getSpecialCharLength(char c) {  
	    if (isLetter(c)) {  
	        return 1;  
	    } else {  
	        return 2;  
	    }  
	}  
	 
	/** 
	 * 判断一个字符是Ascill字符还是其它字符（如汉，日，韩文字符） 
	 *  
	 * @param char c, 需要判断的字符 
	 * @return boolean, 返回true,Ascill字符 
	 */  
	public static boolean isLetter(char c) {  
	    return c / 0x80 == 0;  
	}
	
}
