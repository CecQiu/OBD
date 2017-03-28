package com.hgsoft.carowner.entity;

import java.io.Serializable;
import java.util.Date;

public class FotaSet implements Serializable{

	private static final long serialVersionUID = 8135220893812904679L;
	
	private Integer id;
	private String obdSn;
	private String version;
	private String batchVersion;
	
	private String fileName;
	private String ftpIP;
	private Integer ftpPort;
	
	private String ftpUsername;
	private String ftpPwd;
	private Date createTime;
	
	private String valid;
	private String uploadOper;
	private String auditOper;
	
	private Date auditTime;
	private String auditResult;
	private Date sendTime;
	private String mifiFlag;
	private Date mifiTime;
	private String useFlag;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getObdSn() {
		return obdSn;
	}
	public void setObdSn(String obdSn) {
		this.obdSn = obdSn;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getBatchVersion() {
		return batchVersion;
	}
	public void setBatchVersion(String batchVersion) {
		this.batchVersion = batchVersion;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFtpIP() {
		return ftpIP;
	}
	public void setFtpIP(String ftpIP) {
		this.ftpIP = ftpIP;
	}
	public Integer getFtpPort() {
		return ftpPort;
	}
	public void setFtpPort(Integer ftpPort) {
		this.ftpPort = ftpPort;
	}
	public String getFtpUsername() {
		return ftpUsername;
	}
	public void setFtpUsername(String ftpUsername) {
		this.ftpUsername = ftpUsername;
	}
	public String getFtpPwd() {
		return ftpPwd;
	}
	public void setFtpPwd(String ftpPwd) {
		this.ftpPwd = ftpPwd;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getValid() {
		return valid;
	}
	public void setValid(String valid) {
		this.valid = valid;
	}
	public Date getAuditTime() {
		return auditTime;
	}
	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}
	public Date getSendTime() {
		return sendTime;
	}
	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getUploadOper() {
		return uploadOper;
	}
	public void setUploadOper(String uploadOper) {
		this.uploadOper = uploadOper;
	}
	public String getAuditOper() {
		return auditOper;
	}
	public void setAuditOper(String auditOper) {
		this.auditOper = auditOper;
	}
	
	public String getUseFlag() {
		return useFlag;
	}
	public void setUseFlag(String useFlag) {
		this.useFlag = useFlag;
	}
	
	public String getAuditResult() {
		return auditResult;
	}
	public void setAuditResult(String auditResult) {
		this.auditResult = auditResult;
	}
	public String getMifiFlag() {
		return mifiFlag;
	}
	public void setMifiFlag(String mifiFlag) {
		this.mifiFlag = mifiFlag;
	}
	public Date getMifiTime() {
		return mifiTime;
	}
	public void setMifiTime(Date mifiTime) {
		this.mifiTime = mifiTime;
	}
	@Override
	public String toString() {
		return "FotaSet [id=" + id + ", obdSn=" + obdSn + ", version=" + version + ", batchVersion=" + batchVersion
				+ ", fileName=" + fileName + ", ftpIP=" + ftpIP + ", ftpPort=" + ftpPort + ", ftpUsername="
				+ ftpUsername + ", ftpPwd=" + ftpPwd + ", createTime=" + createTime + ", valid=" + valid
				+ ", uploadOper=" + uploadOper + ", auditOper=" + auditOper + ", auditTime=" + auditTime
				+ ", auditResult=" + auditResult + ", sendTime=" + sendTime + ", mifiFlag=" + mifiFlag + ", mifiTime="
				+ mifiTime + ", useFlag=" + useFlag + "]";
	}
	

}
