package com.hgsoft.carowner.dao;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.hgsoft.carowner.entity.ObdReports;
import com.hgsoft.common.dao.BaseDao;
/**
 */
@Repository
public class ObdReportsDao extends BaseDao<ObdReports> {
	
	public ObdReports queryByDate(Date date){
		return queryFirst("FROM ObdReports WHERE date = ?", date);
	}
	
	public void obdReportsSave(ObdReports obdReports){
		save(obdReports);
	}
	
	public List<ObdReports> queryByDate(Date begin, Date end, Integer type){
		return queryByHQL("FROM ObdReports WHERE date >= ? AND date < ? AND type = ?", begin, end, type);
	}
}
