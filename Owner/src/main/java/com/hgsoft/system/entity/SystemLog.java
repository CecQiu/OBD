package com.hgsoft.system.entity;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class SystemLog implements Serializable {
	
	//private static final long serialVersionUID = 1L;
	private Integer sys_id;//系统日志记录号(PK)
	private Date logTime;//日志时间
	private Integer logType;//日志类型：0-登录登出；1-用户操作；2-异常错误 ; 3-执行免费操作; 
	private Integer operatorID;
	private String logData;//日志具体内容
	private String coverage;//日志影响范围（数据库表名、字段、sql语句等）
	private String remark;//备注

	public SystemLog(){
		
	}
	
	public SystemLog(Date logTime, Integer logType, Integer operatorID,
			String logData, String coverage, String remark) {
		super();
		this.logTime = logTime;
		this.logType = logType;
		this.operatorID = operatorID;
		this.logData = logData;
		this.coverage = coverage;
		this.remark = remark;
	}
	public Integer getSys_id() {
		return sys_id;
	}
	public void setSys_id(Integer sys_id) {
		this.sys_id = sys_id;
	}
	public Date getLogTime() {
		return logTime;
	}
	public void setLogTime(Date logTime) {
		this.logTime = logTime;
	}
	public Integer getLogType() {
		return logType;
	}
	public void setLogType(Integer logType) {
		this.logType = logType;
	}
	public String getLogData() {
		return logData;
	}
	public void setLogData(String logData) {
		this.logData = logData;
	}
	public String getCoverage() {
		return coverage;
	}
	public void setCoverage(String coverage) {
		this.coverage = coverage;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getOperatorID() {
		return operatorID;
	}
	public void setOperatorID(Integer operatorID) {
		this.operatorID = operatorID;
	}

	//日志类型字符串
	public String getLogTypeString(){
		if(this.logType==0) return "登录登出";
		else if(this.logType==1) return "操作";
		else if(this.logType==2) return "异常错误";
		else return "";
	}
}
