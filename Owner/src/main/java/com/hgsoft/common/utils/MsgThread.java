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
 * 当服务端在channelRead方法里获取到响应的数据
 */
public class MsgThread extends Thread {
	private final Log logger = LogFactory.getLog(MsgThread.class);

	private String msgId;
	private Object resMsg;
	public MsgThread(String msgId){
		this.msgId = msgId;
	}
	
	@Override
	public void run() {
		//5秒钟后，如果客户端还是没有返回消息，则立即停止线程
		long temp = System.currentTimeMillis();
		do{
			if(System.currentTimeMillis() - temp >= 2000){
				break;
			}
			//如果服务器已经取到客户端返回的结果，也停止线程
			Map<String, Object> msgMap = RunningData.getIdResponseMap();
			if(msgMap.get(msgId)!=null){
				resMsg=msgMap.get(msgId);
				break;
			}
		}while(RunningData.getIdResponseMap().get(msgId)==null);
	}

	public Object getResMsg() {
		return resMsg;
	}

	public void setResMsg(Object resMsg) {
		this.resMsg = resMsg;
	}

}
