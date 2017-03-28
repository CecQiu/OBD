<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
<title></title>
<jsp:include page="../../include/common.jsp" />
<script language="javascript">
function check(){
// 利用正则表达式对输入数据匹配
   var drivingOutTime=document.getElementById("drivingOutTime").value;
   var userName=document.getElementById("userName").value;
   var drivingOutAddress=document.getElementById("drivingOutAddress").value;
   var flag=true;
   if(drivingOutTime==null||drivingOutTime.replace(/(^s*)|(s*$)/g, "").length ==0){	
	   $('#error')[0].innerHTML = "<font color=red size=2>出车时间不能为空 !</font>";
	   flag=false;
   }
   if(userName==null||userName.replace(/(^s*)|(s*$)/g, "").length ==0){	
	   $('#error')[0].innerHTML = "<font color=red size=2>司机姓名不能为空  !</font>";
	   flag=false;
   }
   if(drivingOutAddress==null||drivingOutAddress.replace(/(^s*)|(s*$)/g, "").length ==0){	
	   $('#error')[0].innerHTML = "<font color=red size=2>出车地点不能为空  !</font>";
	   flag=false;
   }
    
  if(flag==true){
	  alert("新增成功 ！");
	  $('#addBusManagement').submit();
	 window.parent.hideIframe();
  }
  
}
function reback(){
	var url="admin/carOwnerManager_busManagement.do";
	window.location.href=url;
	window.parent.hideIframe();
}
</script>
</head>
<body>
	<form id="addBusManagement" method="post" action="admin/carOwnerManager_addBusManagement.do">
		<ul class="breadcrumb">
			<li><i class="icon-home icon-2x"></i></li>
			<li>当前位置：${currentPosition}&nbsp;>&nbsp;新增出车信息</li>
		</ul>
		<div class="widget widget-edit">
			<div class="widget-content">
				<table  class="table table-hover"  align="center">
					<tbody>
						<tr>
							<td>车牌号码:&nbsp;&nbsp;
							<select style="width: 120px;" name="license">
							<s:iterator value="licenseList" var="st">
								<option id="license"><s:property value="st"/></option>							
							</s:iterator>
							</select>
							</td>
							<td>出车时间:&nbsp;&nbsp;<input id="drivingOutTime" name="drivingOutTime" type="text"  readonly="readonly" style="width: 120px;" class="Wdate" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'%y-%M-%d'})" /></td>
						</tr>
						<tr>
							<td>司机姓名:&nbsp;&nbsp;&nbsp;<input id="userName" name="userName" type="text" style="width: 120px;" /></td>
							<td>联系电话:&nbsp;&nbsp;<input id="mobileNumber" name="mobileNumber" type="text"  style="width: 120px;"/></td>
						</tr>
						<tr>
							<td>出车地点: &nbsp;&nbsp;<input id="drivingOutAddress" name="drivingOutAddress" type="text" style="width: 120px;" /></td>
							<td>目的地:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input id="destination" name="destination" type="text"  style="width: 120px;"/></td>
						</tr>
						<tr>
							<span id="error"></span>
						</tr>
					</tbody>
				</table>
				<div class="widget-bottom"> 
					<center>
						<input id="addBusManagement" class="btn btn-primary pull-center" type="button" value="保 存" onclick="check()" />&nbsp;
						<input class="btn btn-primary pull-center" type="button" value="返回" onclick="reback()"/>&nbsp;
					</center>
				</div>
			</div>
			<!-- /widget-content -->
		</div>

	</form>
</body>
</html>