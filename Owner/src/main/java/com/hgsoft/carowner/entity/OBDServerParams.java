package com.hgsoft.carowner.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * OBD服务器参数信息
 * @author sujunguang
 * 2016年1月18日
 * 上午10:57:42
 */
public class OBDServerParams implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6780934336370055673L;
	private String id;
	private String obdSn;
	private String ipAddress;
	private int port;
	private String APN;
	private int type;
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
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getAPN() {
		return APN;
	}
	public void setAPN(String aPN) {
		APN = aPN;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public OBDServerParams() {
		super();
	}
	public OBDServerParams(String id, String obdSn, String ipAddress, int port,
			String aPN, int type, Date createTime) {
		super();
		this.id = id;
		this.obdSn = obdSn;
		this.ipAddress = ipAddress;
		this.port = port;
		APN = aPN;
		this.type = type;
		this.createTime = createTime;
	}
	@Override
	public String toString() {
		return "OBDServerParams [id=" + id + ", obdSn=" + obdSn
				+ ", ipAddress=" + ipAddress + ", port=" + port + ", APN="
				+ APN + ", type=" + type + ", createTime=" + createTime + "]";
	}
}
