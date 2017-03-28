package com.hgsoft.carowner.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * obd设备升级文件实体类
 * @author fdf
 */
public class AGPS implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String obdSn;
	private String longitude;
	private String latitude;
	private String oprecision;
	private Integer size;
	private byte[] agpsData;
	private Date createTime;
	private String remark;
	
	public AGPS() {}
	
	public AGPS(Integer id, String obdSn,String longitude, String latitude, String oprecision, Integer size,
			byte[] agpsData, Date createTime,String remark) {
		this.id = id;
		this.obdSn = obdSn;
		this.longitude = longitude;
		this.latitude = latitude;
		this.oprecision = oprecision;
		this.size = size;
		this.agpsData = agpsData;
		this.createTime = createTime;
		this.remark = remark;
	}


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getOprecision() {
		return oprecision;
	}

	public void setOprecision(String oprecision) {
		this.oprecision = oprecision;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public byte[] getAgpsData() {
		return agpsData;
	}

	public void setAgpsData(byte[] agpsData) {
		this.agpsData = agpsData;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getObdSn() {
		return obdSn;
	}

	public void setObdSn(String obdSn) {
		this.obdSn = obdSn;
	}
	
}
