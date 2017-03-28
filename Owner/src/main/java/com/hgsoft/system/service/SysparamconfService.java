package com.hgsoft.system.service;

import com.hgsoft.common.service.BaseService;
import com.hgsoft.common.utils.Order;
import com.hgsoft.common.utils.Pager;
import com.hgsoft.system.dao.SysparamconfDao;
import com.hgsoft.system.entity.Sysparamconf;
import com.hgsoft.system.entity.Admin;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service
public class SysparamconfService extends BaseService<Sysparamconf> {
	@Resource
	private SysparamconfDao sysparamconfDao;
	
	@Resource
	public void setDao(SysparamconfDao dao) {
		super.setDao(dao);
	}
	
	/**
	 * 根据查询条件查询其他参数配置信息
	 * @param pager
	 * @param name
	 * @param value
	 * @param type
	 * @param remark
	 * @return
	 */
	public List<Sysparamconf> queryForAll(Pager pager, String name,String value, Integer type, String remark) {
		return sysparamconfDao.findAllSysparamconfByHql(pager, name,value, type, remark);
	}
	
	public Sysparamconf getSysparamconfByQname(String pname){
		return this.sysparamconfDao.getSysparamconfByQname(pname);
	}
	
	//----------------------------------------------------------------------

	public List<Sysparamconf> findAll(){
		return getDao().findAll(Order.desc("sys_id"));
	}
	public List<Sysparamconf> findPname(Object sys_id){
		return  sysparamconfDao.findPname(sys_id); 
	}
	
	public List<Sysparamconf> checkPnameByPname(Object pname){
		return sysparamconfDao.checkPnameByPname(pname);
	}
	public String readSysparameter(String pname){	
		return sysparamconfDao.readSysparameter(pname);
	}
	public String readSysparameter_NulltoInit(String pname,String pvalue){	
		return sysparamconfDao.readSysparameter_NulltoInit(pname, pvalue);
	}
	public boolean writeSysparameter(String pname,String pvalue,String remark){
		return sysparamconfDao.writeSysparameter(pname, pvalue, remark);
	}
	public boolean writeSysparameter(Sysparamconf sysparamconf){
		return sysparamconfDao.writeSysparameter(sysparamconf);
	}
	public List<Sysparamconf> findSysparamconfByPname(Object pname){
		return sysparamconfDao.findSysparamconfByPname(pname);
	}
	public boolean _update(Object sys_id,Sysparamconf sysparamconf){
		return sysparamconfDao._update(sys_id, sysparamconf);
	}
	
	public void delete(Integer sys_id){
		if(sys_id!=null){
			if(this.findPname(sys_id)!=null){
				this.deleteById(sys_id);
			}
		}
		
		
	}

	public Sysparamconf queryOrSave(String pname,String initializePvalue,String sysparamconfRemark,String logData,Admin admin) {
		return this.sysparamconfDao.queryOrSave(pname, initializePvalue, sysparamconfRemark, logData, admin);
	}
}