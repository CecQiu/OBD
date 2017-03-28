package com.hgsoft.obd.util;
/**
 * 报警类型
 * @author sujunguang
 * 2016年3月17日
 * 下午2:16:28
 */
public enum WarnType {
	CarFault,//车辆故障
	Idling,//怠速
	RapidSpeedChangePlus,//急变速-加
	RapidSpeedChangeMinus,//急变速-减
	CarShock,//车辆震动提醒
	CarStartUp,//点火启动提醒
	OverSpeed,//超速
	IllegalStartUp,//非法启动
	IllegalShock,//非法震动
	FatigueDrive,//疲劳驾驶
	FatigueDriveCancel,//消除疲劳驾驶
	Efence_In,//进
	Efence_Out,//出
	Efence_InOut_In,//进出-进
	Efence_InOut_Out;//进出-出
	
	public static void main(String[] args) {
		System.out.println(WarnType.OverSpeed);
		WarnType type = WarnType.OverSpeed;
		System.out.println(type);
	}
}
