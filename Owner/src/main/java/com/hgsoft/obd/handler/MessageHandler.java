package com.hgsoft.obd.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.hgsoft.carowner.api.PushApi;
import com.hgsoft.carowner.entity.OBDPacket;
import com.hgsoft.carowner.service.ObdIpService;
import com.hgsoft.common.message.OBDMessage;
import com.hgsoft.common.utils.IDUtil;
import com.hgsoft.obd.pool.HandlerThreadPool;
import com.hgsoft.obd.server.GlobalData;
import com.hgsoft.obd.server.OBDException;
import com.hgsoft.obd.server.ObdConstants;
import com.hgsoft.obd.server.ObdServerInit;
import com.hgsoft.obd.service.ObdService;
import com.hgsoft.obd.service.ServerResponse;
import com.hgsoft.obd.util.ObdInHostUtil;
import com.hgsoft.obd.util.SendUtil;
import com.hgsoft.obd.util.SerialNumberUtil;

/**
 * 与OBD通信报文处理类
 * 
 * @author sujunguang 2015年12月14日 下午3:24:06
 */
@Component
public class MessageHandler {

	/**
	 * 报文处理日志
	 */
	private static Logger obdHandlerLogger = LogManager.getLogger("obdHandlerLogger");
	private static Logger obdLogger = LogManager.getLogger("obdLogger");
	
	@Resource
	private ObdIpService obdIpService;
	@Resource
	private ServerResponse serverResponse;
	@Resource
	private ObdService obdService;
	@Resource
	private ObdInHostUtil obdInHostUtil;
	@Resource
	private PushApi pushApi;
	@Resource
	private MessageHandlerThread messageHandlerThread;
	@Resource
	MessageObdRequestOrACKHandler messageObdRequestOrACKHandler;
	@Resource
	private SendUtil sendUtil;
	
	/** 起始符 */
	private final static String START_CODE = "aa";
	/** 结束符 */
	private final static String END_CODE = "aa";
	/** 需要转义的字符数组 */
	private final static String[][] ESCES = { { "5501", "aa" },	{ "5502", "55" } };

	private static MessageHandler messageHandel;

	@PostConstruct
	public void init() {
		messageHandel = this;
	}

	private MessageHandler() {
	}
	 
	public static MessageHandler instance() {
		if (messageHandel == null) {
			messageHandel = new MessageHandler();
		}
		return messageHandel;
	}

	/**
	 * 处理接收报文
	 * @param msg
	 *            报文字符串
	 * @param ctx
	 *            客户端实体
	 */
	@SuppressWarnings("rawtypes")
	public void handelMsg(ChannelHandlerContext ctx,AttributeKey key, String msg) {
		final Channel channel = ctx.channel();
		obdHandlerLogger.info("----------------------开始处理上传报文-------------------------");
		obdHandlerLogger.info("原始报文："+msg);
		final OBDMessage om = parseMsg(msg.replaceAll(" ", ""));
		obdHandlerLogger.info("解析报文为实体对象："+om);
		// 如果请求消息内容不正确，直接返回数据
		if (om == null) {
			// 请求报文格式不正确，直接忽略，且不做任何响应
			obdHandlerLogger.info("解析报文实体对象为null");
			return;
		}
		// 校验码
		if (!om.getCheckCodeRight()) {
			obdHandlerLogger.info("校验码:"+om.getCheckCodeRight()+"校验失败！");
			obdMsgSave(msg, om);//保存报文
			return;
		}
		final String obdSn = om.getId();// obd设备号
		final String waterNo = om.getWaterNo();// 流水号
		String command = om.getCommand();// 命令字
		//保存设备ID跟会话通道channel
		GlobalData.putToOBDChannel(obdSn, channel);
		
		obdHandlerLogger.info(obdSn+"，在线："+obdService.obdTTL(obdSn,90));
		//记录设备在哪个服务器上
		try {
			obdInHostUtil.putObdInHost(obdSn, GlobalData.HostIP);
		} catch (OBDException e1) {
			obdHandlerLogger.info(obdSn+"记录设备在哪个服务器上，异常：",e1);
			e1.printStackTrace();
		}
		obdHandlerLogger.info(obdSn+" active Channel:~~~~"+channel);
		//获取系统时间和回复ACK则立马处理。
		if(ObdConstants.OBD_RequestOrACK_Cmd.equals(command)
				&& (om.getMsgBody().startsWith("ff")
				|| om.getMsgBody().startsWith("7b")
//				|| om.getMsgBody().startsWith("f9")
				|| om.getMsgBody().startsWith("bb"))
			){
			try {
				messageObdRequestOrACKHandler.entry(om);
			} catch (Exception e) {
				e.printStackTrace();
				obdHandlerLogger.error(obdSn+",异常：",e);
			}
			obdMsgSave(msg, om);//保存报文
			return;
		}
		//报文解析正确立马响应ACK到设备
		//请求服务器时间不给ACK
		if(ObdConstants.OBD_RequestOrACK_Cmd.equals(command)//如果设备报文是请求帧或者应答ACK不给ACK应答
				|| ObdConstants.OBD_RequestUpgradeData_Cmd.equals(command)//请求升级包
				|| ObdConstants.OBD_UpgradeResult_Cmd.equals(command)){//升级结果
			
		}else{
    		//发送ACK应答到设备
			String msgBody = ObdConstants.Server_ResponseACK_OBD_Cmd + waterNo + "00";
			try {
				obdHandlerLogger.info("ACK响应报文："+msgBody);
				obdLogger.info("ACK响应报文："+msgBody);
				//发送ACK应答到设备
				sendUtil.msgSend(obdSn, SerialNumberUtil.getSerialnumber(obdSn), msgBody,channel);
			} catch (OBDException e) {
				e.printStackTrace();
			}
		}
		ctx.attr(key).set(obdSn);
		final String _msg = msg;
		Runnable runnableTask = new Runnable() {
			@Override
			public void run() {
				obdMsgSave(_msg, om);//保存报文
				messageHandlerThread.setOm(om);
				messageHandlerThread.setChannel(channel);
				long beginHandlerTime = System.currentTimeMillis();
				try {
					messageHandlerThread.run();
				} catch (Exception e) {
					e.printStackTrace();
					obdLogger.error("异常：",e);
				}
				obdHandlerLogger.info("【报文处理耗时统计】"+obdSn+"->"+waterNo+",耗时->"+(System.currentTimeMillis()-beginHandlerTime));
			}
		};
		if(command.equals(ObdConstants.OBD_RequestOrACK_Cmd) ||
			command.equals(ObdConstants.OBD_DeviceData_Cmd) || 
			command.equals(ObdConstants.OBD_ExtensionData_Cmd)){
			HandlerThreadPool.FirstHandlerPool.execute(runnableTask);
		}else{
			HandlerThreadPool.SecondHandlerPool.submit(runnableTask);
		}

	}

	/**
	 * 获取服务端最新的IP和端口
	 * 
	 * @param ctx
	 */
	@SuppressWarnings("rawtypes")
	public void sessionSave(ChannelHandlerContext ctx, AttributeKey key,OBDMessage om) {
		// 获取客户端IP和地址
		try {
			obdIpService.obdIpSave(ctx,key, om);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 把接收的报文初步解析成报文类
	 * 
	 * @param msg
	 *            报文字符串
	 * @return 报文类对象
	 */
	public OBDMessage parseMsg(String msg) {
		obdHandlerLogger.info("-----报文开始进行解析为对象-------");
		String id = "";
		String waterNo = "";
		int msgLen = 0;
		String command = "";
		String msgBody = "";
		String checkCode = "";
		boolean checkCodeRight = true;

		// 非空判断
		if (msg == null || msg.trim().equals("")) {
			obdHandlerLogger.error("报文为空！"+msg);
			return null;
		}

		// 起始符和结束符判断
		if (!msg.startsWith(START_CODE) || !msg.endsWith(END_CODE)) {
			obdHandlerLogger.error("起始符和结束符判断无效，消息体："+msg);
			return null;
		}

		// 如果位数是奇数位，是因为数据因为数据丢失，直接返回为空
		if (msg.length() % 2 != 0) {
			obdHandlerLogger.error("消息长度为奇数，数据丢失，消息体为：" + msg);
			return null;
		}

		// 去掉首尾的起始符和结束符
		Pattern p = Pattern.compile("^" + START_CODE + "(.*)" + END_CODE + "$");
		Matcher m = p.matcher(msg);
		String tempStr = "";
		if (m.find()) {
			tempStr = m.group(1);
		}

		obdHandlerLogger.info("消息体："+tempStr);
		// 反转义
		for (String[] strs : ESCES) {
			tempStr = unEscape(tempStr, strs[0], strs[1]);
		}
		obdHandlerLogger.info("消息体-特殊字符反转义结果："+tempStr);
		
		// 数据校验，由于信号收到干扰，数据在传输的过程中，可能会出现改变，所以要对数据进行校验，校验不通过，告诉obd错误
		// 如果校验码正确的话，就直接返回正确的服务器ACK
		String msgAll = tempStr.substring(0, tempStr.length() - 2);// 消息属性+消息体
		String code = tempStr.substring(tempStr.length() - 2, tempStr.length());
		obdHandlerLogger.info("完整报文(去除开始和结束符):"+tempStr);
		obdHandlerLogger.info("消息属性和消息体："+msgAll);
		obdHandlerLogger.info("报文校验码:"+code);
		// 本地校验
		ServerResponse sr = new ServerResponse();
		try {
			String codeTest = sr.xor(msgAll);
			obdHandlerLogger.info("本地校验码:" + codeTest + "****原始校验码:" + code);
			// 如果校验码不正确就不响应
			if (!codeTest.equals(code)) {
				checkCodeRight = false;
				obdHandlerLogger.error("校验码校验失败************" + msg);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			obdHandlerLogger.error(e1);
			return null;
		}

		// 基本长度判断
		int tempLen = tempStr.length();
		if (tempLen < 2 * 10) {
			obdHandlerLogger.info("error, message's length is too short.");
			obdHandlerLogger.error("error,基本长度错误**************" + msg);
			return null;
		}

		// 截取各部分参数
		id = tempStr.substring(0, 2 * 4);
		waterNo = tempStr.substring(2 * 4, 2 * 5);
		msgLen = Integer.parseInt(tempStr.substring(2 * 5, 2 * 7), 16);
		
		// 如果消息体内容错误，直接返回错误信息
		try {
			command = tempStr.substring(2 * 7, 2 * 9);
			msgBody = tempStr.substring(2 * 9, tempLen - 2);
			checkCode = tempStr.substring(tempLen - 2, tempLen);

		} catch (Exception e) {
			obdHandlerLogger.error(e);
			obdHandlerLogger.info("消息体内容截取错误,**********" + msg);
			return null;
		}
		return new OBDMessage(id, waterNo, msgLen, command, msgBody, checkCode,
				msg, checkCodeRight);
	}

	/**
	 * 消息体反转义<br/>
	 * （若数据中遇到oxaa 转义为0x55 0x01，若数据中遇到0x55 转义为0x55 0x02）
	 * 
	 * @param msgBody
	 *            信息体
	 * @param oldCode
	 *            被转义后的转义字串
	 * @param newCode
	 *            还原后的字符串
	 * @return 反转义后的消息体
	 */
	private String unEscape(String msgBody, String oldCode, String newCode) {
		int index = msgBody.indexOf(oldCode);

		if (index == -1)
			return msgBody;

		if (index % 2 == 0) {
			msgBody = msgBody.replaceFirst(oldCode, newCode);
			return msgBody.substring(0, index + newCode.length())
					+ unEscape(msgBody.substring(index + newCode.length()),
							oldCode, newCode);
		} else {
			return msgBody.substring(0, index + oldCode.length() - 1)
					+ unEscape(msgBody.substring(index + oldCode.length() - 1),
							oldCode, newCode);
		}
	}
	/**
	 * 保存日记报文
	 * @param msg 源报文
	 * @param om 转义后报文
	 */
	private void obdMsgSave(String msg,OBDMessage om){
		//分包之后的报文保存
		OBDPacket obdPacket = new OBDPacket(IDUtil.createID());
		obdPacket.setPacketType(1);
		obdPacket.setPacketData(msg);
		obdPacket.setInsertTime(new Date());
		if(om!=null){
			obdPacket.setObdSn(om.getId());
			obdPacket.setComman(om.getCommand());
			obdPacket.setApacketData(om.getMessage());
		}
		ObdServerInit.obdPacketService.add(obdPacket);
	}
}
