package com.hgsoft.common.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.handler.timeout.WriteTimeoutException;
import io.netty.util.AttributeKey;
import java.net.InetAddress;
import java.util.Date;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.hgsoft.common.message.MessageHandel;
import com.hgsoft.common.message.ObdStateService;
import com.hgsoft.common.utils.DateUtil;
import com.hgsoft.system.utils.ByteUtil;

public class HelloServerHandler extends ChannelInboundHandlerAdapter {
	private final Log logger = LogFactory.getLog(HelloServerHandler.class);
	private static final AttributeKey<String> attributeKey = AttributeKey.valueOf("obdSn");

    @Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
    	
    	try {
			//服务器端读取请求消息
			if (msg instanceof ByteBuf) {
				ByteBuf buf = (ByteBuf) msg;
				byte[] dst = new byte[buf.readableBytes()];
				buf.readBytes(dst);
				String sb = ByteUtil.bytesToHexString(dst);
//				String sb= new String(dst);
				logger.info("SERVER接收到消息0**********:" + sb);
				//如果网络延迟，一起下发报文过来，如果报文正确，要进行切割
				
				MessageHandel.instance().handelMsg(ctx,attributeKey,sb);
				
//				String responseStr=MessageHandel.instance().handelMsg(ctx,sb);
//				//服务器端发送请求消息
//				//十六进制字符串转成byte数组
//				if(responseStr!=null){
//					byte[] respbyte=ByteUtil.hexStringToBytes(responseStr);
//					ByteBuf resp = Unpooled.copiedBuffer(respbyte);
//					ctx.writeAndFlush(resp);
//				}
			} else {
				System.out.println("error object !");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}
		
	}

    
    @Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.channelReadComplete(ctx);
		ctx.flush();
	}


	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		// TODO Auto-generated method stub
		super.exceptionCaught(ctx, cause);
		//对读取超时异常进行判断
		if(cause instanceof ReadTimeoutException){
			logger.info("*********************读取超时异常");
		}
		//对写超时异常进行判断
		if(cause instanceof WriteTimeoutException){
			logger.info("*********************写数据异常");
		}
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

        System.out.println("RamoteAddress : " + ctx.channel().remoteAddress()
                + " active !");

        ctx.writeAndFlush("Welcome to "
                + InetAddress.getLocalHost().getHostName() + " service!\n");
        //将ChannelHandlerContext缓存到

        super.channelActive(ctx);
    }

    //会话失效时触发
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		String obdSn = ctx.attr(attributeKey).getAndRemove();//obd掉线时间
		if(obdSn!=null){
			boolean flag=ObdStateService.instance().obdStateChange(obdSn);
			logger.info(obdSn+"*********心跳包,车辆离线时间:"+(String)DateUtil.fromatDate(new Date(), "yyyy-MM-dd HH:mm:ss")+"***结果:"+flag);
		}
		super.channelInactive(ctx);
	}
    
}
