package com.hgsoft.common.message;

import java.util.HashMap;
import java.util.Map;

/**
 * 报文命令任务枚举类
 * @author fdf
 */
public enum MUC {
	//命令任务枚举
	Muc1, Muc2, Muc3, Muc4, Muc5,
	Muc6, Muc7, Muc8, Muc9, Muc10,
	Muc11, Muc12, Muc13, Muc14, Muc15,
	Muc16, Muc17, Muc18, Muc19, Muc20;
	
	/**命令字与命令任务的map对象*/
	private static Map<String, MUC> map;
	
	//初始化map对象
	static {
		map = new HashMap<String, MUC>();
		map.put("0001", Muc1);
		map.put("0002", Muc2);
		map.put("0003", Muc3);
		map.put("0004", Muc4);
		map.put("0005", Muc5);
		map.put("0006", Muc6);
		map.put("0007", Muc7);
		map.put("0008", Muc8);
		map.put("0009", Muc9);
		map.put("000a", Muc10);
		map.put("000b", Muc11);
		map.put("000c", Muc12);
		map.put("000d", Muc13);
		map.put("000e", Muc14);
		map.put("000f", Muc15);
		map.put("0010", Muc16);
		map.put("0011", Muc17);
		map.put("0012", Muc18);
		map.put("0013", Muc19);
		map.put("0014", Muc20);
	}
	
	/**
	 * 通过命令字获取命令任务
	 * @param code 命令字
	 * @return 命令任务
	 */
	public static MUC getMuc(String code) {
		return map.get(code);
	}
}
