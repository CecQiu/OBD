package com.hgsoft.common.utils;

/**
 * @author shunfa
 * Id 生成
 */
public class CreateId {
		
	public static String createFirstId(int len){
		return createFirstId('1', len);
	}
	
	public static String createFirstId(char firstNum, int len){
		if(len <= 0) return "";
		if(len == 1 ) return "" + firstNum;
		StringBuffer id = new StringBuffer("1");
		int i = len-2;
		while(i>0){
			id.append("0");
			i--;
		}
		if(i == 0){
			id.append("1");
		}
		return id.toString();
	}
	
	public static String getNext(String num, int len){
		if(num == null){
			return createFirstId(len);
		}
		if(num.length() != len){
			return createFirstId(len);
		}
		char[] chars = num.toCharArray();
		int count = len - 1;
		boolean again = true;
		while(again && count>0){
			if(chars[count]>='0' && chars[count]<'9'){
				chars[count] ++;
				again = false;
			} else if(chars[count]=='9'){
				chars[count] = '0';
				count --;
			}
		}
		if(count == 0 && again){
			if(chars[count] == '9')
				chars[count] = 'A';
			if(chars[count] == 'Z')
				chars[count] = 'a';
			if(chars[count] == 'z')
				chars[count] = '0';
		}
		StringBuffer sb = new StringBuffer(len);
		sb.append(chars);
		return sb.toString();
	}
	
	public static String getNext(String num){
		return getNext(num, num.length());
	}
}
