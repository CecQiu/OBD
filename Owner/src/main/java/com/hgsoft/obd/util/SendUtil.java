package com.hgsoft.obd.util;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

import com.hgsoft.carowner.entity.OBDPacket;
import com.hgsoft.common.message.OBDMessage;
import com.hgsoft.common.utils.IDUtil;
import com.hgsoft.common.utils.StrUtil;
import com.hgsoft.obd.server.GlobalData;
import com.hgsoft.obd.server.OBDException;
import com.hgsoft.obd.server.ObdConstants;
import com.hgsoft.obd.server.ObdServerInit;
import com.hgsoft.obd.service.ServerResponse;
import com.hgsoft.system.utils.ByteUtil;

/**
 * 报文发送工具
 * @author sujunguang
 * 2016年1月6日
 * 下午3:18:43
 */
@Component
public class SendUtil {
	
	private static SendUtil sendUtil;
	public static SendUtil getInstance(){
		if(sendUtil == null){
			sendUtil = new SendUtil();
		}
		return sendUtil;
	}
	private static Logger obdLogger = LogManager.getLogger("obdLogger");
	/**
	 * 发送计数器数
	 */
	private static ThreadLocal<Integer> sendCountThreadLocal = new ThreadLocal<Integer>() {
		protected Integer initialValue() {
			return 0;
		};
	};
	/**
	 * 限制发送次数
	 */
	private static ThreadLocal<Integer> limitSendCountThreadLocal = new ThreadLocal<Integer>() {
		protected Integer initialValue() {
			return 3;
		};
	};
	/**
	 * 等待计数器数
	 */
	private static ThreadLocal<Integer> waitCountThreadLocal = new ThreadLocal<Integer>() {
		protected Integer initialValue() {
			return 0;
		};
	};
	/**
	 * 限制等待次数
	 */
	private static ThreadLocal<Integer> limitWaitCountThreadLocal = new ThreadLocal<Integer>() {
		protected Integer initialValue() {
			return 5;
		};
	};
	/**
	 * 开始时间
	 */
	private static ThreadLocal<Long> beginThreadLocal = new ThreadLocal<Long>() {
		protected Long initialValue() {
			return System.currentTimeMillis();
		};
	};
	/**
	 * 计数+1
	 */
	private void sendCountAddOne(){
		sendCountThreadLocal.set(sendCountThreadLocal.get()+1);
	}
	private void waitCountAddOne(){
		waitCountThreadLocal.set(waitCountThreadLocal.get()+1);
	}
	private int getSendCount(){
		return sendCountThreadLocal.get();
	}
	private int getWaitCount(){
		return waitCountThreadLocal.get();
	}
	
	/**
	 * 完整报文组装
	 * @param obdSn 设备号
	 * @param serialNum 流水号
	 * @param msgBody 消息体 (包含命令字)
	 * @return
	 */
	public String msgAll(String obdSn,Integer serialNum,String msgBody){
		String data = "";
		//消息体长度
		String len=Integer.toHexString(msgBody.length()/2);
		//转成16进制后
		String msgBodyLength=StrUtil.strAppend(len, 4, 0, "0");
		//消息头：ID+流水号+消息体长度
		String header =obdSn+StrUtil.strAppendByLen(Integer.toHexString(serialNum),1,"0")+msgBodyLength;
		//校验码
		ServerResponse sr = new ServerResponse();
		try {
			String checkCode = sr.xor(header+msgBody);
			data = ObdConstants.OBD_Packet_Head + sr.escape(header + msgBody + checkCode) + ObdConstants.OBD_Packet_Tail;
		} catch (Exception e) {
			e.printStackTrace();
		}
		obdLogger.info("<"+obdSn+">报文组装为："+data);
		return data;
	}
	
	/**
	 * 只往设备发送报文
	 * @param obdSn 设备号
	 * @param msg 发送的报文
	 * @throws OBDException
	 * return true-发送成功 false-发送失败
	 */
	public void msgSend(String obdSn,Integer serialNum,String msgBody,Channel _channel) throws OBDException{
		obdLogger.info("<"+obdSn+">-------------【往设备发送报文】-----------");
		String msg = msgAll(obdSn,serialNum,msgBody);
		obdLogger.info("<"+obdSn+">-------------【往设备发送报文】报文为："+msg);
		Channel channel1 = _channel;
		if(_channel == null){
			channel1 = GlobalData.getChannelByObdSn(obdSn);
		}
		final Channel channel = channel1;
		if(channel == null){
			obdLogger.error("<"+obdSn+">-------------【往设备发送报文】设备："+obdSn+"离线！发送失败");
			throw new OBDException("设备："+obdSn+"离线！发送失败");
		}
		byte[] respbyte=ByteUtil.hexStringToBytes(msg);
		ByteBuf resp = Unpooled.copiedBuffer(respbyte);
		ChannelFuture future = channel.writeAndFlush(resp);
		final String _obdSn = obdSn;
		final Integer _serialNum = serialNum;
		final String _msgBody = msgBody;
		sendCountAddOne();
		future.addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture future) throws OBDException, InterruptedException{
				obdLogger.info("-------------【往设备发送报文】设备："+_obdSn+"流水号："+_serialNum+"，次数："+getSendCount()+"，发送是否成功：isSuccess:"+future.isSuccess());
				if(!future.isSuccess()){
					ObdServerInit.obdRedisStaticsUtil.incrServerSendToObdStaticsFail();
					obdLogger.error(_obdSn+"，"+channel+" 发送异常:"+future.cause());
					obdLogger.error(_obdSn+"，channel.isActive:"+channel.isActive());
					obdLogger.error(_obdSn+"，channel.isOpen:"+channel.isOpen());
					obdLogger.error(_obdSn+"，channel.isWritable:"+channel.isWritable());
//					if(getSendCount() < limitSendCountThreadLocal.get()){//失败重发
//						Thread.sleep(300);//sleep 300毫秒
//						msgSend(_obdSn, _serialNum, _msgBody, channel);
//						return;
//					}
					channel.close();
					GlobalData.removeOBDChannelByChannel(channel);
				}else{
					ObdServerInit.obdRedisStaticsUtil.incrServerSendToObdStaticsSuccess();
				}
				sendCountThreadLocal.remove();
			}
			
		});
		//保存报文
		obdMsgSave(obdSn, serialNum, msg);
	}
	
	/**
	 * 只往设备发送报文
	 * @param obdSn 设备号
	 * @param msg 发送的报文
	 * @throws OBDException
	 */
	public void msgSend(String obdSn,Integer serialNum,String msgBody) throws OBDException{
		msgSend(obdSn, serialNum, msgBody, null);
	}
	
	/**
	 * 发送报文到设备
	 * @param obdSn 设备号
	 * @param msg 报文（字符串）
	 */
	public void msgSend(String obdSn,String msg){
		final Channel channel = GlobalData.getChannelByObdSn(obdSn);
		byte[] respbyte=ByteUtil.hexStringToBytes(msg);
		ByteBuf resp = Unpooled.copiedBuffer(respbyte);
		ChannelFuture future = channel.writeAndFlush(resp);
		final String _obdSn = obdSn;
		final String _msg = msg;
		sendCountAddOne();
		future.addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture future){
				obdLogger.info("-------------【往设备发送报文】设备："+_obdSn+"报文："+ _msg +"，发送是否成功：isSuccess:"+future.isSuccess());
				if(!future.isSuccess()){
					ObdServerInit.obdRedisStaticsUtil.incrServerSendToObdStaticsFail();
					obdLogger.error(_obdSn+"，"+channel+" 发送异常:"+future.cause());
					obdLogger.error(_obdSn+"，channel.isActive:"+channel.isActive());
					obdLogger.error(_obdSn+"，channel.isOpen:"+channel.isOpen());
					obdLogger.error(_obdSn+"，channel.isWritable:"+channel.isWritable());
//					if(getSendCount() < limitSendCountThreadLocal.get()){//失败重发
//						msgSend(_obdSn, _msg);
//						return;
//					}
					channel.close();
					GlobalData.removeOBDChannelByChannel(channel);
				}else{
					ObdServerInit.obdRedisStaticsUtil.incrServerSendToObdStaticsSuccess();
				}
			}
		});
	}
	
	/**
	 * 报文发送到OBD设备,并且等待响应
	 * @param obdSn 设备号
	 * @param serialNum 流水号
	 * @param msgBody 发送的报文
	 * @param waitSencods 每轮等待秒数，默认1秒
	 * @return 设备返回响应报文 null-响应失败 not null-响应成功 01执行成功 00接收成功
	 * @throws Exception 
	 */
	public Object msgSendGetResult(String obdSn,Integer serialNum,String msgBody,Double waitSencods) throws OBDException{
		obdLogger.info("<"+obdSn+">-------------【报文发送到OBD设备,并且等待响应】-----------");
		GlobalData.OBD_ACK_OR_QueryData.remove(obdSn+"_"+serialNum); //TODO
		msgSend(obdSn,serialNum,msgBody,null);
		beginThreadLocal.set(System.currentTimeMillis());
		GetResultThread thread = new GetResultThread(obdSn, serialNum);
		thread.start();
		Object result = getResult(thread, waitSencods, false);
		obdLogger.info("<"+obdSn+">-------------【报文发送到OBD设备,并且等待响应】响应应答结果："+result);
		
		return result;
	}
	
	/**
	 * 等待循环次数
	 */
	private static long totalWaitTime(int n){
		return (long)((0.3*n*n/2 + 0.15*n)*1000);
	}
	public static void main(String[] args) {
		System.out.println(totalWaitTime(1));
		System.out.println(totalWaitTime(2));
		System.out.println(totalWaitTime(3));
	}
	
	/**
	 * 获得响应报文
	 * @param obdSn
	 * @param serialNum
	 * @return
	 */
	private Object getResult(GetResultThread thread,Double waitSencods, boolean isByKey){
		waitCountAddOne();//计数+1
		if(waitSencods == null){//默认等待5秒
			waitSencods = 5.0;
		}
		long nn =  waitSencods.longValue()*1000 - totalWaitTime(limitWaitCountThreadLocal.get());//剩余时间
		long nnn = nn / limitWaitCountThreadLocal.get();
		long n = nnn + 300*getWaitCount();
		try {
//			System.out.println("sleep:"+n);
			thread.sleep(n);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Object result = thread.getResult();
		if(result == null && getWaitCount() < limitWaitCountThreadLocal.get()){
			if(isByKey){
				return getResultByKey(thread, waitSencods);
			}else{
				return getResult(thread,waitSencods, isByKey);
			}
		}else{
			obdLogger.info("-------------【线程获得响应报文】次数："+getWaitCount()+"，结果："+"->"+result + ",耗时："+(System.currentTimeMillis()-beginThreadLocal.get()));
			waitCountThreadLocal.set(0);
			return result;
		}
	}

	/**
	 * 发送报文并且等待结果
	 * @param obdSn
	 * @param serialNum
	 * @param msgBody
	 * @param key 指定的key对应结果
	 * @param waitSencods
	 * @return
	 * @throws OBDException
	 */
	public Object msgSendGetResult(String obdSn, Integer serialNum,	String msgBody, String key,Double waitSencods) throws OBDException {
		obdLogger.info("<"+obdSn+">-------------【报文发送到OBD设备,并且等待响应】-----------");
		msgSend(obdSn,serialNum,msgBody,null);
		beginThreadLocal.set(System.currentTimeMillis());
		GlobalData.OBD_ACK_OR_QueryData.put(key, null);
		GetResultThread thread = new GetResultThread(key);
		thread.start();
		Object result = getResultByKey(thread,waitSencods);
		GlobalData.OBD_ACK_OR_QueryData.remove(key); //TODO
		obdLogger.info("<"+obdSn+">-------------【报文发送到OBD设备,并且等待响应】响应应答结果："+result);
		return result;
	}
	
	/**
	 * 获得响应报文
	 * @param key 指定的key值
	 * @param 每轮等待秒数 默认1秒
	 * @return
	 */
	private synchronized Object getResultByKey(GetResultThread thread,Double waitSencods){
		return getResult(thread, waitSencods, true);
//		if(waitSencods == null){
//			waitSencods = 5.0;
//		}
//		int loop = 3;
//		double mostWaitCount = (waitSencods-1)/loop;
//		long n = (long) (mostWaitCount*1000 + getWaitCount()*1000/3);
//		try {
//			thread.sleep(n);
//			waitCountAddOne();//计数+1
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		Object result = thread.getResult();
//		if(result == null && getWaitCount() < limitWaitCountThreadLocal.get()){
//			return getResultByKey(thread, waitSencods);
//		} else {
//			obdLogger.info("-------------【线程获得响应报文】次数："+getWaitCount()+"，结果："+"->"+result + ",耗时："+(System.currentTimeMillis()-beginThreadLocal.get()));
//			waitCountThreadLocal.set(0);
//			return result;
//		}
	}
	
	private void obdMsgSave(String obdSn,Integer serialNum,String msgBody){
		try {
			//分包之后的报文保存
			OBDPacket obdPacket = new OBDPacket(IDUtil.createID());
			obdPacket.setPacketType(2);
			obdPacket.setPacketData(msgBody);
			obdPacket.setInsertTime(new Date());
			obdPacket.setObdSn(obdSn);
			//如果有转义字符 
			OBDMessage om=ServerResponse.parseMsg(msgBody);
			obdPacket.setComman(om.getCommand());
			ObdServerInit.obdPacketService.add(obdPacket);
		} catch (Exception e) {
			e.printStackTrace();
			obdLogger.info("-------------【下发报文保存失败】异常信息---"+e);
		}
	}
}