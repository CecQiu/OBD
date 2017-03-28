package com.hgsoft.system.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException;
import org.springframework.stereotype.Controller;

import com.hgsoft.common.action.BaseAction;
import com.hgsoft.common.utils.ExcelUtil;
import com.hgsoft.common.utils.ExcelWorkSheet;
import com.hgsoft.system.entity.Admin;
import com.hgsoft.system.entity.SystemLog;
import com.hgsoft.system.service.AdminService;
import com.hgsoft.system.service.SystemLogService;

import org.apache.poi.poifs.filesystem.OfficeXmlFileException;
@Controller
@Scope("prototype")
public class SystemLogAction extends BaseAction {
	@Resource
	private SystemLogService systemLogService;
	@Resource
	private AdminService adminService;
	
	private SystemLog systemlog;
	//查询
	private String startTime;
	private String endTime;
	//删除
	private String startTime1;
	private String endTime1;
	private String importExcelProcess;
	
	//导入的excel文件总行数
	private String rowsNum;

	private String excelFormat;
	private Integer startFailRefleshNum;	
	//上传的文件
	private File excelFile;
	//保存原始的文件名
	private String excelFileFileName;
	//将Excel文件解析完毕后，信息存放到这个对象中
	private ExcelWorkSheet<SystemLog> excelWorkSheet;
	private List<Admin> adminList;
	private Admin admin;
	
	/**
	 * 系统日志查询页面
	 * @return
	 */
	public String list(){
		adminList = adminService.findAll();
		if (systemlog == null) {
			list = systemLogService.findSystemLog(pager);
		} else{
			list = systemLogService.findAllSystemLogByHql(pager, systemlog.getLogType(), admin.getUsername(),
					systemlog.getLogData(), systemlog.getRemark(), systemlog.getCoverage(), startTime, endTime);
		}
		return "list";
	}
	
	/**
	 * 日志导出Excel
	 */
	public String logToExcel()throws ServletException{
	      List<SystemLog> systemlogList= new ArrayList<SystemLog>();
	      systemlogList=systemLogService.findLogByTime(startTime, endTime);
	      if(systemlogList.size()<50000){
		      try {
		    	  
		    	  //需要成功获取HttpServletResponse才能成功配置响应头
		    	  HttpServletResponse response = ServletActionContext.getResponse();
		    	  //response.setContentType("application/octets-stream;charset=gb2312");
		    	  response.setContentType("octets/stream");//简单可以设置为信息流
			      response.addHeader("Content-Disposition", "attachment;filename=systemLog.xls");	      
			      //客户端不缓存
			      response.addHeader("Pragma","no-cache");
			      response.addHeader("Cache-Control","no-cache");
			      
			      ExcelUtil<SystemLog> _excelUtil = new ExcelUtil<SystemLog>();
			      String[] headers = { "日志ID","时间", "日志类型", "用户ID", "日志内容", "影响范围", "备注"  };
	              OutputStream out = response.getOutputStream();
	              _excelUtil.exportExcel(headers, systemlogList, out);
	              systemLogService.save(new Date(), 1,this.getOperator() , "["+this.getOperator().getId()+"]"+this.getOperator().getUsername()+"用户导出日志到Excel文件成功", "系统日志查询-日志导出到Excel", "systemLog.xls");
	              out.close();
	              //reset()--Clears any data that exists in the buffer as well as the status code and headers. 
	              //If the response has been committed, this method throws an IllegalStateException. 
			      response.reset();
	              //response.resetBuffer();
		      }catch (IOException e) {
		    	  systemLogService.save(new Date(), 2, getOperator(),  "["+this.getOperator().getId()+"]"+this.getOperator().getUsername()+"用户将日志导出到Excel时抛出的IO异常", "系统日志查询-日志导出到Excel", "IOException");
				  e.printStackTrace();
		      }
	      }else{
	    	  message="overMaxCount";
	      }

	    return list();
	}

	/**
	 * 将Excel日志数据导入到数据库
	 */
	//根据文件类型创建工作簿
	public Workbook createWorkBook(InputStream is){
		if(excelFileFileName.toLowerCase().endsWith("xls")){
			try {
				return new HSSFWorkbook(is);
			} catch (IOException e) {
				systemLogService.save(new Date(), 2, getOperator(), "["+this.getOperator().getId()+"]"+this.getOperator().getUsername()+"用户导入Excel日志数据时创建工作簿IO异常", "系统日志查询-Excel日志数据导入系统", "IOException");
				e.printStackTrace();
			} catch (OfficeXmlFileException e) {
				systemLogService.save(new Date(), 2, getOperator(), "["+this.getOperator().getId()+"]"+this.getOperator().getUsername()+"用户导入Excel日志数据时创建工作簿OfficeXmlFileException异常", "系统日志查询-Excel日志数据导入系统", "OfficeXmlFileException");
				e.printStackTrace();
			}
		}
		// if(excelFileFileName.toLowerCase().endsWith("xlsx")){
		// try {
		// return new XSSFWorkbook(is);
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// }
		return null;
	}
	//判断是否是数字
	public boolean isNumber(String s) {
		String REG_DIGIT="[0-9]*";
		if(s.matches(REG_DIGIT))return true;
		return false;	
	}
	
	//判断是否是0、1、2
	public boolean isNumberOneTwoorThree(String s) {
		int REG_DIGIT=Integer.valueOf(s.trim());
		if(REG_DIGIT==0){
			//System.out.println("0、1、2判断0："+s);
			return true;
		}else if(REG_DIGIT==1){
			//System.out.println("0、1、2判断1："+s);
			return true;
		}else if(REG_DIGIT==2){
			//System.out.println("0、1、2判断2："+s);
			return true;
		}
		else return false;	
	}
	public String importSystemLog(){return "importSystemLog";}
	
	//导入
	public String importSystemLogToExcel() throws Exception{
		System.out.println("importSystemLogToExcel...");
		Workbook workbook=createWorkBook(new FileInputStream(excelFile));
		if(workbook != null) {
			Sheet sheet=workbook.getSheetAt(0);	
			excelWorkSheet=new ExcelWorkSheet<SystemLog>();
			//保存工作单名称
			excelWorkSheet.setSheetName(sheet.getSheetName());
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			StringBuffer canotUpdateID=new StringBuffer("");
			// 处理第一行(即表头)为空或为数字时的异常
			try {
				Row firstRow = sheet.getRow(0);
				Iterator<Cell> iterator = firstRow.iterator();
				// 保存列名
				List<String> cellNames = new ArrayList<String>();
				while (iterator.hasNext()) {
					cellNames.add(iterator.next().getStringCellValue());
				}
				excelWorkSheet.setColumns(cellNames);
				
			} 
			catch (NullPointerException e) {
				systemLogService.save(new Date(), 2,getOperator(), "["+this.getOperator().getId()+"]"+this.getOperator().getUsername()+"用户将Excel导入数据时表头为空的空指针异常", "系统日志查询-Excel日志数据导入系统", "程序设计抛出NullPointerException");
				e.printStackTrace();
			} 
			catch (IllegalStateException e) {
				systemLogService.save(new Date(), 2, getOperator(), "["+this.getOperator().getId()+"]"+this.getOperator().getUsername()+"用户将Excel导入数据时表头为数字的异常", "系统日志查询-Excel日志数据导入系统", "程序设计抛出IllegalStateException");
				e.printStackTrace();
			}
			finally{
				for(int i=1;i<=sheet.getLastRowNum();i++){
					Row row=sheet.getRow(i);		
					try {
						//设置0 - 5列都为字符串型	,且这六列都不为空，为空则执行下一循环	
						row.getCell(0).setCellType(1);
						row.getCell(1).setCellType(1);
						row.getCell(2).setCellType(1);
						row.getCell(3).setCellType(1);
						row.getCell(4).setCellType(1);	
						row.getCell(5).setCellType(1);	
					} 
					//当有空指针异常时抛出异常，执行下一循环
					catch (NullPointerException e) {
						//System.out.println("程序本身设计抛出的异常 ******"+i);
						e.printStackTrace();
					    continue;
					}
					//当备注为空时，则创建并赋null值,重新做此次循环
					try {
						row.getCell(6).setCellType(1);
					} catch (NullPointerException e) {
						//System.out.println("程序本身设计抛出的异常 ******");
						e.printStackTrace();
						row.createCell(6).setCellValue("null");
						i=i-1;
						continue;
					}
					
					SystemLog systemLog=new SystemLog();
					//sys_id系统日志ID不为空且是数字时set,否则执行下一循环
					if(!row.getCell(0).getStringCellValue().equals("")&&isNumber(row.getCell(0).getStringCellValue())){						
						systemLog.setSys_id(Integer.valueOf(row.getCell(0).getStringCellValue()));
						
					}else continue;
					
					//logTime日志时间不为空时set,,否则执行下一循环
					if(!row.getCell(1).getStringCellValue().equals("")){
						Date date=dateFormat.parse(row.getCell(1).getStringCellValue().trim());
						systemLog.setLogTime(date);
						
					}else continue;
					
					//logType日志类型不为空且是为0、1、2时set,否则执行下一循环
					if(!row.getCell(2).getStringCellValue().equals("")&&isNumber(row.getCell(2).getStringCellValue())&&isNumberOneTwoorThree(row.getCell(2).getStringCellValue())){					
						systemLog.setLogType(Integer.valueOf(row.getCell(2).getStringCellValue().trim()));
					
					}else continue;
					
					//Admin系统操作员不为空时set
					if(!row.getCell(3).getStringCellValue().equals("")){
						systemLog.setOperatorID(Integer.valueOf(row.getCell(3).getStringCellValue().trim()));
						
					}else continue;
					
					//logData日志具体内容不为空set
					if(!row.getCell(4).getStringCellValue().equals("")){
						systemLog.setLogData(row.getCell(4).getStringCellValue());
						
					}else continue;
					
					//coverage日志影响范围日志内容不为空set
					systemLog.setCoverage(row.getCell(5).getStringCellValue());
					
					//remark备注
					systemLog.setRemark(row.getCell(6).getStringCellValue());
					
					excelWorkSheet.getData().add(systemLog);
					//System.out.println("add excelWorkSheet success-:"+systemLog.getSys_id());
				}
				
				List<SystemLog> listData=excelWorkSheet.getData();
				
				int count=0;
				//System.out.println("listData.size():"+listData.size());
				
				for (int i = 0; i < listData.size(); i++) {
					count++;
					
					//当系统数据库中不存在相同的日志ID时就保存该条日志记录
					if (systemLogService.ishasSys_id(listData.get(i).getSys_id()) == null) {
						
						//当系统数据库中不存在相同的日志ID和日期时，就保存该条日志记录
						if(systemLogService.ishasLogTime(listData.get(i).getLogTime())==null){
							
							systemLogService.save(listData.get(i));
							// System.out.println("save- 导入Excel日志数据第"+listData.get(i).getSys_id()+"行成功"
							// +"--"+listData.get(i).getLogTime()+"--"+listData.get(i).getLogType()+"--"
							// +listData.get(i).getAdmin()+"--"+listData.get(i).getLogData()+"--"+listData.get(i).getCoverage()+"--"+listData.get(i).getRemark());
							
						}
						else{
							try {
								startFailRefleshNum = listData.get(i).getSys_id();
							    systemLogService.update(listData.get(i));
								
							} catch (HibernateOptimisticLockingFailureException e) {
								//System.out.println("抛出的更新操作的HibernateOptimisticLockingFailureException异常");
								//excelFormat="noPrimarykey";
								canotUpdateID.append(listData.get(i).getSys_id()+" ");
								//System.out.println("catch canotUpdateID:-"+canotUpdateID+"-");
								e.printStackTrace();
								continue;
							}	
						 }
						
					}
					//当系统数据库中存在相同的日志ID时就更新该条日志记录
					else {
						startFailRefleshNum = listData.get(i).getSys_id();
					    systemLogService.update(listData.get(i));
						// System.out.println("update-  导入Excel日志数据第"+listData.get(i).getSys_id()+"行成功"
						// +"--"+listData.get(i).getLogTime()+"--"+listData.get(i).getLogType()+"--"
						// +listData.get(i).getAdmin()+"--"+listData.get(i).getLogData()+"--"+listData.get(i).getCoverage()+"--"+listData.get(i).getRemark());						
	
					}
				}
				
				if(!canotUpdateID.toString().equals("")){
					excelFormat=canotUpdateID.toString();
					systemLogService.save(new Date(), 2, getOperator(), "["+this.getOperator().getId()+"]"+this.getOperator().getUsername()+"用户将Excel导入数据时抛出的hibernate找不到主键的异常,更新日志记录时发现系统中存在与该记录的时间相同的日志记录但日志ID不匹配，不匹配日志ID对应的日志记录为["+canotUpdateID.toString().split(" ").length+"]条", "系统日志查询-Excel日志数据导入系统", "HibernateOptimisticLockingFailureException");
				}
				//System.out.println("导入成功数："+count);
				
				importExcelProcess="importSuccess";
				//导入的总行数为sheet.getLastRowNum()，count为导入成功的总条数
				//当rowsNum=0时全部导入成功，否则存在失败的条数为rowsNum
				rowsNum=String.valueOf(sheet.getLastRowNum()-count);
				//System.out.println("rowsNum:"+rowsNum);
				
				if(count!=0){ 
					message=String.valueOf(count);
				}
				else message="ExcelToDBfail";
			}
			systemLogService.save(new Date(), 1, getOperator(), "["+this.getOperator().getId()+"]"+this.getOperator().getUsername()+"用户成功将Excel数据导入系统", "系统日志查询-Excel日志数据导入系统", "t_park_systemLog");
		} else {
			this.message="导入失败，excel文件解析错误！";
		}
		return "importSystemLog";
	}

	//qinzuohai
	public String toDelSystemLog(){return "toDelSystemLog";}
	//根据日期范围删除日志
	public String delSystemLog(){
			if (systemLogService.delLogByTime(startTime1, endTime1)) {
				message="删除日志成功 ！";
				systemLogService.save(new Date(), 1, getOperator(), "["+this.getOperator().getId()+"]"+this.getOperator().getUsername()+"用户成功将"+startTime1+"到"+endTime1+"日期段的日志删除", "删除系统日志", "t_park_systemLog");
				System.out.println(message);
				return list();
			} else {
				message = "删除数据失败，请重试！";
				return "error";
			}
		}

	public SystemLogService getSystemLogService() {
		return systemLogService;
	}

	public void setSystemLogService(SystemLogService systemLogService) {
		this.systemLogService = systemLogService;
	}

	public SystemLog getSystemlog() {
		return systemlog;
	}

	public void setSystemlog(SystemLog systemlog) {
		this.systemlog = systemlog;
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

	public String getImportExcelProcess() {
		return importExcelProcess;
	}

	public void setImportExcelProcess(String importExcelProcess) {
		this.importExcelProcess = importExcelProcess;
	}

	public String getRowsNum() {
		return rowsNum;
	}

	public void setRowsNum(String rowsNum) {
		this.rowsNum = rowsNum;
	}

	public String getExcelFormat() {
		return excelFormat;
	}

	public void setExcelFormat(String excelFormat) {
		this.excelFormat = excelFormat;
	}

	public Integer getStartFailRefleshNum() {
		return startFailRefleshNum;
	}

	public void setStartFailRefleshNum(Integer startFailRefleshNum) {
		this.startFailRefleshNum = startFailRefleshNum;
	}

	public File getExcelFile() {
		return excelFile;
	}

	public void setExcelFile(File excelFile) {
		this.excelFile = excelFile;
	}

	public String getExcelFileFileName() {
		return excelFileFileName;
	}

	public void setExcelFileFileName(String excelFileFileName) {
		this.excelFileFileName = excelFileFileName;
	}

	public ExcelWorkSheet<SystemLog> getExcelWorkSheet() {
		return excelWorkSheet;
	}

	public void setExcelWorkSheet(ExcelWorkSheet<SystemLog> excelWorkSheet) {
		this.excelWorkSheet = excelWorkSheet;
	}

	public List<Admin> getAdminList() {
		return adminList;
	}

	public void setAdminList(List<Admin> adminList) {
		this.adminList = adminList;
	}

	public Admin getAdmin() {
		return admin;
	}

	public void setAdmin(Admin admin) {
		this.admin = admin;
	}

	public void setStartTime1(String startTime1) {
		this.startTime1 = startTime1;
	}

	public void setEndTime1(String endTime1) {
		this.endTime1 = endTime1;
	}
}
