package com.hgsoft.carowner.entity;

import java.util.Date;
/**
 * 分组AGPS数据
 *
 * @author sjg
 * @version  [2016年12月13日]
 */
public class ObdGroupAGPS implements java.io.Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 4579833151325348454L;
	private Integer id;
	private String groupNum;
	private byte[] data;
	private Integer size;
	private Date createTime;
	private Date updateTime;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getGroupNum() {
		return groupNum;
	}
	public void setGroupNum(String groupNum) {
		this.groupNum = groupNum;
	}
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
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
	public Integer getSize() {
		return size;
	}
	public void setSize(Integer size) {
		this.size = size;
	}
	@Override
	public String toString() {
		return "ObdGroupAGPS [id=" + id + ", groupNum=" + groupNum + ", size="
				+ size + ", createTime=" + createTime + ", updateTime=" + updateTime + "]";
	}
}
