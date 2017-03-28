package com.hgsoft.carowner.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 设备按钮报警实体
 *
 * @author sjg
 * @version  [版本号, 2016年12月1日]
 */

public class ObdButtonAlarm implements Serializable {
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1274411827986841541L;
	private String id;
	private String obdSn;
	private String positionInfoId;
	private Integer bit;
	private Integer state;
	private Date time;
	private Date createTime;
	
	public ObdButtonAlarm() {}

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

	public String getPositionInfoId() {
		return positionInfoId;
	}

	public void setPositionInfoId(String positionInfoId) {
		this.positionInfoId = positionInfoId;
	}

	public Integer getBit() {
		return bit;
	}

	public void setBit(Integer bit) {
		this.bit = bit;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return "ObdButtonAlarm [id=" + id + ", obdSn=" + obdSn + ", positionInfoId=" + positionInfoId + ", bit=" + bit
				+ ", state=" + state + ", time=" + time + ", createTime=" + createTime + "]";
	}

}
