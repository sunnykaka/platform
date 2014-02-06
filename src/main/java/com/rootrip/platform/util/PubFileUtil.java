package com.rootrip.platform.util;

import java.net.URL;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.web.multipart.MultipartFile;

public class PubFileUtil {
	
	/**
	 * 判断上传文件的是否是常用图片后缀
	 * @param file
	 * @return
	 */
	public static  boolean isImageSuffix(MultipartFile file) {
		if(file != null && !file.isEmpty()) {
			//获取文件的后缀
			String suffix = getSuffix(file);
			if(".jpg".equalsIgnoreCase(suffix) || ".png".equalsIgnoreCase(suffix) || ".jpeg".equalsIgnoreCase(suffix) || ".gif".equalsIgnoreCase(suffix)) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * 获取文件的后缀
	 * @param file
	 * @return
	 */
	public static String getSuffix(MultipartFile file) {
		if(file == null || file.isEmpty()) {
			return null;
		}
		String fileName = file.getOriginalFilename();
		int index = fileName.lastIndexOf(".");
		if(index == -1) {
			return null;
		}
		return fileName.substring(index);
	}
	
	/**
	 * 获取url路径对应的文件后缀
	 * @param file
	 * @return
	 */
	public static String getSuffix(String url) {
		if(url == null || url.isEmpty()) {
			return null;
		}
		int index = url.lastIndexOf(".");
		if(index == -1) {
			return null;
		}
		return url.substring(index);
	}
	
	public static String generateRandomFileName() {
		return new StringBuilder().append(System.currentTimeMillis()).append(RandomStringUtils.randomAlphabetic(6)).toString();
	}

	/**
	 * 判断上传文件的是不是允许的文件名后缀
	 * @param file
	 * @return
	 */
	public static boolean isPermitFileSuffix(MultipartFile file) {
		if(isImageSuffix(file)) {
			return true;
		}
		if(file == null || file.isEmpty()) {
			return false;
		}
		//获取文件的后缀
		String suffix = getSuffix(file);
		if(".exe".equalsIgnoreCase(suffix) || ".cmd".equalsIgnoreCase(suffix) || ".reg".equalsIgnoreCase(suffix) || ".dll".equalsIgnoreCase(suffix)) {
			return false;
		}
		
		return true;
	}

}
