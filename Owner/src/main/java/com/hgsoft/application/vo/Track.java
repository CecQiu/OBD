package com.hgsoft.application.vo;

import java.util.Date;

/**
 * 轨迹虚拟类
 * @author Administrator
 *
 */
public class Track {
	
	private String id;
	private String license;//车牌号
	private Object travelStart;//开始时间
	private Object travelEnd;//结束时间
	private String distance;//里程
	private String totalFuel;//总油耗
	private String totalTime;//行程总时间
	private String avgSpeed;//平均速度
	private String starPoint;//开始点
	private String endPoint;//结束点
	private String obdSn;//设备号
	private String insesrtTime;//插入时间
	private String userName;//用户名
	private String mobileNumber;//手机号码
	private Date date1;//日期类型
	private String startLongitude;//起点经度
	private String startLatitude;//起点纬度
	private String endLongitude;//起点经度
	private String endLatitude;//起点纬度
	
	public String getLicense() {
		return license;
	}
	public void setLicense(String license) {
		this.license = license;
	}
	public Object getTravelStart() {
		return travelStart;
	}
	public void setTravelStart(Object travelStart) {
		this.travelStart = travelStart;
	}
	public Object getTravelEnd() {
		return travelEnd;
	}
	public void setTravelEnd(Object travelEnd) {
		this.travelEnd = travelEnd;
	}
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	public String getTotalFuel() {
		return totalFuel;
	}
	public void setTotalFuel(String totalFuel) {
		this.totalFuel = totalFuel;
	}
	public String getTotalTime() {
		return totalTime;
	}
	public void setTotalTime(String totalTime) {
		this.totalTime = totalTime;
	}
	public String getAvgSpeed() {
		return avgSpeed;
	}
	public void setAvgSpeed(String avgSpeed) {
		this.avgSpeed = avgSpeed;
	}
	public String getStarPoint() {
		return starPoint;
	}
	public void setStarPoint(String starPoint) {
		this.starPoint = starPoint;
	}
	public String getEndPoint() {
		return endPoint;
	}
	public void setEndPoint(String endPoint) {
		this.endPoint = endPoint;
	}
	public String getObdSn() {
		return obdSn;
	}
	public void setObdSn(String obdSn) {
		this.obdSn = obdSn;
	}
	public String getInsesrtTime() {
		return insesrtTime;
	}
	public void setInsesrtTime(String insesrtTime) {
		this.insesrtTime = insesrtTime;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public Date getDate1() {
		return date1;
	}
	public void setDate1(Date date1) {
		this.date1 = date1;
	}
	public String getStartLongitude() {
		return startLongitude;
	}
	public void setStartLongitude(String startLongitude) {
		this.startLongitude = startLongitude;
	}
	public String getStartLatitude() {
		return startLatitude;
	}
	public void setStartLatitude(String startLatitude) {
		this.startLatitude = startLatitude;
	}
	public String getEndLongitude() {
		return endLongitude;
	}
	public void setEndLongitude(String endLongitude) {
		this.endLongitude = endLongitude;
	}
	public String getEndLatitude() {
		return endLatitude;
	}
	public void setEndLatitude(String endLatitude) {
		this.endLatitude = endLatitude;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	
}
