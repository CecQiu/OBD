package com.hgsoft.common.utils;

import java.util.Scanner;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
/**
 * HTTP请求
 * @author sujunguang
 *
 */
public class HTTPFetcher {
	private HttpClient http = new HttpClient();
	/**
	 * POST请求
	 * @param action 请求地址
	 * @param data 数据
	 * @return
	 * @throws Exception
	 */
	public String post(String action, String data) throws Exception {
		
		String url = action;
		
		PostMethod post = new PostMethod(url);
		post.addRequestHeader("Content-Type", "application/json; charset=UTF-8");
		if(data != null)
			post.setRequestEntity(new StringRequestEntity(data, "application/json", "UTF-8"));
		
		HTTPResponse res = new HTTPResponse();
		
		int statusCode = http.executeMethod(post);
		
		res.statusCode = statusCode;
		
		String encode = post.getResponseCharSet() == null ? "UTF-8" : post.getResponseCharSet();
		
		Scanner scan = new Scanner(post.getResponseBodyAsStream(), "UTF-8");
		while (scan.hasNextLine())
			res.responseText.append(scan.nextLine());
		scan.close();
		return res.responseText.toString();
	}
}
