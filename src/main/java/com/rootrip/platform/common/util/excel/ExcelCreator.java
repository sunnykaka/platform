package com.rootrip.platform.common.util.excel;

import java.io.OutputStream;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author liubin
 *
 */
public class ExcelCreator {

	// 日志
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	// Excel
	private WritableWorkbook workbook;
	
	/**
	 * 构造方法
	 * @param os excel的输出流
	 */
	public ExcelCreator(OutputStream os) {
		try {
			workbook = Workbook.createWorkbook(os);
		} catch(Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
	}
	
	/**
	 * 创建sheet
	 * @param name 名称
	 * @param index 索引值
	 */
	public void createSheet(String name, int index) {
		workbook.createSheet(name, index);
	}
	
	/**
	 * 创建一个单元格
	 * @param sheetIndex sheet索引
	 * @param x x坐标
	 * @param y y坐标
	 * @param value 值
	 */
	public void addCell(int sheetIndex, int x, int y, String value) {
		try {
			workbook.getSheet(sheetIndex).addCell(new Label(y, x, value));
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
	}
	
	/**
	 * 创建一行
	 * @param sheetIndex sheet坐标
	 * @param x x坐标
	 * @param y y坐标
	 * @param rowData 行数据
	 */
	public void addRow(int sheetIndex, int x, int y, String[] rowData) {
		try {
			WritableSheet sheet = workbook.getSheet(sheetIndex);
			for(int i = 0; i < rowData.length; i++) {
				sheet.addCell(new Label(y + i, x, rowData[i]));
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
	}
	
	public void write() {
		try {
			workbook.write();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	/**
	 * 关闭workbook,执行写入文件操作
	 *
	 */
	public void close() {
		if(workbook != null) {
			try {
				workbook.close();
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
	}
}
