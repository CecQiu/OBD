package com.hgsoft.obd.service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.hgsoft.carowner.entity.CarGSPTrack;
import com.hgsoft.carowner.entity.ObdGroup;
import com.hgsoft.carowner.entity.ObdGroupAGPS;
import com.hgsoft.carowner.service.CarGSPTrackService;
import com.hgsoft.carowner.service.ObdGroupAGPSService;
import com.hgsoft.carowner.service.ObdGroupService;
import com.hgsoft.common.service.ServerResponses;
import com.hgsoft.common.utils.CoordinateTransferUtil;
import com.hgsoft.common.utils.DateUtil;
import com.hgsoft.common.utils.FileUtil;
import com.hgsoft.common.utils.PropertiesUtil;
import com.hgsoft.common.utils.StrUtil;
import com.hgsoft.obd.client.AGPSClientExecutor;
import com.hgsoft.obd.server.GlobalData;
import com.hgsoft.obd.server.OBDException;
import com.hgsoft.system.utils.ByteUtil;

/**
 * AGPS数据包处理 (新)
 * @author sjg
 * @version 2016年12月13日
 */
@Service
public class AGPSHandlerService {
	private static Logger obdHandlerACKLogger = LogManager.getLogger("obdHandlerACKLogger");
	@Resource
	private ObdGroupAGPSService groupAGPSService;
	@Resource
	private ObdGroupService obdGroupService;
	@Resource
	private CarGSPTrackService carGSPTrackService;
	
	public static Map<String,ObdGroupAGPS> agpsGroupDatas = new ConcurrentHashMap<String, ObdGroupAGPS>();
	private static Map<String,byte[]> obdAGPSData = new ConcurrentHashMap<String, byte[]>(); 

	private static  String AGPS_ADDR = PropertiesUtil.getInstance("owner.properties").readProperty("AGPS_ADDR", "agps.u-blox.com");
	private static  Integer AGPS_PORT = Integer.valueOf(PropertiesUtil.getInstance("owner.properties").readProperty("AGPS_PORT", "46434"));
	private static  String AGPS_REQ = PropertiesUtil.getInstance("owner.properties").readProperty("AGPS_REQ", "cmd=full;user=403291856@qq.com;pwd=Buxogx;lat={0};lon={1};pacc={2}\n");

	public String handler(String obdSn, int packetNum) throws Exception{
		obdHandlerACKLogger.info("<"+obdSn+">请求AGPS升级包数据,序号:"+packetNum);
		try {
			CarGSPTrack carGSPTrack = carGSPTrackService.findLastBySn(obdSn);
			Double lon = null,lat = null;
			String groupNum = null;
			byte[] result = null;
			ObdGroupAGPS obdGroupAGPSInMemory = null;
			ObdGroupAGPS obdGroupAGPSInDB = null;
			ObdGroupAGPS obdGroupAGPSInNetwork = null;
			obdHandlerACKLogger.info("<"+obdSn+">请求AGPS升级包数据,最新位置:"+carGSPTrack);
			if(carGSPTrack != null){
				try {
					lon = Double.parseDouble(CoordinateTransferUtil.lnglatTransferDouble(carGSPTrack.getLongitude()));
					lat = Double.parseDouble(CoordinateTransferUtil.lnglatTransferDouble(carGSPTrack.getLatitude()));
					groupNum = getGroupNumByPointToGroup(lon, lat);
					obdHandlerACKLogger.info("<"+obdSn+">请求AGPS升级包数据,最近分组:"+groupNum);
				} catch (Exception e) {
					e.printStackTrace();
					obdHandlerACKLogger.error("<"+obdSn+">请求AGPS升级包数据,最近分组:"+groupNum, e);
				}
			}
			Date nowTime = new Date();
			if (packetNum == 0) {
				obdHandlerACKLogger.info("<"+obdSn+">请求AGPS升级包数据,新获取数据[内存,数据库,网络],第一个包序号:"+packetNum);
				obdGroupAGPSInMemory = fromMemory(groupNum);
				obdHandlerACKLogger.info("<"+obdSn+">请求AGPS升级包数据,从[内存]获取结果:"+obdGroupAGPSInMemory);
				if (obdGroupAGPSInMemory == null) {
					obdGroupAGPSInDB = fromDB(groupNum);
					obdHandlerACKLogger.info("<"+obdSn+">请求AGPS升级包数据,从[数据库]获取结果:"+obdGroupAGPSInDB);
					if (obdGroupAGPSInDB == null) {
						obdGroupAGPSInNetwork = fromNetwork(lon, lat, "20000");
						obdHandlerACKLogger.info("<"+obdSn+">请求AGPS升级包数据,从[网络]获取结果:"+obdGroupAGPSInNetwork);
						if(obdGroupAGPSInNetwork != null){
							result = obdGroupAGPSInNetwork.getData();
						}
					}else if(obdGroupAGPSInDB != null && nowTime.getTime()-obdGroupAGPSInDB.getUpdateTime().getTime() <= GlobalData.agpsInDBLastTime){
						result = obdGroupAGPSInDB.getData();
						obdHandlerACKLogger.info("<"+obdSn+">请求AGPS升级包数据,从[数据库]获取到结果,有效时间:"+GlobalData.agpsInDBLastTime/60/1000+"分钟");
					}
				}else if(obdGroupAGPSInMemory != null && nowTime.getTime()-obdGroupAGPSInMemory.getUpdateTime().getTime() <= GlobalData.agpsInMemoryLastTime){
					result = obdGroupAGPSInMemory.getData();
					obdHandlerACKLogger.info("<"+obdSn+">请求AGPS升级包数据,从[内存]获取到结果,有效时间:"+GlobalData.agpsInMemoryLastTime/60/1000+"分钟");
				}
				
				if(result == null || result.length <= 0){//最终还是获取不到数据，再次从时间失效数据上获取
					if(obdGroupAGPSInMemory != null){
						result = obdGroupAGPSInMemory.getData();
						obdHandlerACKLogger.info("<"+obdSn+">请求AGPS升级包数据,最终还是获取不到数据,从[内存]获取到结果,不管有效时间。");
					}
				}
				if(result == null || result.length <= 0){//最终还是获取不到数据，再次从时间失效数据上获取
					if(obdGroupAGPSInDB != null){
						result = obdGroupAGPSInDB.getData();
						obdHandlerACKLogger.info("<"+obdSn+">请求AGPS升级包数据,最终还是获取不到数据,从[数据库]获取到结果,不管有效时间。");
					}
				}
				if(result != null && result.length > 0){
					obdAGPSData.put(obdSn, result);//存到内存
					return agpsDataStr(result, packetNum);
				}
			} else {
				obdHandlerACKLogger.info("<"+obdSn+">请求AGPS升级包数据,直接从[临时内存]中获取,包序号:"+packetNum);
				byte[] tmpResult = obdAGPSData.get(obdSn);
				if(tmpResult != null){
					if(packetNum+1 == totalPackets(tmpResult, 800)){
						//请求最后一个包，则清除对应的数据
						obdHandlerACKLogger.info("<"+obdSn+">请求AGPS升级包数据,直接清除[临时内存]数据,最后一个包:"+(packetNum+1));
						obdAGPSData.remove(obdSn);
					}
					return agpsDataStr(tmpResult, packetNum);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			obdHandlerACKLogger.error("<"+obdSn+">请求AGPS升级包数据,异常！", e);
		}
		return null;
	}
	
	/**
	 * 从内存获取
	 * @param groupNum 分组编号
	 * @return
	 */
	public ObdGroupAGPS fromMemory(String groupNum){
		if(GlobalData.agpsInMemorySwitch && !StringUtils.isEmpty(groupNum)){
			obdHandlerACKLogger.info("读取内存AGPS分组数据,分组编号:"+groupNum);
			if(agpsGroupDatas.containsKey(groupNum)){
				ObdGroupAGPS obdGroupAGPS = agpsGroupDatas.get(groupNum);
				return obdGroupAGPS;
//				for(Date date : mapData.keySet()){
//					if(new Date().getTime() - date.getTime() < GlobalData.agpsInMemoryLastTime){
//						return mapData.get(date);
//					}else{
//						obdHandlerACKLogger.info("AGPS分组数据最新时间失效"+GlobalData.agpsInMemoryLastTime+"分钟:"+ThreadLocalDateUtil.formatDate("yyyy-MM-dd HH:mm:ss", date));
//					}
//				}
			}
		}
		return null;
	}

	/**
	 * 从数据库获取
	 * @param groupNum 分组编号
	 * @return
	 */
	public ObdGroupAGPS fromDB(String groupNum){
		if(GlobalData.agpsInDBSwitch && !StringUtils.isEmpty(groupNum)){
			obdHandlerACKLogger.info("读取数据库AGPS分组数据,分组编号:"+groupNum);
			ObdGroupAGPS groupAGPS = groupAGPSService.queryByGroupNum(groupNum);
			if(groupAGPS == null){
				return null;
			}
//			if(new Date().getTime() - groupAGPS.getUpdateTime().getTime() < GlobalData.agpsInDBLastTime){
				byte[] data = groupAGPS.getData();
				if(GlobalData.agpsInMemorySwitch && data != null && data.length > 0){//内存开关
					agpsGroupDatas.put(groupNum, groupAGPS);
				}
				return groupAGPS;
//			}else{
//				obdHandlerACKLogger.info("AGPS分组数据从数据库获取，最新时间失效"+GlobalData.agpsInDBLastTime+"分钟:"+ThreadLocalDateUtil.formatDate("yyyy-MM-dd HH:mm:ss", groupAGPS.getUpdateTime()));
//				obdHandlerACKLogger.info("读取数据库AGPS分组数据,分组编号:"+groupNum);
//			}
		}
		return null;
	}
	
	/**
	 * 从网络获取
	 * @return
	 * @throws Exception
	 * 
	 */
	public ObdGroupAGPS fromNetwork(Double lon, Double lat, String pacc) throws Exception{
		if(lon == null){
			lon = 113.316608;
		}
		if(lat == null){
			lat = 23.140437;
		}
		AGPS_REQ = MessageFormat.format(AGPS_REQ, lat, lon, pacc);
		obdHandlerACKLogger.info("读取设备AGPS数据,请求数据:"+AGPS_REQ);
		AGPSClientExecutor agpsClientExecutor = new AGPSClientExecutor(AGPS_ADDR+":"+AGPS_PORT);
		byte[] data = agpsClientExecutor.send(AGPS_REQ);
		ObdGroupAGPS obdGroupAGPS = null;
		if(data != null && data.length > 0){
			obdGroupAGPS = new ObdGroupAGPS();
			obdGroupAGPS.setCreateTime(new Date());
			obdGroupAGPS.setUpdateTime(new Date());
			obdGroupAGPS.setData(data);
			obdGroupAGPS.setSize(data.length);
		}
		return obdGroupAGPS;
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
	
	/**
	 * 计算包能分成多少个
	 * @param bytes 数据
	 * @param sizePer 每个多大
	 * @return
	 */
	public int totalPackets(byte[] bytes, int sizePer){
		int length = bytes.length;
		int totalPackets = length / sizePer;
		if(length % sizePer != 0){
			totalPackets++;
		}
		return totalPackets;
	}
	
	/**
	 * 获得设备到分组点的最近的分组编号
	 * @param obdSn
	 * @return
	 */
	public String getGroupNumByPointToGroup(double lon, double lat){
		try {
			List<ObdGroup> obdGroups = obdGroupService.queryList();
			Map<String, double[]> groupP = new HashMap<String, double[]>();
			for (ObdGroup obdGroup : obdGroups) {
				groupP.put(obdGroup.getGroupNum(), new double[] { Double.parseDouble(obdGroup.getLongitude()),
						Double.parseDouble(obdGroup.getLatitude()) });
			}
			return searchShortestPathPointToGroupNum(lon, lat, groupP);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 寻找最短路径：点到分组多点的最短分组
	 * @param x0 点，经度
	 * @param y0 点，维度
	 * @param groupP 分组集合点
	 * @return 返回分组编号
	 * @throws Exception 
	 */
	public String searchShortestPathPointToGroupNum(double x0, double y0, Map<String,double[]> groupP) throws Exception{
		if(groupP == null || groupP.size() <= 0){
			throw new Exception("分组点不能为空！");
		}
		List<Double> list = new ArrayList<Double>();
		Map<Double,String> distanceGroupNum = new ConcurrentHashMap<Double, String>();
		for (String groupNum : groupP.keySet()) {
			double[] pXY = groupP.get(groupNum);
			double y = pXY[1]-y0;
			double x = pXY[0]-x0;
			double distance = Math.sqrt(x*x + y*y);
			distanceGroupNum.put(distance, groupNum);
			list.add(distance);
		}
		Collections.sort(list);
		return distanceGroupNum.get(list.get(0));
	}
	
	public static void main(String[] args) {
		System.out.println(AGPS_REQ);
	}
}
