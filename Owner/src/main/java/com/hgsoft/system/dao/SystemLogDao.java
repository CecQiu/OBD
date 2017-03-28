package com.hgsoft.system.dao;

import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Repository;
import com.hgsoft.common.dao.BaseDao;
import com.hgsoft.common.utils.Pager;
import com.hgsoft.system.entity.Admin;
import com.hgsoft.system.entity.SystemLog;
import com.hgsoft.system.service.AdminService;

/**
 * 系统日志DAO
 * @author caijunhua
 */

@Repository
public class SystemLogDao extends BaseDao<SystemLog> {
	@Resource
	private AdminService adminService;

	/**
	 * 根据查询条件查询系统日志
	 * 
	 * @param pager
	 * @param logType
	 *            日志类型
	 * @param username
	 *            用户名（操作员名）
	 * @param logData
	 *            日志内容
	 * @param remark
	 *            备注
	 * @param coverage
	 *            影响范围
	 * @param startTime1
	 *            开始时间
	 * @param endTime11
	 *            结束时间
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SystemLog> findAllSystemLog(Pager pager, Integer logType,
			String username, String logData, String remark, String coverage,
			String startTime, String endTime) {
		StringBuffer hql = new StringBuffer("from SystemLog systemLog where");
		if (logType != null) {
			hql.append(" systemLog.logType = " + logType + " and  ");
		}
		if (startTime != null && !"".equals(startTime)) {
			hql.append(" systemLog.logTime>='" + startTime + "' and  ");
		}
		if (endTime != null && !"".equals(endTime)) {
			hql.append(" systemLog.logTime<='" + endTime + "' and  ");
		}
		Admin admin = null;
		if ((username != null) && (!username.equals(""))) {
			admin = adminService.getAdminByUsername(username);
			if (admin != null) {
				hql.append(" systemLog.operatorID=" + admin.getId() + " and  ");
			} else {
				hql.append(" systemLog.operatorID=-1 and  ");
			}
		}
		
		if (remark != null && !"".equals(remark)) {
			hql.append(" systemLog.remark like '%" + remark + "%' and  ");
		}
		if (logData != null && !"".equals(logData)) {
			hql.append(" systemLog.logData like '%" + logData + "%' and  ");
		}
		if (coverage != null && !"".equals(coverage)) {
			hql.append(" systemLog.coverage like '%" + coverage + "%' and  ");
		}    
		hql = hql.replace(hql.length() - 6, hql.length(), "");
		
		if (pager != null) {
			pager.setTotalSize(this.executeCount(hql.toString()));
		}
		hql.append(" order by systemLog.logTime desc");
		return this.executeQuery(pager, hql.toString());
	}

	
	// 根据时间查询日志
	@SuppressWarnings("unchecked")
	public List<SystemLog> findLogByTime(String startTime, String endTime) {
		// System.out.println(data_startTime+"----dao-----"+data_endTime1);
		StringBuffer hql = new StringBuffer("from SystemLog systemlog where ");
		if (startTime != null && !"".equals(startTime)) {
			hql.append(" systemlog.logTime>='" + startTime + "' and");
		}
		if (endTime != null && !"".equals(endTime)) {
			hql.append(" systemlog.logTime<='" + endTime + "' and");
		}
		if (("".equals(startTime) || startTime == null)
				&& ("".equals(endTime) || endTime == null))
			hql = hql.replace(hql.length() - 6, hql.length(), "");
		else
			hql = hql.replace(hql.length() - 4, hql.length(), "");
		return this.executeQuery(hql.toString());
	}

	// 验证是否存在相同的sys_id,logTime,logType
	@SuppressWarnings("unchecked")
	public List<SystemLog> ishasLogInfo(Integer sys_id, Date logTime,
			Integer logType) {
		String hql = "from SystemLog systemlog where systemlog.sys_id=? and systemlog.logTime=? and systemlog.logType=?";
		return executeQuery(hql, sys_id, logTime, logType);
	}

	// 验证是否存在相同的sys_id
	@SuppressWarnings("unchecked")
	public List<SystemLog> ishasSys_id(Integer sys_id) {
		String hql = "from SystemLog systemlog where systemlog.sys_id=?";
		return executeQuery(hql, sys_id);
	}

	// 验证是否存在相同的logTime
	@SuppressWarnings("unchecked")
	public List<SystemLog> ishasLogTime(Date logTime) {
		String hql = "from SystemLog systemlog where systemlog.logTime=?";
		return executeQuery(hql, logTime);
	}

	// 查询全部日志
	@SuppressWarnings("unchecked")
	public List<SystemLog> findSystemLog(Pager pager) {
		if (pager != null) {
			pager.setTotalSize(this.executeCount("from SystemLog systemlog"));
		} 
		return executeQuery(pager,"from SystemLog systemlog  order by systemlog.logTime desc");
	}

	// 删除全部日志
	public boolean deleteSystemLog() {
		this.getSession().createSQLQuery("delete from tb_systemLog").executeUpdate();
		return true;
	}

	// 根据时间删除日志
	public boolean delLogByTime(String startTime, String endTime) {
		StringBuffer hql = new StringBuffer("delete from tb_systemLog where");
		if (startTime != null && !"".equals(startTime)) {
			hql.append(" logTime>='" + startTime + "' and  ");
		}
		if (endTime != null && !"".equals(endTime)) {
			hql.append(" logTime<='" + endTime + "' and  ");
		}
		hql = hql.replace(hql.length() - 6, hql.length(), "");
		this.getSession().createSQLQuery(hql.toString()).executeUpdate();
		return true;
	}
}
