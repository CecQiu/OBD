package com.hgsoft.carowner.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.hgsoft.carowner.dao.SerInformationmanDao;
import com.hgsoft.carowner.entity.SerInformationman;
import com.hgsoft.common.service.BaseService;
import com.hgsoft.common.utils.Pager;
@Component
public class SerInformationmanService extends BaseService<SerInformationman>{
	
	@Resource
	public void setDao(SerInformationmanDao dao) {
		super.setDao(dao);
	}

	public SerInformationmanDao getSerInformationmanDao() {
		return (SerInformationmanDao) this.getDao();
	}
	/**
	 * 查询资讯信息
	 * @param infoType
	 * @param infoTitle
	 * @param starTime
	 * @param endTime
	 * @param pager
	 * @return
	 */
	public List<SerInformationman> query(String infoType,String infoTitle,String starTime,String endTime,Pager pager){
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式

		String hql="from SerInformationman s where s.infoShow='0'";
		if(infoType!=null&&infoType.length()>0){
			hql=hql+" and s.infoType='"+infoType+"'";
		}
		if(infoTitle!=null&&infoTitle.length()>0){
			hql=hql+" and s.infoTitle like '%"+infoTitle+"%'";
		}
		if(starTime!=null&&starTime.length()>0){
			if(endTime!=null&&endTime.length()>0){
				hql=hql+" and s.infoTime >='"+starTime+"' and infoTime<='"+endTime+"'";
			
			}else{
				hql=hql+" and s.infoTime >='"+starTime+"' and infoTime<='"+df.format(new Date())+"'";
				
			}
		}
		if(endTime!=null&&endTime.length()>0){
			if(starTime!=null&&starTime.length()>0){
				
			}else{
				hql=hql+" and s.infoTime >='1969-01-01 00:00:00' and infoTime<='"+endTime+"'";
				
			}
		}
		List list=this.getDao().queryByHQL(hql, null);
		pager.setTotalSize(list.size());
		
				//WHERE 日期字段名 BETWEEN CONVERT(datetime,'2013-01-01',120) AND CONVERT(datetime,'2013-01-30',120)
		return (List<SerInformationman>) this.getDao().findByHql(hql, pager);
	}
	public SerInformationman query(int infoId){
		
		String hql="from SerInformationman s where s.infoId=?";
		return (SerInformationman) this.getDao().queryByHQL(hql, infoId).get(0);
	}
}
