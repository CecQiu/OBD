/**
 * 
 */
package com.hgsoft.carowner.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.hgsoft.carowner.dao.ServerSetDao;
import com.hgsoft.carowner.entity.Dictionary;
import com.hgsoft.carowner.entity.OBDStockInfo;
import com.hgsoft.carowner.entity.ServerSet;
import com.hgsoft.common.message.RunningData;
import com.hgsoft.common.service.BaseService;
import com.hgsoft.common.utils.MsgSendUtil;
import com.hgsoft.common.utils.SerialNumberUtil;
import com.hgsoft.common.utils.StrUtil;
import com.hgsoft.system.utils.ByteUtil;

/**
 * @author liujialin
 * 0x80 0x0b  配置服务器信息
 */
@Service
public class ServerSetService extends BaseService<ServerSet>{
	private final Log logger = LogFactory.getLog(ServerSetService.class);
	@Resource
	private ServerSetDao  serverSetDao;
	
	@Resource
	private DictionaryService  dictionaryService;
	@Resource
	private CarParamService carParamService;
	@Resource
	private OBDStockInfoService oBDStockInfoService;
	
	
	@Resource
	public void setDao(ServerSetDao serverSetDao){
		super.setDao(serverSetDao);
	}
	
	public ServerSetDao serverSetGetDao() {
		return (ServerSetDao)super.getDao();
	}
	
	/**
	 * 8.	配置服务器信息
	 * @param obdSn obdId
	 * @param setType  0x01：一般数据服务器 0x02:升级服务器
	 * @param serverType 0x01 tcp，ip地址
	 *					 0x02  udp ,ip地址
	 *					 0x03 tcp，域名地址
	 *					 0x04 udp ,域名地址
	 *					 0x05 使用本地服 务器升级
     *
     * @param port  端口号
	 * @param ip    ip地址
	 * @param APN   服务器APN
	 * @return
	 */
	public boolean serverSet(ServerSet serverSet){
		MsgSendUtil msgSendUtil = new MsgSendUtil();
		String obdSn = serverSet.getObdSn();
		String setType = serverSet.getSetType();
		String serverType = serverSet.getServerType();
		String port = serverSet.getPort();
		String ip = serverSet.getIp();
		String APN =serverSet.getApn();
		
		logger.info(obdSn+"***配置服务器参数类型:"+setType+"****服务器类型:"+serverType+"****服务器端口:"+port+"****服务器ip:"+ip+"***APN:"+APN);
		Dictionary dic=dictionaryService.getDicByCodeAndType("owner.command", "server.serverSet");
		String common = dic.getTrueValue();//命令字
		//获取流水号
		int serialNumber=SerialNumberUtil.getSerialnumber();
		logger.info(serialNumber+"***********************800b流水号");
		//确定唯一消息
		String msgId=obdSn+"_"+common+"_"+serialNumber;
		logger.info(msgId+"*********************服务端消息KEY:"+obdSn+"_"+common+"_"+serialNumber);
		Map<String, Object> respMap=RunningData.getIdResponseMap();
		respMap.put(msgId, null);
		StringBuffer msg= new StringBuffer();
		if(setType!=null||setType!=null||port!=null||ip!=null){
			//端口
			String portStr = Integer.toHexString(Integer.parseInt(port));//10进制转成16进制
			//ip地址
			if("01".equals(serverType) || "02".equals(serverType)){
				//7个byte
				String[] ipArr= ip.split("\\.");
				String ip1=StrUtil.strAppendByLen(Integer.toHexString(Integer.parseInt(ipArr[0])),1,"0");
				String ip2=StrUtil.strAppendByLen(Integer.toHexString(Integer.parseInt(ipArr[1])),1,"0");
				String ip3=StrUtil.strAppendByLen(Integer.toHexString(Integer.parseInt(ipArr[2])),1,"0");
				String ip4=StrUtil.strAppendByLen(Integer.toHexString(Integer.parseInt(ipArr[3])),1,"0");
				String ipStr = ip1+ip2+ip3+ip4;
				String ipStrLen = StrUtil.strAppendByLen(Integer.toHexString(ipStr.length()/2),1,"0");
				//配置服务器参数类型:服务器类型:服务器端口:服务器ip长度:服务器ip:服务器APN长度:服务器APN
				msg.append(setType).append(serverType).append(portStr).append(ipStrLen).append(ipStr);
			}
			//域名
			if("03".equals(serverType) || "04".equals(serverType)){
				String ipStr = ByteUtil.ASC2ToHexStr(ip);
				String ipStrLen = StrUtil.strAppendByLen(Integer.toHexString(ipStr.length()/2),1,"0");//十进制转16进制 
				//配置服务器参数类型:服务器类型:服务器端口:服务器ip长度:服务器ip:服务器APN长度:服务器APN
				msg.append(setType).append(serverType).append(portStr).append(ipStrLen).append(ipStr);
			}
		}
		if(APN!=null){
			String APNStr = ByteUtil.ASC2ToHexStr(APN);//ASC2码转16进制
			String APNStrLen = StrUtil.strAppend(Integer.toHexString(APNStr.length()/2), 2, 0, "0");
			msg.append(APNStrLen).append(APNStr);
		}
		
		logger.info(obdSn+"******800b配置服务器请求消息***:"+msg.toString());
		String resultStr=null;
		try {
			//向客户端发送请求消息
			resultStr=msgSendUtil.msgSendAndGetResult(obdSn, common, serialNumber, msg.toString(), msgId);
			logger.info(obdSn+"**********800b配置服务器返回结果:"+resultStr);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e);
		}
		
		respMap.remove(msgId);//获取响应消息后，清除对应的消息
		if(resultStr!=null && "00".equals(resultStr)){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean serverSetList(ServerSet serverSet){
		//1.查询所有设备在线状态
		//2.如果在线，下发设置参数
		//3.如果不在线，保存设置，等待下次上线设置.
		List<OBDStockInfo> osList=oBDStockInfoService.findAll();
		for (OBDStockInfo obdStockInfo : osList) {
			String obdSn=obdStockInfo.getObdMSn().toLowerCase();
			if("01".equals(obdStockInfo.getStockState())){
				serverSet.setObdSn(obdStockInfo.getObdMSn().toLowerCase());
				boolean flag=serverSet(serverSet);
				
				//判断是否存在服务器配置记录，如果存在，更新为无效，并插入新的纪录
				ServerSet servSet = serverSetDao.queryByObdSnAndValid(obdSn,"1");
				if(servSet!=null){
					servSet.setValid("0");//设为无效
					servSet.setUpdateTime(new Date());
					serverSetDao.serverSetSaveOrUpd(servSet);
				}
				//生成新的纪录
				if(flag){
					serverSet.setValid("0");//设置成功
				}else{
					serverSet.setValid("1");//设置失败
				}
				serverSet.setObdSn(obdSn);
				serverSet.setCreateTime(new Date());
				serverSetSave(serverSet);
				
				logger.info(obdSn+"***配置服务器结果:"+flag);
			}else{
				//保存设置
				ServerSet servSet = serverSetDao.queryByObdSnAndValid(obdSn,"1");
				if(servSet!=null){
					servSet.setValid("0");//设为无效
					servSet.setUpdateTime(new Date());
					serverSetDao.serverSetSaveOrUpd(servSet);
				}
				serverSet.setObdSn(obdSn);
				serverSet.setCreateTime(new Date());
				serverSet.setValid("1");//设置失败
				serverSetSave(serverSet);
			}
		}
		
		return true;
	}
	
	//单个服务器配置
	public boolean serverSetSingle(ServerSet serverSet){
		String obdSn=serverSet.getObdSn();
		//1.查询是否存在当前设备，并且判断设备在线状态.
		OBDStockInfo obdStockInfo =oBDStockInfoService.queryBySN(obdSn);
		if(obdStockInfo!=null){
			if("01".equals(obdStockInfo.getStockState())){
				serverSet.setObdSn(obdStockInfo.getObdMSn().toLowerCase());
				boolean flag=serverSet(serverSet);
				
				//判断是否存在服务器配置记录，如果存在，更新为无效，并插入新的纪录
				ServerSet servSet = serverSetDao.queryByObdSnAndValid(obdSn,"1");
				if(servSet!=null){
					servSet.setValid("0");//设为无效
					servSet.setUpdateTime(new Date());
					serverSetDao.serverSetSaveOrUpd(servSet);
				}
				
				//生成新的纪录
				if(flag){
					serverSet.setValid("0");//设置成功
				}else{
					serverSet.setValid("1");//设置失败
				}
				serverSet.setObdSn(obdSn);
				serverSet.setCreateTime(new Date());
				serverSetSave(serverSet);
				
				logger.info(obdSn+"***配置服务器结果:"+flag);
			}else{
				//保存设置
				ServerSet servSet = serverSetDao.queryByObdSnAndValid(obdSn,"1");
				if(servSet!=null){
					servSet.setValid("0");//设为无效
					servSet.setUpdateTime(new Date());
					serverSetDao.serverSetSaveOrUpd(servSet);
				}
				serverSet.setObdSn(obdSn);
				serverSet.setCreateTime(new Date());
				serverSet.setValid("1");//设置失败
				serverSetSave(serverSet);
			}
		}else {
			//保存设置
			ServerSet servSet = serverSetDao.queryByObdSnAndValid(obdSn,"1");
			if(servSet!=null){
				servSet.setValid("0");//设为无效
				servSet.setUpdateTime(new Date());
				serverSetDao.serverSetSaveOrUpd(servSet);
			}
			serverSet.setObdSn(obdSn);
			serverSet.setCreateTime(new Date());
			serverSet.setValid("1");//设置失败
			serverSetSave(serverSet);
		}
		return true;
	}
	
	private boolean serverSetSave(ServerSet serverSet){
		try {
			ServerSet ss = new ServerSet();
			if(serverSet==null){
				return false;
			}
			String apn=serverSet.getApn();
			if(!StringUtils.isEmpty(apn)){
				ss.setApn(apn);
			}
			
			ss.setCreateTime(serverSet.getCreateTime());
			String ip=serverSet.getIp();
			if(!StringUtils.isEmpty(ip)){
				ss.setIp(ip);
			}
			
			String port=serverSet.getPort();
			if(!StringUtils.isEmpty(port)){
				ss.setPort(port);
			}
			
			ss.setObdSn(serverSet.getObdSn());
			
			String serverType=serverSet.getServerType();
			if(!StringUtils.isEmpty(serverType)){
				ss.setServerType(serverType);
			}
			
			String setType=serverSet.getSetType();
			if(!StringUtils.isEmpty(setType)){
				ss.setSetType(setType);
			}
			
			ss.setUpdateTime(serverSet.getUpdateTime());
			String valid=serverSet.getValid();
			if(!StringUtils.isEmpty(valid)){
				ss.setValid(valid);
			}
			
			serverSetDao.save(ss);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public ServerSet queryByObdSnAndValid(String obdSn,String valid) {
		return serverSetDao.queryByObdSnAndValid(obdSn, valid);
	}
}
