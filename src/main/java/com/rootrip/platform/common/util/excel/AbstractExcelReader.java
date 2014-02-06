package com.rootrip.platform.common.util.excel;

import java.io.InputStream;

import jxl.Sheet;
import jxl.Workbook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author liubin
 *
 */
public abstract class AbstractExcelReader {
	
	// 日志
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * 每处理完一行后具体逻辑实现方法
	 * @param rowData 行数据
	 * @param rowIndex 行号
	 */
	public abstract void afterRow(String[] rowData, int rowIndex);
	
	/**
	 * 读取内容
	 * @param in 输入流
	 * @param sheetIndex sheet的索引号
	 * @param startRow 开始行
	 * @param startCell 开始列
	 * @param cellCount 列数
	 */
	public void read(InputStream in, int sheetIndex, int startRow, int startCell, int cellCount) {
		Workbook workbook = null;
		try {
			workbook = Workbook.getWorkbook(in);
			Sheet sheet = workbook.getSheet(sheetIndex);
			if(cellCount <= 0) {
				cellCount = sheet.getColumns();
			}
			for(int i = startRow; i < sheet.getRows(); i++) {
				String[] rowData = new String[cellCount];
				for(int j = startCell; j < startCell + cellCount; j++) {
					int index = j - startCell;
					rowData[index] = sheet.getCell(j, i).getContents();
				}
				afterRow(rowData, i);
			}
		} catch(Exception ex) {
			logger.error(ex.getMessage(), ex);
		} finally {
			try {
				if(workbook != null) {
					workbook.close();
				}
			} catch (Exception ex) {
				logger.error(ex.getMessage(), ex);
			}
		}
	}
	
	/**
	 * 读取内容
	 * @param in 输入流
	 * @param sheetIndex sheet的索引号
	 * @param startRow 开始行
	 * @param startCell 开始列
	 */
	public void read(InputStream in, int sheetIndex, int startRow, int startCell) {
		read(in, sheetIndex, startRow, startCell, 0);
	}
}
