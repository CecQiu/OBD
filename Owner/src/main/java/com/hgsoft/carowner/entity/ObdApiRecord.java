package com.hgsoft.carowner.entity;

import java.io.Serializable;
import java.util.Date;
/**
 * OBD存储所有报文数据
 * @author sujunguang
 * 2015年12月30日
 * 上午11:04:08
 */
public class ObdApiRecord implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private String id;//编号
	private String obdMsn;//表面号
	private String url;//
	private String method;//
	private String param;//
	private String returnMsg;//
	private Date createTime;//报文内容
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getObdMsn() {
		return obdMsn;
	}
	public void setObdMsn(String obdMsn) {
		this.obdMsn = obdMsn;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getParam() {
		return param;
	}
	public void setParam(String param) {
		this.param = param;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getReturnMsg() {
		return returnMsg;
	}
	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
	}
	
	
}
