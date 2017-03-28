package com.hgsoft.system.service;

import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.hgsoft.common.service.BaseService;
import com.hgsoft.common.utils.Pager;
import com.hgsoft.system.dao.SystemLogDao;
import com.hgsoft.system.entity.Admin;
import com.hgsoft.system.entity.SystemLog;

/**
 * 系统日志Service
 * 
 * @author caijunhua
 * 
 */

@Service
public class SystemLogService extends BaseService<SystemLog> {
	@Resource
	private SystemLogDao systemLogDao;

	private SystemLog systemLog;

	// private Admin admin;
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
	 * @param endTime1
	 *            结束时间
	 * @return
	 */
	public List<SystemLog> findAllSystemLogByHql(Pager pager,Integer logType, String username, 
			String logData, String remark, String coverage,String startTime,String endTime) {
		return systemLogDao.findAllSystemLog(pager, logType, username, logData, remark, coverage, startTime, endTime);
	}

	/**
	 * 保存信息到系统日志
	 * 
	 * @param logTime
	 *            日志时间
	 * @param logType
	 *            日志类型
	 * @param admin
	 *            用户对象（操作员对象）
	 * @param logData
	 *            日志内容
	 * @param coverage
	 *            影响范围
	 * @param remark
	 *            备注
	 * @return
	 */
	public void save(Date logTime, int logType, Admin admin, String logData,
			String coverage, String remark) {
		systemLog = new SystemLog();
		// System.out.println("systemLog:"+logTime+","+admin+","+logData+","+coverage+","+remark);
		systemLog.setLogTime(logTime);
		systemLog.setLogType(logType);
		systemLog.setOperatorID(admin.getId());
		systemLog.setLogData(logData);
		systemLog.setCoverage(coverage);
		systemLog.setRemark(remark);

		systemLogDao.save(systemLog);
	}

	public List<SystemLog> findLogByTime(String startTime, String endTime) {
		return systemLogDao.findLogByTime(startTime, endTime);
	}

	public List<SystemLog> ishasSys_id(Integer sys_id) {
		return systemLogDao.ishasSys_id(sys_id);
	}

	public List<SystemLog> ishasLogTime(Date logTime) {
		return systemLogDao.ishasLogTime(logTime);
	}

	public List<SystemLog> ishasLogInfo(Integer sys_id, Date logTime,
			Integer logType) {
		return systemLogDao.ishasLogInfo(sys_id, logTime, logType);
	}

	public boolean delLogByTime(String startTime, String endTime) {
		return systemLogDao.delLogByTime(startTime, endTime);
	}

	public List<SystemLog> findSystemLog(Pager pager) {
		return systemLogDao.findSystemLog(pager);
	}

	public boolean deleteSystemLog() {
		return systemLogDao.deleteSystemLog();
	}

	public SystemLog getSystemLog() {
		return systemLog;
	}

	public void setSystemLog(SystemLog systemLog) {
		this.systemLog = systemLog;
	}

	@Resource
	public void setDao(SystemLogDao dao) {
		super.setDao(dao);
	}

}
