package com.hgsoft.carowner.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import com.hgsoft.carowner.entity.Dictionary;
import com.hgsoft.common.message.Muc4Task;
import com.hgsoft.common.message.Muc7Task;
import com.hgsoft.common.message.OBDMessage;
import com.hgsoft.common.utils.StrUtil;

/**
 * @author liujialin 6. Mcu6上传批量信息
 */
@Service
public class MsgUploadBatchService {
	private static Log logger = LogFactory.getLog(MsgUploadBatchService.class);
	@Resource
	DictionaryService dictionaryService;
	@Resource
	private Muc4Task muc4Task;
	@Resource
	private Muc7Task muc7Task;

	/**
	 * 批量信息保存入库
	 * 
	 * @param om
	 * @return
	 * @throws Exception
	 */
	public boolean msgSave(OBDMessage om) {
		String msgBody = om.getMsgBody();// 消息体
		String obdSn = om.getId();// 消息ID，即是OBD或车辆 唯一标识符
		logger.info(obdSn + "***批量位置或行程信息上传:" + msgBody);
		int msgLen = om.getMsgLen();// 消息体长度
		String waterNo = om.getWaterNo();// 流水号
		String command = om.getCommand();// 命令字

		logger.info(obdSn + "***命令字:" + command + "***流水号:" + waterNo + "***消息长度:" + msgLen + "***消息体:" + msgBody);
		int byteNum = 0;// 参数列表开始下标,前面的byte总数
		boolean flag = true;
		String id = StrUtil.subStrByByteNum(msgBody, StrUtil.subStrIndexByByteNum(byteNum), 1);// 参数ID
		logger.info(obdSn + "********批量信息上传类别" + id);
		byteNum = byteNum + 1;// 前面的byte总数
		String paramLen = StrUtil.subStrByByteNum(msgBody, StrUtil.subStrIndexByByteNum(byteNum), 2);// 参数总长度
		int totalLen = Integer.valueOf(paramLen, 16);// 十六进制转十进制
		byteNum = byteNum + 2;// 前面的byte总数
		int len = 0;
		// 如果是位置信息a4
		if ("a4".equals(id)) {
			String type = StrUtil.subStrByByteNum(msgBody, StrUtil.subStrIndexByByteNum(byteNum), 1);// 位置信息类型，对应消息长度不一样
			logger.info(type + "**********位置信息类别");
			len = 24;// 位置信息1,24个byte
		} else if ("a5".equals(id)) {
			len = 41;// 0xa5表示行程记录信息,41个byte
		} else {
			// 非法类型
			logger.error(obdSn + "****************************批量信息上传非法类别");
			return false;
		}
		try {
			do {
				String paramMsg = StrUtil.subStrByByteNum(msgBody, StrUtil.subStrIndexByByteNum(byteNum), len);// 参数长度
				// 如果截取到的报文都是ff,停止本次循环
				String error = StrUtil.strSame(len * 2, "f");
				if (error.equals(paramMsg)) {
					logger.error(paramMsg + "**********错误报文");
					byteNum = byteNum + len;// 前面的byte总数
					continue;// 如果全是ff
				}
				byteNum = byteNum + len;// 前面的byte总数
				Map<String, String> map = new ConcurrentHashMap<String, String>();
				map.put(id, paramMsg);
				flag = msgAnalyze(map, om);// 解析报文
			} while (byteNum < totalLen + 3);// 如果字符串长度大于等于消息体长度
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			return false;
		}
		return flag;
	}

	/**
	 * 报文解析
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public boolean msgAnalyze(Map<String, String> map, OBDMessage om) {
		boolean flag = true;
		try {
			for (String key : map.keySet()) {
				logger.info(key + "***" + map.get(key));
				String paramStr = map.get(key);// 消息内容
				String paramStrLen = Integer.toHexString(paramStr.length() / 2);
				// 0xa4表示位置信息，0xa5表示行程记录信息， 0x5a故障码消息
				// String msgBody=om.getMsgBody();//消息体
				// int msgLen=om.getMsgLen();//消息体长度
				String waterNo = om.getWaterNo();// 流水号
				// String command=om.getCommand();//命令字
				String obdSn = om.getId();// 消息ID，即是OBD或车辆 唯一标识符
				OBDMessage obd = new OBDMessage();
				if ("a4".equals(key)) {
					obd.setId(obdSn);
					Dictionary dic = dictionaryService.getDicByCodeAndType("owner.command", "client.position");
					String common = dic.getTrueValue();// 命令字
					obd.setCommand(common);// 命令字
					obd.setMessage(
							"aa" + om.getId() + om.getWaterNo() + paramStrLen + paramStr + om.getCheckCode() + "aa");// 拼接完整报文
					obd.setMsgBody(paramStr);
					obd.setWaterNo(waterNo);
					obd.setMsgLen(paramStr.length() / 2);
//					muc4Task.doTask(obd);
					logger.info(obdSn + "***************批量上传位置信息是否成功");
				}
				if ("a5".equals(key)) {
					obd.setId(obdSn);
					Dictionary dic = dictionaryService.getDicByCodeAndType("owner.command", "client.journey");
					String common = dic.getTrueValue();// 命令字
					obd.setCommand(common);// 命令字
					obd.setMessage(
							"aa" + om.getId() + om.getWaterNo() + paramStrLen + paramStr + om.getCheckCode() + "aa");// 拼接完整报文
					obd.setMsgBody(paramStr);
					obd.setWaterNo(waterNo);
					obd.setMsgLen(paramStr.length() / 2);
					muc7Task.doTask(obd);
					logger.info(obdSn + "***************批量上传位置信息是否成功");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		}
		return flag;
	}
}
