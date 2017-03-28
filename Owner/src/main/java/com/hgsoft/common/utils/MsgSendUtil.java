/**
 * 
 */
package com.hgsoft.common.utils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.hgsoft.common.message.RunningData;
import com.hgsoft.common.service.ServerResponses;
import com.hgsoft.obd.server.GlobalData;
import com.hgsoft.system.utils.ByteUtil;

/**
 * @author liujialin
 * 平台客户端请求obd报文
 */
@Service
public class MsgSendUtil {
	private final Log logger = LogFactory.getLog(MsgSendUtil.class);
	private int i=0;//请求消息次数

	   /**
	    * 拼接完整请求报文
	    * @param obdId obd设备id
	    * @param command  命令字
	    * @param msg  消息体
	    * @return
	    */
		public String msgAll(String obdId,String command,int serialNumber,String msg) throws Exception{
				//消息体
			String msgBody=msg==null?command:command+msg;
			
			//消息体长度小于4位
			String len=Integer.toHexString(msgBody.length()/2);
			
			//转成16进制后，消息的长度可能是a
			String msgBodyLength=StrUtil.strAppend(len, 4, 0, "0");
				//消息头
			String header=obdId+StrUtil.strAppendByLen(Integer.toHexString(serialNumber),1,"0")+msgBodyLength;
			String resultStr="";
			ServerResponses sr=new ServerResponses();//消息返回
			try {
				//验证码
				String checkCode=sr.xor(header+msgBody);
				//转义
				resultStr="aa"+sr.escape(header+msgBody+checkCode)+"aa";
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new Exception();
			}	
			return 	resultStr;
			
		}
		/**
		 * 发送请求消息
		 * @param obdId
		 * @param command 命令字
		 * @param serialNumber 流水号
		 * @param msg 消息体
		 * @throws Exception
		 */
		public void msgSend(String obdId,String command,int serialNumber,String msg) throws Exception{
			//获取请求消息体
			String msgBody="";
			try {
				msgBody = msgAll(obdId,command,serialNumber,msg);
				sendMsg(obdId,msgBody);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new Exception();
			}
		}
		/**
		 * 发送请求消息
		 * @param obdId obdId
		 * @param msgBody 完整的请求报文
		 * @throws Exception
		 */
		public void sendMsg(String obdId,String msgBody) {
			logger.info("obd设备号:"+obdId+"**********请求消息:"+msgBody);
			try {
				//获取指定的会话
//				Map<String, ChannelHandlerContext> IdSessionMap= RunningData.getIdSessionMap();
//				ChannelHandlerContext ctx=IdSessionMap.get(obdId);
				Channel channel = GlobalData.getChannelByObdSn(obdId);
				System.out.println("------------channel-----------:"+channel);
				//如果存在会话，发送请求消息
				if(channel!=null){
					//十六进制字符串转成byte数组
					byte[] respbyte=ByteUtil.hexStringToBytes(msgBody);
					ByteBuf resp = Unpooled.copiedBuffer(respbyte);
					logger.info(msgBody);
					logger.info(channel.remoteAddress().toString());
					//服务端向指定的客户端发送请求消息
					channel.writeAndFlush(resp);
					
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(obdId+"************发送消息失败.");
				logger.error(e);
			}
		}
		//判断obd是否在线
		public boolean obdOnline(String obdId)throws Exception{
			//获取指定的会话
//			Map<String, ChannelHandlerContext> IdSessionMap= RunningData.getIdSessionMap();
//			ChannelHandlerContext ctx=IdSessionMap.get(obdId);
			Channel channel = GlobalData.getChannelByObdSn(obdId);
			//如果存在会话，发送请求消息
			if(channel!=null){
				return true;
			}else{
				return false;
			}
		}
		/**
		 * 发送请求并线程获取返回结果
		 * @param obdSn
		 * @param common
		 * @param serialNumber
		 * @param msg
		 * @param msgId
		 * @return
		 */
		public String msgSendAndGetResult(String obdSn,String common,int serialNumber,String msg,String msgId){
			i++;
			System.out.println(i+"****次数");
			//向客户端发送请求消息
			try {
				msgSend(obdSn,common,serialNumber,msg);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//如果服务端在：HelloServerHandler.channelRead()方法里读取到客户端的响应消息，则线程获取消息。
			MsgThread thread =new MsgThread(msgId);
			thread.start();
			try {
				//线程同步锁，顺序执行
				thread.join(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//获取到客户端的响应消息
			String resultStr= (String) thread.getResMsg();
			if(i<=2 && resultStr==null){
				resultStr=msgSendAndGetResult(obdSn, common, serialNumber, msg, msgId);
			}
			i=0;
			return resultStr;
		}
		
		
		/**
		 * 发送请求并线程获取返回结果
		 * @param obdSn
		 * @param common
		 * @param serialNumber
		 * @param msg
		 * @param msgId
		 * @return
		 */
		public String msgSendAndGetResult(String obdSn,String msg,String msgId){
			i++;
			//向客户端发送请求消息
			try {
				sendMsg(obdSn,msg);
			} catch (Exception e1) {
				e1.printStackTrace();
				logger.error(e1);
			}
			//如果服务端在：HelloServerHandler.channelRead()方法里读取到客户端的响应消息，则线程获取消息。
			MsgThread thread =new MsgThread(msgId);
			thread.start();
			try {
				//线程同步锁，顺序执行
				thread.join(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
				logger.error(e);
			}
			//获取到客户端的响应消息
			String resultStr= (String) thread.getResMsg();
			if(i<=2 && resultStr==null){
				resultStr=msgSendAndGetResult(obdSn,msg,msgId);
			}
			i=0;
			return resultStr;
		}
}
