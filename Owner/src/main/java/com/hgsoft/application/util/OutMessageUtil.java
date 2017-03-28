package com.hgsoft.application.util;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

public class OutMessageUtil {
	
	public static void outMessage(String message) throws IOException {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/text; charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		OutputStream out = response.getOutputStream();
		try {
			out.write(message.getBytes("utf-8"));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.flush();
				out.close();
			}
		}
	}
	
	public static void outJsonMessage(String message) throws IOException {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/Json; charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		OutputStream out = response.getOutputStream();
		try {
			out.write(message.getBytes("utf-8"));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.flush();
				out.close();
			}
		}
	}
	
	public static void outMessage(StringBuffer message) throws IOException {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/text; charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		OutputStream out = response.getOutputStream();
		try {
			out.write(message.toString().getBytes("utf-8"));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.flush();
				out.close();
			}
		}
	}

	public static void outJsonMessage(StringBuffer message) throws IOException {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/Json; charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		OutputStream out = response.getOutputStream();
		try {
			out.write(message.toString().getBytes("utf-8"));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.flush();
				out.close();
			}
		}
	}
	
}
