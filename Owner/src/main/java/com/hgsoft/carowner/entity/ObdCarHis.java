package com.hgsoft.carowner.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * OBD车辆型号历史数据
 * @author 刘家林
 */
public class ObdCarHis implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String originId;
	private String obdSn;
	private String typeId;
	private Integer operType;
	private Date createTime;
	private Date delTime;
	
	public ObdCarHis() {}

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

	public String getOriginId() {
		return originId;
	}

	public void setOriginId(String originId) {
		this.originId = originId;
	}

	public Integer getOperType() {
		return operType;
	}

	public void setOperType(Integer operType) {
		this.operType = operType;
	}

	public Date getDelTime() {
		return delTime;
	}

	public void setDelTime(Date delTime) {
		this.delTime = delTime;
	}

	@Override
	public String toString() {
		return "ObdCarHis [id=" + id + ", originId=" + originId + ", obdSn=" + obdSn + ", typeId=" + typeId
				+ ", operType=" + operType + ", createTime=" + createTime + ", delTime=" + delTime + "]";
	}
	
}
