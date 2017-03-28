package com.hgsoft.obd.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import com.hgsoft.common.message.OBDMessage;
import com.hgsoft.common.utils.StrUtil;

/**
 * 服务器ACK
 *
 */
@Service
public class ServerResponse{
	private static Log logger = LogFactory.getLog(ServerResponse.class);

	/** 起始符 */
	private final static String START_CODE = "aa";
	/** 结束符 */
	private final static String END_CODE = "aa";
	/** 需要转义的字符数组 */
	private final static String[][] ESCES = { { "5501", "aa" },	{ "5502", "55" } };
/**
 * 服务器ACK
 * @param obdId obd设备ID
 * @param serialnumber 流水号 请求报文里的流水号
 * @param mucCommand 接收消息的数据
 * @param flag 标志 ：00接收成功，01执行成功，02格式错误
 * @return
 */
	public String recvACK(String obdId,String serialnumber,String data,String flag){
//		logger.info("obdID:"+obdId+"****流水号:"+serialnumber+"****数据帧:"+dataFrame+"***是否成功:"+flag);
		if(StringUtils.isEmpty(flag)){
			try {
				throw new Exception("状态标识flag不能为空！");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//消息体，005-ACK命令字
		String msgBody="8001"+data;//+serialnumber+flag;
		logger.info("消息体："+msgBody);
		//消息体长度:服务器ACK的长度就是6个byte
		String len=Integer.toHexString(msgBody.length()/2);
		
		String msgBodyLength = StrUtil.strAppend(len, 4, 0, "0");
		
		logger.info("消息体长度："+msgBodyLength);
		//消息头
		String header=obdId+serialnumber+msgBodyLength;
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
				e.printStackTrace();
				logger.info(e);
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

	
	/**
	  * 若消息属性，消息体或者校验码中出现 0xaa 0x55：
		若数据中遇到0x55 0x01 转义为oxaa
		若数据中遇到0x55 0x02 转义为0x55
	 * @param msgBody 消息头+消息体+校验码
	 * @return 转义后的消息体
	 * @throws Exception 
	 */
	public static OBDMessage parseMsg(String msg) throws Exception {
		logger.info("-----报文开始进行解析为对象-------");
		logger.info("转义前报文:"+msg);
		String id = "";
		String waterNo = "";
		int msgLen = 0;
		String command = "";
		String msgBody = "";
		String checkCode = "";
		boolean checkCodeRight = true;

		// 非空判断
		if (msg == null || msg.trim().equals("")) {
			logger.error("报文为空！");
			throw new Exception("报文为空！");
		}

		// 起始符和结束符判断
		if (!msg.startsWith(START_CODE) || !msg.endsWith(END_CODE)) {
			logger.error("起始符和结束符判断无效，消息体："+msg);
			throw new Exception("起始符和结束符判断无效，消息体："+msg);
		}

		// 如果位数是奇数位，是因为数据因为数据丢失，直接返回为空
		if (msg.length() % 2 != 0) {
			logger.error("消息长度为奇数，数据丢失，消息体为：" + msg);
			throw new Exception("消息长度为奇数，数据丢失，消息体为：" + msg);
		}

		// 去掉首尾的起始符和结束符
		Pattern p = Pattern.compile("^" + START_CODE + "(.*)" + END_CODE + "$");
		Matcher m = p.matcher(msg);
		String tempStr = "";
		if (m.find()) {
			tempStr = m.group(1);
		}

		// 反转义
		for (String[] strs : ESCES) {
			tempStr = unEscape(tempStr, strs[0], strs[1]);
		}
		logger.info("消息体-特殊字符反转义结果："+tempStr);
		
		// 数据校验，由于信号收到干扰，数据在传输的过程中，可能会出现改变，所以要对数据进行校验，校验不通过，告诉obd错误
		// 如果校验码正确的话，就直接返回正确的服务器ACK
		String msgAll = tempStr.substring(0, tempStr.length() - 2);// 消息属性+消息体
		String code = tempStr.substring(tempStr.length() - 2, tempStr.length());
		logger.info("消息属性和消息体："+msgAll);
		// 本地校验
		ServerResponse sr = new ServerResponse();
		String codeTest = sr.xor(msgAll);
		logger.info("本地校验码:" + codeTest + "****原始校验码:" + code);
		// 如果校验码不正确就不响应
		if (!codeTest.equals(code)) {
			checkCodeRight = false;
			throw new Exception("校验码校验失败************:" + msg);
		}

		// 基本长度判断
		int tempLen = tempStr.length();
		if (tempLen < 2 * 10) {
			throw new Exception("error,基本长度错误**************" + msg);
		}

		// 截取各部分参数
		id = tempStr.substring(0, 2 * 4);
		waterNo = tempStr.substring(2 * 4, 2 * 5);
		msgLen = Integer.parseInt(tempStr.substring(2 * 5, 2 * 7), 16);
		
		// 如果消息体内容错误，直接返回错误信息
		command = tempStr.substring(2 * 7, 2 * 9);
		msgBody = tempStr.substring(2 * 9, tempLen - 2);
		checkCode = tempStr.substring(tempLen - 2, tempLen);

		return new OBDMessage(id, waterNo, msgLen, command, msgBody, checkCode,
				msg, checkCodeRight);
	}
	
	/**
	 * 消息体反转义<br/>
	 * （若数据中遇到oxaa 转义为0x55 0x01，若数据中遇到0x55 转义为0x55 0x02）
	 * 
	 * @param msgBody
	 *            信息体
	 * @param oldCode
	 *            被转义后的转义字串
	 * @param newCode
	 *            还原后的字符串
	 * @return 反转义后的消息体
	 */
	private static String unEscape(String msgBody, String oldCode, String newCode) {
		int index = msgBody.indexOf(oldCode);

		if (index == -1)
			return msgBody;

		if (index % 2 == 0) {
			msgBody = msgBody.replaceFirst(oldCode, newCode);
			return msgBody.substring(0, index + newCode.length())
					+ unEscape(msgBody.substring(index + newCode.length()),
							oldCode, newCode);
		} else {
			return msgBody.substring(0, index + oldCode.length() - 1)
					+ unEscape(msgBody.substring(index + oldCode.length() - 1),
							oldCode, newCode);
		}
	}

}

