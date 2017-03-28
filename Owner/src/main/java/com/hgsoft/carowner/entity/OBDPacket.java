package com.hgsoft.carowner.entity;

import java.io.Serializable;
import java.util.Date;
/**
 * OBD存储所有报文数据
 * @author sujunguang
 * 2015年12月30日
 * 上午11:04:08
 */
public class OBDPacket implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4545324596579394065L;
	private String id;//编号
	private Integer packetType;//报文类型：0-原始 1-拆分报文
	private String packetData;//报文数据
	private Date insertTime;//插入时间
	private String obdSn;//obd设备号
	private String comman;//命令字
	private String apacketData;//转码后的报文
	private String msg;//报文内容
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getPacketType() {
		return packetType;
	}
	public void setPacketType(Integer packetType) {
		this.packetType = packetType;
	}
	public String getPacketData() {
		return packetData;
	}
	public void setPacketData(String packetData) {
		this.packetData = packetData;
	}
	public OBDPacket(String id, int packetType, String packetData) {
		super();
		this.id = id;
		this.packetType = packetType;
		this.packetData = packetData;
	}
	public OBDPacket() {
		super();
	}
	public OBDPacket(String id) {
		super();
		this.id = id;
	}
	public Date getInsertTime() {
		return insertTime;
	}
	public void setInsertTime(Date insertTime) {
		this.insertTime = insertTime;
	}
	public String getObdSn() {
		return obdSn;
	}
	public void setObdSn(String obdSn) {
		this.obdSn = obdSn;
	}
	public String getComman() {
		return comman;
	}
	public void setComman(String comman) {
		this.comman = comman;
	}
	public String getApacketData() {
		return apacketData;
	}
	public void setApacketData(String apacketData) {
		this.apacketData = apacketData;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}
