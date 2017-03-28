package com.hgsoft.system.entity;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Sysparamconf implements Serializable {

	private Integer id;//PK,参数序号(自动增长型)
	private String pname;//参数名
	private String pvalue;//参数值
	private Integer ptype;//类型
	private String remark;//备注
	
	public Sysparamconf(){}
	public Sysparamconf(Integer id, String pname, String pvalue, Integer ptype, String remark) {
		this.id=id;
		this.pname=pname;
		this.pvalue=pvalue;
		this.ptype=ptype;
		this.remark=remark;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getPname() {
		return pname;
	}
	public void setPname(String pname) {
		this.pname = pname;
	}
	public String getPvalue() {
		return pvalue;
	}
	public void setPvalue(String pvalue) {
		this.pvalue = pvalue;
	}
	public Integer getPtype() {
		return ptype;
	}
	public void setPtype(Integer ptype) {
		this.ptype = ptype;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

}
