package com.hgsoft.obd.service;


import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.hgsoft.carowner.entity.FOTA;
import com.hgsoft.carowner.entity.FotaSet;
import com.hgsoft.carowner.entity.OBDDeviceVersion;
import com.hgsoft.carowner.service.FotaService;
import com.hgsoft.carowner.service.FotaSetService;
import com.hgsoft.carowner.service.OBDDeviceVersionService;
import com.hgsoft.carowner.service.OBDStockInfoService;
import com.hgsoft.common.utils.IDUtil;
import com.hgsoft.obd.server.GlobalData;
/**
 * 未完成的任务
 *		离线时的操作，或在线由于网络原因操作不成功的
 * @author sujunguang
 * 2016年1月22日
 * 下午3:55:31
 */
@Service
public class FotaHandleService {
	private static Logger serverSendObdLogger = LogManager.getLogger("serverSendObdLogger");
	
	@Resource
	private OBDStockInfoService obdStockInfoService;
	@Resource
	private ServerSettingService serverSettingService;
	@Resource
	private ServerRequestQueryService serverRequestQueryService;
	@Resource
	private FotaSetService fotaSetService;
	@Resource
	private OBDDeviceVersionService obdDeviceVersionService;
	@Resource
	private FotaService fotaService;
	/**
	 * 1、获得最新设置的FOTA记录
	 * 2、判断最新记录设置是否下发设置不成功或未下发  + 是否审核通过——》如果满足条件，则下发
	 * 3、成功，则不操作
	 * 4、不成功，则设备在线进行再次设置
	 */
	public void fotaSetSend(String obdSn){
		serverSendObdLogger.info("----------【FOTA设置下发】---------"+obdSn);
		try {
			FotaSet fotaSet =  fotaSetService.queryByObdSn(obdSn);
			FotaSet fotaSetTmp = fotaSet;
			//有效+未下发fota+审核通过
			if(fotaSet!=null){
				if("1".equals(fotaSet.getAuditResult()) && !"1".equals(fotaSet.getValid())){
					serverSendObdLogger.info("----------【FOTA设置下发】---------"+obdSn+"---下发设置前:"+fotaSet);
					FOTA fota = new FOTA();
					fota.setId(IDUtil.createID());
					fota.setCreateTime(new Date());
					fota.setFileName(fotaSet.getFileName());
					fota.setAddress(fotaSet.getFtpIP());
					fota.setPort(fotaSet.getFtpPort());
					fota.setUsername(fotaSet.getFtpUsername());
					fota.setPassword(fotaSet.getFtpPwd());
					String result=serverSettingService.fotaSet(obdSn, fota);
					serverSendObdLogger.info("----------【FOTA设置下发】---------"+obdSn+"---下发设置结果:"+result);
					boolean flag=GlobalData.isSendResultSuccess(result);
					serverSendObdLogger.info("----------【FOTA设置下发】---------"+obdSn+"---下发设置结果:"+flag);
					if(flag){
						fotaSet.setSendTime(new Date());
						fotaSet.setValid("1");//已下发
						boolean setFlag=fotaSetService.fsSaveOrUpdate(fotaSet);
						serverSendObdLogger.info("----------【FOTA设置下发】---------"+obdSn+"---fotaSet记录更新结果:"+setFlag+"---"+fotaSet);
					}
				}
				mifiSend(obdSn,fotaSetTmp);
			}
		} catch (Exception e) {
			e.printStackTrace();
			serverSendObdLogger.info("----------【FOTA设置下发】---------"+obdSn+"---异常信息:"+e);
		}
	}
	
	/**
	 * 如果下发fota成功——》下发MIFI指令
	 * 查询是否下发了MIFI指令——》如果没有——》查询设备MIFI版本号，如果一样就更新MIFI字段，如果不一样就下发MIFI指令
	 * 查询版本都要入库
	 * MIFI——》就是下发一条指令让obd升级fota
	 * 1、获得最新设置的FOTA记录
	 * 2、判断最新记录设置是否下发设置不成功或未下发 MIFI——》如果没
	 * 3、成功，则不操作
	 * 4、不成功，则设备在线进行再次设置
	 */
	public void mifiSend(String obdSn,FotaSet fotaSet){
		serverSendObdLogger.info("----------【MIFI设置下发】---------"+obdSn);
		try {
//			FotaSet fotaSet =  fotaSetService.queryByObdSn(obdSn);
			//有效+已下发fota+mifi未下发
			if(fotaSet!=null && "1".equals(fotaSet.getValid()) && !"1".equals(fotaSet.getMifiFlag())){
				serverSendObdLogger.info("----------【MIFI设置下发】---------"+obdSn+"---下发设置前:"+fotaSet);
				//查询mifi版本
				OBDDeviceVersion odv = serverRequestQueryService.deviceVersion(obdSn);
				serverSendObdLogger.info("----------【MIFI设置下发】---------"+obdSn+"---查询obd的版本信息:"+odv);
				boolean isMifiSendSuccess = false;
				if(odv!=null){
//					暂不开启~
//					if(StringUtils.isEmpty(odv.getMifiVersion())){//读取到版本号为空，则调用重启指令
//						String result = serverSettingService.restart(obdSn, "1");
//						serverSendObdLogger.info("----------【MIFI设置下发】读取到版本号MifiVersion为空!---------"+obdSn+"---【下发重启指令】---，结果:"+result);
//						return;
//					}
					//判断是否一致
					if(fotaSet.getVersion().equals(odv.getMifiVersion())){
						isMifiSendSuccess = true;
					}
				}
				
				if(isMifiSendSuccess){
					//对应更新FOTA表
					FOTA ft = fotaService.queryByObdSn(obdSn);
					if(ft==null){
						ft = new FOTA();
						ft.setId(IDUtil.createID());
						ft.setCreateTime(new Date());
					}
					ft.setVersion(fotaSet.getVersion());
					ft.setFileName(fotaSet.getFileName());
					ft.setAddress(fotaSet.getFtpIP());
					ft.setPort(fotaSet.getFtpPort());
					ft.setUsername(fotaSet.getFtpUsername());
					ft.setPassword(fotaSet.getFtpPwd());
					boolean fsFlag=fotaService.fotaSaveOrUpdate(ft);
					serverSendObdLogger.info("----------【MIFI设置下发】---------"+obdSn+"---更新fota记录结果:"+fsFlag+"---"+ft);
					
					fotaSet.setMifiTime(new Date());
					fotaSet.setMifiFlag("1");
					boolean setFlag=fotaSetService.fsSaveOrUpdate(fotaSet);
					
					serverSendObdLogger.info("----------【MIFI设置下发】---------"+obdSn+"---更新fotaSet记录结果:"+setFlag+"---"+fotaSet);
				}else{
					//下发mifi指令
					String result = serverSettingService.fotaUpgradeSet(obdSn, "1");
					serverSendObdLogger.info("----------【MIFI设置下发】---------"+obdSn+"---下发设置结果:"+result);
					boolean flag=GlobalData.isSendResultSuccess(result);
					serverSendObdLogger.info("----------【MIFI设置下发】---------"+obdSn+"---下发设置结果:"+flag);
					fotaSet.setMifiTime(new Date());
					boolean setFlag=fotaSetService.fsSaveOrUpdate(fotaSet);
					serverSendObdLogger.info("----------【MIFI设置下发】---------"+obdSn+"---更新fota记录结果:"+setFlag+"---"+fotaSet);
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			serverSendObdLogger.info("----------【MIFI设置下发】---------"+obdSn+"---异常信息:"+e);
		}
	}
	
}
