/**
 * 
 */
package com.hgsoft.carowner.service;

import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import com.hgsoft.carowner.dao.PortalDao;
import com.hgsoft.carowner.entity.Dictionary;
import com.hgsoft.carowner.entity.Portal;
import com.hgsoft.common.message.RunningData;
import com.hgsoft.common.service.BaseService;
import com.hgsoft.common.utils.MsgSendUtil;
import com.hgsoft.common.utils.SerialNumberUtil;
import com.hgsoft.common.utils.StrUtil;
import com.hgsoft.system.utils.ByteUtil;

/**
 * @author liujialin
 * 10.	请求远程升级
 */
@Service
public class ProtalService extends BaseService<Portal> {
	private final Log logger = LogFactory.getLog(ProtalService.class);
	
	@Resource
	public void setDao(PortalDao portalDao){
		super.setDao(portalDao);
	}
	
	@Override
	public PortalDao getDao() {
		return (PortalDao)super.getDao();
	}

	@Resource
	DictionaryService  dictionaryService;
//	@Resource
//	private MsgSendUtil msgSendUtil;
	
	/**
	 * 远程升级
	 * @param obdSn obd设备号
	 * @param obdVersion 最新版本号
	 * @return
	 * @throws Exception 
	 */
	public boolean protalSet(Portal portal) {
		MsgSendUtil msgSendUtil = new MsgSendUtil();
		String obdSn = portal.getObdSn().toLowerCase();
		Dictionary dic=dictionaryService.getDicByCodeAndType("owner.command", "server.portal");
		String common = dic.getTrueValue();//命令字
		//获取流水号
		int serialNumber=SerialNumberUtil.getSerialnumber();
		logger.info(serialNumber+"***********************8006流水号");
		//确定唯一消息
		String msgId=obdSn+"_"+common+"_"+serialNumber;
		logger.info(msgId+"*********************服务端消息KEY:"+obdSn+"_"+common+"_"+serialNumber);
		Map<String, Object> respMap=RunningData.getIdResponseMap();
		respMap.put(msgId, null);

		String msg="";//消息体
		String type = portal.getType();//类别
		String typeStr=ByteUtil.ASC2ToHexStr(type);//asc码转16禁止
		if("0".equals(type)){
			String url=portal.getUrl();//url路径asc2码
			String urlHex=ByteUtil.ASC2ToHexStr(url);//ASC2码转16进制数
			String msgLen = StrUtil.strAppend(Integer.toHexString(urlHex.length()/2), 2, 0, "0");//十进制转16进制
			msg = typeStr + msgLen + urlHex;
		}else if("1".equals(type)){
			//保留
		}else if("2".equals(type)){
			String body = portal.getMac()+","+portal.getMb()+",1";
			String bodyHex = ByteUtil.ASC2ToHexStr(body);
			String msgLen = StrUtil.strAppend(Integer.toHexString(bodyHex.length()/2), 2, 0, "0");//十进制转16进制
			msg = typeStr + msgLen + bodyHex;
		}else if("3".equals(type)){
			String body = portal.getWhitelists();
			String bodyHex = ByteUtil.ASC2ToHexStr(body);
			String msgLen = StrUtil.strAppend(Integer.toHexString(bodyHex.length()/2), 2, 0, "0");//十进制转16进制
			msg = typeStr + msgLen + bodyHex;
		}else if("4".equals(type)){
			msg = typeStr;
		}else if("5".equals(type)){
			String body =portal.getMac();
			String bodyHex = ByteUtil.ASC2ToHexStr(body);
			String msgLen = StrUtil.strAppend(Integer.toHexString(bodyHex.length()/2), 2, 0, "0");//十进制转16进制
			msg = typeStr + msgLen + bodyHex;
		}else if("6".equals(type)){
			String body=portal.getOnOff();
			String bodyHex = ByteUtil.ASC2ToHexStr(body);
			String msgLen="01";
			msg = typeStr + msgLen + bodyHex;
		}
		
		String resultStr=msgSendUtil.msgSendAndGetResult(obdSn, common, serialNumber, msg, msgId);

		logger.info(resultStr+"*************************8006-portal响应结果");
		//如果成功接收，保存入库
		respMap.remove(msgId);//获取响应消息后，清除对应的消息
		if(resultStr==null && !"00".equals(resultStr)){
			return false;
		}
		
		return true;
	}
	
	
	public List<Portal> queryListByObdSn(String obdSn){
		return getDao().queryListByObdSn(obdSn);
	}
	
	public boolean portalSaveOrUpdate(Portal portal){
		try {
			getDao().saveOrUpdate(portal);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
