package com.hgsoft.obd.util;
/**
 * 升级类型
 * @author sujunguang
 * 2016年9月27日
 * 下午5:05:58
 */
public enum UpgradeType {
	APP("APP","01"),
	IAP("IAP","02");
	
	private String name;
	private String value;
	private UpgradeType(String name,String value){
		this.name = name;
		this.value = value;
	}
	
	public static UpgradeType getUpgradeTypeByValue(String value){
		for (UpgradeType upgradeType : UpgradeType.values()) {
			if(value.equals(upgradeType.value)){
				return upgradeType;
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
		System.out.println(UpgradeType.IAP.getValue());
		System.out.println(UpgradeType.APP.getValue());
	}
}
