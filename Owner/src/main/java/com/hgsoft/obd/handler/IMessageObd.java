package com.hgsoft.obd.handler;

import com.hgsoft.common.message.OBDMessage;

/**
 * OBD消息入口接口
 * @author sujunguang
 * 2015年12月13日
 * 下午1:11:48
 */
public interface IMessageObd {
	/**
	 * 入口
	 * @param message 消息报文
	 * @return 返回处理报文结果的消息体
	 * @throws Exception 
	 */
	public String entry(OBDMessage message) throws Exception;
}
