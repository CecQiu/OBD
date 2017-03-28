package com.hgsoft.carowner.service;


import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.hgsoft.carowner.dao.ObdBarrierDao;
import com.hgsoft.carowner.entity.CarParam;
import com.hgsoft.carowner.entity.ObdBarrier;
import com.hgsoft.common.service.BaseService;
import com.hgsoft.common.utils.CoordinateTransferUtil;

@Service
public class ObdBarrierService extends BaseService<ObdBarrier> {
	private final Log logger = LogFactory.getLog(ObdBarrierService.class);
	@Resource
	private CarParamSetService carParamSetService;
	
	@Resource
	public void setDao(ObdBarrierDao obdBarrierDao){
		super.setDao(obdBarrierDao);
	}
	
	@Override
	public ObdBarrierDao getDao() {
		return (ObdBarrierDao)super.getDao();
	}
	
	//判断该用户是否已设置电子围栏
	public ObdBarrier barrierExist(String obdSn){
		return getDao().barrierExist(obdSn);
	}
	
	//设置OBD栅栏
	public boolean sendObdBarrier(ObdBarrier obdBarrier){
		CarParam carParam = new CarParam();
		carParam.setObdSn(obdBarrier.getObdSn());
		carParam.setAreaNum(obdBarrier.getAreaNum().toString());
		if(obdBarrier.getValid() == 0)
			carParam.setRailAndAlert("00000010");
		if(obdBarrier.getValid() == 1)//取消
			carParam.setRailAndAlert("00000100");
		carParam.setMaxLatitude(CoordinateTransferUtil.lnglatBarrierTransferString(obdBarrier.getMaxLatitude()));
		carParam.setMaxLongitude(CoordinateTransferUtil.lnglatBarrierTransferString(obdBarrier.getMaxLongitude()));
		carParam.setMinLatitude(CoordinateTransferUtil.lnglatBarrierTransferString(obdBarrier.getMinLatitude()));
		carParam.setMinLongitude(CoordinateTransferUtil.lnglatBarrierTransferString(obdBarrier.getMinLongitude()));
		String ret = carParamSetService.paramSet(carParam);
//		System.out.println("obdSn:"+obdBarrier.getObdSn()+"...OBD栅栏参数设置..."+ret);
		logger.info("obdSn:"+obdBarrier.getObdSn()+"...OBD栅栏参数设置..."+ret);
		if(!"00".equals(ret)){//失败
			return false;
		}
		return true;
			
	}
	
	public boolean update(ObdBarrier obdBarrier) {
		// TODO 对OBD设备进行设置 ok
		boolean b = sendObdBarrier(obdBarrier);
		if(b) super.update(obdBarrier);//设置成功则更新数据库
		return b;
	}
	
	public int saveMore(List<String> sns, String minX,String minY,String maxX,String maxY,Integer areaNo,String type) {
		// TODO 对OBD设备进行设置 ok
		List<ObdBarrier>obdBarriers =  getDao().saveMore(sns, minX, minY, maxX, maxY, areaNo, type);
		int i = 0;
		for (ObdBarrier obdBarrier : obdBarriers) {
			boolean b = this.update(obdBarrier);
			if(b) i++;
		}
		return i;
	}

	public boolean isHasOBDSN(String sn) {
		return getDao().isHasOBDSN(sn);
	}
	
}
