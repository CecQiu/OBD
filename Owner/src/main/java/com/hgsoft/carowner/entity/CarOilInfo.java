package com.hgsoft.carowner.entity;

import java.util.Date;

/**
 * 车辆油耗信息
 * @author sujunguang
 * 2015-8-5
 */
public class CarOilInfo implements java.io.Serializable {

	private static final long serialVersionUID = 4136784679113555872L;
	
	private String oilInfoId;//编号
	private String carId;//车辆编号
	private String obdSN;//智能盒激活码（SN码）
	private String petrolConsumeNum;//油耗（升）
	private String mileageNum;//里程（公里）
	private String timeSpanNum;//驾驶时长（秒）
	private Date oilInfoTime;//时间yyyy-MM-dd hh:mm:ss
	private Integer type;//0-正常行程油耗 1-半条行程油耗 2-半条行程油耗失效
	
	public String getOilInfoId() {
		return oilInfoId;
	}
	public void setOilInfoId(String oilInfoId) {
		this.oilInfoId = oilInfoId;
	}
	public String getCarId() {
		return carId;
	}
	public void setCarId(String carId) {
		this.carId = carId;
	}
	public String getObdSN() {
		return obdSN;
	}
	public void setObdSN(String obdSN) {
		this.obdSN = obdSN;
	}
	public String getPetrolConsumeNum() {
		return petrolConsumeNum;
	}
	public void setPetrolConsumeNum(String petrolConsumeNum) {
		this.petrolConsumeNum = petrolConsumeNum;
	}
	public String getMileageNum() {
		return mileageNum;
	}
	public void setMileageNum(String mileageNum) {
		this.mileageNum = mileageNum;
	}
	public String getTimeSpanNum() {
		return timeSpanNum;
	}
	public void setTimeSpanNum(String timeSpanNum) {
		this.timeSpanNum = timeSpanNum;
	}
	public Date getOilInfoTime() {
		return oilInfoTime;
	}
	public void setOilInfoTime(Date oilInfoTime) {
		this.oilInfoTime = oilInfoTime;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}

}
