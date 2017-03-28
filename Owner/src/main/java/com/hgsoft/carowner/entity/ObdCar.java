package com.hgsoft.carowner.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * OBD车辆型号
 * @author 刘家林
 */
public class ObdCar implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String obdSn;
	private String typeId;
	private Date createTime;
	
	public ObdCar() {}

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

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
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

	@Override
	public String toString() {
		return "ObdCar [id=" + id + ", obdSn=" + obdSn + ", typeId=" + typeId + ", createTime=" + createTime + "]";
	}
	


	
}
