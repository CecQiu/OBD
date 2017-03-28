package com.hgsoft.common.utils;

import org.apache.commons.codec.binary.Base64;

public class Base64Utils {
	/**
	 * @param bytes
	 * @return
	 */
	public static byte[] decode(final byte[] bytes) {
		return Base64.decodeBase64(bytes);
	}

	/**
	 * 二进制数据编码为BASE64字符串
	 * 
	 * @param bytes
	 * @return
	 * @throws Exception
	 */
	public static String encode(final byte[] bytes) {
		return new String(Base64.encodeBase64(bytes));
	}

	public static void main(String[] args) {
		System.out.println(encode("123456".getBytes()));  //----MTIzNDU2
		System.out.println(decode(encode("123456".getBytes()).getBytes()));
	}
}
