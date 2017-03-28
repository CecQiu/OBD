package com.hgsoft.carowner.entity;

import java.io.Serializable;
import java.util.Date;

public class FOTA implements Serializable{

	private static final long serialVersionUID = 8135220893812904679L;
	
	private String id;
	private String obdSn;
	private String version;
	private String fileName;
	private String address;
	private Integer port;
	private String username;
	private String password;
	private Date createTime;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
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
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Override
	public String toString() {
		return "FOTA [id=" + id + ", obdSn=" + obdSn + ", version=" + version + ", fileName=" + fileName + ", address="
				+ address + ", port=" + port + ", username=" + username + ", password=" + password + ", createTime="
				+ createTime + "]";
	}
	
	
}
