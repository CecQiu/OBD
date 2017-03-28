package com.hgsoft.carowner.entity;

import java.io.Serializable;
import java.util.Date;

public class ObdUnReg implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5985289492350632390L;

	private String id;
	private String obdSn;
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
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
}
