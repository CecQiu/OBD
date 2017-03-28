package com.hgsoft.obd.client;

import java.util.Date;
import java.util.concurrent.CountDownLatch;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class AGPSClientChannelInitializer extends ChannelInitializer<SocketChannel> {
	
	private CountDownLatch lathc;

    public AGPSClientChannelInitializer(CountDownLatch lathc) {
        this.lathc = lathc;
    }
    public AGPSClientChannelInitializer() {
    }
    private AGPSClientHandler handler;
    
	//重置同步锁
    public void resetLathc(CountDownLatch initLathc) {
        handler.resetLatch(initLathc);
    }
    
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		handler = new AGPSClientHandler(lathc);
		ChannelPipeline pipeline = ch.pipeline();
		System.out.println(new Date()+":connect init");
		pipeline.addLast(handler);
	}

	public byte[] getServerResult(){
        return handler.getResult();
	}
}
