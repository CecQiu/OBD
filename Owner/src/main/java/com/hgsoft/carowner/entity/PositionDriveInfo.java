package com.hgsoft.carowner.entity;

import java.io.Serializable;
import java.util.Date;
/**
 * 位置-驾驶行为表，新协议
 * @author sujunguang
 * 2015年12月29日
 * 上午10:33:44
 */
public class PositionDriveInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1086071284283863188L;
	
	private String id;//主键:时间戳13位+5位随机数[a-z0-9]
	private String positionInfoId;//位置信息ID
	private int overspeed;//超速 0-否 1-是
	private int suddenTurn;//急转弯 0-否 1-是
	private int 	suddenUpSpeed;//急加速 0-否 1-是
	private int 	suddenDownSpeed;//急减速 0-否 1-是
	private int 	suddenBrake;//急刹车 0-否 1-是
	private int 	suddenChangeRoad;//急变道 0-否 1-是
	private int 	speedNotMatch;//车速转速不匹配 0-否 1-是
	private int 	longLowSpeed;//长怠速 0-否 1-是
	private Date time;// 发生时间
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPositionInfoId() {
		return positionInfoId;
	}
	public void setPositionInfoId(String positionInfoId) {
		this.positionInfoId = positionInfoId;
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
	public PositionDriveInfo(String id) {
		super();
		this.id = id;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public PositionDriveInfo(String id, String positionInfoId, int overspeed,
			int suddenTurn, int suddenUpSpeed, int suddenDownSpeed,
			int suddenBrake, int suddenChangeRoad, int speedNotMatch,
			int longLowSpeed, Date time) {
		super();
		this.id = id;
		this.positionInfoId = positionInfoId;
		this.overspeed = overspeed;
		this.suddenTurn = suddenTurn;
		this.suddenUpSpeed = suddenUpSpeed;
		this.suddenDownSpeed = suddenDownSpeed;
		this.suddenBrake = suddenBrake;
		this.suddenChangeRoad = suddenChangeRoad;
		this.speedNotMatch = speedNotMatch;
		this.longLowSpeed = longLowSpeed;
		this.time = time;
	}
	public PositionDriveInfo() {
		super();
	}
	
}
