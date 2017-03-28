package com.hgsoft.carowner.entity;


import java.util.Date;

/**
 * obd离线设置表
 * ljl 20160421
 */
public class BatchSet implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String type;
	private String msg;
	private String bodyMsg;
	private String version;
	private String auditOper;
	private Date auditTime;
	private Integer auditState;
	private String auditMsg;
	private Date createTime;
	private Date updateTime;
	private Integer valid;

	public BatchSet() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}


	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Integer getValid() {
		return valid;
	}

	public void setValid(Integer valid) {
		this.valid = valid;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getAuditOper() {
		return auditOper;
	}

	public void setAuditOper(String auditOper) {
		this.auditOper = auditOper;
	}

	public Date getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}

	public Integer getAuditState() {
		return auditState;
	}

	public void setAuditState(Integer auditState) {
		this.auditState = auditState;
	}

	public String getAuditMsg() {
		return auditMsg;
	}

	public void setAuditMsg(String auditMsg) {
		this.auditMsg = auditMsg;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getBodyMsg() {
		return bodyMsg;
	}

	public void setBodyMsg(String bodyMsg) {
		this.bodyMsg = bodyMsg;
	}

	@Override
	public String toString() {
		return "BatchSet [id=" + id + ", type=" + type + ", msg=" + msg + ", bodyMsg=" + bodyMsg + ", version="
				+ version + ", auditOper=" + auditOper + ", auditTime=" + auditTime + ", auditState=" + auditState
				+ ", auditMsg=" + auditMsg + ", createTime=" + createTime + ", updateTime=" + updateTime + ", valid="
				+ valid + "]";
	}

}
