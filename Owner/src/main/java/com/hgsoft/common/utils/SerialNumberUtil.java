/**
 * 
 */
package com.hgsoft.common.utils;

/**
 * @author Administrator
 * 流水号
 */
public class SerialNumberUtil {
	//序列号,默认为0
		private Integer serialNum=0;
		//私有的默认构造函数
		private SerialNumberUtil(){}
		//实例化
		private static final SerialNumberUtil serialNumberUtil=new SerialNumberUtil();
		//加锁，防止同步访问问题
		public synchronized static Integer getSerialnumber(){
			//成功发生一次自动加一  达到最大255后从0开始
			if(serialNumberUtil.serialNum==255){
				serialNumberUtil.serialNum=0;
				return 255;
			}
			return serialNumberUtil.serialNum++;
		}
}
