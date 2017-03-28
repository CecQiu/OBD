package com.hgsoft.common.netty.decode;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.hgsoft.carowner.entity.OBDPacket;
import com.hgsoft.common.utils.IDUtil;
import com.hgsoft.obd.server.ObdServerInit;
import com.hgsoft.system.utils.ByteUtil;

/**
 * OBD 报文解码器
 * 
 * @author sujunguang 2015年12月17日 上午10:35:37
 */
public class ObdByteDecoder extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf msg,
			List<Object> out) throws Exception {
		ByteBuf buf = (ByteBuf) msg;
		byte[] dst = new byte[buf.readableBytes()];
		buf.readBytes(dst);
		
		String str = ByteUtil.bytesToHexString(dst);
		
		if (!StringUtils.isEmpty(str)) {
//			OBDPacket obdPacket = new OBDPacket(IDUtil.createID());
//			obdPacket.setPacketType(0);
//			obdPacket.setPacketData(str);
//			obdPacket.setInsertTime(new Date());
//			ObdServerInit.obdPacketService.add(obdPacket);

			String result = "";
			char[] cc = str.toCharArray();
			
 			for (int i = 0; i < cc.length; i++) {
				// System.out.println(new String(""+(char)cc[i]+(char)cc[++i]));
				String s = new String("" + (char) cc[i] + (char) cc[++i]);
				if (!"aa".equals(s) && s.contains("a")) {
					s = s.replace("a", "_");
				}
				result += s;
			}

			if (!StringUtils.isEmpty(result)) {
				out.add(Unpooled.copiedBuffer((result).getBytes()));
			}
		}
	}

}
