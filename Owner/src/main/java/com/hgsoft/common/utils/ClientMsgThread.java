/**
 * 
 */
package com.hgsoft.common.utils;

import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.hgsoft.common.message.RunningData;

/**
 * @author liujialin
 * 当第三方服务器响应数据，线程读取
 */
public class ClientMsgThread extends Thread {
	private final Log logger = LogFactory.getLog(ClientMsgThread.class);

	private String userId;
	private Object resMsg;
	public ClientMsgThread(String userId){
		this.userId = userId;
	}
	
	@Override
	public void run() {
		//10秒钟后，如果客户端还是没有返回消息，则立即停止线程
		long temp = System.currentTimeMillis();
		do{
			if(System.currentTimeMillis() - temp >= 10000){
				break;
			}
			//如果服务器已经取到客户端返回的结果，也停止线程
			Map<String, Object> msgMap = RunningData.getIdClientMap();
			if(msgMap.get(userId)!=null){
				resMsg=msgMap.get(userId);
				break;
			}
		}while(RunningData.getIdClientMap().get(userId)==null);
	}

	public Object getResMsg() {
		return resMsg;
	}

	public void setResMsg(Object resMsg) {
		this.resMsg = resMsg;
	}

}
