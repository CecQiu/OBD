package com.hgsoft.common.utils;

import java.text.DecimalFormat;

/**
 * 数字工具类
 * @author sujunguang
 * 2015-8-6
 */
public class NumUtil {

	/**
	 * 一个数保留几位小数
	 * @param num
	 * @param n
	 * @return
	 * @throws Exception 
	 */
	public static String getNumFractionDigits(float num, int n) throws Exception{
		String str = "0.";
		if(n <= 0 ){
			throw new Exception("位数不能小于1");
		}
		for (int i = 0; i < n; i++) {
			str += "0";
		}
		DecimalFormat df = new DecimalFormat(str);//格式化小数   
		
		return df.format(num);//返回的是String类型 
		
	}
}
