package com.hgsoft.obd.server;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import io.netty.util.internal.ConcurrentSet;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.hgsoft.carowner.service.OBDStockInfoService;
import com.hgsoft.common.utils.PropertiesUtil;
import com.hgsoft.obd.service.ObdService;
import com.hgsoft.obd.util.DriveTimeUtil;

/**
 * 全局数据共享区
 * 
 * @author sujunguang 2015年12月18日 下午4:02:52
 */
@Service
public class GlobalData {
	
	private static OBDStockInfoService obdStockInfoService;
	private static int ReadTimeoutCount = Integer.valueOf(PropertiesUtil.getInstance("owner.properties").readProperty("readTimeoutCount", "30"));
	//域名单查询个数
	public static int DomainCount = Integer.valueOf(PropertiesUtil.getInstance("params.properties").readProperty("domainCount", "4"));
	public static OBDStockInfoService getObdStockInfoService() {
		return obdStockInfoService;
	}
	@Resource
	public void setObdStockInfoService(OBDStockInfoService obdStockInfoService) {
		GlobalData.obdStockInfoService = obdStockInfoService;
	}
	
	private static DriveTimeUtil driveTimeUtil;
	
	public static DriveTimeUtil getDriveTimeUtil() {
		return driveTimeUtil;
	}
	@Resource
	public void setDriveTimeUtil(DriveTimeUtil driveTimeUtil) {
		GlobalData.driveTimeUtil = driveTimeUtil;
	}
	
	private static ObdService obdService;
	
	public static ObdService getObdService() {
		return obdService;
	}
	@Resource
	public void setObdService(ObdService obdService) {
		GlobalData.obdService = obdService;
	}

	/**
	 * 保存服务器连接，自动断开连接会自动删除对应channel
	 */
	public static final ChannelGroup CHANNEL_GROUP = new DefaultChannelGroup(
			GlobalEventExecutor.INSTANCE);
	/**
	 * 保存全局OBD对应Channel的数据集合 key:唯一对应设备 ；value:对应设备连接
	 */
	public static final Map<String, Channel> OBD_CHANNEL = new ConcurrentHashMap<String, Channel>();

	/**
	 * OBD设备ACK信息存储 
	 * key:设备+(下发)流水号 	value:报文数据ACK(应答)
	 */
	public static final Map<String, Object> OBD_ACK_OR_QueryData = Collections.synchronizedMap(new HashMap<String, Object>());//new ConcurrentHashMap<String, Object>();
	//添加存在Key的数据
	public static void putExitsQueryDataToMap(String key,Object obj){
		if(OBD_ACK_OR_QueryData.containsKey(key)){
			OBD_ACK_OR_QueryData.put(key, obj);
		}
	}
	
	/**
	 * 存储分包数据，再合并
	 */
	public static final Map<String, Map<Integer,String>> OBD_Packet = new ConcurrentHashMap<String,  Map<Integer,String>>();
	/**
	 * 存储分包数据，包数量
	 */
	public static final Map<String, Integer> OBD_PacketNum = new ConcurrentHashMap<String, Integer>();

	public static void putPacketToMap(String key,Integer packetNo,String packetData){
		Map<Integer,String> data = new TreeMap<Integer,String>();
		if(OBD_Packet.containsKey(key)){
			data = OBD_Packet.get(key);
			data.put(packetNo, packetData);
			OBD_Packet.put(key, data);
		}else{
			data.put(packetNo, packetData);
			OBD_Packet.put(key, data);
		}
	}

	/**
	 * channel read timeout count
	 */
	public static final Map<Channel, AtomicInteger> ChannelReadTimeoutCount =  new ConcurrentHashMap<Channel, AtomicInteger>();
	/**
	 * Channel读取超时次数是否达到
	 * @param channel
	 * @return
	 */
	public static boolean isReadTimeoutCount(Channel channel){
		AtomicInteger atomicInteger = new AtomicInteger(1);
		if(ChannelReadTimeoutCount.containsKey(channel)){
			atomicInteger = ChannelReadTimeoutCount.get(channel);
			if(atomicInteger.addAndGet(1) == ReadTimeoutCount){
				return true;
			}
		}else{
			ChannelReadTimeoutCount.put(channel, atomicInteger);
		}
		return false;
	}
	
	/**
	 * 根据channel删除对应设备连接关联——离线
	 * 
	 * @param channel
	 */
	public static void removeOBDChannelByChannel(Channel channel) {
		for (Map.Entry<String, Channel> entry : OBD_CHANNEL.entrySet()) {
			if (channel == entry.getValue()) {
				String obdSn = entry.getKey();
				OBD_CHANNEL.remove(obdSn);
				driveTimeUtil.startRestByRedis(obdSn, new Date());//记录离线时间
				driveTimeUtil.putDrivingByRedis(obdSn,"0");
				driveTimeUtil.putRestingByRedis(obdSn, "1");
				//清除位置数据部分统计
				if(GlobalData.OBD_PositionCarSpeed.containsKey(obdSn)){
					GlobalData.OBD_PositionCarSpeed.remove(obdSn);
				}
				if(GlobalData.OBD_PositionGPSSpeed.containsKey(obdSn)){
					GlobalData.OBD_PositionGPSSpeed.remove(obdSn);
				}
				if(GlobalData.OBD_PositionECUWarn.containsKey(obdSn)){
					GlobalData.OBD_PositionECUWarn.remove(obdSn);
				}
				// 离线更新操作
				try {
					obdService.removeTTL(obdSn);
					obdStockInfoService.obdSetOffLine(obdSn);
				} catch (OBDException e) {
					e.printStackTrace();
				}
				break;//找到对应的channel则立马退出。
			}
		}
	}

	/**
	 * 将连接上来的设备跟会话保存起来——在线
	 * 
	 * @param obdSn
	 *            设备obdSn
	 * @param channel
	 *            会话通道
	 */
	public static final Map<String, Date> OBD_OnLine_Time = new ConcurrentHashMap<String, Date>();
	public static void putToOBDChannel(String obdSn,Channel channel) {
		OBD_OnLine_Time.put(obdSn, new Date());
		OBD_CHANNEL.put(obdSn, channel);
	}
	
	public static void putOBDInitChannel(String obdSn,Channel channel) {
		OBD_CHANNEL.put(obdSn, channel);
		// 在线更新操作
		try {
			obdStockInfoService.obdSetOnLine(obdSn);
		} catch (OBDException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 通过OBDSN获取Channel
	 * 
	 * @param obdSn
	 *            设备SN
	 * @return null则表明设备离线
	 */
	public static Channel getChannelByObdSn(String obdSn) {
		Channel channel = OBD_CHANNEL.get(obdSn);
		return channel;
	}

	public synchronized static void clearChannel() {
		OBD_CHANNEL.clear();
	}
	
	/**
	 * 发送结果是否成功
	 * @param result
	 * @return
	 */
	public static boolean isSendResultSuccess(String result){
		if(ObdConstants.SendResultSucessState00.equals(result) ||
				ObdConstants.SendResultSucessState01.equals(result)){
			return true;
		}
		return false;
	}
	
	//存放未注册设备
	public static Set<String> ObdUnRegSet = new ConcurrentSet<String>(); 
	
	//存放有前次休眠原因
	public static Set<String> ObdLastSleep = new ConcurrentSet<String>(); 
	
	public final static String HostIP = PropertiesUtil.getInstance("owner.properties").readProperty("hostIP", "127.0.0.1:6060");
	/**
	 * 存放查询数据之后多数据进行整合：分页数据
	 * obdSn 
	 * 标号 
	 *      -1：数据不完整   
	 * 		0：数据完整
	 * 		1,2,...n:分页数据
	 */
	public static final Map<String, Map<Integer,String>> OBD_QueryDataMap = new ConcurrentHashMap<>();
	public static void putQueryDataMap(String obdSn,Integer offset,String data){
		if(!StringUtils.isEmpty(obdSn)){
			Map<Integer,String> map = new HashMap<Integer,String>();
			if(OBD_QueryDataMap.containsKey(obdSn)){
				map = OBD_QueryDataMap.get(obdSn);
			}
			map.put(offset, data);
			OBD_QueryDataMap.put(obdSn, map);
		}
	}
	/**
	 * 查询数据整合结果
	 * @param obdSn
	 * @return -1数据不完整 
	 */
	public static String getQueryDataMap(String obdSn){
		if(!StringUtils.isEmpty(obdSn)){
			Map<Integer,String> map = new HashMap<Integer,String>();
			if(OBD_QueryDataMap.containsKey(obdSn)){
				map = OBD_QueryDataMap.get(obdSn);
				if(map.containsKey(0)){
					//进行数据整合
					String data = "";
					for (int i = 1; i < map.size(); i++) {
						String str = map.get(i);
						if(StringUtils.isEmpty(str)){
							return "-1";
						}
						data += str;
					}
					OBD_QueryDataMap.remove(obdSn);//清除
					return data.substring(0, data.length()-1);
				}
			}
		}
		return null;
	}

	/**
	 * 存放设备位置报文的上一流水号
	 */
	public static final Map<String, String> OBD_PositionWaterNo = new ConcurrentHashMap<>();
	/**
	 * 存放设备位置报文是否发生ECU故障告警
	 */
	public static final Map<String, Boolean> OBD_PositionECUWarn = new ConcurrentHashMap<>();
	/**
	 * 存放设备位置报文最新车速（OBD速度）
	 */
	public static final Map<String, Integer> OBD_PositionCarSpeed = new ConcurrentHashMap<>();
	/**
	 * 存放设备位置报文最新GPS速度
	 */
	public static final Map<String, Integer> OBD_PositionGPSSpeed = new ConcurrentHashMap<>();
	/**
	 * 是否打印字符格式为2字符一空格
	 */
	public static boolean isPrint2Char = Boolean.valueOf(PropertiesUtil.getInstance("params.properties").readProperty("isPrint2Char", "false"));
	/**
	 * tiredDrive statics 疲劳驾驶统计，在线时间大于此则不列入统计任务:12*60分钟
	 */
	public static long tiredDriveTime = Long.valueOf(PropertiesUtil.getInstance("params.properties").readProperty("tiredDriveTime", "720")) * 60 * 1000;
	/**
	 * 间隔多久清除设备的一些内存无用数据（分钟）
	 */
	public static long cleanOBDDataTime = Long.valueOf(PropertiesUtil.getInstance("params.properties").readProperty("cleanOBDDataTime", "720")) * 60 * 1000;
	
	/***************************************配置文件：开关等参数********************************************/
	public static boolean openLastPositionRedis = Boolean.valueOf(PropertiesUtil.getInstance("params.properties").readProperty("openLastPositionRedis", "false"));
	public static boolean openLastHandShakeRedis = Boolean.valueOf(PropertiesUtil.getInstance("params.properties").readProperty("openLastHandShakeRedis", "true"));
	public static boolean cleanOBDDataSwitch = Boolean.valueOf(PropertiesUtil.getInstance("params.properties").readProperty("cleanOBDDataSwitch", "true"));
	public static boolean agpsInMemorySwitch = Boolean.valueOf(PropertiesUtil.getInstance("params.properties").readProperty("agpsInMemorySwitch", "true"));
	public static boolean agpsInDBSwitch = Boolean.valueOf(PropertiesUtil.getInstance("params.properties").readProperty("agpsInDBSwitch", "true"));
	public static long agpsInMemoryLastTime = Long.valueOf(PropertiesUtil.getInstance("params.properties").readProperty("agpsInMemoryLastTime", "20")) * 60 * 1000;
	public static long agpsInDBLastTime = Long.valueOf(PropertiesUtil.getInstance("params.properties").readProperty("agpsInDBLastTime", "20")) * 60 * 1000;
	public static boolean buttonAlarmSwitch = Boolean.valueOf(PropertiesUtil.getInstance("params.properties").readProperty("buttonAlarmSwitch", "false"));
	public static Integer configSendCount = Integer.valueOf(PropertiesUtil.getInstance("params.properties").readProperty("configSendCount", "3"));
}
