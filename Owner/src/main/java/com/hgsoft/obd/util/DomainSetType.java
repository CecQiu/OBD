package com.hgsoft.obd.util;
/**
 * 域设置类型
 * @author sujunguang
 * 2016年7月18日
 * 下午5:22:35
 */
public enum DomainSetType {
	WhiteSwitch("WhiteSwitch","1000 0000 0000 0000"),/*域白名单开关*/
	BlackSwitch("BlackSwitch","0100 0000 0000 0000"),/*域黑名单开关*/
	ForbidMAC("ForbidMAC","0010 0000 0000 0000"),/*禁止MAC上网*/
	AddWhite("AddWhite","0001 0000 0000 0000"),/*增加域白名单*/
	DelWhite("DelWhite","0000 1000 0000 0000"),/*删除域白名单*/
	AddBlack("AddBlack","0000 0100 0000 0000"),/*增加域黑名单*/
	DelBlack("DelBlack","0000 0010 0000 0000");/*删除域黑名单*/
	
	private String name;
	private String value;
	private DomainSetType(String name,String value){
		this.name = name;
		this.value = value;
	}
	
	public static DomainSetType getDomainSetTypeByValue(String value){
		for (DomainSetType domainSetType : DomainSetType.values()) {
			if(value.equals(domainSetType.value)){
				return domainSetType;
			}
		}
		return null;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public static void main(String[] args) {
		System.out.println(DomainSetType.AddWhite);
	}
}
