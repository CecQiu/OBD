package com.hgsoft.carowner.api;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.annotation.Resource;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import com.hgsoft.application.util.Base64;
import com.hgsoft.carowner.entity.ObdUpgrade;
import com.hgsoft.carowner.service.OBDStockInfoService;
import com.hgsoft.carowner.service.ObdUpgradeService;
import com.hgsoft.common.service.BaseService;
import com.hgsoft.common.utils.DateUtil;
import com.hgsoft.common.utils.HTTPFetcher;
import com.hgsoft.common.utils.MD5Coder;
import com.hgsoft.common.utils.PropertiesUtil;
import net.sf.json.JSONObject;

/**
 * 升级固件推送
 * @author liujialin
 *
 */
@Service
public class UpgradeApi extends BaseService{
	private static Logger upgradeDataLogger = LogManager.getLogger("upgradeDataLogger");

	protected String upgradeAddress = PropertiesUtil.getInstance("owner.properties").readProperty("upgradeAddress");
	
	@Resource
	private OBDStockInfoService obdStockInfoService;
	private static ExecutorService executor = Executors.newFixedThreadPool(4); 
	@Resource
	private ObdUpgradeService obdUpgradeService;
	
	/**
	 * 升级固件传输或删除
	 * ObdUpgrade obdUpgrade 固件
	 * @param type 00：新增固件，01删除固件（删除固件时文件内允许填入特殊值）
	 * @return code 0推送成功,-1推送失败,1异常
	 */
	public String[] fileSent(ObdUpgrade obdUpgrade,Integer operType) throws Exception{
		String[] res = new String[2];
		
		String size = obdUpgrade.getSize().toString();
		String filename = obdUpgrade.getFileName();
		String version = obdUpgrade.getVersion();
		String firmVersion = obdUpgrade.getFirmVersion();
		Integer firmType = obdUpgrade.getFirmType();
		String create_time = DateUtil.getTimeString(obdUpgrade.getCreateTime(), "yyyy-MM-dd HH:mm:ss");
		
		upgradeDataLogger.info("----------------【升级固件传输或删除】---------------");
		upgradeDataLogger.info("----------------【升级固件传输或删除】---大小:"+size+"---文件名:"+filename+"---版本号:"+version+"---创建时间:"+create_time);
		HTTPFetcher fetcher = new HTTPFetcher();
		JSONObject params = new JSONObject();
		params.put("username", "obdUpgrade");
		String pass = MD5Coder.encodeMD5Hex("obdhg@123");
		params.put("password", pass);
		long time =  System.currentTimeMillis();
		params.put("time",time);
		String sign = MD5Coder.encodeMD5Hex("obdUpgrade"+time+pass+"obdhgsoft");
		params.put("sign", sign);
		JSONObject body = new JSONObject();
		body.put("fileName", filename);
		body.put("version", version);
		body.put("firmVersion", firmVersion);
		body.put("firmType", firmType);
		if(obdUpgrade.getFile()!=null){
			File file = byteArrToFile(obdUpgrade.getFile(),filename);
			body.put("file", Base64.encode(FileUtils.readFileToByteArray(file)));
			body.put("size", Long.parseLong(size));
			body.put("checkSum", FileUtils.checksumCRC32(file)+"");
		}
		body.put("operType", operType);
		params.put("body", body);
		String code = "0";//默认成功
		String desc = "请求成功.";
		try {
			String result = fetcher.post(upgradeAddress+"services/upgrade/importFirmware", params.toString());
			upgradeDataLogger.info("----------------【升级固件传输或删除】返回报文:-----"+result);
			JSONObject json = JSONObject.fromObject(result);
			code = (String) json.get("code");
			desc =  (String) json.get("desc");
		} catch (Exception e) {
			e.printStackTrace();
			upgradeDataLogger.info("----------------【升级固件传输或删除】异常:-----"+e);
			code ="1";//异常
			desc ="异常:"+e;
		}
//		file.deleteOnExit();//删除文件
		res[0] = code;
		res[1] = desc;
		upgradeDataLogger.info("----------------【升级固件传输或删除】结果:-----"+code + "---描述:"+desc);
		return res;
	}
	
	private File byteArrToFile(byte[] data,String filename) throws IOException{
		File file = new File(filename);
		OutputStream output = new FileOutputStream(file);
		BufferedOutputStream bufferedOutput = new BufferedOutputStream(output);
		bufferedOutput.write(data); 
		return file;
	}
	
	/**
	 * 升级列表导入
	 * @param obdSns 设备号列表
	 * @param version 升级版本号
	 * @param type 操作类型 0-删除 1-新增
	 * @return map 0 删除成功,-1删除失败,1异常
	 * @throws Exception
	 */
	public String[] importObdList(List<String> obdSns,String version,String firmVersion,Integer type,Integer firmType) throws Exception{
		String[] res = new String[2];
		upgradeDataLogger.info("----------------【升级列表导入】---------------");
		upgradeDataLogger.info("----------------【升级列表导入】---设备号列表:"+obdSns+"---版本号:"+version+"---操作类型:"+type);
		HTTPFetcher fetcher = new HTTPFetcher();
		JSONObject params = new JSONObject();
		params.put("username", "obdUpgrade");
		String pass = MD5Coder.encodeMD5Hex("obdhg@123");
		params.put("password", pass);
		long time =  System.currentTimeMillis();
		params.put("time",time);
		String sign = MD5Coder.encodeMD5Hex("obdUpgrade"+time+pass+"obdhgsoft");
		params.put("sign", sign);
		JSONObject body = new JSONObject();
		body.put("obdSns", obdSns);
		body.put("version", version);
		body.put("firmVersion", firmVersion);
		body.put("type", type);
		body.put("firmType", firmType);
		params.put("body", body);
		upgradeDataLogger.info("----------------【升级列表导入】参数:-----"+params);
		String code = "0";
		String desc = "请求成功";
		try {
			String result = fetcher.post(upgradeAddress+"services/upgrade/importObdList", params.toString());
			JSONObject json = JSONObject.fromObject(result);
			code = (String) json.get("code");
			desc =  (String) json.get("desc");
		} catch (Exception e) {
			e.printStackTrace();
			upgradeDataLogger.error("----------------【升级列表导入】异常:-----"+e);
			code = "1";
			desc ="异常:"+e;
		}
		res[0] = code;
		res[1] = desc;
		upgradeDataLogger.info("----------------【升级列表导入】结果:-----"+code+"---描述:"+desc);
		return res;
	}
	
	
	public static void main(String[] args) {
		try {
//			System.out.println(new PushApi().pushWarningInfo("00000000000000687e9fca4d", "11", "2015-12-05 13:04:00", "非法启动"));
//			System.out.println(new PushApi().getTokenId("acquisition", "!231@312#", "2"));
//			String tokenId = new PushApi().getTokenId("acquisition", "!231@312#", "2");
//			String state = null;
//			if(!StringUtils.isEmpty(tokenId)){
//			  state =new PushApi().cllMsgSent(tokenId, "4411610050957", "13", "超速告警", " 2016-04-26 15:40:40");
//			}
//			System.out.println(state);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
