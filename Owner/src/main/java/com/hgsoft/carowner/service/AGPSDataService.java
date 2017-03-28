/**
 * 
 */
package com.hgsoft.carowner.service;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import com.hgsoft.carowner.dao.AGPSDao;
import com.hgsoft.carowner.entity.AGPS;
import com.hgsoft.carowner.entity.CarGSPTrack;
import com.hgsoft.carowner.entity.Dictionary;
import com.hgsoft.carowner.entity.UbloxData;
import com.hgsoft.common.message.OBDMessage;
import com.hgsoft.common.message.RunningData;
import com.hgsoft.common.netty.HelloClient;
import com.hgsoft.common.service.BaseService;
import com.hgsoft.common.service.ServerResponses;
import com.hgsoft.common.utils.ClientMsgThread;
import com.hgsoft.common.utils.CoordinateTransferUtil;
import com.hgsoft.common.utils.DateUtil;
import com.hgsoft.common.utils.FileUtil;
import com.hgsoft.common.utils.MsgSendUtil;
import com.hgsoft.common.utils.SerialNumberUtil;
import com.hgsoft.common.utils.StrUtil;
import com.hgsoft.system.utils.ByteUtil;

/**
 * @author liujialin
 * 12.	Mcu15 更新AGPS请求包
 * 20150812
 */
@Service
public class AGPSDataService extends BaseService<AGPS>{
	private final Log logger = LogFactory.getLog(AGPSDataService.class);
	
	@Resource
	DictionaryService  dictionaryService;
	@Resource
	CarGSPTrackService carGSPTrackService;
	@Resource
	AGPSDao aGPSDao;
//	@Resource
//	private MsgSendUtil msgSendUtil;
	
	public boolean AGPSData(OBDMessage om) {
		MsgSendUtil msgSendUtil = new MsgSendUtil();
		boolean mark = true;
		try {
			//消息体
			String msg = om.getMsgBody();
			//obd设备ID
			String obdSn=om.getId();
			//请求包序号
			int packageNum = Integer.valueOf(msg,16);
			logger.info("obd设备号:"+obdSn+"请求包序号:"+packageNum);
			//获取命令字
			Dictionary dic=dictionaryService.getDicByCodeAndType("owner.command", "server.AGPSData");
			String command = dic.getTrueValue();//命令字
			//获取流水号
			int serialNumber=SerialNumberUtil.getSerialnumber();
//			ServerResponses sr=new ServerResponses();//消息返回
			//如果请求包是第0个,需向ublox服务器请求
			if(packageNum==0){
				//1获取数据库里的最新的升级包文件
//				AGPS agps=aGPSDao.findLastBySn(obdSn);
//				//2将数据库里的文件转成file
//				byte[] fileByte=agps.getAgpsData();
//				String fileMsgHex=ByteUtil.bytesToHexString(fileByte);
//				logger.info(obdSn+"***完整报文:");
//				logger.info(fileMsgHex);
//				MsgSendUtil.sendMsg(obdSn, agpsDataStr(fileByte,obdSn,packageNum,command,serialNumber));
				
				//获取车辆最新的经纬度
				CarGSPTrack carGSPTrack=carGSPTrackService.findLastBySn(obdSn);
				if(carGSPTrack==null){
					logger.info(obdSn+"****agps数据查询为空,因为这辆车是新跑的车没有位置信息.");
					return false;
				}
				String lng=carGSPTrack.getLongitude();//精度
				String lngStr=CoordinateTransferUtil.lnglatTransferDouble(lng);//转成小数点
				String lat=carGSPTrack.getLatitude();//维度
				String latStr=CoordinateTransferUtil.lnglatTransferDouble(lat);//转成小数点
				StringBuffer sb = new StringBuffer("");
				sb.append("cmd=full;");//请求方式
				sb.append("user=403291856@qq.com;");//请求用户名
				sb.append("pwd=Buxogx;");//pwd
				sb.append("lat="+latStr+";");//纬度
				sb.append("lon="+lngStr+";");//经度
				sb.append("pacc=20000\n");//精度
				
				//获取到AGPS数据包:"cmd=full;user=403291856@qq.com;pwd=Buxogx;lat=113.34;lon=23.17;pacc=20000\n"
//				HelloClient.clientConnectServer("agps.u-blox.com", 46434, sb.toString(),obdSn,command);
				HelloClient.clientConnectServer("195.34.89.144", 46434, sb.toString(),obdSn,command);
				//线程读取返回的数据
				//如果服务端在：HelloServerHandler.channelRead()方法里读取到客户端的响应消息，则线程获取消息。
				ClientMsgThread thread =new ClientMsgThread(obdSn);
				thread.start();
				try {
					//线程同步锁，顺序执行
					thread.join(10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				//获取到客户端的响应消息
				UbloxData ublox= (UbloxData) thread.getResMsg();
				Date date=new Date();
				if(ublox!=null){
					int len= ublox.getSize();//agps数据长度
					int flag=ublox.getType();//成功
					if(flag==0){
						byte[] byteArr=ublox.getAgpsData();//AGPS二进制数组
						//1数据保存入库
						AGPS agps = new AGPS();
						agps.setAgpsData(byteArr);
						agps.setCreateTime(date);
						agps.setLatitude(lat);
						agps.setLongitude(lng);
						agps.setOprecision("20000");
						agps.setSize(len);
						agps.setObdSn(obdSn);
						aGPSDao.save(agps);//保存入库
						
						//清除map记录
						Map<String, Object> respMap=RunningData.getIdClientMap();
						respMap.remove(obdSn);//获取响应消息后，清除对应的消息
						
						msgSendUtil.sendMsg(obdSn, agpsDataStr(byteArr,obdSn,packageNum,command,serialNumber));
					}else if(flag==1){
						String err= ublox.getError();
						//返回报错信息
						logger.info(err+"*********************AGPS数据请求失败");
						mark = false;
					}
				}
			}else{
				//1获取数据库里的最新的升级包文件
				AGPS agps=aGPSDao.findLastBySn(obdSn);
				if(agps==null){
					return false;
				}
				//2将数据库里的文件转成file
				byte[] fileByte=agps.getAgpsData();
				if(fileByte==null||fileByte.length==0){
					return false;
				}
				msgSendUtil.sendMsg(obdSn, agpsDataStr(fileByte,obdSn,packageNum,command,serialNumber));
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
			logger.error(e);
			mark = false;
		} catch (InterruptedException e) {
			e.printStackTrace();
			logger.error(e);
			mark = false;
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e);
			mark = false;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			mark = false;
		}
		return mark;
	}
	
	/**
	 * 获取AGPS数据
	 * @param obdSn obd设备ID
	 * @param packageNum 包序号
	 * @return
	 * @throws Exception
	 */
	public String agpsDataStr(byte[] fileByte,String obdSn,int packageNum,String command,int serialNumber) throws Exception{
		MsgSendUtil msgSendUtil = new MsgSendUtil();
		ServerResponses sr=new ServerResponses();//消息返回
		//获取命令字
		Dictionary dic=dictionaryService.getDicByCodeAndType("owner.command", "server.AGPSData");
		//1获取数据库里的最新的升级包文件
//		AGPS agps=aGPSDao.findLastBySn(obdSn);
		//2将数据库里的文件转成file
//		byte[] fileByte=agps.getAgpsData();
		//3读取文件内容，指定的byte[],包数据
		byte[] fileMsgByte=FileUtil.cutByte(fileByte, packageNum);
		//将byte数组转成16进制的字符串,包数据
		String fileMsgHex=ByteUtil.bytesToHexString(fileMsgByte);
		logger.info("AGPS请求************obd设备号:"+obdSn+"***请求序号:"+packageNum);
		logger.info(fileMsgHex);
		//包序号packageNum:2个byte
		String packNumHex = StrUtil.strAppend(Integer.toHexString(packageNum), 4, 0, "0");
		//当前包长度
//		String packLen=StrUtil.strAppend(Integer.toHexString(packLength(agps.getSize().intValue(),packageNum)), 4, 0, "0");
		String packLen=StrUtil.strAppend(Integer.toHexString(fileMsgByte.length), 4, 0, "0");
		//固件长度,仅当包序号为0时有此项,以byte[]实际长度为准
//		String packSumLenHex = StrUtil.strAppend(Integer.toHexString(agps.getSize().intValue()), 4*2, 0, "0");
		String packSumLenHex = StrUtil.strAppend(Integer.toHexString(fileByte.length), 4*2, 0, "0");
		//固件异或校验和,仅当包序号为0时有此项
		String fileHex=ByteUtil.bytesToHexString(fileByte);
		String checkCode=sr.xor(fileHex);//固件异或和
		StringBuffer msg = new StringBuffer("");
		msg.append(packNumHex).append(packLen).append(fileMsgHex);
		if(packageNum==0){
			msg.append(packSumLenHex).append(checkCode);
			String dateStr=DateUtil.getTimeString(new Date(), "yyMMddHHmmss");//星历数据更新时间
			msg.append(dateStr);//星历数据更新时间
		}
		return msgSendUtil.msgAll(obdSn, dic.getTrueValue(), serialNumber, msg.toString());
	}
	
	/**
	 * 当前包长度
	 * @param size 包总长度
	 * @param num 包序号
	 * @return
	 */
	public int packLength(int size,int num){
		int unitSize=50;//每个小文件的大小  
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
}
