package com.hgsoft.common.message;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.hgsoft.carowner.entity.OBDStockInfo;
import com.hgsoft.carowner.service.OBDStockInfoService;
import com.hgsoft.carowner.service.ObdIpService;
import com.hgsoft.common.service.ServerResponses;
import com.hgsoft.common.utils.DateUtil;
import com.hgsoft.common.utils.MsgSendUtil;
import com.hgsoft.common.utils.StrUtil;

/**
 * 报文处理类
 * 
 * @author fdf
 */
@Component
public class MessageHandel {

	@Resource
	private Muc1Task muc1Task;
	@Resource
	private Muc2Task muc2Task;
	@Resource
	private Muc3Task muc3Task;
	@Resource
	private Muc4Task muc4Task;
	@Resource
	private Muc5Task muc5Task;
	@Resource
	private Muc6Task muc6Task;
	@Resource
	private Muc7Task muc7Task;
	@Resource
	private Muc8Task muc8Task;
	@Resource
	private Muc9Task muc9Task;
	@Resource
	private Muc10Task muc10Task;
	@Resource
	private Muc11Task muc11Task;
	@Resource
	private Muc13Task muc13Task;
	@Resource
	private Muc14Task muc14Task;
	@Resource
	private Muc15Task muc15Task;
	@Resource
	private Muc16Task muc16Task;
	@Resource
	private Muc18Task muc18Task;
	@Resource
	private Muc20Task muc20Task;
//	@Resource
//	private MsgSendUtil msgSendUtil;
//	@Resource
//	private UnhandledTask unhandledTask;
	
	
	/*** 日志记录 */
	private static Logger interfaceLog = LogManager.getLogger("interfacePermission");
	private String logMsg = "";
	private final Log logger = LogFactory.getLog(MessageHandel.class);

	@Resource
	private ObdIpService obdIpService;
	@Resource
	private ServerResponses serverResponses;
	@Resource
	private OBDStockInfoService obdStockInfoService;
	

	/** 起始符 */
	private final static String START_CODE = "aa";
	/** 结束符 */
	private final static String END_CODE = "aa";
	/** 需要转义的字符数组 */
	private final static String[][] ESCES = { { "5501", "aa" }, { "5502", "55" } };

	private static MessageHandel messageHandel;

	@PostConstruct
	public void init() {
		messageHandel = this;
		messageHandel.muc4Task = this.muc4Task;
		messageHandel.muc7Task = this.muc7Task;
		messageHandel.muc8Task = this.muc8Task;
		messageHandel.muc5Task = this.muc5Task;
	}

	private MessageHandel() {
	}

	public static MessageHandel instance() {
		if (messageHandel == null) {
			messageHandel = new MessageHandel();
		}

		return messageHandel;
	}


	/**
	 * 处理接收报文
	 * 
	 * @param msg
	 *            报文字符串
	 * @param ctx
	 *            客户端实体
	 */
	@SuppressWarnings("rawtypes")
	public void handelMsg(ChannelHandlerContext ctx,AttributeKey key, String msg) {
//		MsgSendUtil msgSendUtil = new MsgSendUtil();
//		final OBDMessage om = parseMsg(msg);
//		logger.info(om + "*************请求消息体1");// 打印请求消息对象
//		// 返回消息体
//		String resultStr = "";
//		// 如果请求消息内容不正确，直接返回数据
//		if (om == null) {
//			// 请求报文格式不正确，直接忽略，且不做任何响应
//			return;
//		}
//		
//		String obdSn=om.getId();//obd设备号
//		String waterNo=om.getWaterNo();//流水号
//		String command=om.getCommand();//命令字
//		
//		//暂时转换
//		OBDStockInfo obdStockInfo = obdStockInfoService.queryBySNAndMSN(obdSn);// 设备号
//		if(obdStockInfo!=null){
//			if("00".equals(obdStockInfo.getStockState())){
//				obdStockInfo.setStockState("01");//在线
//				boolean flag=obdStockInfoService.obdStateUpdate(obdStockInfo);
//				logger.info(obdSn+"**********上线结果:"+flag+"***上线时间:"+(String)DateUtil.fromatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
//			}
//		}
//		
//		// 将会话缓存到map里
//		sessionSave(ctx,key, om);
//		
//		//服务器ACK
//		String serverACK="";
//		
//		//校验码
//		if (!om.getCheckCodeRight()) {
//			logger.info(om.getCheckCodeRight() + "*********校验码失败:");
//			serverACK=serverResponses.recvACK(obdSn, waterNo, command, 1);
//			msgSendUtil.sendMsg(obdSn, serverACK);
//			return;
//		}else{
//			serverACK=serverResponses.recvACK(obdSn, waterNo, command, 0);
//		}
//		
//		//obd状态
//		Map<String, Date> obdState=RunningData.getObdStateMap();
//		obdState.put(obdSn, new Date());
//		
//
//		if (om != null) {
//			MUC muc = MUC.getMuc(command);
//			if (muc == null) {
//				logMsg = "error, undefinde commond by code 【" + command + "】！";
//				markErrorLog(logMsg);
//				String errorACK=serverResponses.recvACK(obdSn, waterNo, command, 1);
//				msgSendUtil.sendMsg(obdSn, errorACK);
//				return;
//			}
//			
//			switch (muc) {
//			case Muc1:
//				logger.info(obdSn + "***********************终端心跳1");
//				msgSendUtil.sendMsg(obdSn, serverACK);
//				muc1Task.entrance(om);
//				logger.info(resultStr + "*********终端心跳响应消息体1");
//				break;
//			case Muc2:
//				logger.info(obdSn + "***********************终端ACK2");
//				// 1.逻辑处理
//				muc2Task.entrance(om);
//				logger.info(obdSn + "*******************终端ACK响应消息体2");
//				break;
//			case Muc3:
//				logger.info(obdSn + "***********************参数查询应答3");
//				muc3Task.entrance(om);
//				logger.info(obdSn + "*******************参数查询应答响应消息体3");
//				break;
//			case Muc4:
//				logger.info(obdSn + "***********************位置信息4");
//				//1.校验成功，返回服务器ACK
//				msgSendUtil.sendMsg(obdSn, serverACK);
//				// 2.逻辑处理
////				muc4Task.doTask(om);
//				logger.info(obdSn + "***************位置信息响应消息体4");
//				break;
//			case Muc5:
//				logger.info(obdSn + "***********************上传故障码5");
//				//1.校验成功，返回服务器ACK
//				msgSendUtil.sendMsg(obdSn, serverACK);
//				// 2.逻辑处理
//				muc5Task.entrance(om);
//				logger.info(obdSn + "***************上传故障码响应消息体5");
//				break;
//			case Muc6:
//				logger.info(obdSn + "***********************上传批量信息6");
//				//1.校验成功，返回服务器ACK
//				msgSendUtil.sendMsg(obdSn, serverACK);
//				// 2.逻辑处理
//				muc6Task.entrance(om);
//				logger.info(obdSn + "***************上传批量信息响应消息体6");
//				break;
//			case Muc7:
//				logger.info(obdSn + "***********************上传行程记录7");
//				//1.校验成功，返回服务器ACK
//				msgSendUtil.sendMsg(obdSn, serverACK);
//				// 2.逻辑处理
//				muc7Task.doTask(om);
//				logger.info(obdSn + "***************上传行程记录响应消息体7");
//				break;
//			case Muc8:
//				// logger.info(om+"***********************8");
//				// resultStr = muc8Task.obdStatus(om);
//				// logger.info(resultStr+"*********响应消息体8");
//				break;
//			case Muc9:
//				logger.info(om + "***********************远程升级请求9");
//				muc9Task.entrance(om);
//				logger.info(obdSn + "*********远程升级请求包响应消息体9");
//				break;
//			case Muc10:
//				logger.info(obdSn + "***********************远程升级结果10");
//				//1.校验成功，返回服务器ACK
//				msgSendUtil.sendMsg(obdSn, serverACK);
//				// 2.逻辑处理
//				muc10Task.entrance(om);
//				logger.info(obdSn + "***************远程升级结果响应消息体10");
//				break;
//			case Muc11:
//				logger.info(obdSn + "***********************请求系统时间11");
//				muc11Task.entrance(om);
//				logger.info(obdSn + "***************请求系统时间响应消息体11");
//				break;
//			case Muc12:
//				break;
//			case Muc13:
//				logger.info(obdSn + "***********************版本号13");
//				//1.校验成功，返回服务器ACK
//				msgSendUtil.sendMsg(obdSn, serverACK);
//				// 2.逻辑处理
//				muc13Task.entrance(om);
//				logger.info(obdSn + "*********版本号响应消息体13");
//				break;
//			case Muc14:
//				break;
//			case Muc15:
//				logger.info(obdSn + "***********************更新AGPS请求包15");
//				//1.校验成功，返回服务器ACK
////				msgSendUtil.sendMsg(obdSn, serverACK);
//				// 2.逻辑处理
//				muc15Task.entrance(om);
//				logger.info(obdSn + "*************更新AGPS请求包响应消息体15");
//				break;
//			case Muc16:
//				//1.校验成功，返回服务器ACK
//				logger.info(obdSn + "***********************更新AGPS结果16");
//				msgSendUtil.sendMsg(obdSn, serverACK);
//				logger.info(obdSn + "*************更新AGPS结果响应消息体16");
//				break;
//			case Muc17:
//				break;
//			case Muc18:
//				break;
//			case Muc19:
//				break;
//			case Muc20:
//				logger.info(obdSn + "***********************车辆体检数据20");
//				//1.校验成功，返回服务器ACK
//				msgSendUtil.sendMsg(obdSn, serverACK);
//				// 2.逻辑处理
//				muc20Task.entrance(om);
//				logger.info(obdSn + "*************车辆体检数据响应消息体20");
//				break;
//			default:
//				logMsg = "error, no such command!";
//				markErrorLog(logMsg);
//				break;
//			}
//		}
		return;
	}
	
	

	/**
	 * 获取服务端最新的IP和端口
	 * 
	 * @param ctx
	 */
	@SuppressWarnings("rawtypes")
	public void sessionSave(ChannelHandlerContext ctx, AttributeKey key, OBDMessage om) {
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
		String id = "";
		String waterNo = "";
		int msgLen = 0;
		String command = "";
		String msgBody = "";
		String checkCode = "";
		boolean checkCodeRight = true;

		// 非空判断
		if (msg == null || msg.trim().equals("")) {
			logMsg = "error, message is null or empty.";
			markErrorLog(logMsg);
			logger.info("error,消息体为空*********************");
			// checkCodeRight = false;
			return null;
		}

		msg = msg.trim();

		// 起始符和结束符判断
		if (!msg.startsWith(START_CODE) || !msg.endsWith(END_CODE)) {
			logMsg = "error, message is not match the start or end code.";
			markErrorLog(logMsg);
			logger.info("error,起始符和结束符判断************" + msg);
			return null;
		}

		// 如果位数是奇数位，是因为数据因为数据丢失，直接返回为空
		if (msg.length() % 2 != 0) {
			logger.info("error,消息长度为奇数，数据丢失***********" + msg);
			return null;
		}

		// 如果请求报文出现3次以上aa，则是网络延迟，直接返回空
		if (StrUtil.subStringTimes(msg, START_CODE) > 2) {
			logMsg = "error, message is too much request.";
			logger.info("error,网络延迟,多次请求报文同时过来*********" + msg);
			return null;
		}

		// 去掉首尾的起始符和结束符
		Pattern p = Pattern.compile("^" + START_CODE + "(.*)" + END_CODE + "$");
		Matcher m = p.matcher(msg);
		String tempStr = "";
		if (m.find()) {
			tempStr = m.group(1);
		}

		// 反转义
		for (String[] strs : ESCES) {
			tempStr = unEscape(tempStr, strs[0], strs[1]);
		}

		// 数据校验，由于信号收到干扰，数据在传输的过程中，可能会出现改变，所以要对数据进行校验，校验不通过，告诉obd错误
		// 如果校验码正确的话，就直接返回正确的服务器ACK
		String msgAll = tempStr.substring(0, tempStr.length() - 2);// 消息属性+消息体
		String code = tempStr.substring(tempStr.length() - 2, tempStr.length());
		logger.info(tempStr + "***********完整报文");
		// 本地校验
		ServerResponses sr = new ServerResponses();
		try {
			String codeTest = sr.xor(msgAll);
			logger.info("本地校验码:" + codeTest + "****原始校验码:" + code);
			// 如果校验码不正确就不响应
			if (!codeTest.equals(code)) {
				checkCodeRight = false;
				logger.info("校验码校验失败************" + msg);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.info(e1);
			// 如果报异常，就直接返回null
			return null;
		}

		// 基本长度判断
		int tempLen = tempStr.length();
		if (tempLen < 2 * 18) {
			logger.info("error, message's length is too short.");
			logger.info("error,基本长度错误**************" + msg);
			return null;
		}

		// 截取各部分参数
		id = tempStr.substring(0, 2 * 12);
		waterNo = tempStr.substring(2 * 12, 2 * 13);
		msgLen = Integer.parseInt(tempStr.substring(2 * 13, 2 * 15), 16);
		// 如果校验码校验失败，且是丢包导致，则不返回数据
//		String body = tempStr.substring(2 * 15, tempLen - 2);
//		if (msgLen != body.length() / 2) {
//			logger.info("消息体丢失数据" + tempStr);
//			return null;
//		}
		// 如果消息体内容错误，直接返回错误信息
		try {
			command = tempStr.substring(2 * 15, 2 * 17);
			msgBody = tempStr.substring(2 * 17, tempLen - 2);
			checkCode = tempStr.substring(tempLen - 2, tempLen);

		} catch (Exception e) {
			logger.info(e);
			logger.info("消息体内容截取错误,**********" + msg);
			return null;
		}
		return new OBDMessage(id, waterNo, msgLen, command, msgBody, checkCode, msg, checkCodeRight);
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
					+ unEscape(msgBody.substring(index + newCode.length()), oldCode, newCode);
		} else {
			return msgBody.substring(0, index + oldCode.length() - 1)
					+ unEscape(msgBody.substring(index + oldCode.length() - 1), oldCode, newCode);
		}
	}

	public void obdState(){
		
	}
	
	/**
	 * 错误日志记录
	 * 
	 * @param msg
	 *            简单的异常说明
	 */
	private void markErrorLog(String msg) {
		interfaceLog.error(msg);
		System.out.println(msg);
	}

}
