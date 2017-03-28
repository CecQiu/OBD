package com.hgsoft.carowner.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.hgsoft.carowner.entity.PositionDriveInfo;
import com.hgsoft.common.dao.BaseDao;

@Repository
public class PositionDriveInfoDao extends BaseDao<PositionDriveInfo> {

	/**
	 * 保存位置-驾驶行为信息
	 * @param positionDriveInfo
	 */
	public void add(PositionDriveInfo positionDriveInfo) {
		List<PositionDriveInfo> positionDriveInfos = queryByHQL("from PositionDriveInfo where positionInfoId =? ", positionDriveInfo.getId());
		if(positionDriveInfos.size() < 1){
			save(positionDriveInfo);
		}
	}
 
}
