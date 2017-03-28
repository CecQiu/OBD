package com.hgsoft.carowner.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * obd设备升级文件实体类
 * @author fdf
 */
public class ObdUpgrade implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String fileName;
	private String version;
	private Integer firmType;
	private String firmVersion;
	private Long size;
	private byte[] file;
	private String memo;
	private Date createTime;
	private Date updateTime;
	private String auditOper;//审核人
	private String auditMsg;//审核意见
	private Date auditTime;//审核时间
	private String auditSend;//升级固件推送结果
	private String auditState;//审核结果
	private String valid;//是否有效
	
	public ObdUpgrade() {}
	
	public ObdUpgrade(String id, String fileName, String version, Integer firmType,String firmVersion, Long size,
			String memo, Date createTime, Date updateTime , String auditOper,
			String auditMsg,Date auditTime, String auditState, String auditSend) {
		this.id = id;
		this.fileName = fileName;
		this.version = version;
		this.firmType = firmType;
		this.firmVersion = firmVersion;
		this.size = size;
		this.memo = memo;
		this.createTime = createTime;
		this.updateTime = updateTime;
		this.auditOper = auditOper;
		this.auditMsg = auditMsg;
		this.auditTime = auditTime;
		this.auditState = auditState;
		this.auditSend = auditSend;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public Long getSize() {
		return size;
	}
	public void setSize(Long size) {
		this.size = size;
	}
	public byte[] getFile() {
		return file;
	}
	public void setFile(byte[] file) {
		this.file = file;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
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

	public String getAuditState() {
		return auditState;
	}

	public void setAuditState(String auditState) {
		this.auditState = auditState;
	}

	public String getValid() {
		return valid;
	}

	public void setValid(String valid) {
		this.valid = valid;
	}

	public String getAuditMsg() {
		return auditMsg;
	}

	public void setAuditMsg(String auditMsg) {
		this.auditMsg = auditMsg;
	}

	public String getAuditSend() {
		return auditSend;
	}

	public void setAuditSend(String auditSend) {
		this.auditSend = auditSend;
	}

	public String getFirmVersion() {
		return firmVersion;
	}

	public void setFirmVersion(String firmVersion) {
		this.firmVersion = firmVersion;
	}

	public Integer getFirmType() {
		return firmType;
	}

	public void setFirmType(Integer firmType) {
		this.firmType = firmType;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
}
