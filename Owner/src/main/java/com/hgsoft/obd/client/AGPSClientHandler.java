package com.hgsoft.obd.client;

import java.util.Date;
import java.util.concurrent.CountDownLatch;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class AGPSClientHandler extends ChannelInboundHandlerAdapter {
	
	
	private CountDownLatch lathc;
	private byte[] result;
	public byte[] getResult() {
        return result;
    }
	
    public AGPSClientHandler(CountDownLatch lathc) {
        this.lathc = lathc;
    }
    public void resetLatch(CountDownLatch initLathc){
        this.lathc = initLathc;
    }
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println(new Date()+":connect active");
		result = new byte[]{};
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("read server...");
		if(msg instanceof ByteBuf){
			ByteBuf buf = (ByteBuf) msg;
			byte[] dst = new byte[buf.readableBytes()];
			buf.readBytes(dst);
			final String data = new String(dst);
			byte[] strNew = new byte[dst.length + result.length];
			System.arraycopy(result, 0, strNew, 0, result.length);
			System.arraycopy(dst, 0, strNew, result.length, dst.length);
			result = strNew;
			System.out.println(dst.length);
			System.out.println(data.length());
			System.out.println("read data:"+ data);
		}
		
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		super.exceptionCaught(ctx, cause);
		ctx.close();
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		System.out.println(new Date()+":connect inactive...");
		lathc.countDown();//消息收取完毕后释放同步锁
	}
	
}