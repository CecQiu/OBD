package com.hgsoft.common.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

public class HelloClientInitializer extends ChannelInitializer<SocketChannel> {
	private String msg;//请求参数
	private String userId;//用户id
	private String command;//命令字
	public HelloClientInitializer(String msg){
		this.msg=msg;
	}
	public HelloClientInitializer(String msg,String userId,String command){
		this.msg=msg;
		this.userId=userId;
		this.command=command;
	}
	
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
	    pipeline.addLast("idlehandler", new IdleStateHandler(20, 0 , 0));
        pipeline.addLast("handler", new HelloClientHandler(msg,userId,command));
    }
}
