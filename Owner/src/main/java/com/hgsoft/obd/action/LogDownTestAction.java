package com.hgsoft.obd.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.hgsoft.common.action.BaseAction;

@Controller
@Scope("prototype")
public class LogDownTestAction extends BaseAction {
	private static String tomcatPath = System.getProperty("catalina.home");
	private static String obdLogPath = tomcatPath + File.separator + "bin" + File.separator + "logs" +
																File.separator + "obd-2.0";
	private static String tomcatLogPath = tomcatPath + File.separator + "logs";
	
	private String[] fileNames;

	public String[] getFileNames() {
		return fileNames;
	}

	public void setFileNames(String[] fileNames) {
		this.fileNames = fileNames;
	}

	private String fileName;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
 
	public String download() {
		if(fileName.startsWith("/")){
			fileName = fileName.substring(1,fileName.length());
		}
		String path = obdLogPath;
		File file = null;
		file = new File(obdLogPath + File.separator + fileName);
		if(!file.exists()){
			file = new File(tomcatLogPath + File.separator + fileName);
			path = tomcatLogPath;
		}
		if (file.isDirectory()) {
			path += File.separator + fileName;
			return listFiles(path);
		}
		if (file.exists()) {
			// 获取网站部署路径(通过ServletContext对象)，用于确定下载文件位置，从而实现下载
			HttpServletResponse response = ServletActionContext.getResponse();

			// 1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
			response.setContentType("multipart/form-data");
			// 2.设置文件头：最后一个参数是设置下载文件名(假如我们叫a.pdf)
			response.setHeader("Content-Disposition", "attachment;fileName="
					+ fileName);
			ServletOutputStream out = null;
			FileInputStream inputStream = null;
			try {
				inputStream = new FileInputStream(file);

				// 3.通过response获取ServletOutputStream对象(out)
				out = response.getOutputStream();

				int b = 0;
				byte[] buffer = new byte[512];
				while (b != -1) {
					b = inputStream.read(buffer);
					// 4.写到输出流(out)中
					out.write(buffer, 0, b);
				}
				out.flush();

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					inputStream.close();
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			outMessage("文件不存在！");
		}
		return null;
	}

	public String listFiles(String path) {
		File files = new File(path);
		fileNames = files.list();
		return "listFiles";
	}

	public String listObdFiles() {
		return listFiles(obdLogPath);
	}

	public String listTomcatFiles() {
		return listFiles(tomcatLogPath);
	}

}
