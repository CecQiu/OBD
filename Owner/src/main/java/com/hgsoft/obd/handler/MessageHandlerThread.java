package com.hgsoft.obd.handler;

import io.netty.channel.Channel;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.hgsoft.carowner.entity.OBDStockInfo;
import com.hgsoft.carowner.entity.ObdUnReg;
import com.hgsoft.carowner.service.OBDStockInfoService;
import com.hgsoft.carowner.service.ObdUnRegService;
import com.hgsoft.common.message.OBDMessage;
import com.hgsoft.common.message.RunningData;
import com.hgsoft.common.utils.IDUtil;
import com.hgsoft.obd.server.GlobalData;
import com.hgsoft.obd.server.ObdConstants;
import com.hgsoft.obd.service.ServerRequestQueryService;

@Component
public class MessageHandlerThread/* implements Runnable */{

	private static Logger obdHandlerLogger = LogManager.getLogger("obdHandlerLogger");
	private static Logger obdLogger = LogManager.getLogger("obdLogger");

	private static ThreadLocal<OBDMessage> omThreadLocal = new ThreadLocal<OBDMessage>();
	private static ThreadLocal<Channel> channelThreadLocal = new ThreadLocal<Channel>();
	
	@Resource
	MessageObdInitHandler messageObdInitHandler;
	@Resource
	MessageObdRequestOrACKHandler messageObdRequestOrACKHandler;
	@Resource
	MessageObdTravelHandler messageObdTravelHandler;
	@Resource
	MessageObdPositionHandler messageObdPositionHandler;
	@Resource
	MessageObdHandler messageObdHandler;
	@Resource
	MessageObdExtensionDataHandler messageObdExtensionDataHandler;
	@Resource
	MessageObdExtension2DataHandler messageObdExtension2DataHandler;
	@Resource
	MessageObdUpgradeResultHandler messageObdUpgradeResultHandler;
	@Resource
	MessageObdRequestUpgradeHandler messageObdRequestUpgradeHandler;
	@Resource
	private OBDStockInfoService obdStockInfoService;
	@Resource
	private ObdUnRegService obdUnRegService;
	
	@Resource
	private ServerRequestQueryService serverRequestQueryService;
	
	public OBDMessage getOm() {
		return om;
	}
	public void setOm(OBDMessage om) {
		omThreadLocal.set(om);
	}
	public Channel getChannel() {
		return channel;
	}
	public void setChannel(Channel channel) {
		channelThreadLocal.set(channel);
	}
	
	private OBDMessage om;
	private Channel channel;

    public static AtomicInteger count = new AtomicInteger(0);
	public MessageHandlerThread() {
	}
	
	private MessageHandlerThread handlerThread;

	@PostConstruct
	public void init() {
		handlerThread = this;
	}

	public MessageHandlerThread instance(OBDMessage om, Channel channel) {
		omThreadLocal.set(om);
		channelThreadLocal.set(channel);
		return handlerThread;
	}
	
	public void run() throws Exception {
		OBDMessage om = omThreadLocal.get();
		Channel channel = channelThreadLocal.get();
		omThreadLocal.remove();
		channelThreadLocal.remove();
		count.addAndGet(1);
		String obdSn = om.getId();
		if(om.getCommand().equals(ObdConstants.OBD_Init_Cmd)//上传初始化数据包
				|| om.getCommand().equals(ObdConstants.OBD_Position_Cmd)//上传位置数据包
				|| om.getCommand().equals(ObdConstants.OBD_Travel_Cmd)//上传位置数据包
		   ){//不处理未注册设备
			
			OBDStockInfo obdStockInfo =  obdStockInfoService.queryBySN(obdSn);
			if(obdStockInfo == null 
					&& !om.getCommand().equals(ObdConstants.OBD_RequestUpgradeData_Cmd)//请求升级数据包
					&& !om.getCommand().equals(ObdConstants.OBD_UpgradeResult_Cmd)//上传升级结果包
				){//请求的不加判断设备是否存在
				obdHandlerLogger.info("【设备不存在】:设备号"+ obdSn);
				if(obdUnRegService.queryByObdSn(obdSn) == null){
					ObdUnReg obdUnReg = new ObdUnReg();
					obdUnReg.setId(IDUtil.createID());
					obdUnReg.setObdSn(obdSn);
					obdUnReg.setCreateTime(new Date());
					obdUnRegService.obdUnRegSave(obdUnReg);
				}
				GlobalData.ObdUnRegSet.add(obdSn);
				return;
			}
		}
		
		// obd状态
		Map<String, Date> obdState = RunningData.getObdStateMap();
		obdState.put(obdSn, new Date());
		//TODO 业务处理。。。
		if (om != null) {
			GlobalData.ChannelReadTimeoutCount.remove(channel);//移除次数
			String returnMsgBody = "";
			String waterNo = om.getWaterNo();
			long beginTime = System.currentTimeMillis();
			switch (om.getCommand()) {
			case ObdConstants.OBD_Init_Cmd:// 设备初始化
				obdHandlerLogger.info("开始进行【设备初始化】处理......"+obdSn);
				GlobalData.putOBDInitChannel(obdSn, channel);//存放设备对应Channel
				returnMsgBody = messageObdInitHandler.entry(om);
				obdHandlerLogger.info("结束进行【设备初始化】处理."+obdSn+",结果："+returnMsgBody);
				obdHandlerLogger.info("【设备初始化耗时统计】"+obdSn+"->"+waterNo+",耗时->"+(System.currentTimeMillis()-beginTime));
				break;
				
			case ObdConstants.OBD_Position_Cmd://位置数据上传
				obdHandlerLogger.info("开始进行【位置数据上传】处理......"+obdSn);
				returnMsgBody = messageObdPositionHandler.entry(om);
				obdHandlerLogger.info("结束进行【位置数据上传】处理"+obdSn+",结果："+returnMsgBody);
				obdHandlerLogger.info("【位置数据上传耗时统计】"+obdSn+"->"+waterNo+",耗时->"+(System.currentTimeMillis()-beginTime));
				break;
				
			case ObdConstants.OBD_Travel_Cmd://行程记录数据
				obdHandlerLogger.info("开始进行【行程记录数据】处理....."+obdSn);
				returnMsgBody = messageObdTravelHandler.entry(om);
				obdHandlerLogger.info("结束进行【行程记录数据】处理"+obdSn+",结果："+returnMsgBody);
				obdHandlerLogger.info("【行程记录数据耗时统计】"+obdSn+"->"+waterNo+",耗时->"+(System.currentTimeMillis()-beginTime));
				break;
			
			case ObdConstants.OBD_DeviceData_Cmd://设备数据上传
				obdHandlerLogger.info("开始进行【设备数据上传】处理...."+obdSn);
				returnMsgBody = messageObdHandler.entry(om);
				obdHandlerLogger.info("结束进行【设备数据上传】处理"+obdSn+",结果："+returnMsgBody);
				obdHandlerLogger.info("【设备数据上传耗时统计】"+obdSn+"->"+waterNo+",耗时->"+(System.currentTimeMillis()-beginTime));
				break;
			
			case ObdConstants.OBD_RequestOrACK_Cmd:// 请求/ACK帧
				obdHandlerLogger.info("开始进行【请求/ACK帧】处理....."+obdSn);
				returnMsgBody = messageObdRequestOrACKHandler.entry(om);
				obdHandlerLogger.info("结束进行【请求/ACK帧】处理"+obdSn+",结果："+returnMsgBody);
				obdHandlerLogger.info("【请求/ACK帧耗时统计】"+obdSn+"->"+waterNo+",耗时->"+(System.currentTimeMillis()-beginTime));
				break;

			case ObdConstants.OBD_ExtensionData_Cmd:// 扩展数据上传
				obdHandlerLogger.info("开始进行【扩展数据上传】处理...."+obdSn);
				returnMsgBody = messageObdExtensionDataHandler.entry(om);
				obdHandlerLogger.info("结束进行【扩展数据上传】处理"+obdSn+",结果："+returnMsgBody);
				obdHandlerLogger.info("【扩展数据上传耗时统计】"+obdSn+"->"+waterNo+",耗时->"+(System.currentTimeMillis()-beginTime));
				break;

			case ObdConstants.OBD_Extension2Data_Cmd:// 扩展数据上传
				obdHandlerLogger.info("开始进行【扩展2数据上传】处理...."+obdSn);
				returnMsgBody = messageObdExtension2DataHandler.entry(om);
				obdHandlerLogger.info("结束进行【扩展2数据上传】处理"+obdSn+",结果："+returnMsgBody);
				obdHandlerLogger.info("【扩展2数据上传耗时统计】"+obdSn+"->"+waterNo+",耗时->"+(System.currentTimeMillis()-beginTime));
				break;
//
//			case ObdConstants.OBD_RequestUpgradeData_Cmd:// 请求升级数据包
//				obdHandlerLogger.info("开始进行【请求升级数据包】处理....."+obdSn);
//				messageObdRequestUpgradeHandler.entrance(om);
//				break;
				
//			case ObdConstants.OBD_UpgradeResult_Cmd:// 远程升级结果
//				obdHandlerLogger.info("开始进行【远程升级结果】处理...."+obdSn);
//				messageObdUpgradeResultHandler.entrance(om);
//				break;
				
			default:
				obdLogger.error("非法请求，没有对应的命令字！"+om.getMessage());
				obdHandlerLogger.error("非法请求，没有对应的命令字！无法处理！"+om.getMessage());
				break;
			}
			
			if("error".equals(returnMsgBody)){//说明解析有问题，不给OBD设备发ACK响应
				obdLogger.error("解析有问题！"+om.getMessage());
				return;
			}
			
		}
	}

}
