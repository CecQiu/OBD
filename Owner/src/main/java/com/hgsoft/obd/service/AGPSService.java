package com.hgsoft.obd.service;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.hgsoft.carowner.dao.AGPSDao;
import com.hgsoft.carowner.entity.AGPS;
import com.hgsoft.carowner.entity.CarGSPTrack;
import com.hgsoft.carowner.entity.OBDStockInfo;
import com.hgsoft.carowner.entity.ObdGroup;
import com.hgsoft.carowner.entity.UbloxData;
import com.hgsoft.carowner.service.CarGSPTrackService;
import com.hgsoft.carowner.service.OBDStockInfoService;
import com.hgsoft.carowner.service.ObdGroupService;
import com.hgsoft.common.message.RunningData;
import com.hgsoft.common.netty.HelloClient;
import com.hgsoft.common.service.ServerResponses;
import com.hgsoft.common.utils.ClientMsgThread;
import com.hgsoft.common.utils.CoordinateTransferUtil;
import com.hgsoft.common.utils.DateUtil;
import com.hgsoft.common.utils.FileUtil;
import com.hgsoft.common.utils.PropertiesUtil;
import com.hgsoft.common.utils.StrUtil;
import com.hgsoft.obd.server.OBDException;
import com.hgsoft.obd.server.ObdConstants;
import com.hgsoft.obd.util.SendUtil;
import com.hgsoft.obd.util.SerialNumberUtil;
import com.hgsoft.system.utils.ByteUtil;

/**
 * AGPS数据包升级
 * 
 * @author sujunguang 2016年1月7日 下午6:23:08
 */
@Service
public class AGPSService {
	@Resource
	private CarGSPTrackService carGSPTrackService;
	@Resource
	private AGPSDao agpsDao;
	@Resource
	private OBDStockInfoService obdStockInfoService;
	@Resource
	private ObdGroupService obdGroupService;
	@Resource
	private SendUtil sendUtil;
	
	private static  String AGPS_ADDR = PropertiesUtil.getInstance("owner.properties").readProperty("AGPS_ADDR", "agps.u-blox.com");
	private static  Integer AGPS_PORT = Integer.valueOf(PropertiesUtil.getInstance("owner.properties").readProperty("AGPS_PORT", "46434"));
	private static  String AGPS_REQ = PropertiesUtil.getInstance("owner.properties").readProperty("AGPS_REQ", "cmd=full;user=403291856@qq.com;pwd=Buxogx;lat={0};lon={1};pacc={2}\n");
	
	public String sendAGPS(String obdSn, Integer packageNum) throws Exception{
		String data = "";
		String agpsData = getAgpsData(obdSn,packageNum);
		if(StringUtils.isEmpty(agpsData)){
			return null;
		}
		data = ObdConstants.Server_ResponseAGPS_OBD_Cmd + agpsData;
		Integer serialNum = SerialNumberUtil.getSerialnumber(obdSn);
		return (String) sendUtil.msgSendGetResult(obdSn, serialNum, data, null);
	}
	/**
	 * AGPS
	 * @param obdSn
	 * @param packageNum
	 * @return null-失败
	 * @throws Exception 
	 */
	public String getAgpsData(String obdSn, Integer packageNum) throws Exception {
		// 如果请求包是第0个,需向ublox服务器请求
		if (packageNum == 0) {
//			StringBuffer sb = new StringBuffer("");
			String lng = "";
			String lngStr = "";
			String lat = "";
			String latStr = "";
			String  radius="300000";//半径
//			sb.append("cmd=full;");// 请求方式
//			sb.append("user=403291856@qq.com;");// 请求用户名
//			sb.append("pwd=Buxogx;");// pwd
			// 获取车辆最新的经纬度
			CarGSPTrack carGSPTrack = carGSPTrackService.findLastBySn(obdSn);
			if (carGSPTrack == null) {
				System.err.println("获取车辆最新的经纬度为空！！！");
				OBDStockInfo obdStockInfo = obdStockInfoService.queryBySN(obdSn);// 查询设备
				if(obdStockInfo!=null){
					String groupNum = obdStockInfo.getGroupNum();
					if(!StringUtils.isEmpty(groupNum)){
						ObdGroup obdGroup = obdGroupService.queryByGroupNum(groupNum);
						if(obdGroup!=null){
							lngStr =obdGroup.getLongitude();//经度
							latStr = obdGroup.getLatitude();//纬度
							radius = obdGroup.getRadius();//半径
						}
					}
				}
			}else{
				lng = carGSPTrack.getLongitude();// 精度
				lngStr = CoordinateTransferUtil.lnglatTransferDouble(lng);// 转成小数点
				lat = carGSPTrack.getLatitude();// 维度
				latStr = CoordinateTransferUtil.lnglatTransferDouble(lat);// 转成小数点
			}
			//默认是广中中心
			if(StringUtils.isEmpty(lngStr)||StringUtils.isEmpty(latStr)){
				lngStr="113.316608";
				latStr="23.140437";
			}
//			sb.append("lat=" + latStr + ";");// 纬度
//			sb.append("lon=" + lngStr + ";");// 经度
//			sb.append("pacc="+radius+"\n");// 精度
			AGPS_REQ = MessageFormat.format(AGPS_REQ, latStr, lngStr, radius);
			// 获取到AGPS数据包:"cmd=full;user=403291856@qq.com;pwd=Buxogx;lat=113.34;lon=23.17;pacc=20000\n"
			String command = "8014";
			try {
				//向第三方发送请求
				HelloClient.clientConnectServer(AGPS_ADDR, AGPS_PORT, AGPS_REQ, obdSn, command);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			// 线程读取返回的数据
			// 如果服务端在：HelloServerHandler.channelRead()方法里读取到客户端的响应消息，则线程获取消息。
			ClientMsgThread thread = new ClientMsgThread(obdSn);
			thread.start();
			try {
				// 线程同步锁，顺序执行
				thread.join(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// 获取到客户端的响应消息
			UbloxData ublox = (UbloxData) thread.getResMsg();
			Date date = new Date();
			if (ublox != null) {
				int len = ublox.getSize();// agps数据长度
				int flag = ublox.getType();// 成功
				if (flag == 0) {
					byte[] byteArr = ublox.getAgpsData();// AGPS二进制数组
					// 1数据保存入库
					AGPS agps = new AGPS();
					agps.setAgpsData(byteArr);
					agps.setCreateTime(date);
					agps.setLatitude(lat);
					agps.setLongitude(lng);
					agps.setOprecision(radius);
					agps.setSize(len);
					agps.setObdSn(obdSn);
					agpsDao.save(agps);// 保存入库

					// 清除map记录
					Map<String, Object> respMap = RunningData.getIdClientMap();
					respMap.remove(obdSn);// 获取响应消息后，清除对应的消息
					return agpsDataStr(byteArr, packageNum);
				} else if (flag == 1) {
					String err = ublox.getError();
					System.err.println(err);
				}
			}
		} else {
			 //1获取数据库里的最新的升级包文件
			 AGPS agps=agpsDao.findLastBySn(obdSn);
			 if(agps==null){
				 System.err.println("获取数据库里的最新的升级包文件为空！！！");
				 return null;
			 }
			 //2将数据库里的文件转成file
			 byte[] fileByte=agps.getAgpsData();
			 if(fileByte==null||fileByte.length==0){
				 return null;
			 }
			 return agpsDataStr(fileByte,packageNum);
		}
		return null;
	}

	/**
	 * AGPS数据包生成
	 * @param packageNum 包序号
	 * @return
	 * @throws Exception
	 */
	public String agpsDataStr(byte[] fileByte, int packageNum) throws Exception {
		if(packageNum > (fileByte.length/800) ){
			throw new OBDException("请求长度超过了数据包的分包数！");
		}
		byte[] fileMsgByte = FileUtil.cutByte(fileByte, packageNum);
		// 将byte数组转成16进制的字符串,包数据
		String fileMsgHex = ByteUtil.bytesToHexString(fileMsgByte);
		// 包序号packageNum:2个byte
		String packNumHex = StrUtil.strAppend(Integer.toHexString(packageNum),4, 0, "0");
		// 当前包长度
		String packLen = StrUtil.strAppend(Integer.toHexString(fileMsgByte.length), 4, 0, "0");
		StringBuffer msg = new StringBuffer("");
		msg.append(packNumHex).append(packLen).append(fileMsgHex);
		if (packageNum == 0) {
			// 固件长度,仅当包序号为0时有此项,以byte[]实际长度为准
			String packSumLenHex = StrUtil.strAppend(Integer.toHexString(fileByte.length), 4 * 2, 0, "0");
			// 固件异或校验和,仅当包序号为0时有此项
			String fileHex = ByteUtil.bytesToHexString(fileByte);
			ServerResponses sr = new ServerResponses();
			String checkCode = sr.xor(fileHex);// 固件异或和
			msg.append(packSumLenHex).append(checkCode);
			String dateStr = DateUtil.getTimeString(new Date(), "yyMMddHHmmss");// 星历数据更新时间
			msg.append(dateStr);// 星历数据更新时间
		}
		return msg.toString();
	}

}
