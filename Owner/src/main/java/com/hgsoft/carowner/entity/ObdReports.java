package com.hgsoft.carowner.entity;

import java.io.Serializable;
import java.util.Date;

public class ObdReports implements Serializable{

	private static final long serialVersionUID = 3982170620590947075L;
	private Integer id;
	private Integer type;
	private Integer count;
	private Date date;
	private Date createTime;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	@Override
	public String toString() {
		return "ObdReports [id=" + id + ", type=" + type + ", count=" + count
				+ ", date=" + date + ", createTime=" + createTime + "]";
	}
	
}
