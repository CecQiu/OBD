package com.hgsoft.carowner.entity;

import java.util.Date;

/**
 * 用户和车辆信息类
 * @author fdf
 */
public class ObdMsg implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private String s_obdId;//二维码
	private String s_obdSn;//表面码
	private String s_obdMSn;//obd设备号
	private String s_stockType;//obd设备类型
	private String s_stockState;//obd设备状态
	private String s_regUserId;//关联用户id
	private String s_startDate;//注册时间
	private String s_gpsState;//gps状态
	private String s_wifiState;//wifi状态
	private String s_groupNum;//wifi状态
	private Date s_updateTime;//更新时间
	private String m_mobileNumber;//用户手机号码
	private String m_carId;//关联车辆id
	private String m_license;//车牌号
	private String m_name;//用户名
	private String m_sex;//性别
	private Integer m_userType;//用户类别
	private String ci_carState;//车辆状态
	private String ci_cartypeId;//车辆类别id
	private String oc_hardware;//硬件号
	private String oc_iap;//IAP版本号
//	private String oc_osoftware;//硬件版本号
	private String oc_software;//硬件版本号


	

	@Override
	public String toString() {
		return "ObdMsg [s_obdId=" + s_obdId + ", s_obdSn=" + s_obdSn
				+ ", s_obdMSn=" + s_obdMSn + ", s_stockType=" + s_stockType
				+ ", s_stockState=" + s_stockState + ", s_regUserId="
				+ s_regUserId + ", s_startDate=" + s_startDate
				+ ", s_gpsState=" + s_gpsState + ", s_wifiState=" + s_wifiState
				+ ", s_groupNum=" + s_groupNum + ", s_updateTime="
				+ s_updateTime + ", m_mobileNumber=" + m_mobileNumber
				+ ", m_carId=" + m_carId + ", m_license=" + m_license
				+ ", m_name=" + m_name + ", m_sex=" + m_sex + ", m_userType="
				+ m_userType + ", ci_carState=" + ci_carState
				+ ", ci_cartypeId=" + ci_cartypeId + ", oc_hardware="
				+ oc_hardware + ", oc_iap=" + oc_iap + ", oc_software="
				+ oc_software + "]";
	}

	public ObdMsg() {}

	public String getS_obdId() {
		return s_obdId;
	}

	public void setS_obdId(String s_obdId) {
		this.s_obdId = s_obdId;
	}

	public String getS_obdSn() {
		return s_obdSn;
	}

	public void setS_obdSn(String s_obdSn) {
		this.s_obdSn = s_obdSn;
	}

	public String getS_obdMSn() {
		return s_obdMSn;
	}

	public void setS_obdMSn(String s_obdMSn) {
		this.s_obdMSn = s_obdMSn;
	}

	public String getS_stockType() {
		return s_stockType;
	}

	public void setS_stockType(String s_stockType) {
		this.s_stockType = s_stockType;
	}

	public String getS_stockState() {
		return s_stockState;
	}

	public void setS_stockState(String s_stockState) {
		this.s_stockState = s_stockState;
	}

	public String getS_regUserId() {
		return s_regUserId;
	}

	public void setS_regUserId(String s_regUserId) {
		this.s_regUserId = s_regUserId;
	}

	public String getS_startDate() {
		return s_startDate;
	}

	public void setS_startDate(String s_startDate) {
		this.s_startDate = s_startDate;
	}

	public String getS_gpsState() {
		return s_gpsState;
	}

	public void setS_gpsState(String s_gpsState) {
		this.s_gpsState = s_gpsState;
	}

	public String getS_wifiState() {
		return s_wifiState;
	}

	public void setS_wifiState(String s_wifiState) {
		this.s_wifiState = s_wifiState;
	}

	public Date getS_updateTime() {
		return s_updateTime;
	}

	public void setS_updateTime(Date s_updateTime) {
		this.s_updateTime = s_updateTime;
	}

	public String getM_mobileNumber() {
		return m_mobileNumber;
	}

	public void setM_mobileNumber(String m_mobileNumber) {
		this.m_mobileNumber = m_mobileNumber;
	}

	public String getM_carId() {
		return m_carId;
	}

	public void setM_carId(String m_carId) {
		this.m_carId = m_carId;
	}

	public String getM_license() {
		return m_license;
	}

	public void setM_license(String m_license) {
		this.m_license = m_license;
	}

	public String getM_name() {
		return m_name;
	}

	public void setM_name(String m_name) {
		this.m_name = m_name;
	}

	public String getM_sex() {
		return m_sex;
	}

	public void setM_sex(String m_sex) {
		this.m_sex = m_sex;
	}


	public Integer getM_userType() {
		return m_userType;
	}

	public void setM_userType(Integer m_userType) {
		this.m_userType = m_userType;
	}

	public String getCi_carState() {
		return ci_carState;
	}

	public void setCi_carState(String ci_carState) {
		this.ci_carState = ci_carState;
	}

	public String getCi_cartypeId() {
		return ci_cartypeId;
	}

	public void setCi_cartypeId(String ci_cartypeId) {
		this.ci_cartypeId = ci_cartypeId;
	}

	public String getOc_hardware() {
		return oc_hardware;
	}

	public void setOc_hardware(String oc_hardware) {
		this.oc_hardware = oc_hardware;
	}

	public String getOc_iap() {
		return oc_iap;
	}

	public void setOc_iap(String oc_iap) {
		this.oc_iap = oc_iap;
	}

	public String getOc_software() {
		return oc_software;
	}

	public void setOc_software(String oc_software) {
		this.oc_software = oc_software;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getS_groupNum() {
		return s_groupNum;
	}

	public void setS_groupNum(String s_groupNum) {
		this.s_groupNum = s_groupNum;
	}
	
}
