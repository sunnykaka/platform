package com.rootrip.platform.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.ParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @company suteam
 * @author liubin
 * @since Feb 24, 2011
 * @version 1.0
 */
public class MathUtil {

	private static Logger logger = LoggerFactory.getLogger(MathUtil.class);
	
	private static final int DEF_DIV_SCALE = 7;

	// 私有化构造函数使之不能实例化该类
	private MathUtil() {
	}

	/**
	 * 计算增长率 公式：增长率=(value1-value2)/value2*100
	 * 
	 * @param value1
	 * @param value2
	 * @return 增长率
	 */
	public static Double getRiseRate(Double value1, Double value2) {
		Double rate = null;
		double result = 0d;
		try {
			if (value1 == null || value2 == null) {
				return rate;
			}
			if (Double.parseDouble(value2.toString()) == 0.0) {
				return rate;
			}
			result = (Double.parseDouble(value1.toString()) - Double
					.parseDouble(value2.toString()))
					/ Double.parseDouble(value2.toString()) * 100;
			if (Double.isNaN(result) || Double.isInfinite(result)) {
				return null;
			}
			return new Double(result);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	/**
	 * 计算增长率 公式：增长率=(value1-value2)/value2*100
	 * 
	 * @param value1
	 * @param value2
	 * @return 增长率
	 */
	public static Double getRiseRate(Integer value1, Integer value2) {
		Double rate = null;
		double result = 0d;
		try {
			if (value1 == null || value2 == null) {
				return rate;
			}
			if (Double.parseDouble(value2.toString()) == 0.0) {
				return rate;
			}
			result = (Double.parseDouble(value1.toString()) - Double
					.parseDouble(value2.toString()))
					/ Double.parseDouble(value2.toString()) * 100;
			if (Double.isNaN(result) || Double.isInfinite(result)) {
				return null;
			}
			return new Double(result);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
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

		Double temp = Double.valueOf(value.toString());
		return format.format(temp);
	}

	/**
	 * 将传入字符串根据格式转化为数字
	 * 
	 * @param digit
	 *            小数点位数
	 * @param value
	 *            要格式化的数据
	 * @return
	 */
	public static Double parseNumber(int digit, Object value) {
		if (value == null) {
			return null;
		}
		NumberFormat format = NumberFormat.getInstance();
		format.setMaximumFractionDigits(digit);
		format.setMinimumFractionDigits(digit);
		format.setGroupingUsed(false);

		try {
			return format.parse(value.toString()).doubleValue();
		} catch (ParseException e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	public static void main(String[] args) {
		Double value1 = 258.364485;
		System.out.println(MathUtil.formatNumber(2, value1));

		Integer value2 = 258;
		System.out.println(MathUtil.formatNumber(2, value2));

		Long value3 = 258l;
		System.out.println(MathUtil.formatNumber(2, value3));

		Object value4 = "2565";
		System.out.println(MathUtil.formatNumber(2, value4));
	}


	/**
	 * 提供精确的加法运算。
	 * 
	 * @param args
	 *            进行加法运算的参数
	 * @return 
	 * 		  计算得到的和,double类型
	 */
	public static double add(Number... args) {
		if(args.length < 2) {
			throw new IllegalArgumentException("加法运算的参数个数必须是2个以上.");
		}
		BigDecimal start = new BigDecimal(args[0] == null ? "0" : args[0].toString());
		for (int i = 1; i < args.length; i++) {
			start = start.add(new BigDecimal(args[i] == null ? "0" : args[i].toString()));
		}
		return start.doubleValue();
	}

	/**
	 * 提供精确的减法运算。
	 * 
	 * @param args
	 *            进行减法运算的参数
	 * @return 
	 * 		  计算得到的差,double类型
	 */
	public static double sub(Number... args) {
		if(args.length < 2) {
			throw new IllegalArgumentException("减法运算的参数个数必须是2个以上.");
		}
		BigDecimal start = new BigDecimal(args[0] == null ? "0" : args[0].toString());
		for (int i = 1; i < args.length; i++) {
			start = start.subtract(new BigDecimal(args[i] == null ? "0" : args[i].toString()));
		}
		return start.doubleValue();
	}

	/**
	 * 提供精确的乘法运算。
	 * 
	 * @param args
	 *            进行乘法运算的参数
	 * @return 
	 * 		  计算得到的乘积,double类型
	 */
	public static double mul(Number... args) {
		if(args.length < 2) {
			throw new IllegalArgumentException("乘法运算的参数个数必须是2个以上.");
		}
		BigDecimal start = new BigDecimal(args[0].toString());
		for (int i = 1; i < args.length; i++) {
			start = start.multiply(new BigDecimal(args[i] == null ? "0" : args[i].toString()));
		}
		return start.doubleValue();
	}

	/**
	 * 提供（相对）精确的除法运算，当发生除不尽的情况时， 精确到小数点以后10位，以后的数字四舍五入。
	 * 
	 * @param v1
	 *            被除数
	 * @param v2
	 *            除数
	 * @param scale
	 *            表示表示需要精确到小数点以后几位。
	 * @return 
	 * 		  计算得到的商,double类型
	 */
	public static double div(Number v1, Number v2) {
		return div(v1, v2, DEF_DIV_SCALE);
	}

	/**
	 * 提供（相对）精确的除法运算。 当发生除不尽的情况时，由scale参数指定精度，以后的数字四舍五入。
	 * 
	 * @param v1
	 *            被除数
	 * @param v2
	 *            除数
	 * @param scale
	 *            表示表示需要精确到小数点以后几位。
	 * @return 
	 * 		  计算得到的商,double类型
	 */
	public static double div(Number v1, Number v2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		return new BigDecimal(v1 == null ? "0" : v1.toString()).divide(new BigDecimal(v2 == null ? "1" : v2.toString()), 
				scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	/**
	 * 四舍五入
	 * @param num
	 * @param scale
	 * @return
	 */
	public static Double round(Double num, int scale) {
		if(num == null) return null;
		BigDecimal b = new BigDecimal(num);
		return b.setScale(2, RoundingMode.HALF_UP).doubleValue();
	}
}
