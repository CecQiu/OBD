package com.hgsoft.carowner.entity;


import java.util.Date;

/**
 *
 */
public class ObdTestSendPacket implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String obdSn;
	private String typeStr;
	private String msgBody;
	private String result;
	private Integer sended;
	private Integer sendCount;//下发次数
	private String operator;
	private Date createTime;
	private Date updateTime;

	public ObdTestSendPacket() {
	}

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

	public String getTypeStr() {
		return typeStr;
	}

	public void setTypeStr(String typeStr) {
		this.typeStr = typeStr;
	}

	public String getMsgBody() {
		return msgBody;
	}

	public void setMsgBody(String msgBody) {
		this.msgBody = msgBody;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public Integer getSended() {
		return sended;
	}

	public void setSended(Integer sended) {
		this.sended = sended;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
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

	public Integer getSendCount() {
		return sendCount;
	}

	public void setSendCount(Integer sendCount) {
		this.sendCount = sendCount;
	}

	@Override
	public String toString() {
		return "ObdTestSendPacket [id=" + id + ", obdSn=" + obdSn
				+ ", typeStr=" + typeStr + ", msgBody=" + msgBody + ", result="
				+ result + ", sended=" + sended + ", createTime=" + createTime
				+ ", updateTime=" + updateTime + "]";
	}

}
