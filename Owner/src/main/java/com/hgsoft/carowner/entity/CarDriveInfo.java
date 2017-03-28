package com.hgsoft.carowner.entity;

import java.io.Serializable;
import java.util.Date;
/**
 * 车辆驾驶行为信息
 * @author Sjg
 * 2015-8-10
 * 上午11:40:53
 */
public class CarDriveInfo implements Serializable{

	private static final long serialVersionUID = -202609104240545671L;

	private String driveInfoId;//编号
	private String carId;//车辆编号
	private String obdSn;//智能盒激活码（SN码）
	private Integer tmsSpeeding;//超速(次数)
	private Integer tmsFatigue;//疲劳驾驶次数
	private Integer tmsRapDec;//急减速(次数)
	private Integer tmsRapAcc;//急加速(次数)
	private Integer highSpeed;//高转速(次数)
	private Integer tmsSharpTurn;//急转弯(次数)
	private Integer notMatch;//车速转速不匹配(次数)
	private Integer longLowSpeed;//长时间怠速(次数)
	private Integer tmsBrakes;//急刹车(次数)
	private Integer tmsSteep;//急变道(次数)
	private Double avSpeed;//车速 km/h
	private Date driveDate;//时间yyyy-MM-dd HH:mm:ss
	private Integer type;//0-正常行程驾驶行为	1-半条行程驾驶行为	2-半条行程失效驾驶行为
	/**
	 * @return the driveInfoId
	 */
	public String getDriveInfoId() {
		return driveInfoId;
	}
	/**
	 * @param driveInfoId the driveInfoId to set
	 */
	public void setDriveInfoId(String driveInfoId) {
		this.driveInfoId = driveInfoId;
	}
	/**
	 * @return the carId
	 */
	public String getCarId() {
		return carId;
	}
	/**
	 * @param carId the carId to set
	 */
	public void setCarId(String carId) {
		this.carId = carId;
	}
	/**
	 * @return the obdSn
	 */
	public String getObdSn() {
		return obdSn;
	}
	/**
	 * @param obdSn the obdSn to set
	 */
	public void setObdSn(String obdSn) {
		this.obdSn = obdSn;
	}
	/**
	 * @return the tmsSpeeding
	 */
	public Integer getTmsSpeeding() {
		return tmsSpeeding;
	}
	/**
	 * @param tmsSpeeding the tmsSpeeding to set
	 */
	public void setTmsSpeeding(Integer tmsSpeeding) {
		this.tmsSpeeding = tmsSpeeding;
	}
	/**
	 * @return the tmsRapDec
	 */
	public Integer getTmsRapDec() {
		return tmsRapDec;
	}
	/**
	 * @param tmsRapDec the tmsRapDec to set
	 */
	public void setTmsRapDec(Integer tmsRapDec) {
		this.tmsRapDec = tmsRapDec;
	}
	/**
	 * @return the tmsRapAcc
	 */
	public Integer getTmsRapAcc() {
		return tmsRapAcc;
	}
	/**
	 * @param tmsRapAcc the tmsRapAcc to set
	 */
	public void setTmsRapAcc(Integer tmsRapAcc) {
		this.tmsRapAcc = tmsRapAcc;
	}
	/**
	 * @return the highSpeed
	 */
	public Integer getHighSpeed() {
		return highSpeed;
	}
	/**
	 * @param highSpeed the highSpeed to set
	 */
	public void setHighSpeed(Integer highSpeed) {
		this.highSpeed = highSpeed;
	}
	/**
	 * @return the tmsSharpTurn
	 */
	public Integer getTmsSharpTurn() {
		return tmsSharpTurn;
	}
	/**
	 * @param tmsSharpTurn the tmsSharpTurn to set
	 */
	public void setTmsSharpTurn(Integer tmsSharpTurn) {
		this.tmsSharpTurn = tmsSharpTurn;
	}
	/**
	 * @return the notMatch
	 */
	public Integer getNotMatch() {
		return notMatch;
	}
	/**
	 * @param notMatch the notMatch to set
	 */
	public void setNotMatch(Integer notMatch) {
		this.notMatch = notMatch;
	}
	/**
	 * @return the longLowSpeed
	 */
	public Integer getLongLowSpeed() {
		return longLowSpeed;
	}
	/**
	 * @param longLowSpeed the longLowSpeed to set
	 */
	public void setLongLowSpeed(Integer longLowSpeed) {
		this.longLowSpeed = longLowSpeed;
	}
	/**
	 * @return the tmsBrakes
	 */
	public Integer getTmsBrakes() {
		return tmsBrakes;
	}
	/**
	 * @param tmsBrakes the tmsBrakes to set
	 */
	public void setTmsBrakes(Integer tmsBrakes) {
		this.tmsBrakes = tmsBrakes;
	}
	/**
	 * @return the tmsSteep
	 */
	public Integer getTmsSteep() {
		return tmsSteep;
	}
	/**
	 * @param tmsSteep the tmsSteep to set
	 */
	public void setTmsSteep(Integer tmsSteep) {
		this.tmsSteep = tmsSteep;
	}
	/**
	 * @return the avSpeed
	 */
	public Double getAvSpeed() {
		return avSpeed;
	}
	/**
	 * @param avSpeed the avSpeed to set
	 */
	public void setAvSpeed(Double avSpeed) {
		this.avSpeed = avSpeed;
	}
	/**
	 * @return the driveDate
	 */
	public Date getDriveDate() {
		return driveDate;
	}
	/**
	 * @param driveDate the driveDate to set
	 */
	public void setDriveDate(Date driveDate) {
		this.driveDate = driveDate;
	}
	/**
	 * @return the tmsFatigue
	 */
	public Integer getTmsFatigue() {
		return tmsFatigue;
	}
	/**
	 * @param tmsFatigue the tmsFatigue to set
	 */
	public void setTmsFatigue(Integer tmsFatigue) {
		this.tmsFatigue = tmsFatigue;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	
	
}
