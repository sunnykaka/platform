package com.rootrip.platform.util;

import java.security.MessageDigest;

/**
 * MD5加密
 * @author liubin
 * @date 2012-11-14
 *
 */
public class MD5Encoder {

private static final char[] HEX = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
	
	public static final String encode(String source){
		try {
            byte[] sourceBytes = source.getBytes();
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(sourceBytes);
            byte[] md = mdInst.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                str[k++] = HEX[md[i] >>> 4 & 0xf];
                str[k++] = HEX[md[i] & 0xf];
            }
            return new String(str);
        }
        catch (Exception e) {
            return null;
        }
	}
}