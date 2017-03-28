package com.hgsoft.common.netty;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hgsoft.carowner.entity.UbloxData;
import com.hgsoft.common.message.RunningData;
import com.hgsoft.system.utils.ByteUtil;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

public class HelloClientHandler extends ChannelInboundHandlerAdapter {
	private final Log logger = LogFactory.getLog(HelloClientHandler.class);
	//客户端请求消息
	private ByteBuf message;
	//用户id
	private String userId;
	//请求参数
	private String msg;
	//命令字
	private String command;
	//消息响应map
	private Map<String, Object> clientMap = RunningData.getIdClientMap();
	//hashMap保存byte[]数组
	Map<String, Object> respMap = new HashMap<String, Object>();
	
	/**
	 * 在构造方法里
	 */
	public HelloClientHandler(String msg) {
		
	}
	/**
	 * 在构造方法里
	 */
	public HelloClientHandler(String msg,String userId,String command) {
		this.msg =msg;//请求参数
		this.userId=userId;//绑定指定用户
		this.command=command;//命令字
	}
	
    @Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.channelRegistered(ctx);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		
		if (msg instanceof ByteBuf) {
			ByteBuf buf = (ByteBuf) msg;
			byte[] dst = new byte[buf.readableBytes()];
			buf.readBytes(dst);
			//发送AGPS数据包
			if("8014".equals(command)){
				if(!respMap.containsKey(userId)) {
					byte[] byteArr = new byte[]{};
					respMap.put(userId, byteArr);
				}
				byte[] str = (byte[]) respMap.get(userId);
				//数组拼接
				byte[] strNew = new byte[dst.length + str.length];
				System.arraycopy(str, 0, strNew, 0, str.length);
				System.arraycopy(dst, 0, strNew, str.length, dst.length);
				respMap.put(userId, strNew);
			}

		}
	}
	/**
	 * channel被激活时触发请求信息
	 */
	@Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client active ");
        //发送请求参数
        byte[] respbyte=msg.getBytes("UTF-8");//utf-8方式传参数
		message=Unpooled.buffer(respbyte.length);
		message.writeBytes(respbyte);
        ctx.writeAndFlush(message);  
    }
	//会话失效时触发
    @Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    	//发送AGPS数据包
//		Dictionary dic=dictionaryService.getDicByCodeAndType("owner.command", "server.AGPSData");
		if ("8014".equals(command)) {
			byte[] str = (byte[]) respMap.get(userId);//获取完整的报文
			logger.info("设备号:"+userId+"***命令字:8014"+"****完整的AGPS报文数据,转16进制:");
			logger.info(str.length+"********agps完整报文长度");
			String fileMsgHex=ByteUtil.bytesToHexString(str);
			logger.info(fileMsgHex);
			String sb = new String(str, "UTF-8");
			System.out.println(sb);//打印响应报文
			// 正则表达式截取
			String regex = "(.*)[\\r|\\n]{1,2}(.*)[\\r|\\n]{1,2}(.*)[\\r|\\n]{1,2}(.*)[\\r|\\n]{1,2}";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(sb);
			if (matcher.find()) {
				// length长度
				String lengthStr = matcher.group(2);
				Pattern pa = Pattern.compile("[^\\d]*(\\d*)");
				Matcher ma = pa.matcher(lengthStr);
				if (ma.matches()) {
					lengthStr = ma.group(1);
					logger.info(lengthStr+"********agps数据长度");
				}
				// Type类型
				String typeStr = matcher.group(3);
				UbloxData ublox = new UbloxData();
				//截取agps的byte[]
				if(typeStr.indexOf("application/ubx")>0){
					// 后面数据的开始下标，也就是前面正则表达式的第四组的最后下标s
//					int index = matcher.end(4);
////					System.out.println(index+"**************下标");
//					String value = sb.substring(index).trim();
//					byte[] byteArr = value.getBytes("UTF-8");
//					logger.info(byteArr.length+"******byte数组长度");
//					logger.info("设备号:"+userId+"***命令字:8014"+"****完整的AGPS报文数据,转16进制:");
//					String fileMsgHex=ByteUtil.bytesToHexString(byteArr);
//					logger.info(fileMsgHex);
//					System.out.println("----------------------------------------------------------------");
//					String ss = new String(byteArr, "UTF-8");
//					System.out.println(ss);
//					System.out.println("-----------------------------------------------------------------");
					ublox.setType(0);//成功响应
					ublox.setSize(str.length);
					ublox.setAgpsData(str);//连报文头一起发送过去
					clientMap.put(userId, ublox);
					
				}else if(typeStr.indexOf("text/plain")>0){
					//请求失败
					logger.info("设备号:"+userId+"*************AGPS请求失败");
					int index = matcher.end(4);
					String value = sb.substring(index);
					System.out.println(value);//ASC2码,不需要再转成byte[]
					ublox.setType(1);//响应失败
					ublox.setSize(Integer.parseInt(lengthStr));
					ublox.setError(value);
					clientMap.put(userId, ublox);
				}
			}
		}

		System.out.println("Client close ");
		super.channelInactive(ctx);
		ctx.close();
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.channelUnregistered(ctx);
	}
	/**
	 * channel读取数据完成后触发的操作
	 */
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		super.channelReadComplete(ctx);
	}

	@Override
	public void channelWritabilityChanged(ChannelHandlerContext ctx)
			throws Exception {
		// TODO Auto-generated method stub
		super.channelWritabilityChanged(ctx);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		// TODO Auto-generated method stub
		super.exceptionCaught(ctx, cause);
		ctx.close();
	}
	
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		Channel channel = ctx.channel();
		if (evt instanceof IdleStateEvent) {  
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
            	channel.close();
            	System.out.println(new Date()+"===客户端===(读超时)");
            } else if (event.state() == IdleState.WRITER_IDLE) {  
                /*写超时：数据没向客户端返回写数据*/     
            } else if (event.state() == IdleState.ALL_IDLE) {  
                /*总超时*/  
            }  
        }  
	}
	
    
}
