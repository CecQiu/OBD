package com.hgsoft.common.utils;

import java.util.regex.Pattern;

public class IPUtil {

	/**
	 * 判断是否是IP地址
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isIPAdress(String str) {
		Pattern pattern = Pattern
				.compile("^((\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5]|[*])\\.){3}(\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5]|[*])$");
		return pattern.matcher(str).matches();
	}

	public static void main(String[] args) {
		System.out.println(isIPAdress("12.12.12.1"));
		System.out.println(isIPAdress("12.12.1"));
		System.out.println(isIPAdress("12.12.12.1222"));
		System.out.println(isIPAdress("12.277.12.1"));
		System.out.println(isIPAdress("12.12.12.256"));
		System.out.println(isIPAdress("1.01.01.1"));
		System.out.println(isIPAdress("147.133.11.111"));
	}
}
