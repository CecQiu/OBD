package com.hgsoft.carowner.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.hgsoft.carowner.entity.PositionWarnInfo;
import com.hgsoft.common.dao.BaseDao;

@Repository
public class PositionWarnInfoDao extends BaseDao<PositionWarnInfo> {

	/**
	 * 保存位置-报警行为
	 * @param positionWarnInfo
	 */
	public void add(PositionWarnInfo positionWarnInfo) {
		List<PositionWarnInfo> positionWarnInfos = queryByHQL("from PositionWarnInfo where positionInfoId =? ", positionWarnInfo.getId());
		if(positionWarnInfos.size() < 1){
			save(positionWarnInfo);
		}
	}

}
