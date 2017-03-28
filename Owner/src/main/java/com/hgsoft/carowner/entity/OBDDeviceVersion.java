package com.hgsoft.carowner.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * OBD设备版本信息
 * @author sujunguang
 * 2016年1月18日
 * 上午10:57:42
 */
public class OBDDeviceVersion implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6780934336370055673L;
	private String id;
	private String obdSn;
	private String appVersion;
	private String iapVersion;
	private String mifiVersion;
	private String gpsVersion;
	private String mifiHardwareVersion;
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
	public String getAppVersion() {
		return appVersion;
	}
	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}
	public String getIapVersion() {
		return iapVersion;
	}
	public void setIapVersion(String iapVersion) {
		this.iapVersion = iapVersion;
	}
	public String getMifiVersion() {
		return mifiVersion;
	}
	public void setMifiVersion(String mifiVersion) {
		this.mifiVersion = mifiVersion;
	}
	public String getGpsVersion() {
		return gpsVersion;
	}
	public void setGpsVersion(String gpsVersion) {
		this.gpsVersion = gpsVersion;
	}
	public String getMifiHardwareVersion() {
		return mifiHardwareVersion;
	}
	public void setMifiHardwareVersion(String mifiHardwareVersion) {
		this.mifiHardwareVersion = mifiHardwareVersion;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public OBDDeviceVersion() {
		super();
	}
	public OBDDeviceVersion(String id, String obdSn, String appVersion,
			String iapVersion, String mifiVersion, String gpsVersion,
			String mifiHardwareVersion, Date createTime) {
		super();
		this.id = id;
		this.obdSn = obdSn;
		this.appVersion = appVersion;
		this.iapVersion = iapVersion;
		this.mifiVersion = mifiVersion;
		this.gpsVersion = gpsVersion;
		this.mifiHardwareVersion = mifiHardwareVersion;
		this.createTime = createTime;
	}
	@Override
	public String toString() {
		return "OBDDeviceVersion [id=" + id + ", obdSn=" + obdSn
				+ ", appVersion=" + appVersion + ", iapVersion=" + iapVersion
				+ ", mifiVersion=" + mifiVersion + ", gpsVersion=" + gpsVersion
				+ ", mifiHardwareVersion=" + mifiHardwareVersion
				+ ", createTime=" + createTime + "]";
	}
	
}
