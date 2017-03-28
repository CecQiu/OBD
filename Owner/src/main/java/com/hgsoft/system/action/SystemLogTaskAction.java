package com.hgsoft.system.action;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.hgsoft.common.action.BaseAction;

import com.hgsoft.system.service.SystemLogService;

@Controller
@Scope("prototype")
public class SystemLogTaskAction extends BaseAction {
	
	@Resource
	private SystemLogService systemLogService;
	private HttpSession session;

	//备份盘符
	private String position;
	//备份目录
	private String backupDir;
	//时间间隔
	private String intervalTime;
	//开始日期
	private Date startDate;
	
	private String hour="00";
	private String minute="00";
	private String second="00";
	
	//备份目录路径在其它参数里的对应的参数名
	public String timeTask(){return "timeTask";}
	

	public String delSystemLog() {
		systemLogService.deleteSystemLog();
		return "timeTask";
	}
	
	// 验证输入的盘符是否存在
	public void ishasPosition() {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text;charset=gbk");
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String path = this.position.trim()+":/";
		File _file = new File(path);
		if(!_file.exists()) {
			resetPosition();
			out.print("noPosition");
		} else {
			out.print("hasPosition");
		}
	}

	// 异步验证不存在盘符时，重置为D盘
	public void resetPosition() {
		HttpServletRequest request=ServletActionContext.getRequest();
		session =request.getSession();
		session.setAttribute("_disk", "D");
	}
	
	//Getter/Setter
	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getBackupDir() {
		return backupDir;
	}

	public void setBackupDir(String backupDir) {
		this.backupDir = backupDir;
	}

	public String getIntervalTime() {
		return intervalTime;
	}

	public void setIntervalTime(String intervalTime) {
		this.intervalTime = intervalTime;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public String getHour() {
		return hour;
	}

	public void setHour(String hour) {
		this.hour = hour;
	}

	public String getMinute() {
		return minute;
	}

	public void setMinute(String minute) {
		this.minute = minute;
	}

	public String getSecond() {
		return second;
	}

	public void setSecond(String second) {
		this.second = second;
	}
}
