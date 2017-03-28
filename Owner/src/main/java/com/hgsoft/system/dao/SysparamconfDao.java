package com.hgsoft.system.dao;

import com.hgsoft.common.dao.BaseDao;
import com.hgsoft.common.utils.JdbcUtil;
import com.hgsoft.common.utils.Pager;
import com.hgsoft.system.entity.Sysparamconf;
import com.hgsoft.system.entity.Admin;
import com.hgsoft.system.entity.SystemLog;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository 
public class SysparamconfDao extends BaseDao<Sysparamconf> {
	@Resource
	private SystemLogDao systemLogDao;
	/**
	 * 根据查询条件查询其他参数配置信息
	 * @param pager
	 * @param name
	 * @param value
	 * @param type
	 * @param remark
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Sysparamconf> findAllSysparamconfByHql(Pager pager,
			String name, String value, Integer type, String remark) {

		StringBuffer sf = new StringBuffer(
				"from Sysparamconf sysparamconf where");
		List<Object> obj = new ArrayList<Object>();
		if (!name.equals("")) {
			sf.append(" sysparamconf.pname like ? and");
			obj.add("%" + name + "%");
		}
		if (!value.equals("")) {
			sf.append(" sysparamconf.pvalue like ? and");
			obj.add("%" + value + "%");
		}
		if (type != null) {
			sf.append(" sysparamconf.ptype = ? and");
			obj.add(type);
		}
		if (!remark.equals("")) {
			sf.append(" sysparamconf.remark like ? and");
			obj.add("%" + remark + "%");
		}
		sf = sf.replace(sf.length() - 4, sf.length(), "");
		// 查出总行数，对分页进行设置
		Long count = this.executeCount(sf.toString(), obj.toArray());
		pager.setTotalSize(count);
		return this.executeQuery(pager, sf.toString(), obj.toArray());
	}

	/**
	 * 根据查询条件查询
	 * @param pname
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Sysparamconf getSysparamconfByQname(String pname){
		String hql="from Sysparamconf sysparamconf where  sysparamconf.pname=?";
		List resultList=executeQuery(hql,pname);
		if(resultList.isEmpty())return null;
		else return (Sysparamconf)resultList.get(0);
	}


	//---------------------------------------------------
	@Resource
	private JdbcUtil jdbcUtil;

	//验证保存或者修改参数时，参数名是否存在
	@SuppressWarnings("unchecked")
	public List<Sysparamconf> checkPnameByPname(Object pname){
		String hql="select sysparamconf.pname from Sysparamconf sysparamconf where sysparamconf.pname=?";
		if(!executeQuery(hql,pname).isEmpty()){
			return executeQuery(hql,pname);
		}else
			return null;
	}

	//根据参数名查询参数值
	@SuppressWarnings("unchecked")
	public List<Sysparamconf> findSysparamconfByPname(Object pname){
		String hql="from Sysparamconf sysparamconf where sysparamconf.pname=?";
		List<Sysparamconf> list=executeQuery(hql,pname);
		if(!list.isEmpty()){
			return list;
		}else
			return null;
	}

	//根据Id查询参数名
	@SuppressWarnings("unchecked")
	public List<Sysparamconf> findPname(Object sys_id){
		String hql="from Sysparamconf sysparamconf where sysparamconf.sys_id=?";
		if(!executeQuery(hql,sys_id).isEmpty()){
			return executeQuery(hql,sys_id);
		}else
			return null;
	}
	//根据Id更新参数
	public boolean _update(Object sys_id,Sysparamconf sysparamconf){
		String sql="update t_park_sysparamconf set pvalue='"+sysparamconf.getPvalue()+"', remark='"+sysparamconf.getRemark()+"' where sys_id ="+sys_id+"";
	    jdbcUtil.update(sql);
	    return true;

	}

	//根据参数名读取系统参数信息
	public String readSysparameter(String pname){
		try {
			Query query=getSession().createQuery("select spf.pvalue from Sysparamconf spf where spf.pname=?");
			query.setString(0, pname);
			if(query.list().isEmpty()){
				return null;
			}else
				return query.list().get(0).toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	//根据参数名读取系统参数信息，如果没有则插入默认的一条参数
	public String readSysparameter_NulltoInit(String pname,String pvalue){
		try {
			Query query=getSession().createQuery("select spf.pvalue from Sysparamconf spf where spf.pname=?");
			query.setString(0, pname);
			if(query.list().isEmpty()){
				Sysparamconf sysparamconf=new Sysparamconf();
				sysparamconf.setPname(pname);
				sysparamconf.setPvalue(pvalue);
				sysparamconf.setPtype(0);
				sysparamconf.setRemark("默认初始化参数");
				save(sysparamconf);
				return null;
			}
            return query.list().get(0).toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	//向系统写入参数信息
	public boolean writeSysparameter(String pname,String pvalue,String remark){
		Sysparamconf sysparamconf=new Sysparamconf();
		sysparamconf.setId(1);
		sysparamconf.setPname(pname);
		sysparamconf.setPvalue(pvalue);
		sysparamconf.setPtype(0);
		sysparamconf.setRemark(remark);
		try {
			getSession().save(sysparamconf);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	//向系统写入参数信息根据对象
	public boolean writeSysparameter(Sysparamconf sysparamconf){
		try {
			getSession().save(sysparamconf);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	public Sysparamconf queryOrSave(String pname,String initializePvalue,String sysparamconfRemark,String logData,Admin admin){
		SystemLog systemLog=new SystemLog();
		Sysparamconf sysparamconf1 =this.getSysparamconfByQname(pname);
		Sysparamconf sysparamconf=new Sysparamconf();
		if(sysparamconf1==null){
			 sysparamconf.setPname(pname);
			 sysparamconf.setPvalue(initializePvalue);
			 sysparamconf.setPtype(0);
			 sysparamconf.setRemark(sysparamconfRemark);
			 this.save(sysparamconf);
			 return sysparamconf;
		} else{
			try{
				sysparamconf.setPvalue(sysparamconf1.getPvalue());
				return sysparamconf1;
			} catch(Exception e){
				sysparamconf.setPvalue(initializePvalue);
				systemLog.setLogTime(new Date());
				systemLog.setLogType(2);
				if(admin!=null)
				systemLog.setOperatorID(admin.getId());
				systemLog.setLogData(logData);
				systemLog.setCoverage(e.getMessage());
				systemLog.setRemark("Exception");
				systemLogDao.save(systemLog);
				e.printStackTrace();
				return sysparamconf1;
			}
		}
	}
}
