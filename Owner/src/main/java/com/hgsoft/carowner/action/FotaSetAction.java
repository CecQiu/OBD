package com.hgsoft.carowner.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import com.hgsoft.carowner.entity.FotaSet;
import com.hgsoft.carowner.service.FotaSetService;
import com.hgsoft.common.action.BaseAction;
import com.hgsoft.common.utils.DateUtil;
import com.hgsoft.common.utils.ExcelUtil;
import com.hgsoft.common.utils.Order;
import com.hgsoft.common.utils.Pager;
import com.hgsoft.common.utils.Property;

/**
 * FOTA上传列表
 * 
 * @author Administrator liujialin
 */
@Controller
@Scope("prototype")
public class FotaSetAction extends BaseAction {
	private final Log logger = LogFactory.getLog(FotaSetAction.class);

	@Resource
	private FotaSetService fotaSetService;
	private FotaSet fotaSet;
	// 上传的文件
	private File obdexcel;
	// 保存原始的文件名
	private String obdexcelFileName;

	private List<FotaSet> fotaSetList = new ArrayList<FotaSet>();
	private String starTime;
	private String endTime;
	private String fotaSetIds="";

	// 列表展示
	public String list() {

		return "list";
	}

	// 列表展示
	public String fsList() {
		fotaSetList = listFotaSets(pager);
		
		List<FotaSet> fotaSets = listFotaSets(null);
		for (FotaSet fs : fotaSets) {
			fotaSetIds += fs.getId() + ";";
		}

		return "fsList";
	}

	/**
	 * 查询结果
	 * 
	 * @param pager
	 * @return
	 */
	public List<FotaSet> listFotaSets(Pager pager) {

		List<Property> list = new ArrayList<Property>();
		list.add(Property.eq("useFlag", "1"));
		if (fotaSet != null) {

			String obdSn = fotaSet.getObdSn();
			String version = fotaSet.getVersion();
			String valid = fotaSet.getValid();
			String mifiFlag = fotaSet.getMifiFlag();
			String batchVersion = fotaSet.getBatchVersion();

			String fileName = fotaSet.getObdSn();
			String ftpIP = fotaSet.getVersion();
			String ftpPort = fotaSet.getValid();

			if (!StringUtils.isEmpty(obdSn)) {
				list.add(Property.like("obdSn", "%" + obdSn.trim() + "%"));
			}
			if (!StringUtils.isEmpty(version)) {
				list.add(Property.like("version", "%" + version.trim() + "%"));
			}

			if (!StringUtils.isEmpty(batchVersion)) {
				list.add(Property.eq("batchVersion", batchVersion.trim()));
			}

			if (!StringUtils.isEmpty(fileName)) {
				list.add(Property.like("fileName", "%" + fileName.trim() + "%"));
			}

			if (!StringUtils.isEmpty(ftpIP)) {
				list.add(Property.like("ftpIP", "%" + ftpIP.trim() + "%"));
			}
			if (!StringUtils.isEmpty(ftpPort)) {
				list.add(Property.eq("ftpPort", ftpPort.trim()));
			}

			if (!StringUtils.isEmpty(valid)) {
				list.add(Property.eq("valid", valid.trim()));
			}
			if (!StringUtils.isEmpty(mifiFlag)) {
				list.add(Property.eq("mifiFlag", mifiFlag.trim()));
			}
			if (starTime != null && !"".equals(starTime)) {
				list.add(Property.ge("createTime", (Date)DateUtil.fromatDate(starTime, "yyyy-MM-dd HH:mm:ss")));
			}
			if (endTime != null && !"".equals(endTime)) {
				list.add(Property.le("createTime", (Date)DateUtil.fromatDate(endTime, "yyyy-MM-dd HH:mm:ss")));
			}
		}
		Property[] propertyArr = new Property[list.size()];
		list.toArray(propertyArr);
		return fotaSetService.findByPager(pager, null, propertyArr);
	}

	public String query() {
		// 清除缓存
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		session.removeAttribute("currentPage");
		session.removeAttribute("pageSize");
		session.removeAttribute("rowIndex");
		session.removeAttribute("pname");
		session.removeAttribute("pvalue");
		session.removeAttribute("ptype");
		session.removeAttribute("remark");
		List<Property> list = new ArrayList<Property>();
		list.add(Property.eq("useFlag", "1"));
		if (fotaSet != null) {

			String obdSn = fotaSet.getObdSn();
			String version = fotaSet.getVersion();
			String valid = fotaSet.getValid();
			String mifiFlag = fotaSet.getMifiFlag();
			String batchVersion = fotaSet.getBatchVersion();

			String fileName = fotaSet.getFileName();
			String ftpIP = fotaSet.getFtpIP();
			Integer ftpPort = fotaSet.getFtpPort();

			if (!StringUtils.isEmpty(obdSn)) {
				list.add(Property.like("obdSn", "%" + obdSn.trim() + "%"));
			}
			if (!StringUtils.isEmpty(version)) {
				list.add(Property.like("version", "%" + version.trim() + "%"));
			}

			if (!StringUtils.isEmpty(batchVersion)) {
				list.add(Property.eq("batchVersion", batchVersion.trim()));
			}

			if (!StringUtils.isEmpty(fileName)) {
				list.add(Property.like("fileName", "%" + fileName.trim() + "%"));
			}

			if (!StringUtils.isEmpty(ftpIP)) {
				list.add(Property.like("ftpIP", "%" + ftpIP.trim() + "%"));
			}
			if (ftpPort!=null) {
				list.add(Property.eq("ftpPort", ftpPort));
			}

			if (!StringUtils.isEmpty(valid)) {
				list.add(Property.eq("valid", valid.trim()));
			}
			if (!StringUtils.isEmpty(mifiFlag)) {
				list.add(Property.eq("mifiFlag", mifiFlag.trim()));
			}
			if (starTime != null && !"".equals(starTime)) {
				list.add(Property.ge("createTime", (Date)DateUtil.fromatDate(starTime, "yyyy-MM-dd HH:mm:ss")));
			}
			if (endTime != null && !"".equals(endTime)) {
				list.add(Property.le("createTime", (Date)DateUtil.fromatDate(endTime, "yyyy-MM-dd HH:mm:ss")));
			}
		}
		Property[] propertyArr = new Property[list.size()];
		list.toArray(propertyArr);
		fotaSetList = fotaSetService.findByPager(pager, Order.desc("createTime"), propertyArr);
		return "fsList";
	}

	public void delAll(){
		logger.info("----------------【待升级记录删除全部】---------------");
		Integer total=0;
		if(fotaSet!=null && !StringUtils.isEmpty(fotaSet.getVersion())){
			List<Property> list = new ArrayList<Property>();
			list.add(Property.eq("useFlag", "1"));
			list.add(Property.eq("version", fotaSet.getVersion()));
			Property[] propertyArr = new Property[list.size()];
			list.toArray(propertyArr);
			List<FotaSet> fotaSetList = fotaSetService.findByPager(null, null, propertyArr);
			if(fotaSetList!=null && fotaSetList.size()>0){
				total = fotaSetService.setUnusefulByVersion(fotaSet.getVersion());
				outMessage("删除记录成功："+total+"条，成功！");
			}else{
				outMessage("该版本号没对应的待删除记录.");
			}
			
		}
		
		
	}
	
	/**
	 * 审核
	 */
	public void audit(){
		Integer total=0;
		if(fotaSet!=null){
			String batchVersion=fotaSet.getBatchVersion();
			String auditOper=getOperator().getUsername();
			String auditResult =fotaSet.getAuditResult();
			String auditTime = DateUtil.getCurrentTime();
			List<Property> list = new ArrayList<Property>();
			list.add(Property.eq("useFlag", "1"));
			list.add(Property.isNull("auditResult"));
			Property[] propertyArr = new Property[list.size()];
			list.toArray(propertyArr);
			List<FotaSet> fotaSetList = fotaSetService.findByPager(null, null, propertyArr);
			
			if(fotaSetList!=null && fotaSetList.size()>0){
				total = fotaSetService.setAuditResultByBatchVersion(batchVersion, auditOper, auditResult, auditTime);
			}
			
		}
		if(total>0){
			outMessage("1");
		}else{
			outMessage("0");
		}
	}
	
	/**
	 * 删除单条
	 * 删除待升级列表
	 * 1.下发升级请求，无升级结果,不给删
	 * 2.下发。。。，有升级结果,可以删
	 * 3.无下发,可以删
	 */
	public void del(){
		FotaSet fs = fotaSetService.find(fotaSet.getId());
		if(fotaSet != null){
			fs.setUseFlag("0");
			boolean flag=fotaSetService.fsSaveOrUpdate(fs);
			outMessage("删除结果:"+flag);
		}else{
			outMessage("删除失败,该记录已被他人删除.");
		}
	}
	
	public String exportExcel(){
		String[] headers={"ID","设备号","版本号","上传批号","文件名","ftp地址","ftp端口","用户名","密码","创建时间","是否下发:0-未下发，1-已下发","上传者","审核者","审核结果","审核时间","下发时间","是否有效0无效1有效"};
		String[] cloumn={"id","obdSn","version","batchVersion","fileName","ftpIP","ftpPort","ftpUsername","ftpPwd","createTime","valid","uploadOper","auditOper","auditTime","auditResult","sendTime","useFlag"}; 
		String fileName="待升级设备情况表.xls";
		String filepath = ServletActionContext.getServletContext().getRealPath("/")+fileName;		
//		fss =  listfss(null);
		String[] ids = fotaSetIds.split(";");
		Set<String> obdSnSet = new HashSet<>();
		for (String string : ids) {
			obdSnSet.add(string);
		}
		fotaSetList = fotaSetService.getListByIdMap(obdSnSet);
		
		List<HashMap<String, Object>>  lists = new ArrayList<>();
		for (FotaSet fs : fotaSetList) {
			HashMap map = new  HashMap<>();
			map.put("id",fs.getId());
			map.put("obdSn",fs.getObdSn());
			map.put("version",fs.getVersion());
			map.put("batchVersion",fs.getBatchVersion());
			map.put("fileName",fs.getFileName());
			map.put("ftpIP",fs.getFtpIP());
			map.put("ftpPort",fs.getFtpPort());
			map.put("ftpUsername",fs.getFtpUsername());
			map.put("ftpPwd",fs.getFtpPwd());
			map.put("createTime",fs.getCreateTime());
			
			map.put("valid",fs.getValid());
			map.put("uploadOper",fs.getUploadOper());
			map.put("auditOper",fs.getAuditOper());
			map.put("auditTime",fs.getAuditTime());
			map.put("auditResult",fs.getAuditResult());
			map.put("sendTime",fs.getSendTime());
			map.put("useFlag",fs.getUseFlag());
			
			lists.add(map);
		}
		
		ExcelUtil ex = new ExcelUtil();
		OutputStream out;
		try {
			out = new FileOutputStream(filepath);
			ex.carOwnerExport("FOTA待升级设备情况表", headers, lists, cloumn, out);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		request.setAttribute("filepath", filepath);
		request.setAttribute("fileName", fileName);
		return "excel";
	}
	
	/**
	 * fota待升级——》excel导入
	 */
	public String obdExcel() {

		String msg = "保存成功.";

		try {
			// 1
			String ftype = obdexcelFileName.substring(obdexcelFileName.indexOf("."), obdexcelFileName.length());
			// 2读取excel文档保存入库
			Workbook workbook = null;
			if (".xls".equals(ftype)) {
				workbook = new HSSFWorkbook(new FileInputStream(obdexcel));
			} else if (".xlsx".equals(ftype)) {
				workbook = new XSSFWorkbook(new FileInputStream(obdexcel));
			} else {
				this.message = "请求失败,excel文件后缀名有误,请检查---" + ftype;
				return "list";
			}
			List<FotaSet> fotaList = new ArrayList<>();
			// 循环工作表Sheet
			Map<String, Integer> obdSns = new HashMap<String, Integer>();
			for (int numSheet = 0; numSheet < workbook.getNumberOfSheets(); numSheet++) {
				Sheet sheet = workbook.getSheetAt(numSheet);
				for (Row row : sheet) {
					// //第一行是标题行
					// if(row.getRowNum()==0){
					// continue;
					// }
					if (row.getCell(0) == null) {
						throw new Exception("excel格式有误,请联系管理员.");
					}

					row.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
					String obdSn = row.getCell(0).getStringCellValue();
					if (StringUtils.isEmpty(obdSn)) {
						continue;
					}
					obdSn = obdSn.trim().toLowerCase();// 去掉两边和都转成小写
					if (!obdSns.containsKey(obdSn)) {
						obdSns.put(obdSn.trim(), 1);
					} else {
						int val = obdSns.get(obdSn) + 1;
						obdSns.put(obdSn, val);
					}

					FotaSet fs = new FotaSet();
					fs.setObdSn(obdSn);
					fs.setCreateTime(new Date());
					fs.setValid("0");// 0-未下发，1-已下发
					fs.setUploadOper(getOperator().getUsername());
					fs.setVersion(fotaSet.getVersion());
					fs.setBatchVersion(fotaSet.getBatchVersion());
					fs.setFileName(fotaSet.getFileName());
					fs.setFtpIP(fotaSet.getFtpIP());
					fs.setFtpPort(fotaSet.getFtpPort());
					fs.setFtpUsername(fotaSet.getFtpUsername());
					fs.setFtpPwd(fotaSet.getFtpPwd());
					fs.setUseFlag("1");
					fs.setMifiFlag("0");
					fotaList.add(fs);

				}
			}

			if (fotaList == null || fotaList.size() == 0) {
				msg = "请求失败_文件为空,请检查.";
				this.message = msg;
				return "list";
			}

			// excel重复
			// 2.先判断excel里是否有重复记录
			for (String key : obdSns.keySet()) {
				Integer total = obdSns.get(key);
				if (total > 1) {
					msg = "请求失败,excel记录重复," + key + ":" + total + "---";
					this.message = msg;
					return "list";
				}
				if (key.length() != 8) {
					msg = "请求失败," + key + ":设备号长度有误---";
					this.message = msg;
					return "list";
				}
				if (!key.matches("^[a-z0-9A-Z]+$")) {
					msg = "请求失败," + key + ":设备号有误---";
					this.message = msg;
					return "list";
				}
			}

			// 判断是否存在重复记录
			List<FotaSet> list = fotaSetService.getListByMap(obdSns);
			if (list.size() > 0) {
				StringBuffer sb = new StringBuffer("");
				for (FotaSet fs : list) {
					sb.append(fs.getObdSn() + ",");
				}

				this.message = sb.toString() + ",这些设备存在fota待升级记录,请检查,请求失败.";
				return "list";
			}

			for (FotaSet fs : fotaList) {
				fotaSetService.fsSaveOrUpdate(fs);
			}
			this.message = "保存成功.";
		} catch (Exception e) {
			e.printStackTrace();
			this.message = "请求异常,异常信息------" + e;
			return "list";
		}
		fotaSet = null;
		return fsList();
	}

	public String getStarTime() {
		return starTime;
	}

	public void setStarTime(String starTime) {
		this.starTime = starTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
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

	public FotaSet getFotaSet() {
		return fotaSet;
	}

	public void setFotaSet(FotaSet fotaSet) {
		this.fotaSet = fotaSet;
	}

	public List<FotaSet> getFotaSetList() {
		return fotaSetList;
	}

	public void setFotaSetList(List<FotaSet> fotaSetList) {
		this.fotaSetList = fotaSetList;
	}

	public String getFotaSetIds() {
		return fotaSetIds;
	}

	public void setFotaSetIds(String fotaSetIds) {
		this.fotaSetIds = fotaSetIds;
	}

}
