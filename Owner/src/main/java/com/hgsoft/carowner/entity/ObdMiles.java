package com.hgsoft.carowner.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * obd里程
 * @author 刘家林
 */
public class ObdMiles implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String id;
	private Long miles;
	private String obdSn;
	private Date createTime;
	private Date updateTime;
	
	public ObdMiles() {}
	
	public ObdMiles(String id,  Long miles,String obdSn, Date createTime, Date updateTime) {
		this.id = id;
		this.miles = miles;
		this.obdSn = obdSn;
		this.createTime = createTime;
		this.updateTime = updateTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getMiles() {
		return miles;
	}

	public void setMiles(Long miles) {
		this.miles = miles;
	}

	public String getObdSn() {
		return obdSn;
	}

	public void setObdSn(String obdSn) {
		this.obdSn = obdSn;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	public String toString() {
		return "ObdMiles [id=" + id + ", miles=" + miles + ", obdSn=" + obdSn + ", createTime=" + createTime
				+ ", updateTime=" + updateTime + "]";
	}


}
