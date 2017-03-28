package com.hgsoft.common.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
/**
 * 过滤敏感词
 */
public class SensitiveWordUtils {

	//敏感词库
	private static final String keysContent = "色情@暴力";
	private static String[] keys = null;

	private static ArrayList<String> first = new ArrayList<String>();
	private static String[] sortFirst;
	private static char[] charFirst;
	private static HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
	private static HashMap<String, String[]> sortMap = new HashMap<String, String[]>();
	private static HashMap<String, char[]> charMap = new HashMap<String, char[]>();

	private static ArrayList<String> temp;
	private static String key, value;
	private int length;


	/*
	 * 静态代码块只会被执行一次 用来注册敏感词
	 */
	static {
		keys = keysContent.split("@");
	}

	/**
	 * 带参数的构造函数
	 * 
	 * @param keys
	 *            敏感词
	 */
	private SensitiveWordUtils() {
		for (String k : keys) {
			if (!first.contains(k.substring(0, 1))) {
				first.add(k.substring(0, 1));
			}
			length = k.length();
			for (int i = 1; i < length; i++) {
				key = k.substring(0, i);
				value = k.substring(i, i + 1);
				if (i == 1 && !first.contains(key)) {
					first.add(key);
				}

				// 有，添加
				if (map.containsKey(key)) {
					if (!map.get(key).contains(value)) {
						map.get(key).add(value);
					}
				}
				// 没有添加
				else {
					temp = new ArrayList<String>();
					temp.add(value);
					map.put(key, temp);
				}
			}
		}
		sortFirst = first.toArray(new String[first.size()]);
		Arrays.sort(sortFirst); // 排序

		charFirst = new char[first.size()];
		for (int i = 0; i < charFirst.length; i++) {
			charFirst[i] = first.get(i).charAt(0);
		}
		Arrays.sort(charFirst); // 排序

		String[] sortValue;
		ArrayList<String> v;
		Map.Entry<String, ArrayList<String>> entry;
		Iterator<Entry<String, ArrayList<String>>> iter = map.entrySet()
		.iterator();
		while (iter.hasNext()) {
			entry = (Map.Entry<String, ArrayList<String>>) iter.next();
			v = (ArrayList<String>) entry.getValue();
			sortValue = v.toArray(new String[v.size()]);
			Arrays.sort(sortValue); // 排序
			sortMap.put(entry.getKey(), sortValue);
		}

		char[] charValue;
		iter = map.entrySet().iterator();
		while (iter.hasNext()) {
			entry = (Map.Entry<String, ArrayList<String>>) iter.next();
			v = (ArrayList<String>) entry.getValue();
			charValue = new char[v.size()];
			for (int i = 0; i < charValue.length; i++) {
				charValue[i] = v.get(i).charAt(0);
			}
			Arrays.sort(charValue); // 排序
			charMap.put(entry.getKey(), charValue);
		}
	}
	
	/**
	 * 检测是否包含敏感词
	 * @param content 过滤的内容
	 * @return true 包含敏感词 false 不含敏感词
	 */
	public static boolean checkContent(String content) {
		SensitiveWordUtils su = new SensitiveWordUtils();
		return su.check(content);
	}
	
	/**
	 * 检测是否包含敏感词
	 * @param content
	 * @return
	 */
	private boolean check(String content) {
		boolean flag = false;
		String f, c = content;
		char g;
		char[] temps;
		int length = c.length();
		for (int i = 0; i < length - 1; i++) {
			g = c.charAt(i);
			// 二分查找
			if (Arrays.binarySearch(charFirst, g) > -1) {
				tag : for (int j = i + 1; j < length; j++) {
					f = c.substring(i, j);
					g = c.charAt(j);
					temps = charMap.get(f);
					if (temps == null) { // 找到了
						flag = true;
						break tag;
					}
					// 二分查找
					if (Arrays.binarySearch(temps, g) > -1) {
						if (j == length - 1) { // 找到了
							flag = true;
							break tag;
						}
					} else { // 没有找到了
						break;
					}
				}
			}
		}
		return flag;
	}
	
	/**
	 * 把敏感词替换成*
	 * 
	 * @param content
	 *            需要过滤的内容
	 * @return 过滤完后的符合要求的内容
	 */
	private String replace(String content) {
		String r = null, f, c = content;
		String replacedword = content;
		char g;
		char[] temps;
		int length = c.length();
		for (int i = 0; i < length - 1; i++) {
			g = c.charAt(i);
			// 二分查找
			if (Arrays.binarySearch(charFirst, g) > -1) {
				tag : for (int j = i + 1; j < length; j++) {
					f = c.substring(i, j);
					g = c.charAt(j);
					temps = charMap.get(f);
					if (temps == null) { // 找到了
						//System.out.println("ok");
						r = f;
						String str = "";
						for (int m = 1; m <= r.length(); m++) {
							str = str + "*";
						}
						replacedword = c.replace(r, str);
						c = replacedword;
						break tag;
					}
					// 二分查找
					if (Arrays.binarySearch(temps, g) > -1) {
						if (j == length - 1) {
							// print("find!");
							//System.out.println("find!");
							r = c.substring(i, j + 1);
							String str = "";
							for (int m = 1; m <= r.length(); m++) {
								str = str + "*";
							}
							replacedword = c.replace(r, str);
							c = replacedword;
							break tag;
						}
					} else { // 没有找到了
						break;
					}
				}
			}
		}
		return replacedword;
	}
}

