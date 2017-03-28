package com.hgsoft.obd.util;

import java.util.TreeMap;

/**
 * 扩展数据设置类型
 */
public enum ExtensionDataSetType {
	SleepVolt("SleepVolt","0000"),/*休眠电压*/
	SleepOverSpeed("SleepOverSpeed","0001"),/*休眠加速度*/
	DomainWhiteSwitch("DomainWhiteSwitch","0002"),/*域白名单开关*/
	DomainBlackSwitch("DomainBlackSwitch","0003"),/*域黑名单开关*/
	DomainForbidMAC("DomainForbidMAC","0004"),/*禁止MAC上网*/
	DomainAddWhite("DomainAddWhite","0005"),/*增加域白名单*/
	DomainDelWhite("DomainDelWhite","0006"),/*删除域白名单*/
	DomainAddBlack("DomainAddBlack","0007"),/*增加域黑名单*/
	DomainDelBlack("DomainDelBlack","0008"),/*删除域黑名单*/
	CarTypeSetting("CarTypeSetting","0009"),/*车型功能设置*/
	LowVoltSleepValue("LowVoltSleepValue","000a"),/*低电压休眠通道*/
	EEPROMFLASH("EEPROMFLASH","000b"),/*修改EEPROM/FLASH值*/
	;
	private String name;
	private String value;
	private ExtensionDataSetType(String name,String value){
		this.name = name;
		this.value = value;
	}
	
	public static ExtensionDataSetType getDomainSetTypeByValue(String value){
		for (ExtensionDataSetType domainSetType : ExtensionDataSetType.values()) {
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
		System.out.println(ExtensionDataSetType.SleepVolt.value);
		System.out.println(getDomainSetTypeByValue("0001"));
		TreeMap<ExtensionDataSetType,String> t = new TreeMap<>();
		t.put(ExtensionDataSetType.SleepVolt, "a");
		t.put(ExtensionDataSetType.DomainDelWhite, "b");
		t.put(ExtensionDataSetType.SleepOverSpeed, "c");
		t.put(ExtensionDataSetType.DomainBlackSwitch, "d");
		t.put(ExtensionDataSetType.DomainForbidMAC, "e");
		t.put(ExtensionDataSetType.DomainDelWhite, "b");
		System.out.println(t);
	}
}
