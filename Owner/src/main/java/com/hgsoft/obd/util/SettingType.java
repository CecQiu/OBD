package com.hgsoft.obd.util;
/**
 * obd设置类型
 * @author ljl
 * 2016年4月22日
 * 下午2:16:28
 */
public enum SettingType {
	WIFI_00("wifi开关","wifi_00"),
	WIFI_01("wifi使用时间","wifi_01"),
	WIFI_10("wifi密码","wifi_10"),
	WIFI_11("wifi名称ssid","wifi_11"),
	WIFI_12("wifi密码和名称ssid","wifi_12"),
	GPS_00("gps开关","gps_00"),
	DRIVE_00("驾驶行为设置-急加速","drive_00"),
	DRIVE_01("驾驶行为设置-急减速","drive_01"),
	DRIVE_02("驾驶行为设置-急转弯","drive_02"),
	DRIVE_03("驾驶行为设置-超速","drive_03"),
	DRIVE_04("驾驶行为设置-疲劳驾驶","drive_04"),
	SWITCH_00("非法启动探测开关","switch_00"),
	SWITCH_01("非法震动探测开关","switch_01"),
	SWITCH_02("蓄电电压异常报警开关","switch_02"),
	SWITCH_03("发动机水温高报警开关","switch_03"),
	SWITCH_04("车辆故障报警开关","switch_04"),
	SWITCH_05("超速报警开关","switch_05"),
	SWITCH_06("电子围栏报警开关","switch_06"),
	SWITCH_07("保留开关","switch_07"),
	OBDDEFAULT_00("未激活设备设置默认分组","obdDefault_00"),
	DOMAIN_00("域白名单功能开关","domain_00"),
	DOMAIN_01("域黑名单功能开关","domain_01"),
	DOMAIN_02("禁止MAC上网","domain_02"),
	DOMAIN_03("增加多个域白名单","domain_03"),
	DOMAIN_04("删除单个域白名单","domain_04"),
	DOMAIN_05("删除所有域白名单","domain_05"),
	DOMAIN_06("增加多个域黑名单","domain_06"),
	DOMAIN_07("删除单个域黑名单","domain_07"),
	DOMAIN_08("删除所有域黑名单","domain_08"),
	DOMAINSYN_00("白名单同步","dmsyn_00"),
	DOMAINSYN_01("黑名单同步","dmsyn_01"),
	WAKEUPSWITCH("自动唤醒开关","wakeupSwitch"),
	WAKEUPTIME("自动唤醒时间","wakeupTime"),
	PORTAL_00("portal开关","portalSwitch");
	
	
	
	private String name;
	private String value;
	private SettingType(String name,String value){
		this.name = name;
		this.value = value;
	}
	
	public static SettingType getSettingTypeByValue(String value){
		for (SettingType settingType : SettingType.values()) {
			if(value.equals(settingType.value)){
				return settingType;
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
		System.out.println(SettingType.DRIVE_00.getValue());
		String s = SettingType.DRIVE_00.toString();
		System.out.println(s);
	}
}
