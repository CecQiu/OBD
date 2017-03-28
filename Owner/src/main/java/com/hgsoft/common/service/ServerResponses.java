package com.hgsoft.common.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.hgsoft.common.utils.SerialNumberUtil;
import com.hgsoft.common.utils.StrUtil;

/**
 * 服务器ACK
 * @author ningjiangqiang
 *
 */
@Service
public class ServerResponses{
	private static Log logger = LogFactory.getLog(ServerResponses.class);

/**
 * 服务器ACK
 * @param obdId obd设备ID
 * @param serialnumber 流水号
 * @param mucCommand 接收消息的命令字
 * @param flag 标志 ：0成功接收，1接收错误,
 * @return
 */
	public String recvACK(String obdId,String serialnumber,String mucCommand,int flag){
		logger.info("obdID:"+obdId+"****流水号:"+serialnumber+"****命令字:"+mucCommand+"***是否成功:"+flag);
		if(flag!=0&&flag!=1){
			try {
				throw new Exception("flag标志错误");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
			//消息体
		String msgBody="8001"+mucCommand+serialnumber+"0"+flag;
		logger.info("消息体："+msgBody);
		//消息体长度:服务器ACK的长度就是6个byte
		String len=Integer.toHexString(msgBody.length()/2);
		
		String msgBodyLength = StrUtil.strAppend(len, 4, 0, "0");
		
		logger.info("消息体长度："+msgBodyLength);
			//消息头
		String header=obdId+StrUtil.strAppendByLen(Integer.toHexString(SerialNumberUtil.getSerialnumber()),1,"0")+msgBodyLength;
		logger.info("消息头："+header);
		String resultStr="";
		try {
				//验证码
			String checkCode=xor(header+msgBody);
			logger.info("验证码："+checkCode);
			//		System.out.println(ByteUtil.str2HexStr(escape(header+msgBody+checkCode))+"转换后");
			//转义
			 resultStr="aa"+escape(header+msgBody+checkCode)+"aa";
		} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.info(e);
		}
		return resultStr;		
		
	}
	/**
	 * 19.布防设防设置
	 * @param OBDSN obd设备ID
	 * @param serialnumber 流水号
	 * @param flag 1设防0撤防
	 * @return
	 */
	public String deployment(String OBDSN ,int serialnumber,int flag) throws Exception{
		
		if(flag!=0&&flag!=1){
			try {
				throw new Exception("flag标志错误");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//消息体
		String msgBody="80180"+flag;
		String waterNo=String.valueOf(Integer.toHexString(serialnumber));//把10进制长度转换成16进制表示
		if(waterNo.length()<2){
			for (int i = waterNo.length(); i < 2; i++) {
				waterNo="0"+waterNo;
			}
		}
		String resultStr="";
		//消息头
		String header=OBDSN+waterNo+"0003";
		try {
			//验证码
			String checkCode=xor(header+msgBody);
			//转义
			resultStr = "aa"+escape(header+msgBody+checkCode)+"aa";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception();
		}
		return resultStr;
	}
	
	/**
	 * 16进制字符串按字节的异或校验和
	 * @param context
	 * @return
	 */
	public String xor(String context) throws Exception{
		String sum=null;
		try {
			if(context!=null&&context.length()>0){
				for (int i = 0; i < context.length(); i+=2) {
					String str=context.substring(i, i+2);
//				System.out.println(str+":i="+i);
					if(sum==null){
						sum=str;
						continue;
					}else{
						sum=xor(sum,str);				
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("字符串长度越界****************");
		}
		//如果校验码为1位，前面补0
		if(sum.length()%2!=0){
			sum="0"+sum;
		}
		logger.info("****************异或结果  sum="+sum);
		return sum;
		
	}
	
	/**
	 * 16进制字符串异或
	 * @param strHex_X
	 * @param strHex_Y
	 * @return
	 */
	private String xor(String strHex_X,String strHex_Y){
		
		String anotherBinary=Integer.toBinaryString(Integer.valueOf(strHex_X,16)); 
		String thisBinary=Integer.toBinaryString(Integer.valueOf(strHex_Y,16)); 
		String result = ""; 
		if(anotherBinary.length() != 8){ 
		for (int i = anotherBinary.length(); i <8; i++) { 
				anotherBinary = "0"+anotherBinary; 
			} 
		} 
		if(thisBinary.length() != 8){ 
		for (int i = thisBinary.length(); i <8; i++) { 
				thisBinary = "0"+thisBinary; 
			} 
		} 
		for(int i=0;i<anotherBinary.length();i++){ 
				if(thisBinary.charAt(i)==anotherBinary.charAt(i)) 
					result+="0"; 
				else{ 
					result+="1"; 
				} 
			}
//		System.out.println(Integer.toHexString(Integer.parseInt(result, 2)));
		return Integer.toHexString(Integer.parseInt(result, 2));
	}
	/**
	 * 若消息属性，消息体或者校验码中出现 0xaa 0x55：
		若数据中遇到oxaa 转义为0x55 0x01
		若数据中遇到0x55 转义为0x55 0x02
	
	 * @param msgBody 消息头+消息体+校验码
	 * @return 转义后的消息体
	 */
	public String escape(String msgBody) throws Exception{
		String context="";
		if(msgBody!=null&msgBody.length()>0){
			
			for (int i = 0; i < msgBody.length(); i+=2) {
				
				try {
					String str=msgBody.substring(i, i+2);
//				System.out.println(str+":i="+i);
					if(str.equals("55")){
						str="5502";
					}
					if(str.equals("aa")){
						str="5501";
					}
					context+=str;
				} catch (Exception e) {
					e.printStackTrace();
					logger.info(msgBody.length()+"***"+(i+2)+"****字符串越界");
					logger.error(e);
					throw new Exception();
				}
				
			}
		}

		return context;
	}

}

