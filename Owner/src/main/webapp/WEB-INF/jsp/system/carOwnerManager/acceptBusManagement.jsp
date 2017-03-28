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
   var offRunningTime=document.getElementById("offRunningTime").value;
   var offRunningAddress=document.getElementById("offRunningAddress").value;
   var flag=true;
   if(offRunningTime==null||offRunningTime.replace(/(^s*)|(s*$)/g, "").length ==0){	
	   $('#error')[0].innerHTML = "<font color=red size=2>收车时间不能为空 !</font>";
	   flag=false;
   }
   if(offRunningAddress==null||offRunningAddress.replace(/(^s*)|(s*$)/g, "").length ==0){	
	   $('#error')[0].innerHTML = "<font color=red size=2>收车地点不能为空  !</font>";
	   flag=false;
   } 
  if(flag==true){
	  alert("收车成功 ！");
	  $('#acceptBusManagement').submit();
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
	<form id="acceptBusManagement" method="post" action="admin/carOwnerManager_acceptBusManagement.do">
		<ul class="breadcrumb">
			<li><i class="icon-home icon-2x"></i></li>
			<li>当前位置：${currentPosition}&nbsp;>&nbsp;新增出车信息</li>
		</ul>
		<div class="widget widget-edit">
			<div class="widget-content">
			<br /><br />
				<table  class="table table-hover"  align="center">
					<tbody>
					
						<tr>
							<td >车牌号码:&nbsp;&nbsp;<input type="text"  readonly="readonly" style="width: 120px;" value="${license }"/></td>
							<td>收车时间:&nbsp;&nbsp;<input id="offRunningTime" name="offRunningTime" type="text"  readonly="readonly" style="width: 120px;"  class="Wdate" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'%y-%M-%d'})" /></td>
						</tr>
						<tr>
							<td>收车地点:&nbsp;&nbsp;<input id="offRunningAddress" name="offRunningAddress" type="text" style="width: 120px;" /></td>
							<td>备&nbsp;&nbsp;&nbsp;&nbsp;注:&nbsp;&nbsp;&nbsp;&nbsp;<input id="comment" name="comment" type="text"  style="width: 120px;"/></td>
						</tr>
						<tr>
						<input type="hidden"  name="carDispatchId" id="carDispatchId" value="${carDispatchId }"/>
							<span id="error"></span>
						</tr>
					</tbody>
				</table>
				<div class="widget-bottom"> 
					<center>
						<input id="acceptBusManagement" class="btn btn-primary pull-center" type="button" value="保 存" onclick="check()" />&nbsp;
						<input class="btn btn-primary pull-center" type="button" value="返回" onclick="reback()"/>&nbsp;
					</center>
				</div>
			</div>
			<!-- /widget-content -->
		</div>

	</form>
</body>
</html>