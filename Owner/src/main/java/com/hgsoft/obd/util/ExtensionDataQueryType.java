package com.hgsoft.obd.util;


/**
 * 扩展数据查询类型
 */
public enum ExtensionDataQueryType {
	SleepVolt("SleepVolt","0000"),/*休眠电压*/
	SleepOverSpeed("SleepOverSpeed","0001"),/*休眠加速度*/
	DomainWhite("DomainWhite","0002"),/*域白名单*/
	DomainBlack("DomainBlack","0003"),/*域黑名单*/
	DomainWhiteSwitch("DomainWhiteSwitch","0004"),/*域白名单开关*/
	DomainBlackSwitch("DomainBlack","0005"),/*域黑名单开关*/
	CarTypeSetting("CarTypeSetting","0006"),/*车型功能设置查询*/
	LowVoltSleepValue("LowVoltSleepValue","0007"),/*低电压休眠阈值*/
	NetModel("NetModel","0008"),/*网络模式*/
	;
	private String name;
	private String value;
	private ExtensionDataQueryType(String name,String value){
		this.name = name;
		this.value = value;
	}
	
	public static ExtensionDataQueryType getDomainSetTypeByValue(String value){
		for (ExtensionDataQueryType domainSetType : ExtensionDataQueryType.values()) {
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

}
