/**
 * 
 */
package com.hgsoft.carowner.service;



import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import com.hgsoft.carowner.dao.FaultUploadDao;
import com.hgsoft.carowner.entity.Dictionary;
import com.hgsoft.carowner.entity.FaultUpload;
import com.hgsoft.common.message.OBDMessage;
import com.hgsoft.common.message.RunningData;
import com.hgsoft.common.service.BaseService;
import com.hgsoft.common.utils.IDUtil;
import com.hgsoft.common.utils.StrUtil;

/**
 * @author liujialin
 * 5、OBD上传故障码
 */
@Service
public class FaultUploadService extends BaseService<FaultUpload> {
	private static Log logger = LogFactory.getLog(FaultUploadService.class);
	@Resource
	FaultCodeService faultCodeService;
	@Resource
	DictionaryService  dictionaryService;
	@Resource
	FaultUploadDao  faultUploadDao;

	@Resource
	public void setDao(FaultUploadDao faultUploadDao){
		super.setDao(faultUploadDao);
	}
	
	public FaultUploadDao getFaultUploadDao() {
		return (FaultUploadDao) this.getDao();
	}
	
	public FaultUpload queryById(String id){
		return ((FaultUploadDao) this.getDao()).queryById(id);
	}
	
	public boolean faultUploadSave(OBDMessage om){
		try {
			String msgBody=om.getMsgBody();//消息体
			int msgLen=om.getMsgLen();//消息体长度
			String waterNo  =om.getWaterNo();//流水号
			String command=om.getCommand();//命令字
			String obdId=om.getId();//消息ID，即是OBD或车辆 唯一标识符
			logger.info(obdId+"**********故障码上传");
			int faultTotal=Integer.parseInt(StrUtil.subStrByByteNum(msgBody, StrUtil.subStrIndexByByteNum(0), 1),16);//故障码个数
			//如果故障码个数为0，则不插入数据
			if(faultTotal>0){
				faultUpdate(obdId);//更新故障码
				//故障码
				String faultCodeStr=StrUtil.subStrByByteNum(msgBody, StrUtil.subStrIndexByByteNum(1), faultTotal*3);
				logger.info(msgBody+":"+msgLen+":"+waterNo+":"+command+":"+obdId+":"+faultTotal+":"+faultCodeStr);
				//故障码解析：十六进制字符串解析为ASC2码:501234
				List<String> faultCodeList=StrUtil.StrToStrArrByNum(faultCodeStr, 6);
				List<String> arrList = new ArrayList<String>();
				for (String string : faultCodeList) {
					String type = string.substring(0,1*2);
					String num = string.substring(1*2,string.length());
					//16进制转成
					String asc=StrUtil.hexStrToASC2(type);
					String faultStr=new String(asc+num);
					arrList.add(faultStr);
				}
				//分别记录插入
				logger.info("故障码："+arrList.toString()+"*************");
				//拆分保存入库
				Date date = new Date();
				for (String faultCode : arrList) {
					FaultUpload faultUpload = new FaultUpload();
					faultUpload.setFaultId(IDUtil.createID());
					faultUpload.setCreateTime(date);
					faultUpload.setFaultCode(faultCode);
					faultUpload.setObdSn(obdId);
					faultUpload.setRemark("");
					faultUpload.setState("1");
					this.getDao().save(faultUpload);
				}
				//获取命令字,如果有故障码上传将之前的故障码的state=1的字段更新为0
				Dictionary dic=dictionaryService.getDicByCodeAndType("owner.command", "server.faultCodeRead");
				Map<String, Object> respMap=RunningData.getIdResponseMap();
				//如果该用户有这个请求，则将故障码放进Map去
				String mapId=obdId+"_"+dic.getTrueValue();
				logger.info(obdId+"***故障码KEY:"+mapId);
				logger.info("故障码:"+StrUtil.strListToStr(arrList, ","));
				if(respMap.containsKey(mapId)){
					respMap.put(mapId, StrUtil.strListToStr(arrList, ","));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			return false;
		}
		return true;
	}
	/**
	 * 通过OBDSN获得故障码集合
	 * @param obdSn
	 * @return
	 */
	public List<FaultUpload> queryByobdSn(String obdSn){
		return getFaultUploadDao().queryByobdSn(obdSn);
	}
	/**
	 * 通过OBDSN更新故障码
	 * @param obdSn
	 * @return
	 */
	public boolean faultUpdate(String obdSn){
		return faultUploadDao.faultUpdate(obdSn);
	}
	
	/**
	 * 通过OBDSN获得故障码集合
	 * @param obdSn
	 * @return
	 */
	public List<FaultUpload> queryByObdsnAndState(String obdSn,String state){
		return getFaultUploadDao().queryByObdsnAndState(obdSn, state);
	}
	
	public void add(FaultUpload faultUpload){
		faultUploadDao.save(faultUpload);
	}
}
