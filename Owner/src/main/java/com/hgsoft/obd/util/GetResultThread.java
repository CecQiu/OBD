package com.hgsoft.obd.util;

import org.apache.commons.lang.StringUtils;

import com.hgsoft.obd.server.GlobalData;

/**
 * 获得OBD响应报文线程
 * @author sujunguang 
 * 2016年1月6日 下午5:18:10
 */
public class GetResultThread extends Thread {

	private String obdSn;//设备号
	private Integer serialNum;//流水号
	private Object result;//结果报文
	private String key;
	public GetResultThread(String obdSn, Integer serialNum) {
		this.obdSn = obdSn;
		this.serialNum = serialNum;
	}
	public GetResultThread(String key) {
		this.key = key;
	}
	public Object getResult() {
		System.err.println("------getResult------");
		if(!StringUtils.isEmpty(key)){
			this.result =  GlobalData.OBD_ACK_OR_QueryData.get(key);
		}else{
			this.result =  GlobalData.OBD_ACK_OR_QueryData.get(obdSn + "_" + serialNum);
		}
		System.out.println("result:"+this.result);
		return this.result;
	}

	@Override
	public void run() {
		if(!StringUtils.isEmpty(key)){
			this.result =  GlobalData.OBD_ACK_OR_QueryData.get(key);
		}else{
			this.result =  GlobalData.OBD_ACK_OR_QueryData.get(obdSn + "_" + serialNum);
		}
		System.out.println("result:"+this.result);
	}
}
