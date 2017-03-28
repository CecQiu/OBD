package com.hgsoft.carowner.entity;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 位置信息类
 * @author fdf
 */
public class PositionalInformation implements java.io.Serializable {

	private static final long serialVersionUID = 9032058277886161819L;
	
	// Fields
	private String id;
	private String obdsn;
	private Date insesrtTime;
	private Integer type;
	private Date gpsTime;
	private Integer warnSos;
	private Integer warnOverspeed;
	private Integer warnStation;
	private Integer warnBrown;
	private Integer warnOvervoltage;
	private Integer warnInRectangular;
	private Integer warnOutRectangular;
	private Integer warnCollision;
	private Integer warnMaintenance;
	private Integer warnWaterTemperature;
	private Integer warnVin;
	private Integer warnFailureSerious;
	private Integer warnShock;
	private Integer warnHold1;
	private Integer warnHold2;
	private Integer warnHold3;
	private Integer statusAcc;
	private Integer statusGps;
	private Integer statusLatitude;
	private Integer statusLongitude;
	private Integer statusFatigue;
	private Integer statusMalfunction;
	private Integer statusPosition;
	private Integer statusPlug;
	private Integer satellites;
	private String longitude;
	private String latitude;
	private Integer directionAngle;
	private Integer gpsSpeed;
	private Integer obdSpeed;
	private Integer engineTemperature;
	private String additionalInfo;
	private String gsmStationInformation;
	private String mcc;
	private String mnc;
	private String sid;
	private String nid;
	private String bid;
	private String oprecision;
	
	@Override
	public String toString() {
		String str = "";
		if(type == null || type < 1 || type > 4) {
			str = "错误的位置信息状态！";
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String content = "id=["+id+"],obdsn=["+obdsn+"],insesrtTime=["+sdf.format(insesrtTime)+"],type=["+type+"],gpsTime=["+
					sdf.format(gpsTime)+"],warnSos=["+warnSos+"],warnOverspeed=["+warnOverspeed+"],warnStation=["+warnStation+
					"],warnBrown=["+warnBrown+"],warnOvervoltage=["+warnOvervoltage+"],warnInRectangular=["+warnInRectangular+
					"],warnOutRectangular=["+warnOutRectangular+"],warnCollision=["+warnCollision+"],warnMaintenance=["+warnMaintenance+
					"],warnWaterTemperature=["+warnWaterTemperature+"],warnVin=["+warnVin+"],warnFailureSerious=["+warnFailureSerious+
					"],warnShock=["+warnShock+"],warnHold1=["+warnHold1+"],warnHold2=["+warnHold2+"],warnHold3=["+warnHold3+
					"],statusAcc=["+statusAcc+"],statusGps=["+statusGps+"],statusLatitude=["+statusLatitude+"],statusLongitude=["+
					statusLongitude+"],statusFatigue=["+statusFatigue+"],statusMalfunction=["+statusMalfunction+"],statusPosition=["+
					statusPosition+"],statusPlug=["+statusPlug+"],obdSpeed=["+obdSpeed+"],engineTemperature=["+engineTemperature+
					"],additionalInfo=["+additionalInfo+"],";
			switch(type) {
			case 1:
				str = "位置信息1:GPS定位时，使用GPS定位数据。";
				str = str + content + "satellites=["+satellites+"],longitude=["+longitude+"],latitude=["+latitude+
						"],directionAngle=["+directionAngle+"],gpsSpeed=["+gpsSpeed+"]";
				break;
			case 2:
				str = "位置信息2:GPS不定位时,使用基站信息定位,且基站信息为7个基站Cellid。";
				str = str + content + "gsmStationInformation=["+gsmStationInformation+"]";
				break;
			case 3:
				str = "位置信息3:GPS不定位时,使用基站信息定位，且基站信息为 celiid+lac。";
				str = str + content + "gsmStationInformation=["+gsmStationInformation+"],mcc=["+mcc+"],mnc=["+mnc+"]";
				break;
			case 4:
				str = "位置信息4:GPS不定位时,使用CDMA基站信息定位，且基站信息为 sid+nid+bid+mcc+mnc。";
				str = str + content + "gsmStationInformation=["+gsmStationInformation+
						"],mcc=["+mcc+"],mnc=["+mnc+"],sid=["+sid+"],nid=["+nid+"],bid=["+bid+"],oprecision=["+oprecision+"]";
				break;
			}
		}
		return "PositionalInformation--->" + str;
	}

	// Constructors

	/** default constructor */
	public PositionalInformation() {
	}

	/** minimal constructor */
	public PositionalInformation(String id) {
		this.id = id;
	}

	/** full constructor */
	public PositionalInformation(String id, String obdsn, Timestamp insesrtTime,
			Integer type, Timestamp gpsTime, Integer warnSos,
			Integer warnOverspeed, Integer warnStation, Integer warnBrown,
			Integer warnOvervoltage, Integer warnInRectangular,
			Integer warnOutRectangular, Integer warnCollision,
			Integer warnMaintenance, Integer warnWaterTemperature,
			Integer warnVin, Integer warnFailureSerious, Integer warnShock,
			Integer warnHold1, Integer warnHold2, Integer warnHold3,
			Integer statusAcc, Integer statusGps, Integer statusLatitude,
			Integer statusLongitude, Integer statusFatigue,
			Integer statusMalfunction, Integer statusPosition,
			Integer statusPlug, Integer satellites, String longitude,
			String latitude, Integer directionAngle, Integer gpsSpeed,
			Integer obdSpeed, Integer engineTemperature, String additionalInfo,
			String gsmStationInformation, String mcc, String mnc,
			String sid,String nid,String bid,String oprecision) {
		this.id = id;
		this.obdsn = obdsn;
		this.insesrtTime = insesrtTime;
		this.type = type;
		this.gpsTime = gpsTime;
		this.warnSos = warnSos;
		this.warnOverspeed = warnOverspeed;
		this.warnStation = warnStation;
		this.warnBrown = warnBrown;
		this.warnOvervoltage = warnOvervoltage;
		this.warnInRectangular = warnInRectangular;
		this.warnOutRectangular = warnOutRectangular;
		this.warnCollision = warnCollision;
		this.warnMaintenance = warnMaintenance;
		this.warnWaterTemperature = warnWaterTemperature;
		this.warnVin = warnVin;
		this.warnFailureSerious = warnFailureSerious;
		this.warnShock = warnShock;
		this.warnHold1 = warnHold1;
		this.warnHold2 = warnHold2;
		this.warnHold3 = warnHold3;
		this.statusAcc = statusAcc;
		this.statusGps = statusGps;
		this.statusLatitude = statusLatitude;
		this.statusLongitude = statusLongitude;
		this.statusFatigue = statusFatigue;
		this.statusMalfunction = statusMalfunction;
		this.statusPosition = statusPosition;
		this.statusPlug = statusPlug;
		this.satellites = satellites;
		this.longitude = longitude;
		this.latitude = latitude;
		this.directionAngle = directionAngle;
		this.gpsSpeed = gpsSpeed;
		this.obdSpeed = obdSpeed;
		this.engineTemperature = engineTemperature;
		this.additionalInfo = additionalInfo;
		this.gsmStationInformation = gsmStationInformation;
		this.mcc = mcc;
		this.mnc = mnc;
		this.sid = sid;
		this.nid = nid;
		this.bid = bid;
		this.oprecision = oprecision;
	}

	// Property accessors

	public String getId() {
		return this.id;
	}

	public String getGsmStationInformation() {
		return gsmStationInformation;
	}

	public void setGsmStationInformation(String gsmStationInformation) {
		this.gsmStationInformation = gsmStationInformation;
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

	public String getObdsn() {
		return obdsn;
	}

	public void setObdsn(String obdsn) {
		this.obdsn = obdsn;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getInsesrtTime() {
		return this.insesrtTime;
	}

	public void setInsesrtTime(Date insesrtTime) {
		this.insesrtTime = insesrtTime;
	}

	public Integer getType() {
		return this.type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Date getGpsTime() {
		return this.gpsTime;
	}

	public void setGpsTime(Date gpsTime) {
		this.gpsTime = gpsTime;
	}

	public Integer getWarnSos() {
		return this.warnSos;
	}

	public void setWarnSos(Integer warnSos) {
		this.warnSos = warnSos;
	}

	public Integer getWarnOverspeed() {
		return this.warnOverspeed;
	}

	public void setWarnOverspeed(Integer warnOverspeed) {
		this.warnOverspeed = warnOverspeed;
	}

	public Integer getWarnStation() {
		return this.warnStation;
	}

	public void setWarnStation(Integer warnStation) {
		this.warnStation = warnStation;
	}

	public Integer getWarnBrown() {
		return this.warnBrown;
	}

	public void setWarnBrown(Integer warnBrown) {
		this.warnBrown = warnBrown;
	}

	public Integer getWarnOvervoltage() {
		return this.warnOvervoltage;
	}

	public void setWarnOvervoltage(Integer warnOvervoltage) {
		this.warnOvervoltage = warnOvervoltage;
	}

	public Integer getWarnInRectangular() {
		return this.warnInRectangular;
	}

	public void setWarnInRectangular(Integer warnInRectangular) {
		this.warnInRectangular = warnInRectangular;
	}

	public Integer getWarnOutRectangular() {
		return this.warnOutRectangular;
	}

	public void setWarnOutRectangular(Integer warnOutRectangular) {
		this.warnOutRectangular = warnOutRectangular;
	}

	public Integer getWarnCollision() {
		return this.warnCollision;
	}

	public void setWarnCollision(Integer warnCollision) {
		this.warnCollision = warnCollision;
	}

	public Integer getWarnMaintenance() {
		return this.warnMaintenance;
	}

	public void setWarnMaintenance(Integer warnMaintenance) {
		this.warnMaintenance = warnMaintenance;
	}

	public Integer getWarnWaterTemperature() {
		return this.warnWaterTemperature;
	}

	public void setWarnWaterTemperature(Integer warnWaterTemperature) {
		this.warnWaterTemperature = warnWaterTemperature;
	}

	public Integer getWarnVin() {
		return this.warnVin;
	}

	public void setWarnVin(Integer warnVin) {
		this.warnVin = warnVin;
	}

	public Integer getWarnFailureSerious() {
		return this.warnFailureSerious;
	}

	public void setWarnFailureSerious(Integer warnFailureSerious) {
		this.warnFailureSerious = warnFailureSerious;
	}

	public Integer getWarnShock() {
		return this.warnShock;
	}

	public void setWarnShock(Integer warnShock) {
		this.warnShock = warnShock;
	}

	public Integer getWarnHold1() {
		return this.warnHold1;
	}

	public void setWarnHold1(Integer warnHold1) {
		this.warnHold1 = warnHold1;
	}

	public Integer getWarnHold2() {
		return this.warnHold2;
	}

	public void setWarnHold2(Integer warnHold2) {
		this.warnHold2 = warnHold2;
	}

	public Integer getWarnHold3() {
		return this.warnHold3;
	}

	public void setWarnHold3(Integer warnHold3) {
		this.warnHold3 = warnHold3;
	}

	public Integer getStatusAcc() {
		return this.statusAcc;
	}

	public void setStatusAcc(Integer statusAcc) {
		this.statusAcc = statusAcc;
	}

	public Integer getStatusGps() {
		return this.statusGps;
	}

	public void setStatusGps(Integer statusGps) {
		this.statusGps = statusGps;
	}

	public Integer getStatusLatitude() {
		return this.statusLatitude;
	}

	public void setStatusLatitude(Integer statusLatitude) {
		this.statusLatitude = statusLatitude;
	}

	public Integer getStatusLongitude() {
		return this.statusLongitude;
	}

	public void setStatusLongitude(Integer statusLongitude) {
		this.statusLongitude = statusLongitude;
	}

	public Integer getStatusFatigue() {
		return this.statusFatigue;
	}

	public void setStatusFatigue(Integer statusFatigue) {
		this.statusFatigue = statusFatigue;
	}

	public Integer getStatusMalfunction() {
		return this.statusMalfunction;
	}

	public void setStatusMalfunction(Integer statusMalfunction) {
		this.statusMalfunction = statusMalfunction;
	}

	public Integer getStatusPosition() {
		return this.statusPosition;
	}

	public void setStatusPosition(Integer statusPosition) {
		this.statusPosition = statusPosition;
	}

	public Integer getStatusPlug() {
		return this.statusPlug;
	}

	public void setStatusPlug(Integer statusPlug) {
		this.statusPlug = statusPlug;
	}

	public Integer getSatellites() {
		return this.satellites;
	}

	public void setSatellites(Integer satellites) {
		this.satellites = satellites;
	}

	public String getLongitude() {
		return this.longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return this.latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public Integer getDirectionAngle() {
		return this.directionAngle;
	}

	public void setDirectionAngle(Integer directionAngle) {
		this.directionAngle = directionAngle;
	}

	public Integer getGpsSpeed() {
		return this.gpsSpeed;
	}

	public void setGpsSpeed(Integer gpsSpeed) {
		this.gpsSpeed = gpsSpeed;
	}

	public Integer getObdSpeed() {
		return this.obdSpeed;
	}

	public void setObdSpeed(Integer obdSpeed) {
		this.obdSpeed = obdSpeed;
	}

	public Integer getEngineTemperature() {
		return this.engineTemperature;
	}

	public void setEngineTemperature(Integer engineTemperature) {
		this.engineTemperature = engineTemperature;
	}

	public String getAdditionalInfo() {
		return this.additionalInfo;
	}

	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
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

	
}