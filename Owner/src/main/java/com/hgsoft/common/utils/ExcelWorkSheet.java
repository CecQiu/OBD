package com.hgsoft.common.utils;

import java.util.ArrayList;
import java.util.List;

public class ExcelWorkSheet<T> {
	// 工作单名称
	private String sheetName;
	// 数据行
	private List<T> data = new ArrayList<T>();
	// 列名
	private List<String> columns;
	
	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

	public List<String> getColumns() {
		return columns;
	}

	public void setColumns(List<String> columns) {
		this.columns = columns;
	}


}
