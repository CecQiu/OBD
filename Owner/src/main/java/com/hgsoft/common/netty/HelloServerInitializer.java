package com.hgsoft.common.netty;

import com.hgsoft.common.netty.decode.ObdByteDecoder;
import com.hgsoft.common.netty.decode.ObdDecoder;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
public class HelloServerInitializer extends ChannelInitializer<SocketChannel> {

	
	  @Override
	    protected void initChannel(SocketChannel ch) throws Exception {
	        ChannelPipeline pipeline = ch.pipeline();
//	        //粘包分包
//	        pipeline.addLast(new ObdByteDecoder());//Byte解码
//	        pipeline.addLast(new DelimiterBasedFrameDecoder(1024*1024,Unpooled.copiedBuffer("aa".getBytes())));//切包
//	        pipeline.addLast(new ObdDecoder());//转换
	        // 自己的逻辑Handler
	        pipeline.addLast("handler", new HelloServerHandler());
	    }
	}
