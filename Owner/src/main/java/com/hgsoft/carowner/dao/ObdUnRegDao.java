package com.hgsoft.carowner.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.hgsoft.carowner.entity.ObdUnReg;
import com.hgsoft.common.dao.BaseDao;
/**
 */
@Repository
public class ObdUnRegDao extends BaseDao<ObdUnReg> {
	
	public ObdUnReg queryByObdSn(String obdSn){
		List<ObdUnReg> obdUnRegs = queryByHQL("from ObdUnReg where obdSn = ? ", obdSn);
		return obdUnRegs.size() > 0 ? obdUnRegs.get(0) : null;
	}
}
