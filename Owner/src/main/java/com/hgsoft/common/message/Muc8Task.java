package com.hgsoft.common.message;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.hgsoft.carowner.entity.MebCarFault;
import com.hgsoft.carowner.service.MebCarFaultService;
import com.hgsoft.common.service.ServerResponses;
import com.hgsoft.common.utils.IDUtil;

/**
 * Muc8上传OBD状态接口任务类
 * @author njq
 *
 */
@Component
public class Muc8Task {
	private final Log logger = LogFactory.getLog(Muc8Task.class);
	
	@Resource
	private  MebCarFaultService mebCarFaultService;
	/**
	 * 接收设备上传的OBD状态
	 * @param obdMessage
	 * @return
	 */
	public  String obdStatus(OBDMessage obdMessage){
		return null;
//		System.out.println("-----------------进入obdStatus");
//		ServerResponses sr=new ServerResponses();//消息返回
//		if(obdMessage.getMsgBody()!=null&&obdMessage.getMsgBody().length()>0){
//			MebCarFault carFault=new MebCarFault();
//			//DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			String year=obdMessage.getMsgBody().substring(0, 2);
//			String month=obdMessage.getMsgBody().substring(2, 4);
//			String date=obdMessage.getMsgBody().substring(4, 6);
//			String hrs=obdMessage.getMsgBody().substring(6, 8);
//			String min=obdMessage.getMsgBody().substring(8, 10);
//			String sec=obdMessage.getMsgBody().substring(10, 12);	
//			String time="20"+year+"-"+month+"-"+date+" "+hrs+":"+min+":"+sec;
//			System.out.println(time+":时间");
//			carFault.setObdSn(obdMessage.getId());
//			carFault.setFaultUpdateTime(time);
//			carFault.setSpeed(sub(obdMessage.getMsgBody(),12,14));
//			carFault.setVoltage(sub(obdMessage.getMsgBody(),14,16));
//			carFault.setFaultNum(sub(obdMessage.getMsgBody(),16,18));
//			carFault.setFaultType(sub(obdMessage.getMsgBody(),18,20));
//			carFault.setTemperature(sub(obdMessage.getMsgBody(),20,22));
//			carFault.setJinqiYaLi(sub(obdMessage.getMsgBody(),22,24));
//			carFault.setRotationalSpeed(sub(obdMessage.getMsgBody(),24,28));
//			carFault.setDianHuoOneTiqianJiao(sub(obdMessage.getMsgBody(),28,32));
//			carFault.setJinqiTemp(sub(obdMessage.getMsgBody(),32,34));
//			carFault.setKongqiLiuliang(sub(obdMessage.getMsgBody(),34,38));
//			carFault.setQijiemenWeizi(sub(obdMessage.getMsgBody(),38,40));
//			carFault.setSecKongqiZhuang(sub(obdMessage.getMsgBody(),40,44));
//			carFault.setO2weizi(sub(obdMessage.getMsgBody(),44,48));
//			carFault.setFadongJiStart(sub(obdMessage.getMsgBody(),48,52));
//			carFault.setFaultDriveNum(sub(obdMessage.getMsgBody(),52,56));
//			carFault.setJgyouguiYali(sub(obdMessage.getMsgBody(),56,58));
//			carFault.setDqyouguiYali(sub(obdMessage.getMsgBody(),58,60));
//			carFault.setFeiqiXunhuan(sub(obdMessage.getMsgBody(),60,62));
//			carFault.setEgrkaidu(sub(obdMessage.getMsgBody(),62,64));
//			carFault.setZhengfaChongxi(sub(obdMessage.getMsgBody(),64,66));
//			carFault.setRanyouYewei(sub(obdMessage.getMsgBody(),66,68));
//			carFault.setZhengfaYali(sub(obdMessage.getMsgBody(),68,72));
//			carFault.setDaqiya(sub(obdMessage.getMsgBody(),72,76));
//			carFault.setXiangduiQijiemen(sub(obdMessage.getMsgBody(),76,78));
//			carFault.setHuanjinTemp(sub(obdMessage.getMsgBody(),78,80));
//			carFault.setYouqimenB(sub(obdMessage.getMsgBody(),80,82));
//			carFault.setYouqimenC(sub(obdMessage.getMsgBody(),82,84));
//			carFault.setJiasutaBanD(sub(obdMessage.getMsgBody(),84,86));
//			carFault.setJiasutaBanE(sub(obdMessage.getMsgBody(),84,88));
//			carFault.setJiasutaBanF(sub(obdMessage.getMsgBody(),88,90));
//			carFault.setQijiemenKongziad(sub(obdMessage.getMsgBody(),90,92));
//			carFault.setFaultFadongjiRun(sub(obdMessage.getMsgBody(),92,96));
//			carFault.setYouguijueduiyaLic1(sub(obdMessage.getMsgBody(),96,100));
//			carFault.setJiasutaBanFc2(sub(obdMessage.getMsgBody(),100,102));
//			carFault.setJiyouTemp(sub(obdMessage.getMsgBody(),102,106));
//			//carFault.setDongjieFault("11");//故障码定义不清楚	
//			//carFault.setObdBefore("11");
//			carFault.setFaultId(IDUtil.createID());
//			System.out.println(IDUtil.createID()+"---IDUtil.createID()");
//			mebCarFaultService.add(carFault);
//			String resultStr;
//			try {
//				resultStr = sr.recvACK(obdMessage.getId(), obdMessage.getWaterNo(), obdMessage.getCommand(), 0);
//			} catch (Exception e) {
//				e.printStackTrace();
//				logger.error(e);
//			}
//			return resultStr;
//		}else{
//			return sr.recvACK(obdMessage.getId(), obdMessage.getWaterNo(), obdMessage.getCommand(), 1);
//		}
		
	}
	/**
	 * 截取16进制的字符串 并把其转成10进制
	 * @param msgbody
	 * @param begin
	 * @param end
	 * @return
	 */
	private static String sub(String msgbody,int begin,int end){
		
		return String.valueOf(Integer.parseInt(msgbody.substring(begin, end), 16));
	}
	
	public static void main(String[] args) {
		//System.out.println(restore("550101550202"));
	}
}
