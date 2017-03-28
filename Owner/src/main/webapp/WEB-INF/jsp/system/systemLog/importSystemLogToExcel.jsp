<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=GBK" language="java" pageEncoding="gbk"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
	<head>
		<title>ImportExcel</title>
		<link type="text/css" rel="stylesheet" href="${basePath}/css/admin.css"/>
		<link type="text/css" rel="stylesheet" href="${basePath}/css/theme.css"/>
		<jsp:include page="../../include/common.jsp" />
		<script type="text/javascript" src="${basePath}/js/CJL.0.1.min.js"></script>  
		<script type="text/javascript" src="${basePath}/js/MSGalert.js"></script>  
		<script language="javascript">
		/**if("${excelFormat}"=="noPrimarykey"){
			alert("更新异常：\n\n    更新到日志ID为 "+"${startFailRefleshNum}"+" 的日志记录时，发现系统中存在与该条记录的时间相同的日志记录但日志ID不匹配，\n系统视该日志记录无需更新，将跳过继续导入下一条日志记录，请确认！");
		    }**/
		if("${excelFormat}"!=""){
				alert("更新异常：\n\n    系统中存在与下列日志ID对应记录的时间相同的日志记录但日志ID（主键）不一致,系统对下列日志ID对应的日志记录不进行更新操作：\n\n"+"${excelFormat}");
		    }   
		if("${message}"!=""&&IsNumber("${message}")){
			if("${rowsNum}"=="0")
				alert("成功导入"+"${message}"+"条数据！");
			else
				alert("成功导入"+"${message}"+"条数据，"+"不符合导入规范的数据有"+"${rowsNum}"+"条！"); 
		    }
		if("${message}"=="ExcelToDBfail"){
			alert("导入不成功，不符合导入规范的数据有"+"${rowsNum}"+"条！");
		    }
		function IsNumber(num){
			  var reNum=/^\d*$/;
			  return(reNum.test(num));
			}
					
		function checkFile(){  
			if(!isXLS(get("excelFile").value)){				
				alert("请选择一个Excel文件！");
				return false;  
				}
		}
		function isXLS(file){
			if(file.indexOf(".")>0){ 
				var o = file.split("."); 
				var e = o[o.length-1].toLowerCase(); 
				if(e == "xls"){
					showMSGalert();
					return true;} 
				else{ return false; }
				} 
				
			}
		function back(){
			history.back();
			}
		//1-6记录数
		var count=1;
        function insert(){
        	var text="日志数据正在导入中，请稍候";
        	if(count<=6&&count>0){
        		if(count==1)text=text+".";
        		if(count==2)text=text+"..";
        		if(count==3)text=text+"...";
        		if(count==4)text=text+"....";
        		if(count==5)text=text+".....";
        		if(count==6)text=text+"......";
        		count++;
            	}else {
            		count=1;
                }
            $$("MSGtext").innerHTML ='<b>'+text+'</b>';
            } 
		function showMSGalert(){
			var ab = new MSGalert("idMSG");
			var lock = true;
			
			//锁定键盘
			function lockup(e){ e.preventDefault(); }
			//高亮层不锁定
			function lockout(e){ e.stopPropagation(); }

			ab.onShow = function(){
				if (lock) {
					$$E.addEvent( document, "keydown", lockup );
					$$E.addEvent( this.box, "keydown", lockout );
					OverLay.show();
				}
			};
			ab.onClose = function(){
				$$E.removeEvent( document, "keydown", lockup );
				$$E.removeEvent( this.box, "keydown", lockout );
				OverLay.close();
			};
			ab.center = true;
			$$("MSGtext").innerHTML ='<b class="widget widget-table">日志数据正在导入中，请稍候</b>';
			ab.show();
			//定时更新导入提示内容
            setInterval("insert()",500);
            $$("idMSGClose").onclick = function(){if(confirm("\n确认要取消导入提示吗？取消后不影响数据导入，请耐心等待数据导入完毕！ ")){ ab.close();}}
			//$$("importExcle").onclick = function(){}
			}			
		</script>
		<style>
.MSGalert {
	width: 360px;
	background: #FFFFFF;
	border:1px solid #9DBCEA;
	line-height: 30px;
	display: none;
	margin: 0;
}

.MSGalert dt {
	/**PADDING-RIGHT: 2px;  PADDING-LEFT: 2px; FONT-SIZE: 12px; FILTER: progid:DXImageTransform.Microsoft.Gradient(GradientType=0, StartColorStr=#FFFFFF, EndColorStr=#9DBCEA);  CURSOR: hand; PADDING-TOP: 2px; */
	PADDING-LEFT: 5px;
	PADDING-TOP: 2px;
	padding-bottom: 5px; 
	FONT-SIZE: 12px; 
	font-weight:bold; 
	background-image:url(image/MSGalert.png);
	height: 24px;
}

.MSGalert dd {
	padding: 20px;
	margin: 0;
}

.btn {
	PADDING-RIGHT: 2px;  PADDING-LEFT: 2px; FONT-SIZE: 12px; FILTER: progid:DXImageTransform.Microsoft.Gradient(GradientType=0, StartColorStr=#FFFFFF, EndColorStr=#9DBCEA);   PADDING-TOP: 2px;
}
</style>
</head>
	<body>
	
	<form name="myForm" id="myForm" action="systemLog_importSystemLogToExcel.do" method="post" enctype="multipart/form-data" onsubmit="return checkFile();">
		<ul class="breadcrumb">
			<li><i class="icon-home icon-2x"></i></li>
			<li>当前位置：${currentPosition}</li>
		</ul>
		<div class="widget widget-table">
			<div class="widget-content">
				<input align="top" name="excelFile" type="file" size="32" style="height:22px" id="excelFile" value="dd"/>&nbsp;
				
				<a class="btn btn-primary pull-center" id="importExcle" onclick="$(this).parents('form').submit();" href="javascript:;">导入数据 </a>
				<a class="btn btn-danger pull-center" href="systemLog_list.do">返 回 </a>
				
	    		<label id="importProcess" style="padding-left:580px;font-weight: bold;"></label>
	    		
		    	<table class="pn-ftable table-condensed" border="0" cellpadding="10"><tr><td><br/>成功导入excel日志数据规范：</td></tr>
		               <tr><td> &nbsp;&nbsp;&nbsp;&nbsp;导入的Excel表格格式严格为导出时Excel的排版格式：<br/><br/>
		               			&nbsp;&nbsp;&nbsp;&nbsp;第一行为按[日志ID、时间、日志类型、用户ID、日志内容、影响范围、备注]为顺序的表头。<br/>
		               			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;1、[日志ID、日志类型]为数字，且[日志类型]为0（登录登出），1（操作），2（异常错误）。<br/>
		               			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2、[日志ID、时间、日志类型、用户ID、日志内容]不为空。<br/>
		               			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;3、不符合1、2规范，则该行不导入。<br/>
		               			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;4、Excel只支持到2003版本。
		               </td>
		               </tr>
		               <tr><td> &nbsp;&nbsp;&nbsp;&nbsp;当导入Excel日志数据时，日志ID与时间不存在时为保存操作，否则系统默认为更新操作。若导入某条日志记录时系统
		               </td></tr>
		               <tr><td> &nbsp;&nbsp;&nbsp;&nbsp;中存在与该条记录的日志ID对应记录的时间相同的日志记录但日志ID（主键）不一致,系统对该日志记录不进行更新操作。
		               </td>
		               </tr>
		        </table>
				<dl id="idMSG" class="MSGalert" style="top:10%;left:5%;">
					<dt class="widget-content">导入提示&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<img id="idMSGClose" src="images/MSGalert_C1.png" onmouseout="this.src='images/MSGalert_C1.png'" onmousemove="this.src='images/MSGalert_C2.png'" />
					</dt>
					<dd id="MSGtext">
					</dd>
				</dl> 
			</div>
			<!-- /widget-content -->
		</div>
	</form>
	</body>
</html>