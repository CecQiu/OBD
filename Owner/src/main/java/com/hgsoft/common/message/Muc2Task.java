package com.hgsoft.common.message;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.hgsoft.common.message.OBDMessage;
/**
 * 2.	Mcu2终端ACK 
 * @author liujialin
 *
 */
@Service
public class Muc2Task {
	private final Log logger = LogFactory.getLog(Muc2Task.class);

	/**
	 * 终端ACK 逻辑处理方法
	 * @param om 请求消息对象
	 * @throws Exception 
	 * @return 无应答
	 */
	public void entrance(OBDMessage om) {
		//消息体
		String msg = om.getMsgBody();
		//obd设备ID
		String obdSn=om.getId();
		//获取服务端请求消息的命令字和流水号
		int[] fileLens = {2*2, 2*1, 2*1};
		String[] files = new String[fileLens.length];
		int totalLen = 0;
		for(int i=0; i<fileLens.length; i++) {
			int len = fileLens[i];
			files[i] = msg.substring(totalLen, totalLen + len);
			totalLen += len;
		}
		String commonServ = files[0];//接收消息的命令字
		String seriaNumServ = files[1];//接收消息的流水号
		int seriaNum=Integer.parseInt(seriaNumServ, 16);//十六进制转成十进制
		String flag = files[2];//成功标志,0x00成功接收，0x01接收错误,其它保留
		String key = obdSn+"_"+commonServ+"_"+seriaNum;
		logger.info(key+"***********************终端ACK结果***"+obdSn);
		try {
			RunningData.getIdResponseMap().put(key, flag);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}
	}
}
