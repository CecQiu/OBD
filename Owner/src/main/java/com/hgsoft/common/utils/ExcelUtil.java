package com.hgsoft.common.utils;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFComment;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;


public class ExcelUtil<T> {
	
	public void exportExcel(Collection<T> dataset, OutputStream out) {
		exportExcel("EXCEL文档", null, dataset, out, "yyyy-MM-dd");
	}

	public void exportExcel(String[] headers, Collection<T> dataset,OutputStream out) {
		exportExcel("EXCEL文档", headers, dataset, out, "yyyy-MM-dd");
	}

	public void exportExcel(String[] headers, Collection<T> dataset,OutputStream out, String pattern) {
		exportExcel("EXCEL文档", headers, dataset, out, pattern);
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	public void exportExcel(String title, String[] headers,Collection<T> dataset, OutputStream out, String pattern) {

	      // 声明一个工作薄
	      HSSFWorkbook workbook = new HSSFWorkbook();

	      // 生成一个表格
	      HSSFSheet sheet = workbook.createSheet(title);

	      // 设置表格默认列宽度为15个字节
	      sheet.setDefaultColumnWidth((short) 15);

	      // 生成一个样式
	      HSSFCellStyle style = workbook.createCellStyle();

	      // 设置样式
	      style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);//表头是天空蓝
	      
          //style.setFillForegroundColor(HSSFColor.LIGHT_BLUE.index);

	      style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

	      style.setBorderBottom(HSSFCellStyle.BORDER_THIN);

	      style.setBorderLeft(HSSFCellStyle.BORDER_THIN);

	      style.setBorderRight(HSSFCellStyle.BORDER_THIN);

	      style.setBorderTop(HSSFCellStyle.BORDER_THIN);

	      style.setAlignment(HSSFCellStyle.ALIGN_CENTER);

	      // 生成一个字体
	      HSSFFont font = workbook.createFont();

	      font.setColor(HSSFColor.VIOLET.index);

	      font.setFontHeightInPoints((short) 12);

	      font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

	      // 把字体应用到当前的样式
	      style.setFont(font);

	      // 生成并设置另一个样式
	      HSSFCellStyle style2 = workbook.createCellStyle();

//	      style2.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
	      style2.setFillForegroundColor(HSSFColor.WHITE.index);

	      style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

	      style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);

	      style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);

	      style2.setBorderRight(HSSFCellStyle.BORDER_THIN);

	      style2.setBorderTop(HSSFCellStyle.BORDER_THIN);

	      style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);

	      style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

	      // 生成另一个字体
	      HSSFFont font2 = workbook.createFont();

	      font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);

	      // 把字体应用到当前的样式
	      style2.setFont(font2);

	      

	      // 声明一个画图的顶级管理器
	      HSSFPatriarch patriarch = sheet.createDrawingPatriarch();

	      // 定义注释的大小和位置
	      HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0, 0, 0, 0, (short) 4, 2, (short) 6, 5));

	      // 设置注释内容
	      comment.setString(new HSSFRichTextString(""));

	      // 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.
	      comment.setAuthor(" - qinzuohai");

	      //产生表格标题行
	      HSSFRow row = sheet.createRow(0);

	      for (short i = 0; i < headers.length; i++) {

	         HSSFCell cell = row.createCell(i);

	         cell.setCellStyle(style);

	         //生成表头的字体
	         HSSFRichTextString text = new HSSFRichTextString(headers[i]);

	         cell.setCellValue(text);

	      }

	      //遍历集合数据，产生数据行
	      Iterator<T> it = dataset.iterator();

	      int index = 0;

	      while (it.hasNext()) {

	         index++;

	         row = sheet.createRow(index);

	         T t = (T) it.next();

	         //利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值
	         Field[] fields = t.getClass().getDeclaredFields();

	         for (short i = 0; i < fields.length; i++) {

	            HSSFCell cell = row.createCell(i);

	            cell.setCellStyle(style2);

	            Field field = fields[i];

	            String fieldName = field.getName();

	            String getMethodName = "get"

	                   + fieldName.substring(0, 1).toUpperCase()

	                   + fieldName.substring(1);

	            try {
	            	Object value; 
	                Class tCls = t.getClass(); 
                    //System.out.println(" 取得POJOl类对象                           --- "+tCls.toString());
                    
	                Method getMethod = tCls.getMethod(getMethodName,new Class[]{});
	                //System.out.println(" 取得POJO的getter方法              --- :"+getMethod.getName()+"-");
	                
	                //
	                if(getMethod.getName().equals("getOperatorID")){
	                	//Admin admin=(Admin)getMethod.invoke(t, new Object[]{});
	                	//System.out.println("-----------------"+getMethod.invoke(t, new Object[]{}));
	                	 value = getMethod.invoke(t, new Object[]{});
	                }else{
	                	 value = getMethod.invoke(t, new Object[] {});
	 	                //System.out.println(" 通过getter方法获取属性值     --- "+value);
	                }
    
	                //判断值的类型后进行强制类型转换
	                String textValue = null;

					// if (value instanceof Integer) {
					// int intValue = (Integer) value;
					// cell.setCellValue(intValue);
					// } else {
					// // 其它数据类型都当作字符串简单处理
					// textValue = value.toString();
					// }
	                if(value==null){
	                	value="";
	                }
					textValue = value.toString();
					HSSFRichTextString richString = new HSSFRichTextString(
							textValue);

					HSSFFont font3 = workbook.createFont();

					font3.setColor(HSSFColor.BLUE.index);

					richString.applyFont(font3);

					cell.setCellValue(richString);

	            } catch (SecurityException e) {

	                e.printStackTrace();

	            } catch (NoSuchMethodException e) {
	                e.printStackTrace();

	            } catch (IllegalArgumentException e) {
	                e.printStackTrace();
	            } catch (IllegalAccessException e) {
	                e.printStackTrace();

	            } catch (InvocationTargetException e) {
	                e.printStackTrace();

	            } finally {
	                //清理资源
	            }

	         }

	      }
	      try {
	         workbook.write(out);

	      } catch (IOException e) {
	         e.printStackTrace();
	      }
	   }

	
	//导出白名单  曾
	@SuppressWarnings({ "unchecked", "deprecation" })
	public void exportExcel(String title, String[] headers,Collection<Map> dataset, OutputStream out, String pattern,int sheetCount,Map<String,String> mapUserType,Map<String,String>mapRuleType) {
		  HSSFWorkbook workbook = new HSSFWorkbook();// 声明一个工作薄
	      int index = 0;
	      int currSheet = 0;
	      int rows = 0;
	      HSSFSheet sheet = workbook.createSheet(title+currSheet);// 生成一个表格
	      sheet.setDefaultColumnWidth((short) 15);// 设置表格默认列宽度为15个字节
	      HSSFCellStyle style = workbook.createCellStyle();// 生成一个样式
	      // 设置样式
	      style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);//表头是天空蓝
	      style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	      style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	      style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	      style.setBorderRight(HSSFCellStyle.BORDER_THIN);
	      style.setBorderTop(HSSFCellStyle.BORDER_THIN);
	      style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	      // 生成一个字体
	      HSSFFont font = workbook.createFont();
	      font.setColor(HSSFColor.VIOLET.index);
	      font.setFontHeightInPoints((short) 12);
	      font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	      // 把字体应用到当前的样式
	      style.setFont(font);
	      // 生成并设置另一个样式
	      HSSFCellStyle style2 = workbook.createCellStyle();
	      style2.setFillForegroundColor(HSSFColor.WHITE.index);
	      style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	      style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	      style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	      style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
	      style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
	      style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	      style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
	      // 生成另一个字体
	      HSSFFont font2 = workbook.createFont();
	      font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
	      
	      HSSFFont font3 = workbook.createFont();
          font3.setColor(HSSFColor.BLUE.index);
          
	      // 把字体应用到当前的样式
	      style2.setFont(font2);
	      // 声明一个画图的顶级管理器
	      HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
	      // 定义注释的大小和位置
	      HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0, 0, 0, 0, (short) 4, 2, (short) 6, 5));
	      // 设置注释内容
	      comment.setString(new HSSFRichTextString(""));
	      // 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.
	      comment.setAuthor(" - kevin");
	      //产生表格标题行
	      HSSFRow row = sheet.createRow(0);
	      for (short i = 0; i < headers.length; i++) {
	         HSSFCell cell = row.createCell(i);
	         cell.setCellStyle(style);
	         //生成表头的字体
	         HSSFRichTextString text = new HSSFRichTextString(headers[i]);
	         cell.setCellValue(text);
	      }
	      //遍历集合数据，产生数据行
	      Iterator<Map> it = dataset.iterator();
	      while (it.hasNext()) {
	         if(index>0 && index%sheetCount==0){
	        	 currSheet ++;
	        	 rows = 0;
	        	 sheet = workbook.createSheet(title+currSheet);
	        	 sheet.setDefaultColumnWidth((short) 15);
	        	 patriarch = sheet.createDrawingPatriarch();
	        	 // 定义注释的大小和位置
	   	      	comment = patriarch.createComment(new HSSFClientAnchor(0, 0, 0, 0, (short) 4, 2, (short) 6, 5));
	   	      	// 设置注释内容
	   	      	comment.setString(new HSSFRichTextString(""));
	   	      	// 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.
	   	      	comment.setAuthor(" - kevin");
	   	      	//产生表格标题行
	   	      	row = sheet.createRow(0);
	   	      	for (short i = 0; i < headers.length; i++) {
	   	      		HSSFCell cell = row.createCell(i);
	   	      		cell.setCellStyle(style);
	   	      		//生成表头的字体
	   	      		HSSFRichTextString text = new HSSFRichTextString(headers[i]);
	   	      		cell.setCellValue(text);
	   	      	}
	         }
	         index++;
	         rows++;
	         row = sheet.createRow(rows);
	         //CardNO,VehPlate,OwnerName,OBUID,FirTime,InvaliDateTime,CARDTYPE,Status,HasVehPic,FeeRuleType
	         Map map = it.next();
	         int temp = 0;
	         for(Object o:map.keySet()){
	        	 String value = "";
		         HSSFCell cell = row.createCell(temp);
		         cell.setCellStyle(style2);
		         Object tempO = map.get(o);
		         if(null != tempO){
		        	 value = tempO.toString();
		         }
	             //value = map.get(o).toString();
	             if(temp == 4){
	            	 if(value.length()>0){
	            		 value = value.substring(0, value.length()-10);
	            	 }
	             }
	             if(temp == 5){
	            	 if(value.length()>0){
	            		 value = value.substring(0, value.length()-10);
	            	 }
	             }
	             if(temp == 6){
	            	 value = mapUserType.get(value);
	             }
	             if(temp == 7){
	            	 if(value.equals("0")){
	            		 value="正常";
	            	 }
	            	 if(value.equals("1")){
	            		 value="挂失";
	            	 }
	            	 if(value.equals("9")){
	            		 value="未缴费";
	            	 }
	             }
	             if(temp == 8){
	            	 if(value.equals("0")){
	            		 value="无";
	            	 }
	            	 if(value.equals("1")){
	            		 value="有";
	            	 }
	             }
	             if(temp == 9){
	            	 value = mapRuleType.get(value);
	             }
	             HSSFRichTextString richString = new HSSFRichTextString(value);
	             richString.applyFont(font3);
	             cell.setCellValue(richString);
	             temp++;
	         }
	      }
	      try {
	         workbook.write(out);
	      } catch (IOException e) {
	         e.printStackTrace();
	      }
	   }
	
	//导出IC卡  曾
	@SuppressWarnings({ "unchecked", "deprecation" })
	public void exportICCardExcel(String title, String[] headers,Collection<Map> dataset, OutputStream out, String pattern,int sheetCount,Map<String,String> mapUserCardType,Map<String,String>mapCardType) {
		HSSFWorkbook workbook = new HSSFWorkbook();// 声明一个工作薄
	      int index = 0;
	      int currSheet = 0;
	      int rows = 0;
	      HSSFSheet sheet = workbook.createSheet(title+currSheet);// 生成一个表格
	      sheet.setDefaultColumnWidth((short) 15);// 设置表格默认列宽度为15个字节
	      HSSFCellStyle style = workbook.createCellStyle();// 生成一个样式
	      // 设置样式
	      style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);//表头是天空蓝
	      style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	      style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	      style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	      style.setBorderRight(HSSFCellStyle.BORDER_THIN);
	      style.setBorderTop(HSSFCellStyle.BORDER_THIN);
	      style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	      // 生成一个字体
	      HSSFFont font = workbook.createFont();
	      font.setColor(HSSFColor.VIOLET.index);
	      font.setFontHeightInPoints((short) 12);
	      font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	      // 把字体应用到当前的样式
	      style.setFont(font);
	      // 生成并设置另一个样式
	      HSSFCellStyle style2 = workbook.createCellStyle();
	      style2.setFillForegroundColor(HSSFColor.WHITE.index);
	      style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	      style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	      style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	      style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
	      style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
	      style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	      style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
	      // 生成另一个字体
	      HSSFFont font2 = workbook.createFont();
	      font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
	      
	      HSSFFont font3 = workbook.createFont();
        font3.setColor(HSSFColor.BLUE.index);
        
	      // 把字体应用到当前的样式
	      style2.setFont(font2);
	      // 声明一个画图的顶级管理器
	      HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
	      // 定义注释的大小和位置
	      HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0, 0, 0, 0, (short) 4, 2, (short) 6, 5));
	      // 设置注释内容
	      comment.setString(new HSSFRichTextString(""));
	      // 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.
	      comment.setAuthor(" - kevin");
	      //产生表格标题行
	      HSSFRow row = sheet.createRow(0);
	      for (short i = 0; i < headers.length; i++) {
	         HSSFCell cell = row.createCell(i);
	         cell.setCellStyle(style);
	         //生成表头的字体
	         HSSFRichTextString text = new HSSFRichTextString(headers[i]);
	         cell.setCellValue(text);
	      }
	      //遍历集合数据，产生数据行
	      Iterator<Map> it = dataset.iterator();
	      while (it.hasNext()) {
	         if(index>0 && index%sheetCount==0){
	        	 currSheet ++;
	        	 rows = 0;
	        	 sheet = workbook.createSheet(title+currSheet);
	        	 sheet.setDefaultColumnWidth((short) 15);
	        	 patriarch = sheet.createDrawingPatriarch();
	        	 // 定义注释的大小和位置
	   	      	comment = patriarch.createComment(new HSSFClientAnchor(0, 0, 0, 0, (short) 4, 2, (short) 6, 5));
	   	      	// 设置注释内容
	   	      	comment.setString(new HSSFRichTextString(""));
	   	      	// 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.
	   	      	comment.setAuthor(" - kevin");
	   	      	//产生表格标题行
	   	      	row = sheet.createRow(0);
	   	      	for (short i = 0; i < headers.length; i++) {
	   	      		HSSFCell cell = row.createCell(i);
	   	      		cell.setCellStyle(style);
	   	      		//生成表头的字体
	   	      		HSSFRichTextString text = new HSSFRichTextString(headers[i]);
	   	      		cell.setCellValue(text);
	   	      	}
	         }
	         index++;
	         rows++;
	         row = sheet.createRow(rows);
	         //CardNO,VehPlate,OwnerName,OBUID,FirTime,InvaliDateTime,CARDTYPE,Status,HasVehPic,FeeRuleType
	         Map map = it.next();
	         int temp = 0;
	         for(Object o:map.keySet()){
	        	 String value = "";
		         HSSFCell cell = row.createCell(temp);
		         cell.setCellStyle(style2);
	             value = map.get(o).toString();
	             if(temp == 1){
	            	 if(value.length()>0){
	            		 value = mapCardType.get(value);
	            	 }
	             }
	             if(temp == 2){
	            	 if(value.length()>0){
	            		 value = mapUserCardType.get(value);
	            	 }
	             }
	             if(temp == 3){
	            	 if(value.equals("0")){
	            		 value="正常";
	            	 }
	            	 if(value.equals("1")){
	            		 value="挂失";
	            	 }
	            	 if(value.equals("2")){
	            		 value="丢失";
	            	 }
	            	 if(value.equals("3")){
	            		 value="损坏";
	            	 }
	             }
	             if(temp == 4){
	            	 value = value.substring(0,19);
	             }
	             HSSFRichTextString richString = new HSSFRichTextString(value);
	             richString.applyFont(font3);
	             cell.setCellValue(richString);
	             temp++;
	         }
	      }
	      try {
	         workbook.write(out);
	      } catch (IOException e) {
	         e.printStackTrace();
	      }
	}
	
	//导出校园停放车辆信息  
		@SuppressWarnings({ "unchecked", "deprecation" })
		public void exportCarPartStatusExcel(String title, String[] headers,Collection<Map> dataset, OutputStream out, String pattern,int sheetCount,Map<String,String> mapPark,Map<String,String> mapUserCardType,Map<String,String>mapFeeRuleTypeDef,
				Map<String,String> mapTicketTypeCode,Map<String,String> mapSquad,Map<String,String> mapVehTypeCode,Map<String,String> mapVehClassCode) {
			HSSFWorkbook workbook = new HSSFWorkbook();// 声明一个工作薄
		      int index = 0;
		      int currSheet = 0;
		      int rows = 0;
		      HSSFSheet sheet = workbook.createSheet(title+currSheet);// 生成一个表格
		      sheet.setDefaultColumnWidth((short) 15);// 设置表格默认列宽度为15个字节
		      HSSFCellStyle style = workbook.createCellStyle();// 生成一个样式
		      // 设置样式
		      style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);//表头是天空蓝
		      style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		      style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		      style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		      style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		      style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		      style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		      // 生成一个字体
		      HSSFFont font = workbook.createFont();
		      font.setColor(HSSFColor.VIOLET.index);
		      font.setFontHeightInPoints((short) 12);
		      font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		      // 把字体应用到当前的样式
		      style.setFont(font);
		      // 生成并设置另一个样式
		      HSSFCellStyle style2 = workbook.createCellStyle();
		      style2.setFillForegroundColor(HSSFColor.WHITE.index);
		      style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		      style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		      style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		      style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
		      style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
		      style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		      style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		      // 生成另一个字体
		      HSSFFont font2 = workbook.createFont();
		      font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		      
		      HSSFFont font3 = workbook.createFont();
		      font3.setColor(HSSFColor.BLUE.index);
	        
		      // 把字体应用到当前的样式
		      style2.setFont(font2);
		      // 声明一个画图的顶级管理器
		      HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
		      // 定义注释的大小和位置
		      HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0, 0, 0, 0, (short) 4, 2, (short) 6, 5));
		      // 设置注释内容
		      comment.setString(new HSSFRichTextString(""));
		      // 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.
		      comment.setAuthor(" - kevin");
		      //产生表格标题行
		      HSSFRow row = sheet.createRow(0);
		      for (short i = 0; i < headers.length; i++) {
		         HSSFCell cell = row.createCell(i);
		         cell.setCellStyle(style);
		         //生成表头的字体
		         HSSFRichTextString text = new HSSFRichTextString(headers[i]);
		         cell.setCellValue(text);
		      }
		      //遍历集合数据，产生数据行
		      Iterator<Map> it = dataset.iterator();
		      while (it.hasNext()) {
		         if(index>0 && index%sheetCount==0){
		        	 currSheet ++;
		        	 rows = 0;
		        	 sheet = workbook.createSheet(title+currSheet);
		        	 sheet.setDefaultColumnWidth((short) 15);
		        	 patriarch = sheet.createDrawingPatriarch();
		        	 // 定义注释的大小和位置
		   	      	comment = patriarch.createComment(new HSSFClientAnchor(0, 0, 0, 0, (short) 4, 2, (short) 6, 5));
		   	      	// 设置注释内容
		   	      	comment.setString(new HSSFRichTextString(""));
		   	      	// 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.
		   	      	comment.setAuthor(" - kevin");
		   	      	//产生表格标题行
		   	      	row = sheet.createRow(0);
		   	      	for (short i = 0; i < headers.length; i++) {
		   	      		HSSFCell cell = row.createCell(i);
		   	      		cell.setCellStyle(style);
		   	      		//生成表头的字体
		   	      		HSSFRichTextString text = new HSSFRichTextString(headers[i]);
		   	      		cell.setCellValue(text);
		   	      	}
		         }
		         index++;
		         rows++;
		         row = sheet.createRow(rows);
		         Map map = it.next();
		         int temp = 0;
		         for(Object o:map.keySet()){
		        	 String value = "";
			         HSSFCell cell = row.createCell(temp);
			         cell.setCellStyle(style2);
			         Object tempO = map.get(o);
			         if(null != tempO){
			        	 value = tempO.toString();
			         }
			         if(temp == 2) {
			        	 if(value.length()>0) {
			        		 value = mapPark.get(value);
			        	 }
			         }
			         if(temp == 6) {
			        	 if(value.length()>0) {
			        		 value = mapVehTypeCode.get(value);
			        	 }
			         }
			         if(temp == 9) {
			        	 if(value.length()>0) {
			        		 value = mapSquad.get(value);
			        	 }
			         }
			         if(temp == 11) {
			        	 if(value.length()>0) {
			        		 value = mapUserCardType.get(value);
			        	 }
			         }
			         if(temp == 12) {
			        	 if(value.length()>0) {
			        		 value = mapTicketTypeCode.get(value);
			        	 }
			         }
			         if(temp == 13) {
			        	 if(value.length()>0) {
			        		 value = mapVehClassCode.get(value);
			        	 }
			         }
			         if(temp == 14) {
			        	 if(value.length()>0) {
			        		 value = mapFeeRuleTypeDef.get(value);
			        	 }
			         }
			         
		             HSSFRichTextString richString = new HSSFRichTextString(value);
		             richString.applyFont(font3);
		             cell.setCellValue(richString);
		             temp++;
		         }
		      }
		      try {
		         workbook.write(out);
		      } catch (IOException e) {
		         e.printStackTrace();
		      }
		}
		//导出出口流水信息  
				@SuppressWarnings({ "unchecked", "deprecation" })
				public void exportWaterQueryExcel(String title, String[] headers,Collection<Map> dataset, OutputStream out, String pattern,int sheetCount,Map<String,String> mapPark,
						Map<String,String> mapVehType,Map<String,String> mapVehClass,Map<String,String> mapSquad,Map<String,String> mapUserCardType,Map<String,String> mapTicketType,Map<String,String> mapCardType) {
					HSSFWorkbook workbook = new HSSFWorkbook();// 声明一个工作薄
				      int index = 0;
				      int currSheet = 0;
				      int rows = 0;
				      HSSFSheet sheet = workbook.createSheet(title+currSheet);// 生成一个表格
				      sheet.setDefaultColumnWidth((short) 15);// 设置表格默认列宽度为15个字节
				      HSSFCellStyle style = workbook.createCellStyle();// 生成一个样式
				      // 设置样式
				      style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);//表头是天空蓝
				      style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
				      style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
				      style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
				      style.setBorderRight(HSSFCellStyle.BORDER_THIN);
				      style.setBorderTop(HSSFCellStyle.BORDER_THIN);
				      style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
				      // 生成一个字体
				      HSSFFont font = workbook.createFont();
				      font.setColor(HSSFColor.VIOLET.index);
				      font.setFontHeightInPoints((short) 12);
				      font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				      // 把字体应用到当前的样式
				      style.setFont(font);
				      // 生成并设置另一个样式
				      HSSFCellStyle style2 = workbook.createCellStyle();
				      style2.setFillForegroundColor(HSSFColor.WHITE.index);
				      style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
				      style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
				      style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
				      style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
				      style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
				      style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
				      style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
				      // 生成另一个字体
				      HSSFFont font2 = workbook.createFont();
				      font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
				      
				      HSSFFont font3 = workbook.createFont();
				      font3.setColor(HSSFColor.BLUE.index);
			        
				      // 把字体应用到当前的样式
				      style2.setFont(font2);
				      // 声明一个画图的顶级管理器
				      HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
				      // 定义注释的大小和位置
				      HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0, 0, 0, 0, (short) 4, 2, (short) 6, 5));
				      // 设置注释内容
				      comment.setString(new HSSFRichTextString(""));
				      // 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.
				      comment.setAuthor(" - kevin");
				      //产生表格标题行
				      HSSFRow row = sheet.createRow(0);
				      for (short i = 0; i < headers.length; i++) {
				         HSSFCell cell = row.createCell(i);
				         cell.setCellStyle(style);
				         //生成表头的字体
				         HSSFRichTextString text = new HSSFRichTextString(headers[i]);
				         cell.setCellValue(text);
				      }
				      //遍历集合数据，产生数据行
				      Iterator<Map> it = dataset.iterator();
				      while (it.hasNext()) {
				         if(index>0 && index%sheetCount==0){
				        	 currSheet ++;
				        	 rows = 0;
				        	 sheet = workbook.createSheet(title+currSheet);
				        	 sheet.setDefaultColumnWidth((short) 15);
				        	 patriarch = sheet.createDrawingPatriarch();
				        	 // 定义注释的大小和位置
				   	      	comment = patriarch.createComment(new HSSFClientAnchor(0, 0, 0, 0, (short) 4, 2, (short) 6, 5));
				   	      	// 设置注释内容
				   	      	comment.setString(new HSSFRichTextString(""));
				   	      	// 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.
				   	      	comment.setAuthor(" - kevin");
				   	      	//产生表格标题行
				   	      	row = sheet.createRow(0);
				   	      	for (short i = 0; i < headers.length; i++) {
				   	      		HSSFCell cell = row.createCell(i);
				   	      		cell.setCellStyle(style);
				   	      		//生成表头的字体
				   	      		HSSFRichTextString text = new HSSFRichTextString(headers[i]);
				   	      		cell.setCellValue(text);
				   	      	}
				         }
				         index++;
				         rows++;
				         row = sheet.createRow(rows);
				         Map map = it.next();
				         int temp = 0;
				         for(Object o:map.keySet()){
				        	 String value = "";
					         HSSFCell cell = row.createCell(temp);
					         cell.setCellStyle(style2);
					         Object tempO = map.get(o);
					         if(null != tempO){
					        	 value = tempO.toString();
					         }
					         
					         if(temp == 1) {
					        	 if(value.length()>0) {
					        		 value = mapPark.get(value);
					        	 }
					         }
					         if(temp == 4) {
					        	 if(value.length()>0) {
					        		 value = mapVehType.get(value);
					        	 }
					         }
					         if(temp == 5) {
					        	 if(value.length()>0) {
					        		 value = mapVehClass.get(value);
					        	 }
					         }
					         if(temp == 7 ) {
					        	 if(value.length()>0) {
					        		 value = mapSquad.get(value);
					        	 }
					         }
					         if(temp == 12) {
					        	 if(value.length()>0) {
					        		 value = mapUserCardType.get(value);
					        	 }
					         }
					         if(temp == 13) {
					        	 if(value.length()>0) {
					        		 value = mapTicketType.get(value);
					        	 }
					         }
					         if(temp == 14) {
					        	 if(value.length()>0) {
					        		 if(value.equals("0")) {
					        			 value = "无卡";
					        		 } else {
					        			 value = mapCardType.get(value);
					        		 }
					        	 }
					         }
					         
				             HSSFRichTextString richString = new HSSFRichTextString(value);
				             richString.applyFont(font3);
				             cell.setCellValue(richString);
				             temp++;
				         }
				      }
				      try {
				         workbook.write(out);
				      } catch (IOException e) {
				         e.printStackTrace();
				      }
				}
	/**
	 * 车主通导出统计表			
	 * @param fileName
	 * @param headers
	 * @param lists
	 * @param cloumn
	 * @param out
	 * @throws IOException
	 */
	public void carOwnerExport(String fileName,String[] headers,List<HashMap<String, Object>> lists,String[] cloumn,OutputStream out) throws IOException {
		// 声明一个工作薄
		HSSFWorkbook workbook=new HSSFWorkbook();
		// 生成一个表格
		HSSFSheet sheet = workbook.createSheet(fileName);

		// 设置表格默认列宽度为20个字节
		sheet.setDefaultColumnWidth(13);
		// 生成一个样式
		HSSFCellStyle style = workbook.createCellStyle();
		// 设置这些样式
		style.setFillForegroundColor(HSSFColor.GOLD.index);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// 生成一个字体
		HSSFFont font = workbook.createFont();
		font.setColor(HSSFColor.VIOLET.index);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

		// 把字体应用到当前的样式

		style.setFont(font);

		// 指定当单元格内容显示不下时自动换行

		style.setWrapText(true);
		HSSFRow row = sheet.createRow(0);
		if (headers != null) {

			for (int i = 0; i < headers.length; i++) {

				HSSFCell cell = row.createCell(i);

				cell.setCellStyle(style);

				HSSFRichTextString text = new HSSFRichTextString(headers[i]);

				cell.setCellValue(text);

			}
		}

		// 遍历集合数据，产生数据行

		if (lists != null && lists.size() > 0) {

			int index = 1;

			for (HashMap<String, Object> m : lists) {

				row = sheet.createRow(index);

				int cellIndex = 0;
				if (cloumn != null) {

					for (String s : cloumn) {

						HSSFCell cell = row.createCell(cellIndex);

						HSSFRichTextString richString = new HSSFRichTextString(
								m.get(s) == null ? "" : m.get(s).toString());

						cell.setCellValue(richString);

						cellIndex++;

					}
				}

				index++;

			}

		}
		workbook.write(out);
	}
	/*public static void main(String[] args) {
`
	      // 测试
		  ExcelUtil<Sysparamconf> ex = new ExcelUtil<Sysparamconf>();
	      String[] headers = { "序号", "参数名", "参数值", "类别", "备注" };
	      List<Sysparamconf> sysparamconfList = new ArrayList<Sysparamconf>();
	    
	      try {
	    	  sysparamconfList.add(new Sysparamconf(01,"系统名","小区车辆管理",1,"系统说明" ));
	    	  sysparamconfList.add(new Sysparamconf(02,"费率","降低20%",0,"费率情况" ));
		      OutputStream out = new FileOutputStream("E:"+"参数配置信息.xls");
		      ex.exportExcel(headers, sysparamconfList, out);
	          out.close();
		      JOptionPane.showMessageDialog(null, "Excel导出成功D盘-- 参数配置信息.xls!");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}*/
}