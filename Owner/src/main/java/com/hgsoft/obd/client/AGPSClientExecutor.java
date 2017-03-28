package com.hgsoft.obd.client;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;

import com.hgsoft.common.utils.StrUtil;
import com.hgsoft.system.utils.ByteUtil;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class AGPSClientExecutor {

	private String serverAddr;
	
	final EventLoopGroup group = new NioEventLoopGroup();
	Bootstrap b = new Bootstrap();
	final Channel channel = null;
	
	public AGPSClientExecutor(String serverAddr) {
		this.serverAddr = serverAddr;
	}

	public void close() throws InterruptedException{
		if(channel != null){
			channel.closeFuture().sync();
		}
		if(group != null){
			group.shutdownGracefully();
		}
	}
	
	public byte[] send(String hexStr) throws Exception {
		String host = "127.0.0.1";
		Integer port = 6767;
		if(!StringUtils.isEmpty(serverAddr)){
			String[] serverAddrArray = serverAddr.split(":");
			if(serverAddrArray.length == 2){
				host = serverAddrArray[0];
				port = Integer.valueOf(serverAddrArray[1]);
			}else{
				throw new Exception("服务器地址错误！");
			}
		}
		CountDownLatch lathc = new CountDownLatch(1);
		AGPSClientChannelInitializer clientChannelInitializer = new AGPSClientChannelInitializer(lathc);
		try {
			b.group(group).channel(NioSocketChannel.class)
			 .option(ChannelOption.TCP_NODELAY, true)
			 .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
			 .handler(clientChannelInitializer);
			// 发起一部连接操作
			ChannelFuture f = b.connect(host, port).sync();
			// 等待客户端链路关闭
			final Channel channel = f.channel();
			byte[] respbyte= hexStr.getBytes("UTF-8");
			ByteBuf resp = Unpooled.copiedBuffer(respbyte);
			channel.writeAndFlush(resp);
//			lathc = new CountDownLatch(1);
//			clientChannelInitializer.resetLathc(lathc);
//			lathc.await();
			lathc.await(3000,TimeUnit.MILLISECONDS);//开启等待会等待服务器返回结果之后再执行下面的代码
			byte[] result = clientChannelInitializer.getServerResult();
			String data = new String(result);
//			System.out.println("result:"+);
			System.out.println("result:"+result.length);
			System.out.println("result data:"+data.length());
//			System.out.println(data.substring(data.indexOf("ubx")+7));
//			System.out.println(data.substring(data.indexOf("ubx")+7).replaceAll(" ", "").length());
//			System.out.println(ByteUtil.bytesToHexString(data.substring(data.indexOf("ubx")+7).replaceAll(" ", "").getBytes()));
//			System.out.println(ByteUtil.bytesToHexString(data.substring(data.indexOf("ubx")+7).replaceAll(" ", "").getBytes()).length());
			channel.close();
			channel.closeFuture().sync();
			return result;
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			group.shutdownGracefully();
		}
		return null;
	}
	
	public static void main(String[] args) {
		try {
			new AGPSClientExecutor("agps.u-blox.com:46434").send("cmd=full;user=403291856@qq.com;pwd=Buxogx;lat=113.34;lon=23.17;pacc=20000\n");
//			byte[] b = new byte[]{};
//			System.out.println(b.length);
//			String str = "u-blox a-gps server (c) 1997-2009 u-blox AG\nContent-Length: 2808\nContent-Type: application/ubx\n\n";
//			System.out.println(str.getBytes("UTF-8").length);
//			System.out.println(new String(str.getBytes("UTF-8")).length());
//			System.out.println(str.length());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
