package com.hgsoft.carowner.entity;

import java.io.Serializable;
import java.util.Date;
/**
 * 位置-驾驶行为，驾驶行为
 * @author sujunguang
 * 2015年12月29日
 * 上午10:33:44
 */
public class DriveBehaviour implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1086071284283863188L;
	
	private String obdSn;//设备号
	private Date time;//gps时间
	private String longitude;	//经度
	private String latitude;//	纬度
	private int overspeed;//超速 0-否 1-是
	private int suddenTurn;//急转弯 0-否 1-是
	private int suddenUpSpeed;//急加速 0-否 1-是
	private int suddenDownSpeed;//急减速 0-否 1-是
	private int suddenBrake;//急刹车 0-否 1-是
	private int suddenChangeRoad;//急变道 0-否 1-是
	private int speedNotMatch;//车速转速不匹配 0-否 1-是
	private int longLowSpeed;//长怠速 0-否 1-是
	
	public String getTypeString(){
		StringBuffer sb = new StringBuffer("");
		//0-急加速
		if(this.suddenUpSpeed==1){
			sb.append("0+");
		}
		//1-急减速
		if(this.suddenDownSpeed==1){
			sb.append("1+");
		}
		//2-急转弯
		if(this.suddenChangeRoad==1){
			sb.append("2+");
		}
		//3-发动机高转速 位置行为表没这个字段
		//4-车速转速不匹配
		if(this.speedNotMatch==1){
			sb.append("4+");
		}
		//5-超速
		if(this.overspeed==1){
			sb.append("5+");
		}
		//6疲劳驾驶 没有这个字段
		//7-急刹车
		if(this.suddenBrake==1){
			sb.append("7+");
		}
		//8-急变道
		if(this.suddenChangeRoad==1){
			sb.append("8+");
		}
		//9-怠速 
		if(this.longLowSpeed==1){
			sb.append("9+");
		}
		if(sb.toString().endsWith("+")){
			sb= sb.replace(sb.lastIndexOf("+"), sb.length(), "");
		}
		
		return sb.toString();
	}
	
	public int getOverspeed() {
		return overspeed;
	}
	public void setOverspeed(int overspeed) {
		this.overspeed = overspeed;
	}
	public int getSuddenTurn() {
		return suddenTurn;
	}
	public void setSuddenTurn(int suddenTurn) {
		this.suddenTurn = suddenTurn;
	}
	public int getSuddenUpSpeed() {
		return suddenUpSpeed;
	}
	public void setSuddenUpSpeed(int suddenUpSpeed) {
		this.suddenUpSpeed = suddenUpSpeed;
	}
	public int getSuddenDownSpeed() {
		return suddenDownSpeed;
	}
	public void setSuddenDownSpeed(int suddenDownSpeed) {
		this.suddenDownSpeed = suddenDownSpeed;
	}
	public int getSuddenBrake() {
		return suddenBrake;
	}
	public void setSuddenBrake(int suddenBrake) {
		this.suddenBrake = suddenBrake;
	}
	public int getSuddenChangeRoad() {
		return suddenChangeRoad;
	}
	public void setSuddenChangeRoad(int suddenChangeRoad) {
		this.suddenChangeRoad = suddenChangeRoad;
	}
	public int getSpeedNotMatch() {
		return speedNotMatch;
	}
	public void setSpeedNotMatch(int speedNotMatch) {
		this.speedNotMatch = speedNotMatch;
	}
	public int getLongLowSpeed() {
		return longLowSpeed;
	}
	public void setLongLowSpeed(int longLowSpeed) {
		this.longLowSpeed = longLowSpeed;
	}
	public String getObdSn() {
		return obdSn;
	}
	public void setObdSn(String obdSn) {
		this.obdSn = obdSn;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
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
	
	public static void main(String[] args) {
		DriveBehaviour bd = new DriveBehaviour();
		bd.setObdSn("1111");
		bd.setTime(new Date());
		bd.setLongitude("1223");
		bd.setLatitude("22");
		//bd.setOverspeed(1);
		bd.setSuddenTurn(1);
		bd.setSuddenUpSpeed(1);
		bd.setSuddenDownSpeed(1);
		bd.setSuddenBrake(1);
		bd.setSuddenChangeRoad(1);
		bd.setSpeedNotMatch(1);
		bd.setLongLowSpeed(1);
		String s=bd.getTypeString();
		System.out.println(s);
	}
}
