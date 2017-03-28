package com.hgsoft.carowner.entity;

import java.util.Date;

/**
 * OBD设备库存信息表
 * 
 * @author sujunguang 2015-8-5
 */
public class OBDStockInfo implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private String stockId;// 库存编号
	private String obdId;// OBD编号（PN码）
	private String obdSn;// 智能盒激活码（SN码）
	private String obdMSn;// obd设备号
	private String fourGsimNo;// 流量卡号码
	private String stockType;// 设备激活状态 00:已激活（绑定） 01:待激活（初始）
	private String stockState;// 设备状态 00离线、01在线
	private String regUserId;// 关联用户
	private String startDate;// 设备激活日期YYYY-MM-dd hh:ss:mm
	private String gpsState;// GPS状态 0关闭 1打开
	private String wifiState;// Wifi状态 0关闭 1打开
	private String groupNum;//分组编号
	private Date updateTime;
	private String valid;

	public String getStockId() {
		return stockId;
	}

	public void setStockId(String stockId) {
		this.stockId = stockId;
	}

	public String getObdId() {
		return obdId;
	}

	public void setObdId(String obdId) {
		this.obdId = obdId;
	}

	public String getObdSn() {
		return obdSn;
	}

	public void setObdSn(String obdSn) {
		this.obdSn = obdSn;
	}

	public String getFourGsimNo() {
		return fourGsimNo;
	}

	public void setFourGsimNo(String fourGsimNo) {
		this.fourGsimNo = fourGsimNo;
	}

	public String getStockType() {
		return stockType;
	}

	public void setStockType(String stockType) {
		this.stockType = stockType;
	}

	public String getStockState() {
		return stockState;
	}

	public void setStockState(String stockState) {
		this.stockState = stockState;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getGpsState() {
		return gpsState;
	}

	public void setGpsState(String gpsState) {
		this.gpsState = gpsState;
	}

	public String getWifiState() {
		return wifiState;
	}

	public void setWifiState(String wifiState) {
		this.wifiState = wifiState;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getValid() {
		return valid;
	}

	public void setValid(String valid) {
		this.valid = valid;
	}

	public String getRegUserId() {
		return regUserId;
	}

	public void setRegUserId(String regUserId) {
		this.regUserId = regUserId;
	}

	public String getObdMSn() {
		return obdMSn;
	}

	public void setObdMSn(String obdMSn) {
		this.obdMSn = obdMSn;
	}
	
	public String getGroupNum() {
		return groupNum;
	}

	public void setGroupNum(String groupNum) {
		this.groupNum = groupNum;
	}

	@Override
	public String toString() {
		return "OBDStockInfo [stockId=" + stockId + ", obdId=" + obdId
				+ ", obdSn=" + obdSn + ", obdMSn=" + obdMSn + ", fourGsimNo="
				+ fourGsimNo + ", stockType=" + stockType + ", stockState="
				+ stockState + ", regUserId=" + regUserId + ", startDate="
				+ startDate + ", gpsState=" + gpsState + ", wifiState="
				+ wifiState + ", groupNum=" + groupNum + ", updateTime="
				+ updateTime + ", valid=" + valid + "]";
	}

}
