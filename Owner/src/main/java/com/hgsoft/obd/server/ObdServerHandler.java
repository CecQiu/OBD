package com.hgsoft.obd.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.handler.timeout.WriteTimeoutException;
import io.netty.util.AttributeKey;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.hgsoft.carowner.service.OBDPacketService;
import com.hgsoft.common.utils.DateUtil;
import com.hgsoft.obd.handler.MessageHandler;
/**
 * OBD 服务器消息处理
 * @author sujunguang
 * 2015年12月12日
 * 下午2:24:02
 */
public class ObdServerHandler extends ChannelInboundHandlerAdapter {

	private static Logger obdLogger = LogManager.getLogger("obdLogger");
	
	private static final AttributeKey<String> attributeKey = AttributeKey.valueOf("obdSn");
	@Resource
	private OBDPacketService obdPacketService;
	
    @Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
    	try {
			//服务器端读取请求消息
			if (msg instanceof ByteBuf) {
				ByteBuf buf = (ByteBuf) msg;
				byte[] dst = new byte[buf.readableBytes()];
				buf.readBytes(dst);
				final String data = new String(dst);
				obdLogger.info("SERVER接收到OBD报文:" + data);

//				//分包之后的报文保存
//				OBDPacket obdPacket = new OBDPacket(IDUtil.createID());
//				obdPacket.setPacketType(1);
//				obdPacket.setPacketData(data);
//				obdPacket.setInsertTime(new Date());
//				ObdServerInit.obdPacketService.add(obdPacket);
				
//				final ChannelHandlerContext _ctx = ctx;
//				executor.submit(new Runnable() {
//					@Override
//					public void run() {
						MessageHandler.instance().handelMsg(ctx,attributeKey, data);
//					}
//				});
				
			} else {
				obdLogger.error("error object !");
			}
		} catch (Exception e) {
			e.printStackTrace();
			obdLogger.error(e);
		}
		
	}

    
    @Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		super.channelReadComplete(ctx);
		ctx.flush();
	}


	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		super.exceptionCaught(ctx, cause);
		//对读取超时异常进行判断
		if(cause instanceof ReadTimeoutException){
			obdLogger.info("*********************读取超时异常");
		}
		//对写超时异常进行判断
		if(cause instanceof WriteTimeoutException){
			obdLogger.info("*********************写数据异常");
		}
		cause.printStackTrace();
		ctx.close();
	}


	/*
     * 
     * 覆盖 channelActive 方法 在channel被启用的时候触发 (在建立连接的时候)
     * 
     * channelActive 和 channelInActive 在后面的内容中讲述，这里先不做详细的描述
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
    	//客户端一连接，保存连接
    	GlobalData.CHANNEL_GROUP.add(ctx.channel());
    	
    	obdLogger.info("RemoteAddress : " + ctx.channel().remoteAddress() + " active !");

        super.channelActive(ctx);
    }

    //会话失效时触发
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		String obdSn = ctx.attr(attributeKey).getAndRemove();//obd掉线时间
		Channel channel = ctx.channel();
		if(obdSn != null){
//			boolean flag=ObdStateService.instance().obdStateChange(obdSn);
			obdLogger.info(obdSn+":会话失效:"+channel+",离线时间:"+(String)DateUtil.fromatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
		}
		obdLogger.info(channel+"->掉线！断开连接。。。");
//		设备定时没上传数据，则删除对应会话 TODO
    	GlobalData.removeOBDChannelByChannel(channel);
		super.channelInactive(ctx);
		ctx.close();
	}


	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
			throws Exception {
		super.userEventTriggered(ctx, evt);
		Channel channel = ctx.channel();
		if (evt instanceof IdleStateEvent) {  
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
            	
//            	设备定时没上传数据，则删除对应会话 TODO
            	GlobalData.removeOBDChannelByChannel(channel);
            	if(GlobalData.isReadTimeoutCount(channel)){
            		channel.close();
            	}
            	obdLogger.info("设备定时没上传数据，则删除对应会话!"+channel+"，次数："+GlobalData.ChannelReadTimeoutCount.get(channel));
//            	ctx.channel().close();
                /*读超时:客户端没数据到本Netty服务器*/  
//                System.out.println("===服务端===(READER_IDLE 读超时)");
                // 失败计数器次数大于等于3次的时候，关闭链接，等待client重连
//                if(unRecPingTimes >= MAX_UN_REC_PING_TIMES){
//                    System.out.println("===服务端===(读超时，关闭chanel)");  
//                    // 连续超过N次未收到client的ping消息，那么关闭该通道，等待client重连  
//                    ctx.channel().close();  
//                }else{
//                    // 失败计数器加1  
//                    unRecPingTimes++;  
//                }
            } else if (event.state() == IdleState.WRITER_IDLE) {  
                /*写超时：数据没向客户端返回写数据*/     
//                System.out.println("===服务端===(WRITER_IDLE 写超时)");  
            } else if (event.state() == IdleState.ALL_IDLE) {  
                /*总超时*/  
//                System.out.println("===服务端===(ALL_IDLE 总超时)");  
            }  
        }  
	}
 
	
}
