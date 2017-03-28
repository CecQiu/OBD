package com.hgsoft.carowner.entity;

import java.io.Serializable;
import java.util.Date;
/**
 * 预警消息
 * @author sujunguang
 *
 */
public class WarningMessage implements Serializable{

	/**
	 */
	private static final long serialVersionUID = -3966558179023785316L;

	private Integer id;
	private String obdSn;//OBD设备
	private String messageType;//消息类型
	private String messageDesc;//消息描述
	private Date messageTime;//消息时间
	private String remark;//备注
	private String company;//备注
	
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
	public String getMessageType() {
		return messageType;
	}
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	public String getMessageDesc() {
		return messageDesc;
	}
	public void setMessageDesc(String messageDesc) {
		this.messageDesc = messageDesc;
	}
	public Date getMessageTime() {
		return messageTime;
	}
	public void setMessageTime(Date messageTime) {
		this.messageTime = messageTime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
