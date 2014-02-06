package com.rootrip.platform.common.web.el;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.servlet.jsp.PageContext;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.support.ServletContextResource;

import com.rootrip.platform.Application;
import com.rootrip.platform.Platform;
import com.rootrip.platform.exception.PlatformException;
import com.rootrip.platform.util.Servlets;
import com.rootrip.platform.util.StringUtil;


public class PlatformFunction {
	
	private static Logger log = LoggerFactory.getLogger(PlatformFunction.class);
	
	private static Application app = Platform.getInstance().getApp();
	
	private static ConcurrentMap<String, String> staticFileCache = new ConcurrentHashMap<>();
	
	private static AtomicBoolean loadFileInitialized = new AtomicBoolean(false);
	
	private static final String WRO_Path = "/wro/";
	
	private static final String JS_SCRIPT = "<script type=\"text/javascript\" src=\"%s\"></script>";
	private static final String CSS_SCRIPT = "<link rel=\"stylesheet\" type=\"text/css\" href=\"%s\">";
	
	private static String contextPath = null; 
	
	/**
	 * 该方法根据给出的路径,生成js脚本加载标签
	 * 例如传入参数/wro/custom,该方法会寻找webapp路径下/wro目录中以custom开头,以js后缀结尾的文件名称名称.
	 * 然后拼成<script type="text/javascript" src="${ctxPath}/wro/custom-20130201.js"></script>返回
	 * 如果查找到多个文件,返回根据文件名排序最大的文件
	 * @param str
	 * @return
	 */
	public static String jsFile(String filePath) {
		String jsFile = staticFileCache.get(buildCacheKey(filePath, "js"));
		if(jsFile == null) {
			log.error("加载js文件失败,缓存中找不到对应的文件[{}]", filePath);
		}
		return String.format(JS_SCRIPT, jsFile);
	}
	
	/**
	 * 该方法根据给出的路径,生成css脚本加载标签
	 * 例如传入参数/wro/custom,该方法会寻找webapp路径下/wro目录中以custom开头,以css后缀结尾的文件名称名称.
	 * 然后拼成<link rel="stylesheet" type="text/css" href="${ctxPath}/wro/basic-20130201.css">返回
	 * 如果查找到多个文件,返回根据文件名排序最大的文件
	 * @param str
	 * @return
	 */
	public static String cssFile(String filePath) {
		String cssFile = staticFileCache.get(buildCacheKey(filePath, "css"));
		if(cssFile == null) {
			log.error("加载css文件失败,缓存中找不到对应的文件[{}]", filePath);
		}
		return String.format(CSS_SCRIPT, cssFile);
	}
	
	/**
	 * 替换项目路径
	 * @param str
	 * @return
	 */
	public static String replaceImageUploadPath(String str) {
		if(str == null) return null;
		return decodeHtml(str.replaceAll(app.getRootripAdminContextRoot(), app.getRootripContextRoot()));
	}
	
	
	/**
	 * 将字符串中的换行符格式化成br标签
	 * @param str
	 * @return
	 */
	public static String replaceNR(String str) {
		if(str == null) return null;
		if(str.trim().equals("")) return "";
		str = str.replaceAll("\\r\\n", "<br/>");
		str = str.replaceAll("\\n", "<br/>");
		str = str.replaceAll("\\r", "<br/>");
		return str;
	}
	
	/**
	 * 将字符串中的转义字符输出为正常的html实体
	 * @param str
	 * @return
	 */
	public static String decodeHtml(String str) {
		if(str == null) return null;
		if(str.trim().equals("")) return "";
		return str.replaceAll("&amp;", "&")
			     .replaceAll("&lt;", "<")
			     .replaceAll("&gt;", ">")
			     .replaceAll("&apos;", "\'")
			     .replaceAll("&quot;", "\"")
			     .replaceAll("&nbsp;", " ")
			     .replaceAll("&copy;", "@")
			     .replaceAll("&reg;", "?");
		
	}
	
	/**
	 * 如果字符串的长度超过length个字符,后面的字符显示成省略号
	 * @param str
	 * @return
	 */
	public static String ellipsis(String str, Integer length) {
		return StringUtil.abbreviate(str, length, "...");
	}
	
	/**
	 * 判断数组中是否有某对象
	 * @param arr
	 * @param o
	 * @return
	 */
	public static boolean contains(Object[] arr, Object o) {
		if(arr == null || arr.length == 0) return false;
		if(o == null) return false;
		for(Object objInArr : arr) {
			if(o.equals(objInArr)) return true;
			if(objInArr instanceof String) {
				if(o.toString().equals(objInArr)) return true;
			}
		}
		return false;
	}
	
	/**
	 * 判断数组中是否有某对象
	 * @param arr
	 * @param o
	 * @return
	 */
	public static boolean colContains(Collection<?> c, Object o) {
		if(c == null || c.isEmpty()) return false;
		if(o == null) return false;
		return c.contains(o);
	}
	
	public static void initLoadFile() throws IOException {
		if(loadFileInitialized.compareAndSet(false, true)) {
			ServletContext sc = Platform.getInstance().getServletContext();
			if(sc == null) {
				log.error("查找静态资源的时候的时候发现servlet context 为null");
				return;
			}
			contextPath = Platform.getInstance().getContextPath();
			File wroDirectory = new ServletContextResource(sc, WRO_Path).getFile();
			if(!wroDirectory.exists() || !wroDirectory.isDirectory()) {
				throw new PlatformException("查找静态资源的时候发现对应目录不存在[" + wroDirectory.getAbsolutePath() + "]");
			}
			//将wro目录下已有文件加入缓存
			for(File file : wroDirectory.listFiles()) {
				handleNewFile(file);
			}
			//监控wro目录,如果有文件生成,则判断是否是较新的文件,是的话则把文件名加入缓存
			new Thread(new WroFileWatcher(wroDirectory.getAbsolutePath())).start();
		}
	}

	private static void handleNewFile(File file) {
		String fileName = file.getName();
		Pattern p = Pattern.compile("^(\\w+)\\-\\d+\\.(js|css)$");
		Matcher m = p.matcher(fileName);
		if(!m.find() || m.groupCount() < 2) return;
		String fakeName = m.group(1);
		String fileType = m.group(2);
		//暂时限定只能匹配/wro/目录下的文件
		String key = buildCacheKey(WRO_Path + fakeName, fileType);
		String newCacheFileName = buildCacheFileName(fileName);
		if(staticFileCache.putIfAbsent(key, newCacheFileName) != null) {
			synchronized(staticFileCache) {
				String cachedFileName = staticFileCache.get(key);
				if(newCacheFileName.compareTo(cachedFileName) > 0) {
					staticFileCache.put(key, newCacheFileName);
				}
			}
		}
	}
	
	private static String buildCacheKey(String fakeName, String fileType) {
		return fakeName + "-" + fileType;
	}
	
	private static String buildCacheFileName(String fileName) {
		return contextPath + WRO_Path + fileName;
	}
	
	/**
	 * 得到bindResult对象在request attribute中的key
	 * @param modelAttribute
	 * @return
	 */
	public static String getBindResultKey(String modelAttribute) {
		
		return  new StringBuilder().append(BindingResult.class.getName()).append(".").append(modelAttribute).toString();
	}
	
	/**
	 * 为表单域中添加sParam_开头的参数隐藏域
	 * @param request
	 * @return
	 */
	public static String params(PageContext pageContext) {
		String pageIndex = pageContext.getRequest().getParameter("pageIndex");
		String pageSize = pageContext.getRequest().getParameter("pageSize");
		StringBuilder html = new StringBuilder();
		if(NumberUtils.isNumber(pageIndex)) {
			html.append(String.format("<input type=\"hidden\" name=\"%s\" value=\"%s\" />", "pageIndex", pageIndex));
		}
		if(NumberUtils.isNumber(pageSize)) {
			html.append(String.format("<input type=\"hidden\" name=\"%s\" value=\"%s\" />", "pageSize", pageSize));
		}
		Map<String, String[]> paramsMap = Servlets.getParametersStartingWith(pageContext.getRequest(), "sParam_");
		for(Map.Entry<String, String[]> entry : paramsMap.entrySet()) {
			String key = entry.getKey();
			String[] values = entry.getValue();
			if(values == null) continue;
			for(int i = 0; i < values.length; i++) {
				html.append(String.format("<input type=\"hidden\" name=\"sParam_%s\" value=\"%s\" />", key, values[i]));
			}
		}
		return html.toString();
	}
	
	/**
	 * 得到配置值
	 * @param key
	 * @return
	 */
	public static String getConfig(String key) {
		return app.getConfigValue(key);
	}
	
	static class WroFileWatcher implements Runnable {
		
		private static Logger log = LoggerFactory.getLogger(WroFileWatcher.class);
		
		private String wroAbsolutePathStr;
		
		public WroFileWatcher(String wroPathStr) {
			this.wroAbsolutePathStr = wroPathStr;
		}

		@Override
		public void run() {
			Path path = Paths.get(wroAbsolutePathStr);
			File wroDirectory = path.toFile();
			if(!wroDirectory.exists() || !wroDirectory.isDirectory()) {
				String message = "监控wro目录的时候发现对应目录不存在[" + wroAbsolutePathStr + "]";
				log.error(message);
				throw new PlatformException(message);
			}
			log.warn("开始监控wro目录[{}]", wroAbsolutePathStr);
			try {
				WatchService watcher = FileSystems.getDefault().newWatchService();
				path.register(watcher, StandardWatchEventKinds.ENTRY_CREATE);
				
				while (true) {
					WatchKey key = null;
					try {
						key = watcher.take();
					} catch (InterruptedException e) {
						log.error("", e);
						continue;
					}
					for (WatchEvent<?> event : key.pollEvents()) {
						if (event.kind() == StandardWatchEventKinds.OVERFLOW) {
							continue;
						}
						WatchEvent<Path> e = (WatchEvent<Path>) event;
						Path filePath = e.context();
						handleNewFile(filePath.toFile());
					}
					if (!key.reset()) {
						break;
					}
				}
			} catch (IOException e) {
				log.error("监控wro目录发生错误", e);
			}
			log.warn("停止监控wro目录[{}]", wroAbsolutePathStr);
		}
	}
}
