package com.hgsoft.carowner.entity;

import java.io.Serializable;
import java.util.Date;
/**
 * 位置信息表-新协议
 * @author sujunguang
 * 2015年12月29日
 * 上午10:24:31
 */
public class PositionInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;	//时间戳13位+5位随机数[a-z0-9]
	private String obdSn;	//智能盒激活码（SN码
	private Date insertTime; //创建时间
	private Integer statusGPS;//定位是否成功：0-失败 1-成功
	private Integer type;//定位类型：1-GPS 2-基站
	private Integer timeType;//时间类型	0-GPS时间 1-系统时间
	private Date time;	//时间
	private Integer longitudeType	; //经度类型 东经-1 西经-0
	private String longitude;	//经度
	private Integer latitudeType;	//北纬-1 南纬-0
	private String latitude;//	纬度
	private Integer directionAngle;//方向角
	private Integer gpsSpeed	;//Gps速度
	private Integer obdSpeed;//Obd速度
	private Integer engineTurns;//发动机转数
	private Integer engineTemperature;	//发动机水温
    private String satelliteSys;//卫星系统
    private Integer satellites;//卫星个数
    private Integer satelliteStrength;//卫星强度
	private String netType;	//网络类型
	private String mcc;//	移动用户所属国家代码,（中国——460）
	private String mnc;//移动网号码，（中国移动——00，中国联通——01）
	private String sid;	//系统识别码，每个地级市只有一个sid，是唯一的
	private String nid;	//网络识别码，由各本地网管理，也就是由地级分公司分配。每个地级市可能有1到3个nid
	private String bid;	//是网络中的某一个小区，可以理解为基站
	private String oprecision;//CDMA定位精度
	
	
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


	public Date getInsertTime() {
		return insertTime;
	}


	public void setInsertTime(Date insertTime) {
		this.insertTime = insertTime;
	}


	public Integer getStatusGPS() {
		return statusGPS;
	}


	public void setStatusGPS(Integer statusGPS) {
		this.statusGPS = statusGPS;
	}


	public Integer getType() {
		return type;
	}


	public void setType(Integer type) {
		this.type = type;
	}


	public Integer getTimeType() {
		return timeType;
	}


	public void setTimeType(Integer timeType) {
		this.timeType = timeType;
	}


	public Date getTime() {
		return time;
	}


	public void setTime(Date time) {
		this.time = time;
	}


	public Integer getLongitudeType() {
		return longitudeType;
	}


	public void setLongitudeType(Integer longitudeType) {
		this.longitudeType = longitudeType;
	}


	public String getLongitude() {
		return longitude;
	}


	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}


	public Integer getLatitudeType() {
		return latitudeType;
	}


	public void setLatitudeType(Integer latitudeType) {
		this.latitudeType = latitudeType;
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


	public Integer getEngineTurns() {
		return engineTurns;
	}


	public void setEngineTurns(Integer engineTurns) {
		this.engineTurns = engineTurns;
	}


	public Integer getEngineTemperature() {
		return engineTemperature;
	}


	public void setEngineTemperature(Integer engineTemperature) {
		this.engineTemperature = engineTemperature;
	}


	public String getSatelliteSys() {
		return satelliteSys;
	}


	public void setSatelliteSys(String satelliteSys) {
		this.satelliteSys = satelliteSys;
	}


	public Integer getSatellites() {
		return satellites;
	}


	public void setSatellites(Integer satellites) {
		this.satellites = satellites;
	}


	public Integer getSatelliteStrength() {
		return satelliteStrength;
	}


	public void setSatelliteStrength(Integer satelliteStrength) {
		this.satelliteStrength = satelliteStrength;
	}


	public String getNetType() {
		return netType;
	}


	public void setNetType(String netType) {
		this.netType = netType;
	}


	public String getMcc() {
		return mcc;
	}


	public void setMcc(String mcc) {
		this.mcc = mcc;
	}


	public String getMnc() {
		return mnc;
	}


	public void setMnc(String mnc) {
		this.mnc = mnc;
	}


	public String getSid() {
		return sid;
	}


	public void setSid(String sid) {
		this.sid = sid;
	}


	public String getNid() {
		return nid;
	}


	public void setNid(String nid) {
		this.nid = nid;
	}


	public String getBid() {
		return bid;
	}


	public void setBid(String bid) {
		this.bid = bid;
	}


	public String getOprecision() {
		return oprecision;
	}


	public void setOprecision(String oprecision) {
		this.oprecision = oprecision;
	}


	@Override
	public String toString() {
		return "PositionInfo [id=" + id + ", obdSn=" + obdSn + ", insertTime="
				+ insertTime + ", statusGPS=" + statusGPS + ", type=" + type
				+ ", timeType=" + timeType + ", time=" + time
				+ ", longitudeType=" + longitudeType + ", longitude="
				+ longitude + ", latitudeType=" + latitudeType + ", latitude="
				+ latitude + ", directionAngle=" + directionAngle
				+ ", gpsSpeed=" + gpsSpeed + ", obdSpeed=" + obdSpeed
				+ ", engineTurns=" + engineTurns + ", engineTemperature="
				+ engineTemperature + ", satelliteSys=" + satelliteSys
				+ ", satellites=" + satellites + ", satelliteStrength="
				+ satelliteStrength + ", netType=" + netType + ", mcc=" + mcc
				+ ", mnc=" + mnc + ", sid=" + sid + ", nid=" + nid + ", bid="
				+ bid + ", oprecision=" + oprecision + "]";
	}
	
}
