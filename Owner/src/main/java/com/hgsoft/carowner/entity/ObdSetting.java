package com.hgsoft.carowner.entity;


import java.util.Date;

/**
 * obd离线设置表
 * ljl 20160421
 */
public class ObdSetting implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String obdSn;
	private String type;
	private String settingMsg;
	private Date createTime;
	private Date updateTime;
	private String valid;

	public ObdSetting() {
	}

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSettingMsg() {
		return settingMsg;
	}

	public void setSettingMsg(String settingMsg) {
		this.settingMsg = settingMsg;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getValid() {
		return valid;
	}

	public void setValid(String valid) {
		this.valid = valid;
	}

	@Override
	public String toString() {
		return "ObdSetting [id=" + id + ", obdSn=" + obdSn + ", type=" + type
				+ ", settingMsg=" + settingMsg + ", createTime=" + createTime
				+ ", updateTime=" + updateTime + ", valid=" + valid + "]";
	}

	
}
