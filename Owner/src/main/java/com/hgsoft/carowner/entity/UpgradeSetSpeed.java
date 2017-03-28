package com.hgsoft.carowner.entity;

import java.util.Date;

public class UpgradeSetSpeed implements java.io.Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 5602485352647885203L;
	private String id;
	private String obdSn;
	private Integer type;
	private Integer obdSpeed;
	private Integer gpsSpeed;
	private Integer count;
	private Date createTime;
	private Date updateTime;
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
	public Integer getObdSpeed() {
		return obdSpeed;
	}
	public void setObdSpeed(Integer obdSpeed) {
		this.obdSpeed = obdSpeed;
	}
	public Integer getGpsSpeed() {
		return gpsSpeed;
	}
	public void setGpsSpeed(Integer gpsSpeed) {
		this.gpsSpeed = gpsSpeed;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	@Override
	public String toString() {
		return "UpgradeSetSpeed [id=" + id + ", obdSn=" + obdSn + ", type=" + type + ", obdSpeed=" + obdSpeed
				+ ", gpsSpeed=" + gpsSpeed + ", count=" + count + ", createTime=" + createTime + ", updateTime="
				+ updateTime + "]";
	}
	
}
