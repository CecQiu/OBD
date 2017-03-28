package com.hgsoft.carowner.api;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.annotation.Resource;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.hgsoft.carowner.entity.OBDStockInfo;
import com.hgsoft.carowner.entity.WarningMessage;
import com.hgsoft.carowner.service.OBDStockInfoService;
import com.hgsoft.carowner.service.WarningMessageService;
import com.hgsoft.common.utils.DateUtil;
import com.hgsoft.common.utils.MD5Coder;
import com.hgsoft.common.utils.PropertiesUtil;
import com.hgsoft.obd.util.PushUtil;
import com.hgsoft.obd.util.WarnType;
import net.sf.json.JSONObject;

/**
 * 推送接口
 * @author sujunguang
 *
 */
@Service
public class PushApi{
	private static Logger log = LogManager.getLogger("obdApiLogger");
	
	protected String cllTokenIdAddress = PropertiesUtil.getInstance("owner.properties").readProperty("cllTokenIdAddress","http://open.chelulu.cn:8080/open/api/token/getToken");
	protected String cllMsgAddress = PropertiesUtil.getInstance("owner.properties").readProperty("cllMsgAddress","http://open.chelulu.cn:8080/open/api/warningRelated/pushWarningInfo");
	
	private final String tokenAccount = new String(PropertiesUtil.getInstance("params.properties").readProperty("tokenAccount","acquisition"));
	private final String tokenPwd = new String(PropertiesUtil.getInstance("params.properties").readProperty("tokenPwd","!231@312#"));
	private final String dzwlAddress = new String(PropertiesUtil.getInstance("params.properties").readProperty("dzwlAddress","http://open.chelulu.cn:8080/open/api/warningRelated/pushElecWarningInfo"));
	
	@Resource
	private OBDStockInfoService obdStockInfoService;
	@Resource
	private WarningMessageService warningMessageService;
	private static ExecutorService executor = Executors.newFixedThreadPool(4);
	@Resource
	private PushUtil pushUtil;
	
	private final String rightCode ="0";//电信接口返回码
	
	
	/**
	 * 处理预警信息推送
	 * @param obdSn
	 */
	public void pushWarnHandler(final String obdSn,WarnType pushType,final String type,final String desc) {
		boolean canPush = pushUtil.canPushByRedis(obdSn+"_"+pushType, new Date());
		//修复bug:疲劳驾驶告警不需要卡两分钟再次判断是否推送,车辆故障发送类似
		if(pushType == WarnType.FatigueDrive || pushType == WarnType.FatigueDriveCancel || pushType == WarnType.CarFault){
			canPush = true;
		}
//		obdApiLogger.info(obdSn+"->"+pushType+"—>类型编号:"+type+",是否能推送："+canPush+"->"+PushUtil.PushApiTime.get(obdSn+"_"+pushType));
		log.info("----------【预警推送】设备:"+obdSn+"---pushType:"+pushType+"---type:"+type+"---描述:"+desc+"---是否能推送:"+canPush+"---"+pushUtil.getPushByRedis(obdSn+"_"+pushType));
		if(canPush){
			//推送
			executor.execute(new Runnable() {
				@Override
				public void run() {
					try {
						boolean result = pushWarn(obdSn,type,desc);
						log.info("----------【预警推送】设备:"+obdSn+"---推送结果1:"+result);
						if(!result){
							//推送不成功，再来一次
							log.info("----------【预警推送】设备:"+obdSn+"---推送不成功！再来一次。");
							result=pushWarn(obdSn,type,desc);
							log.info("----------【预警推送】设备:"+obdSn+"---推送结果2:"+result);
						}
					} catch (Exception e) {
						e.printStackTrace();
						log.info("----------【预警推送】设备:"+obdSn+"---推送接口异常:"+e);
					}
				}
			});
		}
	}
	
	/**
	 * 预警消息推送
	 * @param obdSn
	 * @param type
	 * @param desc
	 * @return
	 * @throws Exception
	 */
	private boolean pushWarn(String obdSn,String type,String desc) throws Exception{
		log.info("----------【预警推送】设备:"+obdSn+"---类型:"+type+"---内容:"+desc);
		OBDStockInfo obdStockInfo = obdStockInfoService.queryBySN(obdSn);
		if(obdStockInfo == null){
			throw new Exception("<"+obdSn+">设备对象为空："+obdSn);
		}
		String obdMsn = obdStockInfo.getObdMSn();//表面号
		
		//获取鉴权
		boolean flag = true;
	    try {
	    	log.info("----------【预警推送】设备:"+obdSn+"---tokenId请求参数---"+tokenAccount+"---"+tokenPwd);
	        	 
	            String tokenIdResp = getTokenId();
	            log.info("----------【预警推送】设备:"+obdSn+"---获取tokenId接口返回报文---"+tokenIdResp);
	            if(StringUtils.isEmpty(tokenIdResp)){
	            	throw new Exception("----------【预警推送】设备:"+obdSn+"---获取tokenId接口返回报文为空.");
	            }
	            JSONObject jb = JSONObject.fromObject(tokenIdResp);
	            String code = jb.optString("code");
	            if(StringUtils.isEmpty(code)){
	            	throw new Exception("----------【预警推送】设备:"+obdSn+"---获取tokenId接口返回报文无code参数.");
	            }
	            if(!rightCode.equals(code)){
	            	flag = false;
	            	return flag;
	            }
	            String data = jb.optString("data");
	            if(StringUtils.isEmpty(data)){
	            	throw new Exception("----------【预警推送】设备:"+obdSn+"---获取tokenId接口返回报文无data参数.");
	            }
	            JSONObject jb2 = JSONObject.fromObject(data);
	            String access_token = jb2.optString("access_token");
	            if(StringUtils.isEmpty(access_token)){
	            	throw new Exception("----------【预警推送】设备:"+obdSn+"---获取tokenId接口返回报文无access_token参数.");
	            }
	            //推送短信到电信接口
	            JSONObject json = new JSONObject();  
            	json.put("access_token", access_token);
            	json.put("device_pn", obdMsn);// 表面号
            	json.put("msg_type", type);// 
            	json.put("msg_desc", desc);// 
            	json.put("msg_time", DateUtil.getCurrentTime());// 
               String msgSendResp = requestSever(cllMsgAddress, json.toString(), "UTF-8");
               log.info("----------【预警推送】设备:"+obdSn+"---预警信息推送接口返回报文:"+msgSendResp);
               String msgSendRespCode = jb.optString("code");
               if(StringUtils.isEmpty(msgSendRespCode)){
            	   throw new Exception("----------【预警推送】设备:"+obdSn+"---预警信息推送接口返回报文无code参数.");
	            }
	            if( !rightCode.equals(msgSendRespCode)){
	            	flag = false;
	            	return flag;
	            }
		} catch (Exception e) {
			e.printStackTrace();
			log.info("----------【预警推送】设备:"+obdSn+"---电信权鉴或预警推送异常:"+e);
		}
		String cllResult = flag?"0":"-1";
		boolean sflag=warnMsgSave(obdSn, type, desc, new Date(), cllResult);
		log.info("----------【预警推送】设备:"+obdSn+"---预警推送记录保存结果:"+sflag);
		return flag;
	}
	
	
	/**
	 * 电子围栏预警消息推送
	 * @param obdSn
	 * @param type
	 * @param desc
	 * @return
	 * @throws Exception 
	 */
	private boolean pushDzwlWarn(String obdSn,Integer areaNum,String msgType, String msgDesc, String longitude, String latitude,Date hDate,WarnType pushType) throws Exception{
		log.info("----------【电子围栏预警推送】设备：" + obdSn+"---围栏编号:"+areaNum+"---类型:"+msgType+"---内容:"+msgDesc+"---经度:"+longitude+"---纬度:"+latitude+"---时间:"+hDate);
		OBDStockInfo obdStockInfo = obdStockInfoService.queryBySN(obdSn);
		if(obdStockInfo == null){
			throw new Exception("设备对象为空："+obdSn);
		}
		String obdMsn = obdStockInfo.getObdMSn();//获取表面号
		
		//获取鉴权
		boolean flag = true;
	    try {
	    	log.info("----------【电子围栏预警推送】设备:"+obdSn+"---tokenId请求参数---"+tokenAccount+"---"+tokenPwd);
	    	String tokenIdResp = getTokenId();
	    	log.info("----------【电子围栏预警推送】设备:"+obdSn+"---获取tokenId接口返回报文---"+tokenIdResp);
	    	if(StringUtils.isEmpty(tokenIdResp)){
	    		throw new Exception("----------【电子围栏预警推送】设备:"+obdSn+"---获取tokenId接口返回报文为空.");
            }
            JSONObject jb = JSONObject.fromObject(tokenIdResp);
            String code = jb.optString("code");
            if(StringUtils.isEmpty(code)){
            	throw new Exception("----------【电子围栏预警推送】设备:"+obdSn+"---获取tokenId接口返回报文无code参数.");
            }
            if( !rightCode.equals(code)){
            	flag = false;
            	return flag;
            }
            String data = jb.optString("data");
            if(StringUtils.isEmpty(data)){
            	throw new Exception("----------【电子围栏预警推送】设备:"+obdSn+"---获取tokenId接口返回报文无data参数.");
            }
            JSONObject jb2 = JSONObject.fromObject(data);
            String access_token = jb2.optString("access_token");
            if(StringUtils.isEmpty(access_token)){
            	throw new Exception("----------【电子围栏预警推送】设备:"+obdSn+"---获取tokenId接口返回报文无access_token参数.");
            }
            
				String msgTime = DateUtil.getTimeString(hDate, "yyyy-MM-dd HH:mm:ss");
				String inOrOut=null;
				if(pushType!=null){
					switch (pushType) {
					case Efence_InOut_In:
						inOrOut="1";
						break;
					case Efence_InOut_Out:
						inOrOut="2";
						break;
					default:
						break;
					}
				}
				
				 //推送短信到电信接口
	            JSONObject json = new JSONObject();  
            	json.put("access_token", access_token);
            	json.put("device_pn", obdMsn);// 表面号
            	json.put("msg_type", msgType);// 
            	json.put("msg_desc", msgDesc);// 
            	json.put("msg_time", msgTime);// 
            	json.put("longitude", longitude);// 
            	json.put("latitude", latitude);// 
            	json.put("inOrOut", inOrOut);// 
               String msgSendResp = requestSever(dzwlAddress, json.toString(), "UTF-8");
               log.info("----------【电子围栏预警推送】设备:"+obdSn+"---预警信息推送接口返回报文---"+msgSendResp);
               String msgSendRespCode = jb.optString("code");
               if(StringUtils.isEmpty(msgSendRespCode)){
            	   throw new Exception("----------【电子围栏预警推送】设备:"+obdSn+"---预警信息推送接口返回报文无code参数");
	            }
	            if(!rightCode.equals(msgSendRespCode)){
	            	flag = false;
	            	return flag;
	            }
		} catch (Exception e) {
			e.printStackTrace();
			log.info("----------【电子围栏预警推送】设备:"+obdSn+"---电信权鉴或预警推送异常:"+e);
		}
	    String result = flag?"0":"-1";
	    String msg =msgDesc+"---围栏编号:"+areaNum;
		boolean sflag=warnMsgSave(obdSn, msgType, msg, new Date(), result);
		log.info("----------【电子围栏预警推送】设备:"+obdSn+"---电子围栏预警推送记录保存结果:"+sflag);
		return flag;
	}
	
	/**
	 * 电子围栏预警消息推送
	 * @param obdSn
	 */
	public void pushWarnHandler(final String obdSn,final WarnType pushType,final Integer areaNum,final String msgType,final  String msgDesc,
			final  String longitude,final String latitude,final Date hDate) {
		log.info("----------【电子围栏判断】设备：" + obdSn+"---告警类型:"+pushType+"---围栏编号:"+areaNum+"---消息推送类型:"+msgType+"---描述:"+msgDesc+"---经度:"+longitude+"---纬度:"+latitude+"---gps时间:"+hDate);
//		boolean canPush = PushUtil.canPush(obdSn+"_"+pushType, new Date());
		boolean canPush = pushUtil.canPushByRedis(obdSn+"_"+pushType+areaNum, new Date());
		log.info("----------【电子围栏判断】设备：" + obdSn+"---是否进行消息推送:"+canPush);
		
		if(canPush){
			//推送
			executor.execute(new Runnable() {
				@Override
				public void run() {
					try {
						boolean result = pushDzwlWarn(obdSn, areaNum, msgType, msgDesc, longitude, latitude , hDate,pushType);
						log.info("----------【电子围栏判断】设备：" + obdSn+"---预警消息推送结果1:"+result);
						if(!result){
							log.info("----------【电子围栏判断】设备：" + obdSn+"---推送失败，再推送一次.");
							result =pushDzwlWarn(obdSn, areaNum,msgType, msgDesc, longitude, latitude , hDate,pushType);
							log.info("----------【电子围栏判断】设备：" + obdSn+"---预警消息推送结果2:"+result);
						}
					} catch (Exception e) {
						e.printStackTrace();
						log.info("----------【电子围栏判断】设备：" + obdSn+"---请求异常---"+e);
					}
				}
			});
		}
	}

	
	
	private boolean warnMsgSave(String obdSn,String msgType,String msgDesc,Date msgTime,String result){
		WarningMessage wm = new WarningMessage();
		wm.setObdSn(obdSn);
		wm.setMessageType(msgType);
		wm.setMessageDesc(msgDesc);
		wm.setMessageTime(msgTime);
		wm.setCompany("1");
		wm.setRemark(result);//是否成功
		boolean flag=warningMessageService.warningMsgSave(wm);
		return flag;
	}
	
	private String getTokenId() throws Exception{
		String time = System.currentTimeMillis()+"";
 		String sign = MD5Coder.encodeMD5Hex(tokenAccount+time+tokenPwd);
    	 JSONObject jsonParam = new JSONObject();  
    	 jsonParam.put("userid", tokenAccount);
    	 jsonParam.put("time", time);// 
    	 jsonParam.put("sign", sign);// 
    	 
        String tokenIdResp = requestSever(cllTokenIdAddress, jsonParam.toString(), "UTF-8");
        return tokenIdResp;
	}
	
	/**
	 * 号百3.0 请求电信服务器
	 * @param url
	 * @param data
	 * @param encodeType
	 * @return
	 * @throws Exception
	 */
	private String requestSever(String url, String data , String encodeType)throws Exception{
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		CloseableHttpClient httpClient = httpClientBuilder.build();  
		String strResult = "";
		HttpPost httpPost = new HttpPost(url);
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(7000).setConnectTimeout(7000).build();//设置请求和传输超时时间
		httpPost.setConfig(requestConfig);
		// 设置请求头信息
		httpPost.addHeader("Content-Type", "application/json");
		httpPost.addHeader("charset", encodeType);
		StringEntity se = new StringEntity(data, encodeType);
		se.setContentType("text/json");
		se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
		httpPost.setEntity(se);

		HttpResponse httpResponse = null;
		httpResponse = httpClient.execute(httpPost);
		int statusCode = httpResponse.getStatusLine().getStatusCode();
		if (statusCode == HttpStatus.SC_OK) {
			strResult = EntityUtils.toString(httpResponse.getEntity(), encodeType);// ,"utf-8");//设置返回数据的编码格式
		} else {
			throw new Exception("接口返回状态不是200.");
//			strResult = EntityUtils.toString(httpResponse.getEntity(), encodeType);// ,"utf-8");//设置返回数据的编码格式
		}
		httpPost.abort();//简单的终止请求
		httpClient.close();
		return strResult;
	}
	
	public static void main(String[] args) {
		try {
			System.out.println(new PushApi().pushWarn("30000824", "11", "非法启动"));
//			System.out.println(new PushApi().pushWarningInfo("00000000000000687e9fca4d", "11", "2015-12-05 13:04:00", "非法启动"));
////			System.out.println(new PushApi().getTokenId("acquisition", "!231@312#", "2"));
//			String tokenId = new PushApi().getTokenId("acquisition", "!231@312#", "2");
//			String state = null;
//			if(!StringUtils.isEmpty(tokenId)){
//			  state =new PushApi().cllMsgSent(tokenId, "4411600020041", "27", "超速告警", " 2016-07-01 12:40:40");
//			}
//			System.out.println(state);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
