package com.hgsoft.application.util;

import java.util.HashMap;
import java.util.Map;

public class RandomSecretUtil {

	private static Map<Integer,String> types = new HashMap<Integer,String>();
	
	static {
		types.put(1,"yA/wzbbetv5rm+7");
		types.put(2,"69+ypZCN54Yh$Bq");
		types.put(3,"M/2lxQk3qH/9z2d");
		types.put(4,"0IxFDOCP7uvT3r1");
		types.put(5,"3zURqL$vlMdw4Tj");
		types.put(6,"xn3gBL4z5Pahrmd");
		types.put(7,"YAVtjzMMLdsapMY");
		types.put(8,"XxUZ@WuFeA9V0N7");
		types.put(9,"XHPQHIZf4n/ZsL2");
		types.put(10,"0G42J07MxzomJeS");
		types.put(11,"33AufuHgQjyk6mn");
		types.put(12,"/2cp020zWE$tV*N");
		types.put(13,"NATmPN@SGVuNZJb");
		types.put(14,"Cc7GTN1h+G0GQk0");
		types.put(16,"dLv*JlputoJMTVs");
		types.put(17,"Ufj7XpBpvy2ilxq");
		types.put(18,"D7uay8dlysQcpXl");
		types.put(19,"0ZYma$zMm9R@+Wi");
		types.put(20,"6W*d+wHS8Pe9cpT");
		types.put(15,"sbcOEn2PeLi$VNb");
	}
	
	private RandomSecretUtil() {
	}

	private static char[] chs = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q',
			'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l',
			'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', '+', '/', '*', '@', '$' };

	public static String randomString(int length) {
		if (length < 1) {
			return null;
		}
		StringBuffer buffer = new StringBuffer();
		for(int i=0; i<length; i++) {
			buffer.append(chs[(int)(Math.random()*chs.length)]);
		}
		return buffer.toString();
	}
	
	public static String getType(int type){
		return types.get(type);
	}
}
