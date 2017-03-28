package com.hgsoft.carowner.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * obd设备升级文件实体类
 * @author fdf
 */
public class Agnss implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String id;
	private Long size;
	private String data;
	private Date createTime;
	
	public Agnss() {}
	
	public Agnss(String id,  Long size, Date createTime) {
		this.id = id;
		this.size = size;
		this.createTime = createTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
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


	
}
