package com.hgsoft.carowner.service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.hgsoft.carowner.dao.SimStockInfoDao;
import com.hgsoft.carowner.entity.SimStockInfo;
import com.hgsoft.common.service.BaseService;
import com.hgsoft.common.utils.Pager;
@Component
public class SimStockInfoService extends BaseService<SimStockInfo>{
	@Resource
	public void setDao(SimStockInfoDao dao) {
		super.setDao(dao);
	}

	public SimStockInfoDao getSimStockInfoDao() {
		return (SimStockInfoDao) this.getDao();
	}
	
	/**
	 *  流量卡管理
	 * @param obdSn
	 * @param mobileNumber
	 * @param license
	 * @param simNo
	 * @param pager
	 * @return
	 */
	public List getSimInfo(String obdSn,String mobileNumber,String license,String simNo,Pager pager){
		List simList=new ArrayList();
		String sql="select m.mobileNumber,m.license,s.obdSn,s.simId,s.activateType,s.activationTime,s.name,s.totalFlow,s.flowUse from sim_stock_info s,meb_user m where m.regUserId=s.regUserId ";
		if(obdSn!=null&&obdSn.trim().length()>0){
			sql+=" and s.obdSn like '%"+obdSn+"%'";
		}
		if(mobileNumber!=null&&mobileNumber.trim().length()>0){
			sql+=" and m.mobileNumber like '%"+mobileNumber+"%'";
		}
		if(license!=null&&license.trim().length()>0){
			sql+=" and m.license like '%"+license+"%'";
		}
		if(simNo!=null&&simNo.trim().length()>0){
			sql+=" and s.simId like '%"+simNo+"%'";
		}
		List list=this.getDao().queryBySQL(sql, null);
		int begin=pager.getPageSize()*pager.getCurrentPage()-pager.getPageSize();
        int end=pager.getPageSize()*pager.getCurrentPage();
		for (int i =begin ; i < list.size()&&i<end; i++) {
			Object[] ob=(Object[]) list.get(i);
			Map map=new HashMap();
			map.put("mobileNumber", ob[0]);
			map.put("license", ob[1]);
			map.put("obdSn", ob[2]);
			map.put("simNo", ob[3]);
			map.put("activateType", ob[4]);
			map.put("activationTime", ob[5]);
			map.put("name", ob[6]);
			map.put("flowUse", ob[8]);
			Double totalFlow=Double.parseDouble(String.valueOf(ob[7]));
			Double flowUse=Double.parseDouble(String.valueOf(ob[8]));
		    DecimalFormat fm = new DecimalFormat("0.00");
		      if(totalFlow==0)
		      {
		       map.put("flow", 0);
		      }else
		      {
		    	  map.put("flow", fm.format(totalFlow-flowUse));
		      }
			simList.add(map);
		}
		pager.setTotalSize(list.size());
		return simList;
	}
	
	/**
	 * 通过SIM卡号获得流量卡
	 * @param simId
	 * @return
	 */
	public SimStockInfo queryBySimId(String simId){
		return getSimStockInfoDao().queryBySimId(simId);
	}
	/**
	 * 通过OBDSN获得流量卡
	 * @param obdSn
	 * @return
	 */
	public SimStockInfo queryBySn(String obdSn){
		return getSimStockInfoDao().queryBySn(obdSn);
	}
	
	/**
	 * 根据设备号获取最新流量卡信息
	 * @param obdSn
	 * @return
	 */
	public SimStockInfo queryLastByObdSn(String obdSn){
		return getSimStockInfoDao().queryLastByObdSn(obdSn);
	}

	public void add(SimStockInfo simStockInfo) {
		getSimStockInfoDao().save(simStockInfo);
	}
	
	public void simStockInfoUpdate(SimStockInfo simStockInfo) {
		getSimStockInfoDao().update(simStockInfo);
	}
}
