/**
 * 
 */
package com.hgsoft.carowner.service;

import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import com.hgsoft.carowner.entity.Dictionary;
import com.hgsoft.carowner.entity.ObdVersion;
import com.hgsoft.common.message.RunningData;
import com.hgsoft.common.service.BaseService;
import com.hgsoft.common.utils.MsgSendUtil;
import com.hgsoft.common.utils.SerialNumberUtil;
import com.hgsoft.system.utils.ByteUtil;

/**
 * @author liujialin
 * 10.	请求远程升级
 */
@Service
public class RemoteUpgradeService extends BaseService<ObdVersion> {
	private final Log logger = LogFactory.getLog(RemoteUpgradeService.class);

	@Resource
	DictionaryService  dictionaryService;
	@Resource
	private ObdVersionService obdVersionService;
//	@Resource
//	private MsgSendUtil msgSendUtil;
	
	/**
	 * 远程升级
	 * @param obdSn obd设备号
	 * @param obdVersion 最新版本号
	 * @return
	 * @throws Exception 
	 */
	public boolean remoteUpgrade(String obdSn,String obdVersion){
		MsgSendUtil msgSendUtil = new MsgSendUtil();
		Dictionary dic=dictionaryService.getDicByCodeAndType("owner.command", "server.remoteUpgrade");
		String common = dic.getTrueValue();//命令字
		//获取流水号
		int serialNumber=SerialNumberUtil.getSerialnumber();
		logger.info(serialNumber+"***********************800d流水号");
		//确定唯一消息
		String msgId=obdSn+"_"+common+"_"+serialNumber;
		logger.info(msgId+"*********************服务端消息KEY:"+obdSn+"_"+common+"_"+serialNumber);
		Map<String, Object> respMap=RunningData.getIdResponseMap();
		respMap.put(msgId, null);
		String obdVersionName=ByteUtil.ASC2ToHexStr(obdVersion);//ASC2码转16进制数
		String msg=obdVersionName;
		String resultStr=msgSendUtil.msgSendAndGetResult(obdSn, common, serialNumber, msg, msgId);

		logger.info(resultStr+"*************************800d客户端响应结果");
		//如果成功接收，保存入库
		respMap.remove(msgId);//获取响应消息后，清除对应的消息
		if(resultStr==null && !"00".equals(resultStr)){
			return false;
		}
		return true;
	}
	/**
	 * 10.	请求远程升级
	 * @param obdSnList 远程推送的obd设备号集合
	 * @param obdVersion 最新版本号
	 * @return
	 */
	public boolean remoteUpgradeAsk(String[] obdSnList,final String obdVersion){
		//多线程跑
		try {
			for (final String obdSn : obdSnList) {
			   new Thread(){
				   @Override
					public void run() {
					   remoteUpgrade(obdSn,obdVersion);
					}
			   }.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			return false;
		}
		return true;
	}
}
