/**
 * 
 */
package com.hgsoft.carowner.service;

import java.util.Date;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import com.hgsoft.carowner.dao.WifiDao;
import com.hgsoft.carowner.entity.Dictionary;
import com.hgsoft.carowner.entity.Wifi;
import com.hgsoft.common.message.RunningData;
import com.hgsoft.common.service.BaseService;
import com.hgsoft.common.utils.MD5Coder;
import com.hgsoft.common.utils.MsgSendUtil;
import com.hgsoft.common.utils.SerialNumberUtil;
import com.hgsoft.common.utils.StrUtil;
import com.hgsoft.system.utils.ByteUtil;

/**
 * @author liujialin
 * 801d 设置OBD的WIFI状态
 * wifi的密码不能小于8位，且不能和之前的密码一样
 */
@Service
public class WiFiSetService extends BaseService<Wifi>{
	private final Log logger = LogFactory.getLog(WiFiSetService.class);

	@Resource
	DictionaryService  dictionaryService;
	
	@Resource
	private WifiDao wifiDao;
//	@Resource
//	private MsgSendUtil msgSendUtil;
	
	
	/**
	 *  设置OBD的WIFI状态
	 * @param obdSn 设备Sn
	 * @param state 命令类型,1：修改SSID ；2：修改WIFI密码 3：WIFI出厂设置 ；4：开启；5：关闭
	 * @param message  命令内容,ASCII码
	 * @return 00成功 01失败 其他保留
	 * @throws Exception 
	 */
	public String WiFiSet(String state,Wifi wifi) throws Exception{
		MsgSendUtil msgSendUtil = new MsgSendUtil();
		String obdSn = wifi.getObdSn();
		Dictionary dic=dictionaryService.getDicByCodeAndType("owner.command", "server.wifi");
		String common = dic.getTrueValue();//命令字
		//获取流水号
		int serialNumber=SerialNumberUtil.getSerialnumber();
		logger.info(serialNumber+"***********************801d流水号");
		//确定唯一消息
		String msgId=obdSn+"_"+common+"_"+serialNumber;
		logger.info(msgId+"*********************服务端消息KEY:"+obdSn+"_"+common+"_"+serialNumber);
		Map<String, Object> respMap=RunningData.getIdResponseMap();
		respMap.put(msgId, null);
		String msg="";
		//接收成功，保存入库
		Wifi w=wifiDao.isExist(obdSn);//数据库是否存在
		if(w==null){//如果为空
			w = new Wifi();
			w.setObdSn(obdSn);
		}
		w.setCreateTime(new Date());
		if("01".equals(state)){
			String ssid =wifi.getSsid();
			String msgHex=ByteUtil.ASC2ToHexStr(ssid);//ASC2码转16进制
			logger.info(msgHex+"*****************SSID");
			String msgLen = StrUtil.strAppendByLen(Integer.toHexString(msgHex.length()/2),1,"0");//十进制转16进制
			msg =state+msgLen+msgHex;
			w.setSsid(ssid);//保存ssid
		}else if("02".equals(state)){
			String auth =wifi.getAutu();//auth
			String encrypt = wifi.getEncrypt();
			String pwd = wifi.getWifiPwd();
			
			//如果密码小于8位，直接返回失败01
			if(pwd.length()<8){
				return "01";
			}
//			//如果密码和数据库一样，直接返回失败
//			if(w.getWifiPwd()!=null){
//				String pwdStr=MD5Coder.encodeMD5Hex(pwd);//跟数据库密码匹配,如果一样不做修改
//				if(w.getWifiPwd().equals(pwdStr)){
//					return "01";
//				}
//			}
			StringBuffer sb = new StringBuffer();
			sb.append(ByteUtil.ASC2ToHexStr(auth));//asc2码转16进制
			sb.append(ByteUtil.ASC2ToHexStr(encrypt));
			sb.append(ByteUtil.ASC2ToHexStr(pwd));
			
			String msgHex=sb.toString();//ASC2码转16进制
			String msgLen = StrUtil.strAppendByLen(Integer.toHexString(msgHex.length()/2),1,"0");//十进制转16进制
			msg =state+msgLen+msgHex;
			w.setAutu(auth);//设置auth
			w.setEncrypt(encrypt);//设置encrypt
			w.setWifiPwd(MD5Coder.encodeMD5Hex(pwd));//MD5加密
		}else if("03".equals(state)){
			msg = state;
			w.setRecovery(state);//是否恢复出厂设置
		}else if("04".equals(state)||"05".equals(state)){
			msg = state;
			w.setWifiState(state);//设置wifi状态
		}
		//如果不是wifi出厂设置
		if(!"03".equals(state)){
			w.setRecovery("00");
		}
		logger.info("801d请求消息*****************"+msg);
		
		//发送请求给obd以及线程获取obd返回结果.
		String resultStr=msgSendUtil.msgSendAndGetResult(obdSn, common, serialNumber, msg, msgId);
		//成功接收，保存入库
		wifiDao.saveOrUpdate(w);
		respMap.remove(msgId);//获取响应消息后，清除对应的消息
		logger.info(resultStr+"*************************8002客户端响应结果");

		return resultStr;
	}
}
