<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
    	<jsp:include page="../../include/common.jsp" />
		<script type="text/javascript">
		if("${message}"=="delSuccess"){
			alert(" 删除日志成功 ！  ");
		}
		if("${message}"=="overMaxCount"){
			alert("导出的日志数据超过50000条，请缩短时间范围！");
		}
		
		function check() {
		    /*if(get("startTime").value == null || get("startTime").value == "" || get("endTime").value == null || get("endTime").value == "" ) {
		      	alert("查询时间不能为空");
		      	return false;
		    }*/
		    if(get("startTime").value != "" && get("endTime").value != "" ) {
			    if(get("startTime").value > get("endTime").value) {
			      	alert("开始时间必须早于或等于结束时间");
			    	return false;
			    }
			}    
		    else {
		      	return true;
		    }  	
		}
         
		//导入Excel日志
		function importSystemLog(){
			  window.location.href = "systemLog_importSystemLog.do";
			}

		//导出日志到Excel
		function logToExcel(){
			var startTime=get("data_startTime").value;
			var endTime=get("data_endTime").value;
			if(get("data_startTime").value == "" && get("data_endTime").value == "" ) {
				alert("请输入导出日志时间范围");
				get("data_startTime").focus();
				}
			else if(get("data_startTime").value != "" || get("data_endTime").value != "" ) {
			    if(get("data_startTime").value != "" && get("data_endTime").value != ""){
			    	if(get("data_startTime").value > get("data_endTime").value) {
				      	alert("开始时间必须早于或等于结束时间");
				      	get("data_startTime").focus();
				    	return false;
				    }
				    else {
					    //alert(startTime+"--"+endTime);
				    	window.location.href = "systemLog_logToExcel.do?startTime="+startTime+"&endTime="+endTime;
				      	return true;
				    }
				    }		    
			    else {
				    //alert(startTime+"--"+endTime);
			        window.location.href = "systemLog_logToExcel.do?startTime="+startTime+"&endTime="+endTime;
			      	return true;
			    }
			}    
  	
			}
		
		function reset1(){
			get("systemlog.logData").value = "";
			get("admin.username").value = "";
			get("systemlog.logType").value = "";
			get("startTime").value = "";
			get("endTime").value = "";
			get("systemlog.coverage").value = "";
			get("systemlog.coverage").value = "";
			get("systemlog.remark").value = "";
		}
		function delSystemLog(){
			window.location.href = "systemLog_toDelSystemLog.do";
		}
		</script>
	</head>
	<body>
		<form name="myForm" id="myForm" method="post" action="systemLog_list.do" onsubmit="return check();">
		<ul class="breadcrumb">
			<li><i class="icon-home icon-2x"></i></li>
			<li>当前位置：${currentPosition}</li>
		</ul>
		<div class="widget widget-table">
			<div class="widget-content">
				<table class="pn-ftable table-condensed" border="0" cellpadding="10">
					<tbody>
						<tr>
							<th>日志内容：</th>
							<td class="pn-fcontent">
								<input type="text" name="systemlog.logData" value="${systemlog.logData}" />
							</td>
							<th>用户名：</th>
							<td class="pn-fcontent">
								 <input type="text" name="admin.username" value="${admin.username}" />
							</td>
						</tr>
						<tr>
							<th>日志类型：</th>
							<td class="pn-fcontent">
								<select name="systemlog.logType" style="width:150px">
							  		<option value="" <c:if test="${systemlog.logType==null}">selected</c:if>>所有类型</option>
							  		<option value="0" <c:if test="${systemlog.logType==0}">selected</c:if>>登录登出</option>
									<option value="1" <c:if test="${systemlog.logType==1}">selected</c:if>>用户操作</option>
									<option value="2" <c:if test="${systemlog.logType==2}">selected</c:if>>异常错误</option>
							  	</select>
						 	</td>
						 	<th>备注：</th>
							<td class="pn-fcontent">
								<input type="text" name="systemlog.remark" value="${systemlog.remark}" />
							</td>
						</tr>
						<tr>
							<th>查询时间范围：</th>
							<td class="pn-fcontent">
								<input type="text" name="startTime" readonly="readonly" value="${startTime }" class="Wdate" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"/> - <input type="text" name="endTime" readonly="readonly" value="${endTime }" class="Wdate" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"/>
							</td>
							<th>影响范围：</th>
							<td class="pn-fcontent">
								 <input type="text" name="systemlog.coverage" value="${systemlog.coverage}" />
							</td>
						</tr>
					</tbody>
				</table>
				<div class="widget-bottom">
				<center>
					<input class="btn btn-primary pull-center" type="submit" value="查 询" />&nbsp;
					<input class="btn btn-success pull-center" type="button" value="重 置" onclick="reset1()"/>&nbsp;
					<input class="btn btn-danger pull-center" type="button" value="删 除" onclick="delSystemLog()"/>	
				</center>
				</div>
			</div>
			<!-- /widget-content -->
		</div>
		<div class="separator line"></div>
		<div class="widget widget-table">
			<div class="widget-header">
				<i class="icon-th-list"></i>
				<h5>系统日志列表</h5>
			</div>
			<!-- /widget-header -->
			<div class="widget-content widget-list">
				<table class="table table-striped table-bordered table-condensed table-hover sortable">
					<thead>
						<tr>
							<th>时间</th>
							<th>日志类型</th>
							<th>用户名</th>
							<th>日志内容</th>
							<th>影响范围</th>
							<th>备注</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${list}" var="item" varStatus="status">
							<tr>
								<td><fmt:formatDate value="${item.logTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
								<td>${item.logTypeString}</td>
								<td><c:forEach items="${adminList}" var="adminItem"><c:if test="${item.operatorID==adminItem.id}">${adminItem.username}</c:if></c:forEach></td>
								<td title="${item.logData}">${fn:length(item.logData) >20 ?fn:substring(item.logData ,0,20) : item.logData} ${fn:length(item.logData ) >20 ? '...':''}</td>
								<td title="${item.coverage}">${fn:length(item.coverage) >15 ?fn:substring(item.coverage ,0,15) : item.coverage} ${fn:length(item.coverage ) >15 ? '...':''}</td>
								<td title="${item.remark}">${fn:length(item.remark) >15 ?fn:substring(item.remark ,0,15) : item.remark} ${fn:length(item.remark ) >15 ? '...':''}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				<div class="widget-bottom">
					<jsp:include page="../../include/pager.jsp" />
				</div>
			</div>
			<div class=pn-sp>
					<input type="button" name="" style="padding-left: 5px;padding-right: 5px;padding-bottom: 1px;padding-top: 2px;"  value="导入Excel数据" onclick="importSystemLog();" /> 	
				    <input type="button" name="" style="padding-left: 5px;padding-right: 5px;padding-bottom: 1px;padding-top: 2px;" value="将日志导出Excel" onclick="logToExcel();"/> 
				    <td class="pn-flabel pn-flabel-h" style="white-space:nowrap;padding-left:5px;padding-right:5px;">导出日志时间范围：</td>
					<td class="pn-fcontent">
						 <input type="text" name="data_startTime" value="<fmt:formatDate value="${data_startTime }" pattern="yyyy-MM-dd HH:mm:ss" />" class="Wdate" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})""/> - <input type="text" name="data_endTime" value="<fmt:formatDate value="${data_endTime }" pattern="yyyy-MM-dd HH:mm:ss" />" class="Wdate" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})""/>
					</td>
				<div class="clear"></div>
			    </div>
			<!-- /widget-content -->
		</div>
	</form>
	</body>
</html>