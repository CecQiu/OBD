package com.hgsoft.carowner.service;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.hgsoft.carowner.dao.OBDStockInfoDao;
import com.hgsoft.carowner.entity.OBDStockInfo;
import com.hgsoft.common.service.BaseService;
import com.hgsoft.common.utils.DateUtil;
import com.hgsoft.common.utils.FileUtil;
import com.hgsoft.common.utils.IDUtil;
import com.hgsoft.common.utils.Order;
import com.hgsoft.common.utils.Pager;
import com.hgsoft.common.utils.Property;
import com.hgsoft.obd.server.OBDException;
/**
 * OBD库存设备
 * @author sujunguang
 * 2015-8-5
 */
@Service("obdStockInfoService")
public class OBDStockInfoService extends BaseService<OBDStockInfo> {
	
	@Resource
	private OBDStockInfoDao obdStockInfoDao;
	
	@Resource
	public void setDao(OBDStockInfoDao obdStockInfoDao){
		super.setDao(obdStockInfoDao);
	}
	
	public OBDStockInfoDao getObdDao() {
		return (OBDStockInfoDao) this.getDao();
	}	
	/**
	 * 更新obd设备在线状态
	 * @param obdId
	 * @return
	 */
	public boolean obdStateUpdate(OBDStockInfo os){
		 this.update(os);
		 return true;
	}
	/**
	 * 根据obdId查询OBD设备
	 * @param obdId
	 * @return
	 */
	public OBDStockInfo queryByObdId(String obdId){
		return obdStockInfoDao.queryByObdId(obdId);
	}
	
	/**
	 * 根据SN查询OBD设备
	 * @param obdSn
	 * @return
	 */
	public OBDStockInfo queryBySN(String obdSn){
		return obdStockInfoDao.queryBySN(obdSn);
	}
	
	/**
	 * 根据表面码查询
	 * @param obdmSn
	 * @return
	 */
	public OBDStockInfo queryByObdMSN(String obdmSn){
		return obdStockInfoDao.queryByObdMSN(obdmSn);
	}
	
//	/**
//	 * 根据SN查询OBD设备
//	 * @param obdSn
//	 * @return
//	 */
//	public OBDStockInfo queryByLikeSNAndMSN(String obdSn){
//		return obdStockInfoDao.queryByLikeSNAndMSN(obdSn);
//	}
	
//	/**
//	 * 根据SN查询OBD设备
//	 * @param obdSn
//	 * @return
//	 */
//	public OBDStockInfo queryBySNAndMSN(String obdSn){
//		return obdStockInfoDao.queryBySNAndMSN(obdSn);
//	}
	
	/**
	 * 更新设备分组编号
	 * @param obdId
	 * @return
	 */
	public int obdGroupNumUpd(String obdSn,String groupNum){
		return obdStockInfoDao.obdGroupNumUpd(obdSn,groupNum);
	}
	/**
	 * 查询OBD设备集合
	 * @param obdId
	 * @return
	 */
	public List<OBDStockInfo> queryList(){
		return obdStockInfoDao.queryList();
	}

	public OBDStockInfo queryByLikeObdMSN(String obdMSn){
		return obdStockInfoDao.queryByLikeObdMSN(obdMSn);
	}
	
	public OBDStockInfo queryByLikeSN(String obdSn){
		return obdStockInfoDao.queryByLikeSN(obdSn);
	}
	
	/**
	 * 根据SN与用户ID查询OBD设备
	 * @param obdSn
	 * @param regUserId
	 * @return
	 */
	public OBDStockInfo queryBySNAndUserId(String obdSn,String regUserId){
		return obdStockInfoDao.queryBySNAndUserId(obdSn,regUserId);
	}
	/**
	 * OBD业务统计
	 * @param starTime
	 * @param endTime
	 * @param pager
	 * @param flag 导出标志 0表示导出，1表示不导出 考虑分页
	 * @return
	 * @throws ParseException
	 */
	public List obdStatist(String starTime,String endTime,Pager pager,int flag) throws ParseException{
		
		String sqla="select count(stockId) from obd_stock_info";//OBD总数
		String slqc="select count(stockId) from obd_stock_info o where o.stockType='00' and o.stockState='01'";//活跃设备
		String slqd="select count(stockId) from obd_stock_info o where o.stockType='00'";//已绑定设备
		String slqe="select count(regUserId) from meb_user u ";//车主总数
		DateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd");            
        Calendar startDay = Calendar.getInstance();
        Calendar endDay = Calendar.getInstance();
        startDay.setTime(FORMATTER.parse(starTime));
        endDay.setTime(FORMATTER.parse(endTime));
        // 给出的日期开始日比终了日大则不执行打印
        List list=new ArrayList();
        List obdList=new ArrayList();
        list.add(starTime);
        if(!starTime.equals(endTime)){
            if(startDay.compareTo(endDay)<=0){
                //现在打印中的日期
                Calendar currentPrintDay = startDay;
                while (true){
                    // 日期加一
                    currentPrintDay.add(Calendar.DATE, 1);
                    // 日期加一后判断是否达到终了日，达到则终止打印
                    if (currentPrintDay.compareTo(endDay) == 0) {
                        break;
                    }
                    list.add(FORMATTER.format(currentPrintDay.getTime()));
                }
                list.add(endTime);
            }
        }
        int begin=pager.getPageSize()*pager.getCurrentPage()-pager.getPageSize();
        int end=pager.getPageSize()*pager.getCurrentPage();
        if(flag==0){
        	 for (int i =0; i<list.size(); i++) {
         		String slqb="select count(stockId) from obd_stock_info o where o.startDate like '%"+list.get(i)+"%'";//新激活设备
         		Map map=new HashMap();
         		map.put("obdNumber", this.getDao().queryBySQL(sqla, null).get(0));
         		map.put("obdActive", this.getDao().queryBySQL(slqc, null).get(0));
         		map.put("obdBind", this.getDao().queryBySQL(slqd, null).get(0));
         		map.put("userNumber", this.getDao().queryBySQL(slqe, null).get(0));
         		map.put("obdNew", this.getDao().queryBySQL(slqb, null).get(0));
         		map.put("date",list.get(i));
         		obdList.add(map);
         	}
        }else{
        	 for (int i =begin; i<list.size()&&i<end; i++) {
         		String slqb="select count(stockId) from obd_stock_info o where o.startDate like '%"+list.get(i)+"%'";//新激活设备
         		Map map=new HashMap();
         		map.put("obdNumber", this.getDao().queryBySQL(sqla, null).get(0));
         		map.put("obdActive", this.getDao().queryBySQL(slqc, null).get(0));
         		map.put("obdBind", this.getDao().queryBySQL(slqd, null).get(0));
         		map.put("userNumber", this.getDao().queryBySQL(slqe, null).get(0));
         		map.put("obdNew", this.getDao().queryBySQL(slqb, null).get(0));
         		map.put("date",list.get(i));
         		obdList.add(map);
         	}
        }
       
    pager.setTotalSize(list.size());
	return obdList;
	}
	/**
	 * obd设备管理
	 * @param simNo
	 * @param stockType
	 * @param obdSn
	 * @param mobileNumber
	 * @param license
	 * @param pager
	 * @return
	 */
	public List<OBDStockInfo> getObdInfo(String obdMSn,String obdSn,String stockState,String valid,Pager pager){
		List<Property> list = new ArrayList<Property>();

		if (!StringUtils.isEmpty(obdSn)) {
			list.add(Property.like("obdSn", "%"+obdSn.trim()+"%"));
		}
		
		if (!StringUtils.isEmpty(obdMSn)) {
			list.add(Property.like("obdMSn", "%"+obdMSn.trim()+"%"));
		}
		if (!StringUtils.isEmpty(stockState)) {
			list.add(Property.eq("stockState", stockState.trim()));
		}
		
		if (!StringUtils.isEmpty(valid)) {
			list.add(Property.eq("valid", valid.trim()));
		}

		Property[] propertyArr = new Property[list.size()];
		list.toArray(propertyArr);
		return this.findByPager(pager, Order.desc("startDate"), propertyArr);
	}
	/**
	 * obd设备excel文档导入
	 * @param file
	 * @return
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public boolean obdExcel(File file,String fileName) throws FileNotFoundException, IOException{
		//1保存excel文档到项目制定目录下
		String fname=fileName.substring(0,fileName.indexOf("."));
		String ftype=fileName.substring(fileName.indexOf("."),fileName.length());
		String now=DateUtil.getTimeString(new Date(), "yyyyMMddHHmmss");
		String fnname=fname+now+ftype;
		String saveFilePath = "D:\\Owner\\obdExcel";
		boolean flag=FileUtil.fileTransfer(file, saveFilePath, fnname);
		System.out.println(flag);
		//2读取excel文档保存入库
		Workbook workbook = null;
		if(".xls".equals(ftype)){
			workbook = new HSSFWorkbook(new FileInputStream(file));
		}else if(".xlsx".equals(ftype)){
			try {
				workbook = new XSSFWorkbook(new FileInputStream(file));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		List<OBDStockInfo> obdList = new ArrayList<OBDStockInfo>();
		//循环工作表Sheet
		for (int numSheet = 0;numSheet<workbook.getNumberOfSheets();numSheet++) {
			Sheet sheet = workbook.getSheetAt(numSheet);
	         //循环row
	         for (Row row : sheet) {
	        	//第一行是标题行
	        	if(row.getRowNum()==0){
	        		continue;
	        	}
 	        	OBDStockInfo obd = new OBDStockInfo();
				String obdId =row.getCell(1).getStringCellValue();
				String obdSn = row.getCell(2).getStringCellValue();
				obd.setStockId(IDUtil.createID());
				obd.setObdId(obdId);
				obd.setObdSn(obdSn);
				obd.setStockType("01");
				obdList.add(obd);
			}
		}
		for (OBDStockInfo obdStockInfo : obdList) {
			//先根据obdSn查询是否存在该obd设备，如果存在，直接更新，否则直接插入
			OBDStockInfo obdStock = queryBySN(obdStockInfo.getObdSn());
			if(obdStock==null){
				this.save(obdStockInfo);
			}else{
				obdStock.setObdId(obdStockInfo.getObdId());
				obdStock.setObdSn(obdStockInfo.getObdSn());
				this.update(obdStock);
			}
		}
		return true;
	}

	/**
	 * 设备全部设置离线
	 */
	public void obdAllOffLine() {
		obdStockInfoDao.obdAllOffLine();
	}
	
	/**
	 * 设置设备离线
	 * @param obdSn
	 * @throws OBDException 
	 */
	public void obdSetOffLine(String obdSn) throws OBDException{
		OBDStockInfo obdStockInfo = queryBySN(obdSn);
		if(obdStockInfo != null){
			if("01".equals(obdStockInfo.getStockState())){
				obdStockInfo.setStockState("00");
				obdStateUpdate(obdStockInfo);
			}
		}
	}
	
	/**
	 * 设置设备在线
	 * @param obdSn
	 * @throws OBDException 
	 */
	public void obdSetOnLine(String obdSn) throws OBDException{
		OBDStockInfo obdStockInfo = queryBySN(obdSn);
		if(obdStockInfo != null){
			if("00".equals(obdStockInfo.getStockState())){
				obdStockInfo.setStockState("01");
				obdStateUpdate(obdStockInfo);
			}
		}
	}
	
	//判断升级记录是否存在重复
	public Integer getTotalByMap(Map<String, Integer> obdSns) {
		return obdStockInfoDao.getTotalByMap(obdSns);
	}
	
	//判断升级记录是否存在重复
	public List<OBDStockInfo> getListByMap(Map<String, Integer> obdSns) {
		return obdStockInfoDao.getListByMap(obdSns);
	}
	
	public List find(Date begin, Date end){
		return obdStockInfoDao.find(begin, end);
	}
	
	public OBDStockInfo queryLastByParam(Map<String,Object> map){
		return getObdDao().queryLastByParam(map);
	}
}
