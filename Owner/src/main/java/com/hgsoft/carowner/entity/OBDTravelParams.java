package com.hgsoft.carowner.entity;

import java.io.Serializable;
import java.util.Date;
/**
 * OBD行程参数
 * @author sujunguang
 * 2016年1月14日
 * 下午2:39:30
 */
public class OBDTravelParams implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1227241453770924149L;
	
	private String id;
	private String obdSn;
	private Integer batteryLow;  //蓄电池电压低阈值
	private Integer batteryHigh;//	蓄电池电压高阈值
	private Integer overSpeed;// 超速阈值：时速阈值km/h
	private Integer limitSpeedLazy;// 超速阈值：限速延迟时间阈值s
	private Integer shuddenTurnSpeed;// 急转弯阈值：速度阈值 km/h
	private Integer shuddenTurnAngle;// 急转弯阈值：角度阈值 度
	private Integer shuddenOverSpeed;// 急加速阈值：速度变化阈值km/h
	private Integer shuddenOverSpeedTime;// 急加速阈值：	时间阈值
	private Integer shuddenLowSpeed;// 急减速阈值：	速度变化阈值km/h
	private Integer shuddenLowSpeedTime;// 急减速阈值：	时间阈值
	private Integer shuddenChangeAngle	;// 急变道阈值：	角度阈值 km/h
	private Integer shuddenChangeTime	;// 急变道阈值：	时间阈值
	private Integer engineLowTemperature	;// 发动机水温报警阈值：	低 摄氏度
	private Integer engineHighTemperature	;// 发动机水温报警阈值：	高 摄氏度
	private Integer engineTurnsWarn	;// 发动机转数报警阈值 转/分钟
	private Integer speedNotMatch;// 车速转速不匹配阈值：	速度30km/h时匹配转速(转/分钟)
	private Integer speedNotMatchStep;// 车速转速不匹配阈值：	转速步进值	转/分钟
	private Integer longLowSpeed;// 长怠速阈值：	怠速车速阈值	km/h
	private Integer longLowSpeedTime;// 长怠速阈值：时间阈值 1分钟
	private Integer shuddenBrakeStrength;// 急刹车强度阈值
	private Integer sideTurnAngle;// 侧翻角度阈值
	private Integer crashStrength;// 碰撞强度阈值
	private Integer shockStrength;// 震动报警强度阈值
	private Integer fatigueDrive;//  疲劳驾驶连续驾驶时间,单位小时
	private Integer fatigueSleep;// 疲劳驾驶休息时间,单位分
	private String fenceInLongtitude;// 电子围栏驶出报警坐标：经度
	private String fenceInLatitude;// 电子围栏驶出报警坐标：纬度
	private String fenceOutLongtitude;// 电子围栏驶入报警坐标：经度
	private String fenceOutLatitude;// 电子围栏驶入报警坐标：纬度
	private Date createTime;// 创建时间
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
	public Integer getBatteryLow() {
		return batteryLow;
	}
	public void setBatteryLow(Integer batteryLow) {
		this.batteryLow = batteryLow;
	}
	public Integer getBatteryHigh() {
		return batteryHigh;
	}
	public void setBatteryHigh(Integer batteryHigh) {
		this.batteryHigh = batteryHigh;
	}
	public Integer getOverSpeed() {
		return overSpeed;
	}
	public void setOverSpeed(Integer overSpeed) {
		this.overSpeed = overSpeed;
	}
	public Integer getLimitSpeedLazy() {
		return limitSpeedLazy;
	}
	public void setLimitSpeedLazy(Integer limitSpeedLazy) {
		this.limitSpeedLazy = limitSpeedLazy;
	}
	public Integer getShuddenTurnSpeed() {
		return shuddenTurnSpeed;
	}
	public void setShuddenTurnSpeed(Integer shuddenTurnSpeed) {
		this.shuddenTurnSpeed = shuddenTurnSpeed;
	}
	public Integer getShuddenTurnAngle() {
		return shuddenTurnAngle;
	}
	public void setShuddenTurnAngle(Integer shuddenTurnAngle) {
		this.shuddenTurnAngle = shuddenTurnAngle;
	}
	public Integer getShuddenOverSpeed() {
		return shuddenOverSpeed;
	}
	public void setShuddenOverSpeed(Integer shuddenOverSpeed) {
		this.shuddenOverSpeed = shuddenOverSpeed;
	}
	public Integer getShuddenOverSpeedTime() {
		return shuddenOverSpeedTime;
	}
	public void setShuddenOverSpeedTime(Integer shuddenOverSpeedTime) {
		this.shuddenOverSpeedTime = shuddenOverSpeedTime;
	}
	public Integer getShuddenLowSpeed() {
		return shuddenLowSpeed;
	}
	public void setShuddenLowSpeed(Integer shuddenLowSpeed) {
		this.shuddenLowSpeed = shuddenLowSpeed;
	}
	public Integer getShuddenLowSpeedTime() {
		return shuddenLowSpeedTime;
	}
	public void setShuddenLowSpeedTime(Integer shuddenLowSpeedTime) {
		this.shuddenLowSpeedTime = shuddenLowSpeedTime;
	}
	public Integer getShuddenChangeAngle() {
		return shuddenChangeAngle;
	}
	public void setShuddenChangeAngle(Integer shuddenChangeAngle) {
		this.shuddenChangeAngle = shuddenChangeAngle;
	}
	public Integer getShuddenChangeTime() {
		return shuddenChangeTime;
	}
	public void setShuddenChangeTime(Integer shuddenChangeTime) {
		this.shuddenChangeTime = shuddenChangeTime;
	}
	public Integer getEngineLowTemperature() {
		return engineLowTemperature;
	}
	public void setEngineLowTemperature(Integer engineLowTemperature) {
		this.engineLowTemperature = engineLowTemperature;
	}
	public Integer getEngineHighTemperature() {
		return engineHighTemperature;
	}
	public void setEngineHighTemperature(Integer engineHighTemperature) {
		this.engineHighTemperature = engineHighTemperature;
	}
	public Integer getEngineTurnsWarn() {
		return engineTurnsWarn;
	}
	public void setEngineTurnsWarn(Integer engineTurnsWarn) {
		this.engineTurnsWarn = engineTurnsWarn;
	}
	public Integer getSpeedNotMatch() {
		return speedNotMatch;
	}
	public void setSpeedNotMatch(Integer speedNotMatch) {
		this.speedNotMatch = speedNotMatch;
	}
	public Integer getSpeedNotMatchStep() {
		return speedNotMatchStep;
	}
	public void setSpeedNotMatchStep(Integer speedNotMatchStep) {
		this.speedNotMatchStep = speedNotMatchStep;
	}
	public Integer getLongLowSpeed() {
		return longLowSpeed;
	}
	public void setLongLowSpeed(Integer longLowSpeed) {
		this.longLowSpeed = longLowSpeed;
	}
	public Integer getLongLowSpeedTime() {
		return longLowSpeedTime;
	}
	public void setLongLowSpeedTime(Integer longLowSpeedTime) {
		this.longLowSpeedTime = longLowSpeedTime;
	}
	public Integer getShuddenBrakeStrength() {
		return shuddenBrakeStrength;
	}
	public void setShuddenBrakeStrength(Integer shuddenBrakeStrength) {
		this.shuddenBrakeStrength = shuddenBrakeStrength;
	}
	public Integer getSideTurnAngle() {
		return sideTurnAngle;
	}
	public void setSideTurnAngle(Integer sideTurnAngle) {
		this.sideTurnAngle = sideTurnAngle;
	}
	public Integer getCrashStrength() {
		return crashStrength;
	}
	public void setCrashStrength(Integer crashStrength) {
		this.crashStrength = crashStrength;
	}
	public Integer getShockStrength() {
		return shockStrength;
	}
	public void setShockStrength(Integer shockStrength) {
		this.shockStrength = shockStrength;
	}
	public String getFenceInLongtitude() {
		return fenceInLongtitude;
	}
	public void setFenceInLongtitude(String fenceInLongtitude) {
		this.fenceInLongtitude = fenceInLongtitude;
	}
	public String getFenceInLatitude() {
		return fenceInLatitude;
	}
	public void setFenceInLatitude(String fenceInLatitude) {
		this.fenceInLatitude = fenceInLatitude;
	}
	public String getFenceOutLongtitude() {
		return fenceOutLongtitude;
	}
	public void setFenceOutLongtitude(String fenceOutLongtitude) {
		this.fenceOutLongtitude = fenceOutLongtitude;
	}
	public String getFenceOutLatitude() {
		return fenceOutLatitude;
	}
	public void setFenceOutLatitude(String fenceOutLatitude) {
		this.fenceOutLatitude = fenceOutLatitude;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public Integer getFatigueDrive() {
		return fatigueDrive;
	}
	public void setFatigueDrive(Integer fatigueDrive) {
		this.fatigueDrive = fatigueDrive;
	}
	public Integer getFatigueSleep() {
		return fatigueSleep;
	}
	public void setFatigueSleep(Integer fatigueSleep) {
		this.fatigueSleep = fatigueSleep;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public OBDTravelParams(Integer batteryLow, Integer batteryHigh,
			Integer overSpeed, Integer limitSpeedLazy, Integer shuddenTurnSpeed,
			Integer shuddenTurnAngle, Integer shuddenOverSpeed,
			Integer shuddenOverSpeedTime, Integer shuddenLowSpeed,
			Integer shuddenLowSpeedTime, Integer shuddenChangeAngle,
			Integer shuddenChangeTime, Integer engineLowTemperature,
			Integer engineHighTemperature, Integer engineTurnsWarn,
			Integer speedNotMatch, Integer speedNotMatchStep,
			Integer longLowSpeed, Integer longLowSpeedTime,
			Integer shuddenBrakeStrength, Integer sideTurnAngle,
			Integer crashStrength, Integer shockStrength,
			Integer fatigueDrive,Integer fatigueSleep,
			String fenceOutLongtitude, String fenceOutLatitude,
			String fenceInLongtitude, String fenceInLatitude) {
		super();
		this.batteryLow = batteryLow;
		this.batteryHigh = batteryHigh;
		this.overSpeed = overSpeed;
		this.limitSpeedLazy = limitSpeedLazy;
		this.shuddenTurnSpeed = shuddenTurnSpeed;
		this.shuddenTurnAngle = shuddenTurnAngle;
		this.shuddenOverSpeed = shuddenOverSpeed;
		this.shuddenOverSpeedTime = shuddenOverSpeedTime;
		this.shuddenLowSpeed = shuddenLowSpeed;
		this.shuddenLowSpeedTime = shuddenLowSpeedTime;
		this.shuddenChangeAngle = shuddenChangeAngle;
		this.shuddenChangeTime = shuddenChangeTime;
		this.engineLowTemperature = engineLowTemperature;
		this.engineHighTemperature = engineHighTemperature;
		this.engineTurnsWarn = engineTurnsWarn;
		this.speedNotMatch = speedNotMatch;
		this.speedNotMatchStep = speedNotMatchStep;
		this.longLowSpeed = longLowSpeed;
		this.longLowSpeedTime = longLowSpeedTime;
		this.shuddenBrakeStrength = shuddenBrakeStrength;
		this.sideTurnAngle = sideTurnAngle;
		this.crashStrength = crashStrength;
		this.shockStrength = shockStrength;
		this.fenceInLongtitude = fenceInLongtitude;
		this.fenceInLatitude = fenceInLatitude;
		this.fenceOutLongtitude = fenceOutLongtitude;
		this.fenceOutLatitude = fenceOutLatitude;
		this.fatigueDrive = fatigueDrive;
		this.fatigueSleep = fatigueSleep;
	}
	public OBDTravelParams() {
		super();
	}
	public OBDTravelParams(String id, String obdSn, Integer batteryLow,
			Integer batteryHigh, Integer overSpeed, Integer limitSpeedLazy,
			Integer shuddenTurnSpeed, Integer shuddenTurnAngle,
			Integer shuddenOverSpeed, Integer shuddenOverSpeedTime,
			Integer shuddenLowSpeed, Integer shuddenLowSpeedTime,
			Integer shuddenChangeAngle, Integer shuddenChangeTime,
			Integer engineLowTemperature, Integer engineHighTemperature,
			Integer engineTurnsWarn, Integer speedNotMatch,
			Integer speedNotMatchStep, Integer longLowSpeed,
			Integer longLowSpeedTime, Integer shuddenBrakeStrength,
			Integer sideTurnAngle, Integer crashStrength, Integer shockStrength,
			String fenceInLongtitude, String fenceInLatitude,
			String fenceOutLongtitude, String fenceOutLatitude, Date createTime) {
		super();
		this.id = id;
		this.obdSn = obdSn;
		this.batteryLow = batteryLow;
		this.batteryHigh = batteryHigh;
		this.overSpeed = overSpeed;
		this.limitSpeedLazy = limitSpeedLazy;
		this.shuddenTurnSpeed = shuddenTurnSpeed;
		this.shuddenTurnAngle = shuddenTurnAngle;
		this.shuddenOverSpeed = shuddenOverSpeed;
		this.shuddenOverSpeedTime = shuddenOverSpeedTime;
		this.shuddenLowSpeed = shuddenLowSpeed;
		this.shuddenLowSpeedTime = shuddenLowSpeedTime;
		this.shuddenChangeAngle = shuddenChangeAngle;
		this.shuddenChangeTime = shuddenChangeTime;
		this.engineLowTemperature = engineLowTemperature;
		this.engineHighTemperature = engineHighTemperature;
		this.engineTurnsWarn = engineTurnsWarn;
		this.speedNotMatch = speedNotMatch;
		this.speedNotMatchStep = speedNotMatchStep;
		this.longLowSpeed = longLowSpeed;
		this.longLowSpeedTime = longLowSpeedTime;
		this.shuddenBrakeStrength = shuddenBrakeStrength;
		this.sideTurnAngle = sideTurnAngle;
		this.crashStrength = crashStrength;
		this.shockStrength = shockStrength;
		this.fenceInLongtitude = fenceInLongtitude;
		this.fenceInLatitude = fenceInLatitude;
		this.fenceOutLongtitude = fenceOutLongtitude;
		this.fenceOutLatitude = fenceOutLatitude;
		this.createTime = createTime;
	}
	@Override
	public String toString() {
		return "OBDTravelParams [id=" + id + ", obdSn=" + obdSn
				+ ", batteryLow=" + batteryLow + ", batteryHigh=" + batteryHigh
				+ ", overSpeed=" + overSpeed + ", limitSpeedLazy="
				+ limitSpeedLazy + ", shuddenTurnSpeed=" + shuddenTurnSpeed
				+ ", shuddenTurnAngle=" + shuddenTurnAngle
				+ ", shuddenOverSpeed=" + shuddenOverSpeed
				+ ", shuddenOverSpeedTime=" + shuddenOverSpeedTime
				+ ", shuddenLowSpeed=" + shuddenLowSpeed
				+ ", shuddenLowSpeedTime=" + shuddenLowSpeedTime
				+ ", shuddenChangeAngle=" + shuddenChangeAngle
				+ ", shuddenChangeTime=" + shuddenChangeTime
				+ ", engineLowTemperature=" + engineLowTemperature
				+ ", engineHighTemperature=" + engineHighTemperature
				+ ", engineTurnsWarn=" + engineTurnsWarn + ", speedNotMatch="
				+ speedNotMatch + ", speedNotMatchStep=" + speedNotMatchStep
				+ ", longLowSpeed=" + longLowSpeed + ", longLowSpeedTime="
				+ longLowSpeedTime + ", shuddenBrakeStrength="
				+ shuddenBrakeStrength + ", sideTurnAngle=" + sideTurnAngle
				+ ", crashStrength=" + crashStrength + ", shockStrength="
				+ shockStrength + ", fenceInLongtitude=" + fenceInLongtitude
				+ ", fenceInLatitude=" + fenceInLatitude
				+ ", fenceOutLongtitude=" + fenceOutLongtitude
				+ ", fenceOutLatitude=" + fenceOutLatitude + ", createTime="
				+ createTime + "]";
	}
	 
}
