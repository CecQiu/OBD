package com.hgsoft.obd.server;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;

import com.hgsoft.common.netty.decode.ObdByteDecoder;
import com.hgsoft.common.netty.decode.ObdDecoder;
import com.hgsoft.common.utils.PropertiesUtil;
/**
 * OBD服务器初始化
 * @author sujunguang
 * 2015年12月12日
 * 下午2:24:19
 */
/**
 * OBD服务器初始化
 * @author sujunguang
 * 2015年12月12日
 * 下午2:24:19
 */
public class ObdServerInitializer extends ChannelInitializer<SocketChannel> {
	
	  @Override
	    protected void initChannel(SocketChannel ch) throws Exception {
	        ChannelPipeline pipeline = ch.pipeline();
	        // 自己的逻辑Handler
	        
//	        pipeline.addLast(new OBDTraffic(1,1,1));
	        
	        //增加解码器
	        pipeline.addLast(new ObdByteDecoder());//Byte解码器
	        //aa分隔符解码器
	        pipeline.addLast(new DelimiterBasedFrameDecoder(10240,Unpooled.copiedBuffer("aa".getBytes())));
	        //OBD报文头和尾解码器
	        pipeline.addLast(new ObdDecoder());
	       //设置空闲状态处理操作
	       int readTimeout = Integer.valueOf(PropertiesUtil.getInstance("owner.properties").readProperty("netty.readTimeOut", "45"));
	       pipeline.addLast("idlehandler", new IdleStateHandler(readTimeout, 0 , 0));
	        
	      
	        pipeline.addLast("handler", new ObdServerHandler());
	    }
	}
