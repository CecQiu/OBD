package com.hgsoft.carowner.entity;

import java.util.Date;

/**
 * 车辆行驶记录
 * @author sujunguang
 * 2015-8-5
 */
public class CarGSPTrack implements java.io.Serializable {

	private static final long serialVersionUID = 4136784679343555872L;
	
	private String gspTrackId;//编号
	private String obdSn;//智能盒激活码（SN码）
	private String longitude;//经度
	private String latitude;//纬度
	private Integer directionAngle;//方向角
	private Integer gpsSpeed;//Gps速度
	private Integer obdSpeed;//Obd速度
	private String high;//高程
	private Date gspTrackTime;//时间yyyy-MM-dd hh:mm:ss
	
	public String getGspTrackId() {
		return gspTrackId;
	}
	public void setGspTrackId(String gspTrackId) {
		this.gspTrackId = gspTrackId;
	}
	public String getObdSn() {
		return obdSn;
	}
	public void setObdSn(String obdSn) {
		this.obdSn = obdSn;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public Integer getDirectionAngle() {
		return directionAngle;
	}
	public void setDirectionAngle(Integer directionAngle) {
		this.directionAngle = directionAngle;
	}
	public Integer getGpsSpeed() {
		return gpsSpeed;
	}
	public void setGpsSpeed(Integer gpsSpeed) {
		this.gpsSpeed = gpsSpeed;
	}
	public Integer getObdSpeed() {
		return obdSpeed;
	}
	public void setObdSpeed(Integer obdSpeed) {
		this.obdSpeed = obdSpeed;
	}
	public String getHigh() {
		return high;
	}
	public void setHigh(String high) {
		this.high = high;
	}
	public Date getGspTrackTime() {
		return gspTrackTime;
	}
	public void setGspTrackTime(Date gspTrackTime) {
		this.gspTrackTime = gspTrackTime;
	}
	@Override
	public String toString() {
		return "CarGSPTrack [gspTrackId=" + gspTrackId + ", obdSn=" + obdSn
				+ ", longitude=" + longitude + ", latitude=" + latitude
				+ ", directionAngle=" + directionAngle + ", gpsSpeed="
				+ gpsSpeed + ", obdSpeed=" + obdSpeed + ", high=" + high
				+ ", gspTrackTime=" + gspTrackTime + "]";
	}

}
