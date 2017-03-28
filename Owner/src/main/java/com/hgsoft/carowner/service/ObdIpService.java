/**
 * 
 */
package com.hgsoft.carowner.service;

import java.util.Map;

import javax.annotation.Resource;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.hgsoft.carowner.dao.ObdIpDao;
import com.hgsoft.carowner.entity.ObdIp;
import com.hgsoft.common.message.OBDMessage;
import com.hgsoft.common.message.RunningData;
import com.hgsoft.common.service.BaseService;

/**
 * 保存终端设备最新ip和端口
 * @author liujialin
 *
 */
@Service
public class ObdIpService extends BaseService<ObdIp> {
	private final Log logger = LogFactory.getLog(ObdIpService.class);
	
	@Resource
	public void setDao(ObdIpDao obdIpDao){
		super.setDao(obdIpDao);
	}
	
	public ObdIpDao getObdIpDao() {
		return (ObdIpDao) this.getDao();
	}
	
	/**
	 * 保存终端最新ip和端口
	 * @param ctx 客户端消息体对象
	 * @param om 消息体对象封装类
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void obdIpSave(ChannelHandlerContext ctx,AttributeKey key,OBDMessage om){
		//获取客户端IP
//		String clientIP=socket.getAddress().getHostAddress();
//		logger.info(om.getId()+"*****************OBD的id");
//		logger.info(ctx.channel().remoteAddress().toString()+"****************obd的ip和端口");
		//将会话缓存在map里，key:终端设备ID，value:会话消息
		if(om!=null){
			RunningData.getIdSessionMap().put(om.getId(), ctx);
			Map<String, ChannelHandlerContext> map = RunningData.getIdSessionMap();
		}
		
		if(key!=null){
			//将会话消息
			ctx.attr(key).set(om.getId());
		}

	}
	
	/**
	 * 根据终端设备id，获取当前设备最新的ip地址
	 * @param obdId
	 * @return
	 */
	public ObdIp getObdIpByObdId(String obdId){
		return this.getObdIpDao().getObdIpByObdId(obdId);
	}
}
