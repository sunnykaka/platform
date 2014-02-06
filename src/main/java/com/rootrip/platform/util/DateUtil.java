package com.rootrip.platform.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateUtil {
	
	protected static final Logger log = LoggerFactory.getLogger(DateUtil.class); 
	
	private static final long DAY_IN_MILLISECOND = 1000 * 3600 * 24;
	private static final long HOUR_IN_MILLISECOND = 1000 * 3600;
	private static final long MINUTE_IN_MILLISECOND = 1000 * 60;
	private static final long SECOND_IN_MILLISECOND = 1000;

	public static String formatDate(Date date, String format) {
		if(date == null) return null;
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
	
	public static Date parseDate(String str, String format) {
		if(str == null) return null;
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			return sdf.parse(str);
		} catch (ParseException e) {
			log.error("将日期字符串转化为日期类型时发生错误,str[{}],format[{}]", str, format);
			log.error("", e);
			return null;
		}
	}
	
	/**
	 * 在指定的日期增加或减少天数
	 * 
	 * @param date
	 * @param days
	 * @return
	 */
	public static Date addDays(Date date, int days) {
		long now = date.getTime() + days * DAY_IN_MILLISECOND;
		return (new java.util.Date(now));
	}
	
	/**
	 * 在指定的日期增加或减少小时
	 * 
	 * @param date
	 * @param days
	 * @return
	 */
	public static Date addHours(Date date, int hour) {
		long now = date.getTime() + hour * HOUR_IN_MILLISECOND ;
		return (new java.util.Date(now));
	}

	/**
	 * 在指定的日期增加或减少分钟
	 * 
	 * @param date
	 * @param days
	 * @return
	 */
	public static Date addMinutes(Date date, int minute) {
		long now = date.getTime() + minute * MINUTE_IN_MILLISECOND ;
		return (new java.util.Date(now));
	}
	
	/**
	 * 在指定的日期增加或减少秒
	 * 
	 * @param date
	 * @param days
	 * @return
	 */
	public static Date addSeconds(Date date, int second) {
		long now = date.getTime() + second * SECOND_IN_MILLISECOND ;
		return (new java.util.Date(now));
	}
	
	/**
	 * 在指定的日期增加或减少月数
	 * 
	 * @param date
	 * @param months
	 * @return
	 */
	public static Date addMonths(Date date, int months) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		int m = cal.get(Calendar.MONTH) + months;
		if (m < 0) {
			m += -12;
		}
		cal.roll(Calendar.YEAR, m / 12);
		cal.roll(Calendar.MONTH, months);
		return cal.getTime();
	}
	
	/**
	 * 在指定的日期增加或减少年数
	 * @param date
	 * @param months
	 * @return
	 */
	public static Date addYears(Date date, int years) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.roll(Calendar.YEAR, years);
		return cal.getTime();
	}
	
	/**
	 * 得到日期相差天数
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static int getDifferDays(Date startDate, Date endDate) {
		if(startDate == null || endDate == null) return 0;
		return Days.daysBetween(new DateTime(startDate), new DateTime(endDate)).getDays();
	}
}
