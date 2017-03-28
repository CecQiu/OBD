/**
 * 
 */
package com.hgsoft.carowner.service;

import javax.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import com.hgsoft.carowner.dao.ObdUpgradeDao;
import com.hgsoft.carowner.entity.Dictionary;
import com.hgsoft.carowner.entity.ObdUpgrade;
import com.hgsoft.common.service.BaseService;
import com.hgsoft.common.service.ServerResponses;
import com.hgsoft.common.utils.FileUtil;
import com.hgsoft.common.utils.MsgSendUtil;
import com.hgsoft.common.utils.SerialNumberUtil;
import com.hgsoft.common.utils.StrUtil;
import com.hgsoft.system.utils.ByteUtil;

/**
 * @author liujialin
 * 0x80 0x0c 发送升级包数据
 */
@Service
public class UpdateDataService extends BaseService<ObdUpgrade>{
	private final Log logger = LogFactory.getLog(UpdateDataService.class);
	@Resource
	DictionaryService  dictionaryService;
	@Resource
	ObdUpgradeDao obdUpgradeDao;
//	@Resource
//	MsgSendUtil msgSendUtil;
	/**
	 * 发送升级包数据 逻辑处理方法
	 * @param obdSn obd设备号
	 * @param packageNum 请求包序号
	 * @return boolean
	 * @throws Exception
	 */
	public boolean obdUpdateData(String obdSn,int packageNum) {
		MsgSendUtil msgSendUtil = new MsgSendUtil();
		try {
			String msg=obdUpdateDataStr(obdSn,packageNum);
			if(msg!=null){
				msgSendUtil.sendMsg(obdSn, msg);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e);
			return false;
		}
		return true;
	}
	/**
	 * 获取更新数据
	 * @param obdSn obd设备ID
	 * @param packageNum 包序号
	 * @return
	 * @throws Exception
	 */
	public String obdUpdateDataStr(String obdSn,int packageNum) throws Exception {
		MsgSendUtil msgSendUtil = new MsgSendUtil();
		logger.info(obdSn+"****远程升级请求包序号:"+packageNum);
		ServerResponses sr=new ServerResponses();//消息返回
		//获取命令字
		Dictionary dic=dictionaryService.getDicByCodeAndType("owner.command", "server.updateData");
		//获取流水号
		int serialNumber=SerialNumberUtil.getSerialnumber();
		String command = dic.getTrueValue();//命令字
		//1获取数据库里的最新的升级包文件
		ObdUpgrade ou=obdUpgradeDao.getLatestVersion();
		if(ou==null){
			return null;
		}
		//2将数据库里的文件转成file
		byte[] fileByte=ou.getFile();
		if(fileByte==null || fileByte.length==0){
			return null;
		}
		//3读取文件内容，指定的byte[],包数据
		byte[] fileMsgByte=null;
		try {
			fileMsgByte = FileUtil.cutByte(fileByte, packageNum);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}
		logger.info((fileMsgByte==null)+"****返回结果是否为空");
		if(fileMsgByte==null){
			return null;
		}
		//将byte数组转成16进制的字符串,包数据
		String fileMsgHex=ByteUtil.bytesToHexString(fileMsgByte);
		//包序号packageNum:2个byte
		String packNumHex = StrUtil.strAppend(Integer.toHexString(packageNum), 4, 0, "0");
		//当前包长度
//		String packLen=StrUtil.strAppend(Integer.toHexString(packLength(ou.getSize().intValue(),packageNum)), 4, 0, "0");
		String packLen=StrUtil.strAppend(Integer.toHexString(fileMsgByte.length), 4, 0, "0");//直接算长度
		//固件长度,仅当包序号为0时有此项
		String packSumLenHex = StrUtil.strAppend(Integer.toHexString(ou.getSize().intValue()), 4*2, 0, "0");
		//固件异或校验和,仅当包序号为0时有此项
		String fileHex=ByteUtil.bytesToHexString(fileByte);
		String checkCode="";
		try {
			checkCode = sr.xor(fileHex);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}
		//固件异或和
		StringBuffer msg = new StringBuffer("");
		msg.append(packNumHex).append(packLen).append(fileMsgHex);
		if(packageNum==0){
			msg.append(packSumLenHex).append(checkCode);
		}
		String message="";
		try {
			message = msgSendUtil.msgAll(obdSn, command, serialNumber, msg.toString());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}
		logger.info(obdSn+"***远程升级包:"+packageNum+"***返回包数据");
		logger.info(message);
		return message;
	}
	
	/**
	 * 当前包长度
	 * @param size 包总长度
	 * @param num 包序号
	 * @return
	 */
	public int packLength(int size,int num){
		int unitSize=800;//每个小文件的大小  
        int last=0;//剩余字节数  
        last=size-unitSize*num;
        int res=0;
        if(last<unitSize){
        	res =last;
        }else{
        	res = unitSize;
        }
        return res;
	}
	/**
	 * 获取最新的obd升级包
	 * @return
	 */
	public ObdUpgrade getObdUpgradeLatest(){
		return obdUpgradeDao.getLatestVersion();
	}
	//获取最新的版本号
	public String getLatestVersionNum(){
		return obdUpgradeDao.getLatestVersionNum();
	}
}	
