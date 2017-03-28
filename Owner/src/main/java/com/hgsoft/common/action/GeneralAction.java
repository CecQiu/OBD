package com.hgsoft.common.action;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 * 一般事务action
 * @author fdf
 */

@Controller
@Scope("prototype")
public class GeneralAction extends BaseAction{
	
	private String directory;
	private String filename;
	
	public void checkFileExist() {
		try {
			String result = "00";
			String dir = directory + filename;
			File f = new File(ServletActionContext.getServletContext().getRealPath("/") + dir);
			if(f.exists() && f.isFile()) {
				result = "01";
			} else {
				result = "00";
			}
			this.outMessage(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public InputStream getInputStream() throws Exception {
		String dir = directory + filename;
        //如果dir是Resource下的相对路径
        return ServletActionContext.getServletContext().getResourceAsStream(dir);
    }
	
	public int getFileLength() throws IOException, Exception {
		return getInputStream().available();
	}
	
	public String downLoad() {
		return "downloadfile";
	}

	public String getDirectory() {
		return directory;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}
}
