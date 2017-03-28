package com.hgsoft.common.utils;

import java.util.Random;

/**
 * 数据表主键生成工具类
 * @author fdf
 */
public class IDUtil {
	
	/**生成随机字符串用的char数组*/
	private static char[] chars = {
		'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
		 'k', 'l', 'n', 'm', 'o', 'p', 'q', 'r', 's', 't',
		 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3',
		 '4', '5', '6', '7', '8', '9',
	};
	
	/**
	 * 默认ID生成规则：13位时间戳+5位随机数【a-z 0-9】
	 * @return	18位的ID字符串
	 */
	public static String createID() {
		Random r = new Random();
		StringBuffer randomStr = new StringBuffer("");
		for(int i=0; i<5; i++) {
			randomStr.append(chars[r.nextInt(chars.length)]);
		}
		return System.currentTimeMillis() + randomStr.toString();
	}
	
	public static void main(String[] args) {
		
	}
}
