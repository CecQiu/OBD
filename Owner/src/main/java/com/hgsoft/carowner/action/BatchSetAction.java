package com.hgsoft.carowner.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
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
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import com.hgsoft.carowner.entity.BatchSet;
import com.hgsoft.carowner.entity.ObdBatchSet;
import com.hgsoft.carowner.service.BatchSetService;
import com.hgsoft.carowner.service.ObdBatchSetService;
import com.hgsoft.common.action.BaseAction;
import com.hgsoft.common.utils.DateUtil;
import com.hgsoft.common.utils.IDUtil;
import com.hgsoft.common.utils.Pager;
import com.hgsoft.common.utils.StrUtil;
import com.hgsoft.obd.util.BatchSetType;

import net.sf.json.JSONObject;


/**
 *
 * @author sjg
 */

@Controller
@Scope("prototype")
public class BatchSetAction extends BaseAction {
	@Resource
	private BatchSetService batchSetService;
	@Resource
	private ObdBatchSetService obdBatchSetService;
	
	private String id;
	private String type;
	private String version;
	private String auditState;
	private String auditMsg;
	private String valid;
	private String startTime;
	private String endTime;
	private List<BatchSet> batchSets = new ArrayList<BatchSet>();
	private BatchSet batchSet;
	
	private String message;
	//上传的文件
	private File addressExcel;
	//保存原始的文件名
	private String addressExcelFileName;
	
	//上传的文件
	private File obdexcel;
	//保存原始的文件名
	private String obdexcelFileName;
	
	// 列表展示
	public String list() {
		//清除缓存
		// 分页获取对象
		if (StrUtil.arraySubNotNull(type,startTime,endTime)) {
			batchSets = getData(pager);
		}else{
			Calendar c = Calendar.getInstance();
			Date now = c.getTime();
			endTime=DateUtil.getTimeString(now, "yyyy-MM-dd HH:mm:ss");
			c.add(Calendar.MONTH, -1);
			Date s= c.getTime();
			startTime=DateUtil.getTimeString(s, "yyyy-MM-dd HH:mm:ss");
		}
		
		return "list";
	}

	public String query(){
		batchSets = getData(pager);
		return "list";
	}
	
	/**
	 * 
	 */
	public String toAdd() {
		return "add";
	}
	
	public String toCheck() {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		this.batchSet = batchSetService.queryLastByParams(map);
		return "toCheck";
	}

	/**
	 * 固件审核
	 */
	public String check() {
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("id", id);
			BatchSet bs = batchSetService.queryLastByParams(map);
			bs.setAuditOper(getOperator().getUsername());
			bs.setAuditTime(new Date());
			bs.setAuditState(Integer.parseInt(this.auditState));//审核结果;
			bs.setAuditMsg(this.auditMsg);
			boolean flag=batchSetService.add(bs);
			this.message = "审核完成.";
			this.auditState="";
		} catch (Exception e) {
			e.printStackTrace();
			this.message = "审核失败,请联系管理员.";
		}
		return "list";
	}
	
	
	// 从数据库中查询数据
	public List<BatchSet> getData(Pager pager) {

		Map<String, Object> map = new HashMap<>();
		Integer total=0;
		if(!StringUtils.isEmpty(type)){
			map.put("type", type);
			total++;
		}
		if(!StringUtils.isEmpty(version)){
			map.put("version", version);
			total++;
		}
		if(!StringUtils.isEmpty(auditState)){
			map.put("auditState", Integer.parseInt(auditState));
			total++;
		}
		if(!StringUtils.isEmpty(valid)){
			map.put("valid", Integer.parseInt(valid));
			total++;
		}
		if (!StringUtils.isEmpty(startTime)) {
			map.put("startTime", startTime);
			total++;
		}
		
		if (!StringUtils.isEmpty(endTime)) {
			map.put("endTime", endTime);
			total++;
		}
		
		map.put("paramsTotal", total);

		return batchSetService.queryByParams(pager, map);
		 
	}
	
	public String delete() {
		//查询是否存在批量设置记录
		BatchSet batchSet=batchSetService.find(id);
		Map<String, Object> map = new HashMap<>();
		map.put("version", batchSet.getVersion());
		map.put("valid", 1);
		map.put("paramsTotal", 2);
		Long  sum=obdBatchSetService.getTotalByParams(map);
		if(sum==null || sum!=0){
			this.message="deleteFail";
			return query();
		}
		boolean flag=batchSetService.del(id);
		if(flag){
			this.message="deleteSuccess";
		}else{
			this.message="deleteFail";
		}
		
		return query();
	}
	
	/**
	 * 读取obdExcel文件保存入库
	 */
	public String excel() {
		try {
			String flag=excelAnalysis(addressExcel,addressExcelFileName);
			if(flag.startsWith("true")){
				this.message="保存成功.";
				this.version="";
				return list();
			}else if(flag.startsWith("false")){
				this.message="保存失败,"+flag.substring(flag.indexOf("_")+1,flag.length());
				return "add";
			}else{
				this.message="保存异常,请联系管理员."+flag;
				return "add";
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "add";
	}
	
	public String excelAnalysis(File file,String fileName) throws FileNotFoundException, IOException{
		try {
			//先解析版本号
			if(StringUtils.isEmpty(version)){
				return "false_版本号不能为空.";
			}
			//根据版本号查询
			Map<String, Object> map = new HashMap<>();
			Integer total=0;
			if(!StringUtils.isEmpty(version)){
				map.put("version", version);
				total++;
			}
			map.put("valid", 1);
			total++;
			map.put("paramsTotal", total);
			List<BatchSet> list=batchSetService.queryByParams(pager, map);
			if(list!=null && list.size()>0){
				return "false_版本号已存在.";
			}
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
			
			Sheet sheet = workbook.getSheetAt(0);//获取第一个sheet
			if(sheet==null){
				return "false_excel文件sheet不能为空.";
			}
//			Row firstRow =sheet.getRow(0);//获取第一行
//			if(firstRow==null){
//				return "false_excel文件序号行不能为空.";
//			}
//			Map<Integer,Integer> serialNumMap = new HashMap<>();
//			for (Cell cell : firstRow) {
//				if(cell==null){
//					break;
//				}
//				//第一个空格不解析
//				if(cell.getColumnIndex()==0){
//					continue;
//				}
//				cell.setCellType(Cell.CELL_TYPE_STRING);
//				String seriaStr=cell.getStringCellValue();
//				if(StringUtils.isEmpty(seriaStr)){
//					break;
//				}
//				if(!seriaStr.matches("^[1-9][0-9]*$")){
//					return "false_excel文件序号必须为阿拉伯数字.";
//				}
//				Integer serial =Integer.parseInt(seriaStr);
//				if(!serialNumMap.containsKey(serial)){
//					serialNumMap.put(serial, 1);
//				}else{
//					serialNumMap.put(serial, serialNumMap.get(serial)+1);
//				}
//			}
//			if(serialNumMap==null ||serialNumMap.size()==0){
//				return "false_excel文件序号不能为空.";
//			}
//			//如果序号有重复
//			for (Integer key : serialNumMap.keySet()) {
//				Integer total = serialNumMap.get(key);
//				if(total>1){
//					return "false_excel文件序号不能重复.";
//				}
//			}
			//获取第二行
			Row secondRow =sheet.getRow(1);//获取第一行
			if(secondRow==null){
				return "false_excel文件参数类型行不能为空.";
			}
			//参数类型
			StringBuffer paramSb = new StringBuffer("");
			Map<String, Integer> paramTypeMap = new HashMap<>();
			for (Cell cell : secondRow) {
				if(cell==null){
					break;
				}
				//第一个空格不解析
				if(cell.getColumnIndex()==0){
					continue;
				}
				cell.setCellType(Cell.CELL_TYPE_STRING);
				String paramName = cell.getStringCellValue();
				if(StringUtils.isEmpty(paramName)){
					break;
				}
				int index =cell.getColumnIndex();
				paramSb.append(paramName+index+",");
				paramTypeMap.put(paramName+index, index);
			}
			
//			for (Integer key : serialNumMap.keySet()) {
//				Cell cell=secondRow.getCell(key);
//				cell.setCellType(Cell.CELL_TYPE_STRING);
//				String paramName = cell.getStringCellValue();
//				if(StringUtils.isEmpty(paramName)){
//					return "false_excel文件参数类型有误.";
//				}
//				paramSb.append(paramName+",");
//				paramTypeMap.put(paramName+key, key);
//			}
			//获取第三行
			Row thirdRow =sheet.getRow(2);//获取第一行
			if(thirdRow==null){
				return "false_excel文件偏移地址行不能为空.";
			}
			int thirdNum=thirdRow.getRowNum();
			//偏移地址
			Map<String, String> addressMap = new HashMap<>();
			for (String key : paramTypeMap.keySet()) {
				Integer index = paramTypeMap.get(key);
				Cell cell=thirdRow.getCell(index);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				String address = cell.getStringCellValue();
				if(StringUtils.isEmpty(address)){
					return "false_excel文件偏移地址有误,第几行:"+thirdNum;
				}
				if(!address.matches("^[0-9a-zA-Z]{4}$")){
					return "false_excel文件偏移地址有误,第几行:"+thirdNum;
				}
				addressMap.put(key+"-address", address);
			}
			//获取第四行
			Row fourRow =sheet.getRow(3);
			if(fourRow==null){
				return "false_excel文件地址范围行不能为空.";
			}
			int fourNum=fourRow.getRowNum();
			//地址范围长度
			Map<String, Integer> addressLengthMap = new HashMap<>();
			for (String key : paramTypeMap.keySet()) {
				Integer index = paramTypeMap.get(key);
				Cell cell=fourRow.getCell(index);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				String addressLengthStr = cell.getStringCellValue();
				if(StringUtils.isEmpty(addressLengthStr)){
					return "false_excel文件地址范围长度有误,第几行:"+fourNum;
				}
				if(!addressLengthStr.matches("^[a-z0-9A-Z]{2}$")){
					return "false_excel文件地址范围长度必须为阿拉伯数字,第几行:"+fourNum;
				}
				Integer addressLength = Integer.parseInt(addressLengthStr);
				addressLengthMap.put(key+"-length", addressLength);
			}
			//地址值
			Map<String, String> addressValueMap = new HashMap<>();
			for (String key : paramTypeMap.keySet()) {
				Integer index = paramTypeMap.get(key);//获取列号
				//获取长度
				Integer length=addressLengthMap.get(key+"-length");
				StringBuffer valueSb =new StringBuffer("");
				for (int i = 4; i <4+length; i++) {
					//获取对应行
					Row row =sheet.getRow(i);
					if(row == null){
						return "false_excel文件地址值有误.";
					}
					//获取对应单元格
					Cell cell =row.getCell(index);
					cell.setCellType(Cell.CELL_TYPE_STRING);
					String value = cell.getStringCellValue();
					if(StringUtils.isEmpty(value)){
						return "false_excel文件地址值有误,第几行:"+i;
					}
					if(!value.matches("^[0-9a-zA-Z]{2}$")){
						return "false_excel文件地址值有误,第几行:"+i;
					}
					valueSb.append(value+",");
				}
				String valueAll = valueSb.toString();
				if(valueAll.endsWith(",")){
					valueAll=valueAll.substring(0,valueAll.length()-1);
				}
				if(valueAll.endsWith(",")){
					valueAll=valueAll.substring(0,valueAll.length()-1);
				}
				addressValueMap.put(key+"-value", valueAll);
			}
			//保存入库
			JSONObject json = new JSONObject();
			String params =paramSb.toString();
			if(params.endsWith(",")){
				params=params.substring(0, params.length()-1);
			}
			json.put("params", params);
			//保存偏移地址
			for (String key : addressMap.keySet()) {
				String value = addressMap.get(key);
				json.put(key, value);
			}
			//保存地址长度
			for (String key : addressLengthMap.keySet()) {
				Integer value = addressLengthMap.get(key);
				json.put(key, value);
			}
			//保存地址值
			for (String key : addressValueMap.keySet()) {
				String value = addressValueMap.get(key);
				json.put(key, value);
			}
			BatchSet batchSet = new BatchSet();
			batchSet.setId(IDUtil.createID());
			batchSet.setType(BatchSetType.ADDRESS);
			batchSet.setVersion(version);
			batchSet.setMsg(json.toString());
			//解析消息体
			String bodyMsg=BatchSetService.batchSetMsg(json.toString());
			batchSet.setBodyMsg(bodyMsg);
			batchSet.setAuditState(0);
			batchSet.setCreateTime(new Date());
			batchSet.setValid(1);
			boolean flag=batchSetService.add(batchSet);
			if(flag){
				return "true";
			}else{
				return "false_批量设置记录保存失败,请联系管理员.";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "error"+e;
		}
	}
	
	/**
	 * 跳转到excel导入升级页面
	 */
	public String toObdExcel() {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		this.batchSet = batchSetService.queryLastByParams(map);
		return "toObdExcel";
	}

	/**
	 * 读取obdExcel文件保存入库
	 */
	public String obdExcel() {
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("id", id);
			this.batchSet = batchSetService.queryLastByParams(map);
			String flag=obdExcel(obdexcel,obdexcelFileName,batchSet);
			if(flag.startsWith("true")){
				this.message="保存成功，请及时审核升级列表.";
			}else if(flag.startsWith("false")){
				this.message="保存失败,"+flag.substring(flag.indexOf("_")+1,flag.length());
				return "toObdExcel";
			}else if(flag.startsWith("repeat")){
				this.message="保存失败,设备号有误:"+flag.substring(flag.indexOf("_")+1,flag.length());
				return "toObdExcel";
			}else{
				this.message="保存异常,请联系管理员."+flag;
				return "toObdExcel";
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "list";
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
	public String obdExcel(File file,String fileName,BatchSet batchSet) throws FileNotFoundException, IOException{
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
			List<ObdBatchSet> obdBatchSetList = new ArrayList<ObdBatchSet>();
			//循环工作表Sheet
//			List<String> obdSns = new ArrayList<String>();
			
			Date now = new Date();
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
		        	
		        	ObdBatchSet obdBatchSet = new ObdBatchSet();
		        	String obdSn ="";
			    	if(row.getCell(0)!=null){
			    		row.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
						obdSn = row.getCell(0).getStringCellValue();//设备号
			    	}
					
					if(StringUtils.isEmpty(obdSn)){
						continue;
					}
					
					obdSn=obdSn.trim().toLowerCase();//去掉两边和都转成小写
					if(!obdSns.containsKey(obdSn)){
						obdSns.put(obdSn.trim(), 1);
					}else{
						int val=obdSns.get(obdSn)+1;
						obdSns.put(obdSn, val);
					}
					
					obdBatchSet.setId(IDUtil.createID());
					obdBatchSet.setObdSn(obdSn);
					obdBatchSet.setType(batchSet.getType());
					obdBatchSet.setVersion(batchSet.getVersion());
					obdBatchSet.setCreateTime(now);
					obdBatchSet.setSendedCount(0);
					obdBatchSet.setValid(1);
//					obdBatchSet.setSuccess(0);
					obdBatchSetList.add(obdBatchSet);
				}
			}
			
			if(obdBatchSetList==null || obdBatchSetList.size()==0){
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
			Map<String, Object> map = new HashMap<>();
			
			List<String> obdSnsList = new ArrayList<>();
			for (String key : obdSns.keySet()) {  
				obdSnsList.add(key);
	        } 
			map.put("obdSns", obdSnsList);
			map.put("type", batchSet.getType());
			map.put("valid", 1);
			
			List<ObdBatchSet> list=obdBatchSetService.getListByMap(map);
			if(list.size()>0){
				StringBuffer sb = new StringBuffer("");
				for (ObdBatchSet obs : list) {
					sb.append(obs.getObdSn()+",");
				}
				return "false_"+sb+",这些设备存在待升级记录,请检查.";
			}
			
			for (ObdBatchSet obs : obdBatchSetList) {
				obdBatchSetService.add(obs);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "error"+e;
		}
		
		return "true";
	}
	
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}


	public List<BatchSet> getBatchSets() {
		return batchSets;
	}


	public void setBatchSets(List<BatchSet> batchSets) {
		this.batchSets = batchSets;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}

	public File getAddressExcel() {
		return addressExcel;
	}

	public void setAddressExcel(File addressExcel) {
		this.addressExcel = addressExcel;
	}

	public String getAddressExcelFileName() {
		return addressExcelFileName;
	}

	public void setAddressExcelFileName(String addressExcelFileName) {
		this.addressExcelFileName = addressExcelFileName;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getAuditState() {
		return auditState;
	}

	public void setAuditState(String auditState) {
		this.auditState = auditState;
	}

	public String getValid() {
		return valid;
	}

	public void setValid(String valid) {
		this.valid = valid;
	}

	public BatchSet getBatchSet() {
		return batchSet;
	}

	public void setBatchSet(BatchSet batchSet) {
		this.batchSet = batchSet;
	}

	public String getAuditMsg() {
		return auditMsg;
	}

	public void setAuditMsg(String auditMsg) {
		this.auditMsg = auditMsg;
	}

	public File getObdexcel() {
		return obdexcel;
	}

	public void setObdexcel(File obdexcel) {
		this.obdexcel = obdexcel;
	}

	public String getObdexcelFileName() {
		return obdexcelFileName;
	}

	public void setObdexcelFileName(String obdexcelFileName) {
		this.obdexcelFileName = obdexcelFileName;
	}
	
}
