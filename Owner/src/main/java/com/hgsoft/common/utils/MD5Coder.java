package com.hgsoft.common.utils;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * MD5加密组件
 * 
 * @author huang_cx
 * @version 1.0
 */
public abstract class MD5Coder {

	/**
	 * MD5加密
	 * 
	 * @param data
	 *            待加密数据
	 * @return byte[] 消息摘要
	 * 
	 * @throws Exception
	 */
	public static byte[] encodeMD5(String data) throws Exception {
		// 执行消息摘要
		return DigestUtils.md5(data);
	}

	/**
	 * MD5加密
	 * 
	 * @param data
	 *            待加密数据
	 * @return byte[] 消息摘要
	 * 
	 * @throws Exception
	 */
	public static String encodeMD5Hex(String data) throws Exception {
		// 执行消息摘要
		return DigestUtils.md5Hex(data);
	}

	/**
	 * 登录用户密码加密
	 * 
	 * @param username
	 *            登录用户名
	 * @param password
	 *            登录密码
	 * @return
	 * @throws Exception
	 */
	public static String encodeLoginUser(String username, String password) {
		try {
			return encodeMD5Hex(password + "{" + username + "}");
		} catch (Exception e) {
		}
		return "";
	}

	public static void main(String[] args) throws Exception {
		final String name = "admin";
		final String password = "hgsoft";

		// admin
		// test1
		// Hello
		// second

		System.out.println(encodeMD5Hex(password + "{" + name + "}"));//c0d4d722014c42ec6cbf54b909163524
	}
}
