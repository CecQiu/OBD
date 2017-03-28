package com.hgsoft.common.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.IOException;

public class HelloClient2 {

    /**
     * @param args
     * @throws InterruptedException
     * @throws IOException
     */
    public static void clientConnectServer(String host,int port,String req,String userId,String command) throws InterruptedException,IOException {
        EventLoopGroup group = new NioEventLoopGroup(1);
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
            	.channel(NioSocketChannel.class)
            	.option(ChannelOption.TCP_NODELAY, true)
            	.option(ChannelOption.CONNECT_TIMEOUT_MILLIS,5000)
                .handler(new HelloClientInitializer(req,userId,command));
            //发起一部连接操作
            ChannelFuture f = b.connect(host, port).sync();
            //等待客户端链路关闭
            f.channel().closeFuture().sync();
        }catch(InterruptedException e){
        	e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }
    
    public static void main(String[] args) {
    	try {
    		//clientConnectServer("agps.u-blox.com", 46434, "cmd=full;user=403291856@qq.com;pwd=Buxogx;lat=47.28;lon=8.56;pacc=1000\n");
//    		clientConnectServer("127.0.0.1", 7082, "cmd=full;user=403291856@qq.com;pwd=Buxogx;lat=47.28;lon=8.56;pacc=1000\n","888888888888888888888888");
//    		clientConnectServer("agps.u-blox.com", 46434, "cmd=full;user=403291856@qq.com;pwd=Buxogx;lat=47.28;lon=8.56;pacc=1000\n","888888888888888888888888");
//    		clientConnectServer("195.34.89.144", 46434, "cmd=full;user=403291856@qq.com;pwd=Buxogx;lat=113.34;lon=23.17;pacc=20000\n","888888888888888888888888","8014");
//    		clientConnectServer("agps.u-blox.com", 46434, "cmd=full;user=403291856@qq.com;pwd=Buxogx;lat=113.34;lon=23.17;pacc=20000\n","888888888888888888888888","8014");
    		clientConnectServer("14.29.5.47", 6767, "cmd=full;user=403291856@qq.com;pwd=Buxogx;lat=113.34;lon=23.17;pacc=20000\n","888888888888888888888888","8014");
		} catch (Exception e) {
			e.printStackTrace(); 
		} finally {
			System.out.println("系统异常，请联系管理员！");
		}
			
    }
}
