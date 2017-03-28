package com.hgsoft.obd.util;

public enum EFenceType {
	Timing,//定时
	NoTiming,//无定时
	Circle,//圆
	Rectangle,//矩形
	InArea,//进区域
	OutArea,//出区域
	CancelFence,//取消围栏
	CancelAllFences;//取消所有围栏 
	
	public static Integer getWarnType(EFenceType eFenceType){
		Integer type = -1;
		switch (eFenceType) {
		case InArea:
			type = 1;
			break;
		case OutArea:
			type = 2;
			break;
		case CancelFence:
			type = 4;
			break;
		case CancelAllFences:
			type = 5;
			break;
		default:
			type = -1;
			break;
		}
		return type;
	}
}
