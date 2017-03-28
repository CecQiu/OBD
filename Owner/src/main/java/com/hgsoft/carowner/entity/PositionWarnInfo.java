package com.hgsoft.carowner.entity;

import java.io.Serializable;
import java.util.Date;
/**
 * 位置-报警信息表(Position_Warn_Info，新协议)
 * @author sujunguang
 * 2015年12月29日
 * 上午10:34:43
 */
public class PositionWarnInfo implements Serializable{

	public PositionWarnInfo(String id) {
		super();
		this.id = id;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = -6211285826227146739L;
	private String id; //主键:时间戳13位+5位随机数[a-z0-9]
	private String positionInfoId	;//位置信息ID
	private int illegalStartUp	;//非法启动报警 0-否 1-是
	private int illegalShake	;//非法震动报警 0-否 1-是
	private int batteryLow	;//蓄电池低压报警 0-否 1-是
	private int batteryUp	;//蓄电池高压报警 0-否 1-是
	private int engineTemperature	;//发动机水温高报警 0-否 1-是
	private int vehicleFailure	;//车辆故障报警 0-否 1-是
	private int overSpeed	;//超速报警 0-否 1-是
	private int elecFenceOut	;//电子围栏驶出报警 0-否 1-是
	private int elecFenceIn	;//电子围栏进入报警 0-否 1-是
	private int vehicleSideDown	;//车辆侧翻报警 0-否 1-是
	private int vehicleCrash	;//车辆碰撞报警 0-否 1-是
	private int ecu	;//ECU通信模块报警 0-否 1-是
	private int gps	;//GPS模块故障报警 0-否 1-是
	private int eeprom	;//EEPROM故障报警 0-否 1-是
	private int accelerateSensor	;//加速度传感器故障 0-否 1-是
	private int offData	;//离线数据提醒 0-否 1-是
	private Date time;// 发生时间
	public PositionWarnInfo(String id, String positionInfoId,
			int illegalStartUp, int illegalShake, int batteryLow,
			int batteryUp, int engineTemperature, int vehicleFailure,
			int overSpeed, int elecFenceOut, int elecFenceIn,
			int vehicleSideDown, int vehicleCrash, int ecu, int gps,
			int eeprom, int accelerateSensor, int offData, Date time) {
		super();
		this.id = id;
		this.positionInfoId = positionInfoId;
		this.illegalStartUp = illegalStartUp;
		this.illegalShake = illegalShake;
		this.batteryLow = batteryLow;
		this.batteryUp = batteryUp;
		this.engineTemperature = engineTemperature;
		this.vehicleFailure = vehicleFailure;
		this.overSpeed = overSpeed;
		this.elecFenceOut = elecFenceOut;
		this.elecFenceIn = elecFenceIn;
		this.vehicleSideDown = vehicleSideDown;
		this.vehicleCrash = vehicleCrash;
		this.ecu = ecu;
		this.gps = gps;
		this.eeprom = eeprom;
		this.accelerateSensor = accelerateSensor;
		this.offData = offData;
		this.time = time;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
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
	public int getIllegalStartUp() {
		return illegalStartUp;
	}
	public void setIllegalStartUp(int illegalStartUp) {
		this.illegalStartUp = illegalStartUp;
	}
	public int getIllegalShake() {
		return illegalShake;
	}
	public void setIllegalShake(int illegalShake) {
		this.illegalShake = illegalShake;
	}
	public int getBatteryLow() {
		return batteryLow;
	}
	public void setBatteryLow(int batteryLow) {
		this.batteryLow = batteryLow;
	}
	public int getBatteryUp() {
		return batteryUp;
	}
	public void setBatteryUp(int batteryUp) {
		this.batteryUp = batteryUp;
	}
	public int getEngineTemperature() {
		return engineTemperature;
	}
	public void setEngineTemperature(int engineTemperature) {
		this.engineTemperature = engineTemperature;
	}
	public int getVehicleFailure() {
		return vehicleFailure;
	}
	public void setVehicleFailure(int vehicleFailure) {
		this.vehicleFailure = vehicleFailure;
	}
	public int getOverSpeed() {
		return overSpeed;
	}
	public void setOverSpeed(int overSpeed) {
		this.overSpeed = overSpeed;
	}
	public int getElecFenceOut() {
		return elecFenceOut;
	}
	public void setElecFenceOut(int elecFenceOut) {
		this.elecFenceOut = elecFenceOut;
	}
	public int getElecFenceIn() {
		return elecFenceIn;
	}
	public void setElecFenceIn(int elecFenceIn) {
		this.elecFenceIn = elecFenceIn;
	}
	public int getVehicleSideDown() {
		return vehicleSideDown;
	}
	public void setVehicleSideDown(int vehicleSideDown) {
		this.vehicleSideDown = vehicleSideDown;
	}
	public int getVehicleCrash() {
		return vehicleCrash;
	}
	public void setVehicleCrash(int vehicleCrash) {
		this.vehicleCrash = vehicleCrash;
	}
	public int getEcu() {
		return ecu;
	}
	public void setEcu(int ecu) {
		this.ecu = ecu;
	}
	public int getGps() {
		return gps;
	}
	public void setGps(int gps) {
		this.gps = gps;
	}
	public int getEeprom() {
		return eeprom;
	}
	public void setEeprom(int eeprom) {
		this.eeprom = eeprom;
	}
	public int getAccelerateSensor() {
		return accelerateSensor;
	}
	public void setAccelerateSensor(int accelerateSensor) {
		this.accelerateSensor = accelerateSensor;
	}
	public int getOffData() {
		return offData;
	}
	public void setOffData(int offData) {
		this.offData = offData;
	}
	public PositionWarnInfo() {
		super();
	}
	
}
