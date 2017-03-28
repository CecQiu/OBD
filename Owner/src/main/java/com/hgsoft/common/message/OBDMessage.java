package com.hgsoft.common.message;

/**
 * 报文类
 * @author fdf
 */
public class OBDMessage {
	//ID号
	private String id;
	//流水号
	private String waterNo;
	//消息体长
	private int msgLen;
	//命令字
	private String command;
	//消息主体
	private String msgBody;
	//校验码
	private String checkCode;
	//报文内容
	private String message;
	//校验消息内容是否正确
	private boolean checkCodeRight = true; 
	
	//无参构造方法
	public OBDMessage() {}
	
	//全参数构造方法
	public OBDMessage(String id, String waterNo, int msgLen, String command,
			String msgBody, String checkCode, String message ) {
		this.id = id;
		this.waterNo = waterNo;
		this.msgLen = msgLen;
		this.command = command;
		this.msgBody = msgBody;
		this.checkCode = checkCode;
		this.message = message;
	}
	//全参数构造方法
	public OBDMessage(String id, String waterNo, int msgLen, String command,
			String msgBody, String checkCode, String message,boolean checkCodeRight) {
		this.id = id;
		this.waterNo = waterNo;
		this.msgLen = msgLen;
		this.command = command;
		this.msgBody = msgBody;
		this.checkCode = checkCode;
		this.message = message;
		this.checkCodeRight = checkCodeRight;
	}
	
	@Override
	public String toString() {
		return "id=["+id+"],waterNo=["+waterNo+"],msgLen=["+msgLen+"]," +
				"command=["+command+"],msgBody=["+msgBody+"]," +
						"checkCode=["+checkCode+"]";
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getWaterNo() {
		return waterNo;
	}
	public void setWaterNo(String waterNo) {
		this.waterNo = waterNo;
	}
	public int getMsgLen() {
		return msgLen;
	}
	public void setMsgLen(int msgLen) {
		this.msgLen = msgLen;
	}
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	public String getMsgBody() {
		return msgBody;
	}
	public void setMsgBody(String msgBody) {
		this.msgBody = msgBody;
	}
	public String getCheckCode() {
		return checkCode;
	}
	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	public boolean getCheckCodeRight() {
		return checkCodeRight;
	}

	public void setCheckCodeRight(boolean checkCodeRight) {
		this.checkCodeRight = checkCodeRight;
	}
	
}
