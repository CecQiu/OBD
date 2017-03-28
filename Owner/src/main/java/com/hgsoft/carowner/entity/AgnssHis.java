package com.hgsoft.carowner.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * obd设备升级文件实体类
 * @author fdf
 */
public class AgnssHis implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String originId;
	private Long size;
	private String data;
	private Date createTime;
	private Date delTime;
	
	public AgnssHis() {}
	
	public AgnssHis(String id, String originId, Long size, Date createTime,Date delTime) {
		this.id = id;
		this.originId = originId;
		this.size = size;
		this.createTime = createTime;
		this.delTime = delTime;
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

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getOriginId() {
		return originId;
	}

	public void setOriginId(String originId) {
		this.originId = originId;
	}

	public Date getDelTime() {
		return delTime;
	}

	public void setDelTime(Date delTime) {
		this.delTime = delTime;
	}
	
}
