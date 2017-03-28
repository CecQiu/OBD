package com.hgsoft.obd.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hgsoft.carowner.dao.PortalDao;
import com.hgsoft.carowner.entity.Portal;
import com.hgsoft.common.service.BaseService;
import com.hgsoft.common.utils.SerialNumberUtil;
import com.hgsoft.common.utils.StrUtil;
import com.hgsoft.obd.server.OBDException;
import com.hgsoft.obd.server.ObdConstants;
import com.hgsoft.obd.util.SendUtil;
import com.hgsoft.system.utils.ByteUtil;

/**
 * Portal下发
 * 
 * @author sujunguang
 *  2016年1月7日 
 *  上午11:23:59
 */
@Service
public class ProtalSendService extends BaseService<Portal> {
	@Resource
	private SendUtil sendUtil;
	@Resource
	public void setDao(PortalDao portalDao) {
		super.setDao(portalDao);
	}

	@Override
	public PortalDao getDao() {
		return (PortalDao) super.getDao();
	}

	/**
	 * portal下发
	 */
	public boolean protalSet(Portal portal) {
		
		String msg = "";// 消息体
		String type = portal.getType();// 类别
		String typeStr = ByteUtil.ASC2ToHexStr(type);// asc码转16禁止
		if ("0".equals(type)) {
			String url = portal.getUrl();// url路径asc2码
			String urlHex = ByteUtil.ASC2ToHexStr(url);// ASC2码转16进制数
			String msgLen = StrUtil.strAppend(Integer.toHexString(urlHex.length() / 2), 2, 0, "0");// 十进制转16进制
			msg = typeStr + msgLen + urlHex;
		} else if ("1".equals(type)) {
			// 保留
		} else if ("2".equals(type)) {
			String body = portal.getMac() + "," + portal.getMb() + ",1";
			String bodyHex = ByteUtil.ASC2ToHexStr(body);
			String msgLen = StrUtil.strAppend(Integer.toHexString(bodyHex.length() / 2), 2, 0, "0");// 十进制转16进制
			msg = typeStr + msgLen + bodyHex;
		} else if ("3".equals(type)) {
			String body = portal.getWhitelists();
			String bodyHex = ByteUtil.ASC2ToHexStr(body);
			String msgLen = StrUtil.strAppend(Integer.toHexString(bodyHex.length() / 2), 2, 0, "0");// 十进制转16进制
			msg = typeStr + msgLen + bodyHex;
		} else if ("4".equals(type)) {
			msg = typeStr;
		} else if ("5".equals(type)) {
			String body = portal.getMac();
			String bodyHex = ByteUtil.ASC2ToHexStr(body);
			String msgLen = StrUtil.strAppend(Integer.toHexString(bodyHex.length() / 2), 2, 0, "0");// 十进制转16进制
			msg = typeStr + msgLen + bodyHex;
		} else if ("6".equals(type)) {
			String body = portal.getOnOff();
			String bodyHex = ByteUtil.ASC2ToHexStr(body);
			String msgLen = "01";
			msg = typeStr + msgLen + bodyHex;
		}
		
		try {
			String obdSn = portal.getObdSn().toLowerCase();
			// 流水号
			int serialNumber = SerialNumberUtil.getSerialnumber();
			msg = ObdConstants. Server_SendPortal_OBD_Cmd + msg;
			Object obj = sendUtil.msgSendGetResult(obdSn, serialNumber, msg, null);
			System.out.println("结果："+obj);//TODO
			if(obj == null){
				return false;
			}
		} catch (OBDException e) {
			e.printStackTrace();
		}
		return true;
	}

	public List<Portal> queryListByObdSn(String obdSn) {
		return getDao().queryListByObdSn(obdSn);
	}

	public boolean portalSaveOrUpdate(Portal portal) {
		try {
			getDao().saveOrUpdate(portal);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public int setByParam(String obdSn,String type,String valid) {
		return getDao().setByParam(obdSn, type, valid);
	}
}
