package com.hgsoft.common.netty;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import com.hgsoft.common.utils.PropertiesUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.AdaptiveRecvByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class ServerInit implements ServletContextListener{

	/**
     * 服务端监听的端口地址
     */
    private static final int portNumber = 
    		new Integer(PropertiesUtil.getInstance("owner.properties").readProperty("netty.port"));

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		System.out.println("开始启动socket server...");
		Thread d = new Thread(new SocketServer());
		d.start();
	}

	class SocketServer implements Runnable {
		
		@Override
		public void run() {
	        EventLoopGroup bossGroup = new NioEventLoopGroup();
	        EventLoopGroup workerGroup = new NioEventLoopGroup();
	        try {
	            ServerBootstrap b = new ServerBootstrap();
	            b.group(bossGroup, workerGroup);
	            b.channel(NioServerSocketChannel.class);
	            b.childHandler(new HelloServerInitializer());
	            
	            b.option(ChannelOption.SO_SNDBUF, 1024*1024*2);//发送缓冲区大小
	            b.option(ChannelOption.TCP_NODELAY, true);
	            b.option(ChannelOption.RCVBUF_ALLOCATOR, AdaptiveRecvByteBufAllocator.DEFAULT);
	            //服务器绑定端口监听
	            ChannelFuture f = b.bind(portNumber).sync();
	            
	            System.out.println("socket server 启动完毕！");
	            
	            // 监听服务器关闭监听
	            f.channel().closeFuture().sync();
	            // 可以简写为
	            /* b.bind(portNumber).sync().channel().closeFuture().sync(); */
	            
	        } catch(InterruptedException e){
	        	e.printStackTrace();
	        }finally {
	            bossGroup.shutdownGracefully();
	            workerGroup.shutdownGracefully();
	        }
		}
		
	}
}