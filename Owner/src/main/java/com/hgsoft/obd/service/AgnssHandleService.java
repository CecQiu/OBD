package com.hgsoft.obd.service;

import java.util.Date;
import javax.annotation.Resource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.hgsoft.carowner.entity.Agnss;
import com.hgsoft.carowner.service.AgnssService;
import com.hgsoft.common.service.ServerResponses;
import com.hgsoft.common.utils.DateUtil;
import com.hgsoft.common.utils.PropertiesUtil;
import com.hgsoft.common.utils.StrUtil;
import com.hgsoft.obd.server.OBDException;
import com.hgsoft.obd.server.ObdConstants;
import com.hgsoft.obd.util.SendUtil;
import com.hgsoft.obd.util.SerialNumberUtil;

/**
 * Agnss数据包
 * 
 * @author liujialin 2016年1月7日 下午6:23:08
 */
@Service
public class AgnssHandleService {
	private static Logger agnssTaskLogger = LogManager.getLogger("agnssTaskLogger");
	private static final int agnssDataLength = new Integer(PropertiesUtil.getInstance("params.properties").readProperty("agnssDataLength","800"));// 
	@Resource
	private AgnssService agnssService;

	public String sendAgnss(String obdSn, Integer packageNum) throws Exception {
		agnssTaskLogger.info("----【AGNSS数据下发obd】开始---"+obdSn);
		agnssTaskLogger.info("----【AGNSS数据下发obd】obdSn:"+obdSn+"---请求包序号:"+packageNum);
		String data = "";
		String agpsData = getAgnssData(obdSn,packageNum);
		if (StringUtils.isEmpty(agpsData)) {
			return null;
		}
		//修改
		data = ObdConstants.Server_ResponseAGNSS_OBD_Cmd + agpsData;
		Integer serialNum = SerialNumberUtil.getSerialnumber(obdSn);
		agnssTaskLogger.info("----【AGNSS数据下发obd】结束---"+obdSn);
		return (String) new SendUtil().msgSendGetResult(obdSn, serialNum, data, null);
	}

	/**
	 * Agnss
	 * @param packageNum
	 * @return null-失败
	 * @throws Exception
	 */
	public String getAgnssData(String obdSn,Integer packageNum) throws Exception {
		// 如果请求包是第0个,需向ublox服务器请求
		// 1获取数据库里的最新的升级包文件
		Agnss agnss = agnssService.getLatest();
		if (agnss == null) {
			agnssTaskLogger.info("----【AGNSS数据下发obd】agnss数据表空---"+obdSn);
			return null;
//			// 请求最新的agnss服务器获取数据
//			AgnssJob job = new AgnssJob();
//			String data = job.getAgnssData();
//			boolean flag = job.agnssDataHandle(data);
//			agnssTaskLogger.info("----【AGNSS数据下发obd】agnss数据保存入库结果:" + flag+"---"+obdSn);
//			if(!flag){
//				return null;
//			}
		}
		// 2将数据库里的文件转成file
//		byte[] fileByte = agnss.getData();
//		if (fileByte == null || fileByte.length == 0) {
//			return null;
//		}
//		agnssTaskLogger.info("----【AGNSS数据下发obd】获取agnss数据---"+new String(fileByte,"UTF-8"));
//		return agnssDataStr(obdSn,fileByte, packageNum);
		String data = agnss.getData();
		agnssTaskLogger.info("----【AGNSS数据下发obd】完整agnss数据---"+data);
		return agnssData(obdSn,data, packageNum,agnss.getCreateTime());
	}

	
	/**
	 * AGPS数据包生成
	 * 
	 * @param packageNum包序号
	 * @return
	 * @throws Exception
	 */
	public String agnssData(String obdSn,String data, int packageNum,Date agnssTime) throws Exception {
		agnssTaskLogger.info("----【AGNSS数据下发obd】agnss数据分包:" +obdSn+"---包序号:"+packageNum);
		if (packageNum > ((data.length()/2) / agnssDataLength)) {
			agnssTaskLogger.info("----【AGNSS数据下发obd】请求长度超过了数据包的分包数---" +obdSn);
			throw new OBDException("请求长度超过了数据包的分包数！");
		}
		//当做是16进制的数据截取
		String fileMsgHex = agnssDataCut(data, packageNum);
		// 将byte数组转成16进制的字符串,包数据
		// 包序号packageNum:2个byte
		String packNumHex = StrUtil.strAppend(Integer.toHexString(packageNum), 4, 0, "0");
		// 当前包长度
		String packLen = StrUtil.strAppend(Integer.toHexString(fileMsgHex.length()/2), 4, 0, "0");
		StringBuffer msg = new StringBuffer("");
		msg.append(packNumHex).append(packLen).append(fileMsgHex);
		if (packageNum == 0) {
			// 固件长度,仅当包序号为0时有此项,以byte[]实际长度为准
			String packSumLenHex = StrUtil.strAppend(Integer.toHexString(data.length()/2), 4 * 2, 0, "0");
			// 固件异或校验和,仅当包序号为0时有此项
			ServerResponses sr = new ServerResponses();
			String checkCode = sr.xor(data);// 固件异或和
			msg.append(packSumLenHex).append(checkCode);
			String dateStr = DateUtil.getTimeString(agnssTime, "yyMMddHHmmss");// 星历数据更新时间
			msg.append(dateStr);// 星历数据更新时间
		}
		return msg.toString();
	}
	
	private  String agnssDataCut(String string,int num) throws Exception{
    	if(string.length()%2!=0){
    		throw new Exception("agnss数据包非偶数.");
    	}
		int unitSize=agnssDataLength;//每个小文件的大小  
        int size=string.length();//总字节数  
        int last=0;//剩余字节数  
        last=size-unitSize*num*2;
        String subString = null;
		if(last<unitSize*2){
			if(last<=0){
				throw new Exception("请求包序号超过升级包的总长度.");
			}
			subString=string.substring(size-last,size);
		}else{
			subString=string.substring(num*unitSize*2, (num+1)*unitSize*2);
		}
		return subString;
	}
	
}
