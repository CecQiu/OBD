package com.hgsoft.carowner.entity;

import java.io.Serializable;

/**
 * ublox数据
 * @author liujialin
 */
public class UbloxData implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Integer type;
	private Integer size;
	private byte[] agpsData;
	private String error;
	
	public UbloxData() {}
	
	public UbloxData(Integer type, Integer size,
			byte[] agpsData, String error) {
		this.type = type;
		this.size = size;
		this.agpsData = agpsData;
		this.error = error;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
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

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	
}
