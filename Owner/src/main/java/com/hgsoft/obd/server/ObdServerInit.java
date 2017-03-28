package com.hgsoft.obd.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.Set;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.hgsoft.carowner.service.OBDPacketService;
import com.hgsoft.common.utils.PropertiesUtil;
import com.hgsoft.obd.util.ObdRedisStaticsUtil;

/**
 * 服务器启动初始化
 * 
 * @author sujunguang 2015年12月12日 下午2:20:51
 */
public class ObdServerInit implements ServletContextListener {

	public static OBDPacketService obdPacketService;
	public static ObdRedisStaticsUtil obdRedisStaticsUtil;
	private static EventLoopGroup bossGroup = new NioEventLoopGroup();
	private static EventLoopGroup workerGroup = new NioEventLoopGroup();
	private static volatile boolean obdServerNettyState = false;
	/**
	 * 服务端监听的端口地址
	 */
	private static final int portNumber = new Integer(PropertiesUtil.getInstance("owner.properties").readProperty("obd.server.port","7080"));

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(arg0.getServletContext());
		obdPacketService = (OBDPacketService) context	.getBean("obdPacketService");
		obdRedisStaticsUtil = (ObdRedisStaticsUtil) context.getBean("obdRedisStaticsUtil");

		System.out.println("开始启动obd server...");
		try {
			start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Runtime.getRuntime().addShutdownHook(new Thread(new CloseObdServer()));
	}
	
	class CloseObdServer implements Runnable {
		@Override
		public void run() {
			System.out.println("~ShutdownHook~清理客户端连接");
			try {
				close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public static void start() throws Exception {
		if(!obdServerNettyState){
			new Thread(new Runnable() {
				@Override
				public void run() {
					 try {
						if(bossGroup.isShutdown()){
							bossGroup = new NioEventLoopGroup();
							workerGroup = new NioEventLoopGroup();
						}
						ServerBootstrap b = new ServerBootstrap();
						b.group(bossGroup, workerGroup);
						b.channel(NioServerSocketChannel.class)
						.option(ChannelOption.TCP_NODELAY, true)
						.option(ChannelOption.SO_BACKLOG,1024)
						/* .option(ChannelOption.SO_KEEPALIVE, true) */;
						b.childHandler(new ObdServerInitializer());
						// 服务器绑定端口监听
						ChannelFuture f = b.bind(portNumber).sync();
						System.out.println("obd server 启动完毕！");
						obdServerNettyState = true;
						// 监听服务器关闭监听
						Channel channel = f.channel();
						GlobalData.CHANNEL_GROUP.add(channel);
						channel.closeFuture().sync();
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						bossGroup.shutdownGracefully();
						workerGroup.shutdownGracefully();
					}
				}
			}).start();
			Thread.sleep(3000);
		}else{
			throw new Exception("服务已经开启！obdServerNettyState："+obdServerNettyState);
		}
	}
	
	public static void close() throws Exception{
		if(obdServerNettyState){
			GlobalData.CHANNEL_GROUP.close().awaitUninterruptibly();
			Set<String> obdSns = GlobalData.OBD_CHANNEL.keySet();
			for (String obdSn : obdSns) {
				Channel channel = GlobalData.OBD_CHANNEL.get(obdSn);
				if(channel != null) {
					channel.close();
				}
			}
			if(bossGroup != null && !bossGroup.isShutdown()){
				bossGroup.shutdownGracefully();
			}
			if(workerGroup != null && !workerGroup.isShutdown()){
				workerGroup.shutdownGracefully();
			}
			Thread.sleep(3000);
			obdServerNettyState = false;
		}else{
			throw new Exception("服务已经关闭！obdServerNettyState："+obdServerNettyState);
		}
		
	}
	
	public static boolean bossState(){
		boolean b = bossGroup.isShutdown();
		obdServerNettyState = !b;
		return b;
	}
	public static boolean wokerState(){
		return workerGroup.isShutdown();
	}
}