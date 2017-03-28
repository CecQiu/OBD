package com.hgsoft.carowner.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.hgsoft.carowner.dao.UpgradeSetDao;
import com.hgsoft.carowner.entity.UpgradeSet;
import com.hgsoft.common.service.BaseService;
import com.hgsoft.common.utils.Pager;
/**
 * 未注册设备远程升级service
 * @author liujialin
 * 2015-12-25
 */
@Service
public class UpgradeSetService extends BaseService<UpgradeSet> {
	
	@Resource
	private UpdateDataService updateDataService;
	
	@Resource
	public void setDao(UpgradeSetDao upgradeSetDao){
		super.setDao(upgradeSetDao);
	}
	
	public UpgradeSetDao upgradeSetGetDao() {
		return (UpgradeSetDao)super.getDao();
	}
	
	/**
	 * 通过OBDSN获得UpgradeSet
	 * @param obdSn
	 * @return
	 */
	public UpgradeSet queryByObdSn(String obdSn,String valid){
		return upgradeSetGetDao().queryByObdSn(obdSn,valid);
	}

	public UpgradeSet queryByObdSnLike(String obdSn,String valid){
		return upgradeSetGetDao().queryByObdSnLike(obdSn,valid);
	}

	public UpgradeSet queryByObdSn(String obdSn){
		return upgradeSetGetDao().queryByObdSn(obdSn);
	}
		
	public UpgradeSet queryByObdSnLike(String obdSn){
		return upgradeSetGetDao().queryByObdSnLike(obdSn);
	}

	public UpgradeSet queryByObdSnLikeType(String obdSn, int type){
		return upgradeSetGetDao().queryByObdSnLikeType(obdSn, type);
	}
	
	/**
	 * 根据版本号获取记录
	 */
	public List<UpgradeSet> queryByVersion(String version) {
		return upgradeSetGetDao().getListByVersion(version);
	}
	
	//根据设备号和版本号
	public UpgradeSet queryByObdSnAndVersion(String obdSn,String version) {
		return upgradeSetGetDao().queryByObdSnAndVersion(obdSn,version);
	}
	
	/**
	 * 获取该版本号下所有未升级成功的升级列表
	 * @param version
	 * @param valid
	 * @return
	 */
	public List<UpgradeSet> getListByVersionAndVflag(String version,String vflag) {
		return upgradeSetGetDao().getListByVersionAndVflag(version,vflag);
	}
	/**
	 * 获取审核成功，且未推送过的
	 * @param version
	 * @param sendFlag
	 * @param auditState
	 * @return
	 */
	public List<UpgradeSet> queryByVersionAndSflagAndAstate(String version,String sendFlag,String auditState) {
		return upgradeSetGetDao().queryByVersionAndSflagAndAstate(version, sendFlag,auditState);
	}
	/**
	 * 通过OBDSN修改obd版本号
	 * @param obdSn
	 * @param carState
	 * @return
	 */
	public boolean upgradeSetUpdate(UpgradeSet upgradeSet){
		 try {
			 upgradeSetGetDao().update(upgradeSet);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		 return true;
	}
	
	public boolean upgradeSetSave(UpgradeSet upgradeSet){
		try {
			upgradeSetGetDao().saveOrUpdate(upgradeSet);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	//判断升级记录是否存在重复
	public List<UpgradeSet> getListByMap(Map<String, Integer> obdSns,Integer firmType) {
		return upgradeSetGetDao().getListByMap(obdSns,firmType);
	}
	
	/**
	 * obd设备excel文档导入
	 * @param file 文件
	 * @param fileName 文件名
	 * @param version 固件版本号
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public String upgradeSetExcel(File file,Integer firmType,String fileName,String version,String firmVersion) throws FileNotFoundException, IOException{
		try {
			//1
			String ftype=fileName.substring(fileName.indexOf("."),fileName.length());
			//2读取excel文档保存入库
			Workbook workbook = null;
			if(".xls".equals(ftype)){
				workbook = new HSSFWorkbook(new FileInputStream(file));
			}else if(".xlsx".equals(ftype)){
					workbook = new XSSFWorkbook(new FileInputStream(file));
			}else{
				return "false_excel文件后缀名有误,请检查---"+ftype;
			}
			List<UpgradeSet> upgradeSetList = new ArrayList<UpgradeSet>();
			//循环工作表Sheet
//			List<String> obdSns = new ArrayList<String>();
			
			String obdSnIsNull = "";
			String speedError = "";
			Map<String, Integer> obdSns = new HashMap<String, Integer>();
			for (int numSheet = 0;numSheet<workbook.getNumberOfSheets();numSheet++) {
				Sheet sheet = workbook.getSheetAt(numSheet);
			     //循环row
			     for (Row row : sheet) {
			    	int rowNum=row.getRowNum();
		        	//第一行是标题行
		        	if(rowNum==0){
		        		continue;
		        	}
		        	
		        	UpgradeSet upgradeSet = new UpgradeSet();
		        	String obdSn ="";
			    	if(row.getCell(0)!=null){
			    		row.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
						obdSn = row.getCell(0).getStringCellValue();//设备号
			    	}
					
					//
					String gpsSpeedStr ="";
					if(row.getCell(1)!=null){
						row.getCell(1).setCellType(Cell.CELL_TYPE_STRING);
						 gpsSpeedStr = row.getCell(1).getStringCellValue();//gps速度
					}
					
					//
					String obdSpeedStr = "";
					if(row.getCell(2)!=null){
						row.getCell(2).setCellType(Cell.CELL_TYPE_STRING);
						obdSpeedStr = row.getCell(2).getStringCellValue();//obd速度
					}
					
					
					//如果gps速度不为空，且obd速度不为空,而设备号为空
					if(!StringUtils.isEmpty(gpsSpeedStr) || !StringUtils.isEmpty(obdSpeedStr) ){
						if(StringUtils.isEmpty(obdSn)){
							obdSnIsNull+=rowNum+";";
							continue;
						}
					}
					
					if(StringUtils.isEmpty(obdSn)){
						continue;
					}
					
					//速度非空的话，必须是阿拉伯数组，并且是0-255范围
					if(!StringUtils.isEmpty(gpsSpeedStr)){
						boolean flag=true;
						flag=gpsSpeedStr.matches("^[0-9]+$");
						if(flag){
							Integer gpsSpeed = Integer.parseInt(gpsSpeedStr);
							if(gpsSpeed<0 || gpsSpeed>255){
								flag = false;
							}
						}
						if(!flag){
							speedError+=rowNum+";";
							continue;
						}
					}
					
					if(!StringUtils.isEmpty(obdSpeedStr)){
						boolean flag=true;
						flag=obdSpeedStr.matches("^[0-9]+$");
						if(flag){
							Integer obdSpeed = Integer.parseInt(obdSpeedStr);
							if(obdSpeed<0 || obdSpeed>255){
								flag = false;
							}
						}
						if(!flag){
							speedError+=rowNum+";";
							continue;
						}
					}
					
					obdSn=obdSn.trim().toLowerCase();//去掉两边和都转成小写
					if(!obdSns.containsKey(obdSn)){
						obdSns.put(obdSn.trim(), 1);
					}else{
						int val=obdSns.get(obdSn)+1;
						obdSns.put(obdSn, val);
					}
					
					upgradeSet.setVersion(version);//对应的版本号
					upgradeSet.setFirmVersion(firmVersion);//固件版本号
					upgradeSet.setObdSn(obdSn.trim().toLowerCase());
					upgradeSet.setCreateTime(new Date());
					upgradeSet.setSendFlag("0");
					upgradeSet.setAuditState("0");
					upgradeSet.setUpgradeFlag("0");//默认未升级
					upgradeSet.setValid("1");//0升级成功 1未升级
					upgradeSet.setVflag("1");//是否有效0无效1有效
					upgradeSet.setFirmType(firmType);//
					upgradeSet.setSendedCount(0);
					upgradeSet.setSpeedCount(0);
					if(!StringUtils.isEmpty(obdSpeedStr)){
						upgradeSet.setObdSpeed(Integer.parseInt(obdSpeedStr));
					}
					if(!StringUtils.isEmpty(gpsSpeedStr)){
						upgradeSet.setGpsSpeed(Integer.parseInt(gpsSpeedStr));
					}
					upgradeSetList.add(upgradeSet);
				}
			}
			
			if(!StringUtils.isEmpty(obdSnIsNull)){
				return "false_gps速度或obd速度不为空,但设备号为空,行数为:"+obdSnIsNull;
			}
			
			if(!StringUtils.isEmpty(speedError)){
				return "false_速度必须为阿拉伯数组,且范围为0-255,行数为:"+speedError;
			}
			
			if(upgradeSetList==null || upgradeSetList.size()==0){
				return "false_文件为空,请检查.";
			}
			
			String repeat = "";//excel重复
			//2.先判断excel里是否有重复记录
			for (String key : obdSns.keySet()) {
				Integer total = obdSns.get(key);
				if(total>1){
					repeat +=key+":"+total+"---";
					continue;
				}
				//兼容4个byte和12个byte
				if(key.length()==8 || key.length()==24){
					
				}else {
					repeat +=key+":设备号长度有误---";
					continue;
				}
				if(!key.matches("^[a-f0-9A-F]+$")){
					repeat +=key+":设备号有误---";
					continue;
				}
			}
			if(!StringUtils.isEmpty(repeat)){
				return "repeat_"+repeat;
			}
			
			//判断是否存在重复记录
			List<UpgradeSet> list=getListByMap(obdSns,firmType);
			if(list.size()>0){
				StringBuffer sb = new StringBuffer("");
				for (UpgradeSet upgradeSet : list) {
					sb.append(upgradeSet.getObdSn()+",");
				}
				return "false_"+sb+",这些设备存在待升级记录,请检查.";
			}
			
			for (UpgradeSet upgradeSet : upgradeSetList) {
				upgradeSetSave(upgradeSet);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "error"+e;
		}
		
		return "true";
	}
	
	public List<UpgradeSet> queryByParams(Pager pager,Map<String,Object>map) {
		return upgradeSetGetDao().queryByParams(pager, map);
	}
	
	public Long getTotalByVersion(String version) {
		return upgradeSetGetDao().getTotalByVersion(version);
	}
	
	public int updByVersion(String auditState,String version,String auditOper) {
		return upgradeSetGetDao().updByVersion(auditState, version,auditOper);
	}
}
