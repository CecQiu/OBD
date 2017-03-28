package com.hgsoft.common.netty.decode;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.hgsoft.system.utils.ByteUtil;
/**
 * OBD 报文解码器
 * @author sujunguang
 * 2015年12月17日
 * 上午10:35:37
 */
public class ObdDecoder extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf msg,
			List<Object> out) throws Exception {
		ByteBuf buf = (ByteBuf) msg;
		byte[] dst = new byte[buf.readableBytes()];
		buf.readBytes(dst);
 
//		String str = ByteUtil.bytesToHexString(dst);
		String str = new String(dst);
		str = str.replaceAll("_", "a");
		if(StringUtils.isEmpty(str))
			return;
//		System.err.println("str:"+str);
		if(!str.startsWith("aa"))
			str = "aa" + str;
		if(!str.endsWith("aa"))
			str = str + "aa";
		out.add(Unpooled.copiedBuffer((str).getBytes()));
	}
 
}
