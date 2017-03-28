package com.hgsoft.common.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * 经纬度格式转换
 * @author sujunguang
 *
 */
public class CoordinateTransferUtil{
	
	/**
	 * 经纬度转换格式为Double值（即 度 °）
	 * @param str 23°23.0685'
	 * @return
	 */
	public static String lnglatTransferDouble(String str){
		if(!StringUtils.isEmpty(str)){
			String[] lnglat = str.split("°");
			String lat = lnglat[1].substring(0,lnglat[1].length()-1);
			try {
//				System.out.println(lnglat[0]);new Float(lnglat[0])+
//				System.out.println(new Float(lnglat[0]));
				Double f = new Double(NumUtil.getNumFractionDigits(new Float(lat)/60,6));
				f += new Double(lnglat[0]);
				return f.toString();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "";
	}
	
	/**
	 *  经纬度转换格式为String值（即 度 ° 分 '）
	 * @param str 23.384475
	 * @return
	 */
	public static String lnglatTransferString(String str){
		String[] lnglat = str.split("\\.");
//		String minu = lnglat[0];
		String sec = "0."+ lnglat[1];
		Float f = (new Float(sec))*60;
//		System.out.println(minu);
//		System.out.println(sec);
		try {
//			System.out.println(NumUtil.getNumFractionDigits(f, 4));
			String ret = "";
			String minute = NumUtil.getNumFractionDigits(f, 4);//小数点4位
			ret = lnglat[0]+"°"+minute+"'";
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static String lnglatBarrierTransferString(String str){
		String[] lnglat = str.split("\\.");
//		String minu = lnglat[0];
		String sec = "0."+ lnglat[1];
		Float f = (new Float(sec))*60;
//		System.out.println(minu);
//		System.out.println(sec);
		try {
//			System.out.println(NumUtil.getNumFractionDigits(f, 4));
			String ret = "";
			String minute = NumUtil.getNumFractionDigits(f, 4);//小数点4位
			if(lnglat[0].length() == 3 && minute.split("\\.")[0].length() == 2)
				minute = NumUtil.getNumFractionDigits(f, 3);//小数点3位
			
			if(lnglat[0].length() == 2 && minute.split("\\.")[0].length() == 1){//23°05.1953' 凑齐8位
				ret = lnglat[0]+"°0"+minute+"'";
			}else{
				ret = lnglat[0]+"°"+minute+"'";
			}
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	/** 
	 *  经度格式化
	 *  经度:0x11 0x 60 0x40 0x00 -> 116˚04.000΄
	 * @param longtitude
	 * @return
	 */
	public static String formatLongtitude(String longtitude){
		return longtitude.substring(0,3)+"°"+longtitude.substring(3,5)+"."+longtitude.substring(5)+"'";
	}
	
	/** 去除其他字符，格式成数字串
	 * 116˚04.000΄ -> 11604000
	 * @param coordinate 坐标
	 * @return
	 */
	public static String format(String coordinate){
		return coordinate.replaceAll("'", "").replaceAll("\\.", "").replaceAll("°", "");
	}
	/**
	 * 纬度格式化
	 * 纬度:0x33 0x32 0x00 0x00 -> 33˚32.0000΄
	 * @param latitude
	 * @return
	 */
	public static String formatLatitude(String latitude){
		return latitude.substring(0,2)+"°"+latitude.substring(2,4)+"."+latitude.substring(4)+"'";
	}
	
	public static void main(String[] args) {
		System.out.println(lnglatTransferDouble("23°23.0685'"));
		System.out.println(lnglatTransferString("23.384475"));
		System.out.println(lnglatTransferString("23.086589"));
		
	}
}
