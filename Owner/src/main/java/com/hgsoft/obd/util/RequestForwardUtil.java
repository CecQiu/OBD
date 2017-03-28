package com.hgsoft.obd.util;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.http.client.methods.HttpPatch;
import org.springframework.stereotype.Component;

import com.hgsoft.common.utils.HTTPFetcher;
import com.hgsoft.common.utils.StrUtil;
import com.hgsoft.obd.server.GlobalData;
/**
 * 请求转发
 * @author sujunguang
 * 2016年6月23日
 * 上午11:40:17
 */
@Component
public class RequestForwardUtil {

	@Resource
	private ObdInHostUtil obdInHostUtil;
	
	/**
	 * 转发请求
	 * @param path 请求路径
	 * @param data 请求数据
	 * @return 0 
	 */
	public String forward(String path,String data) {
		JSONObject dataJson = JSONObject.fromObject(data);
		String deviceId = dataJson.optString("deviceId");
		try {
			String obdSn = StrUtil.obdSnChange(deviceId);
			String obdInHost = obdInHostUtil.getObdInHost(obdSn);
			String localhost = GlobalData.HostIP;
			if(StringUtils.isEmpty(obdInHost) || localhost.equals(obdInHost)){//设备所在的服务器与当前服务器一致，则不进行请求
				return "0";
			}else{
				//请求转发
				HTTPFetcher http = new HTTPFetcher();
				String result = http.post("http://"+obdInHost+path, data);
				return result;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "0";
	}
	
}
