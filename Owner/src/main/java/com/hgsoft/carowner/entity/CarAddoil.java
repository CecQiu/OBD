package com.hgsoft.carowner.entity;
// Generated 2015-8-27 14:33:49 by Hibernate Tools 3.4.0.CR1

/**
 * CarAddoil generated by hbm2java
 */
public class CarAddoil implements java.io.Serializable {

	private String id;
	private String carId;
	private String obdSn;
	private String gasStation;
	private String gasStationAdd;
	private String oilType;
	private String fee;
	private String oilNum;
	private String total;
	private String yname;
	private String addTime;

	public CarAddoil() {
	}

	public CarAddoil(String id) {
		this.id = id;
	}

	public CarAddoil(String id, String carId, String obdSn, String gasStation,
			String gasStationAdd, String oilType, String fee, String oilNum,
			String total, String yname, String addTime) {
		this.id = id;
		this.carId = carId;
		this.obdSn = obdSn;
		this.gasStation = gasStation;
		this.gasStationAdd = gasStationAdd;
		this.oilType = oilType;
		this.fee = fee;
		this.oilNum = oilNum;
		this.total = total;
		this.yname = yname;
		this.addTime = addTime;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCarId() {
		return this.carId;
	}

	public void setCarId(String carId) {
		this.carId = carId;
	}

	public String getObdSn() {
		return this.obdSn;
	}

	public void setObdSn(String obdSn) {
		this.obdSn = obdSn;
	}

	public String getGasStation() {
		return this.gasStation;
	}

	public void setGasStation(String gasStation) {
		this.gasStation = gasStation;
	}

	public String getGasStationAdd() {
		return this.gasStationAdd;
	}

	public void setGasStationAdd(String gasStationAdd) {
		this.gasStationAdd = gasStationAdd;
	}

	public String getOilType() {
		return this.oilType;
	}

	public void setOilType(String oilType) {
		this.oilType = oilType;
	}

	public String getFee() {
		return this.fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}

	public String getOilNum() {
		return this.oilNum;
	}

	public void setOilNum(String oilNum) {
		this.oilNum = oilNum;
	}

	public String getTotal() {
		return this.total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getYname() {
		return this.yname;
	}

	public void setYname(String yname) {
		this.yname = yname;
	}

	public String getAddTime() {
		return this.addTime;
	}

	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}

}
