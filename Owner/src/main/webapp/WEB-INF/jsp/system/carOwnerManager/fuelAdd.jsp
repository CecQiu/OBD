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
   var addTime=document.getElementById("addTime").value;
   var gasStation=document.getElementById("gasStation").value;
   var fee=document.getElementById("fee").value;
   var oilNum=document.getElementById("oilNum").value;
   var total=document.getElementById("total").value;
   var flag=true;
 //匹配到一个非数字字符，则返回false 
   var expr =/\D/i;
   if(addTime==null||addTime.replace(/(^s*)|(s*$)/g, "").length ==0){	
	   $('#error')[0].innerHTML = "<font color=red size=2>加油时间不能为空 !</font>";
	   flag=false;
   }else{
	   if(gasStation==null||gasStation.replace(/(^s*)|(s*$)/g, "").length ==0){	
		   $('#error')[0].innerHTML = "<font color=red size=2>加油站不能为空 !</font>";
		   flag=false;
	   }else{
		   if(fee==null||fee.replace(/(^s*)|(s*$)/g, "").length ==0){	
			   $('#error')[0].innerHTML = "<font color=red size=2>单价不能为空 !</font>";
			   flag=false;
		   }else{
			   if(oilNum==null||oilNum.replace(/(^s*)|(s*$)/g, "").length ==0){	
				   $('#error')[0].innerHTML = "<font color=red size=2>加油量不能为空 !</font>";
				   flag=false;
			   }else{
				   if(total==null||total.replace(/(^s*)|(s*$)/g, "").length ==0){	
					   $('#error')[0].innerHTML = "<font color=red size=2>金额不能为空 !</font>";
					   flag=false;
				   }else{
					   if(expr.test(fee)){	
						   $('#error')[0].innerHTML = "<font color=red size=2>单价不能为非数字!</font>";
						   flag=false;
					   }else{
						   if(expr.test(oilNum)){	
							   $('#error')[0].innerHTML = "<font color=red size=2>加油量不能为非数字!</font>";
							   flag=false;
						   }else{
							   if(expr.test(total)){	
								   $('#error')[0].innerHTML = "<font color=red size=2>金额不能为非数字!</font>";
								   flag=false;
							   }
						   }
					   }
				   }
			   }
		   }
	   }
   }
    
  if(flag==true){
	  alert("新增成功 ！");
	  $('#fuelAdd').submit();
	 window.parent.hideIframe();
  }
  
}
function reback(){
	var url="${basePath}/admin/carOwnerManager_fuelManagement.do";
	window.location.href=url;
	window.parent.hideIframe();
}
</script>
</head>
<body>
	<form id="fuelAdd" method="post" action="${basePath}/admin/carOwnerManager_fuelAdd.do">
		<ul class="breadcrumb">
			<li><i class="icon-home icon-2x"></i></li>
			<li>当前位置：${currentPosition}&nbsp;>&nbsp;新增加油信息</li>
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
							<td>加油时间:&nbsp;&nbsp;<input id="addTime" name="addTime" type="text"  readonly="readonly" style="width: 120px;" class="Wdate" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'%y-%M-%d'})" /></td>
						</tr>
						<tr>
							<td>加油站名:&nbsp;&nbsp;&nbsp;<input id="gasStation" name="gasStation" type="text" style="width: 120px;" /></td>
							<td>加油地址:&nbsp;&nbsp;<input id="gasStationAdd" name="gasStationAdd" type="text"  style="width: 120px;"/></td>
						</tr>
						<tr>
							<td>油料标号:&nbsp;&nbsp;
								<select style="width: 120px;" name="oilType">
									<option id="oilType">#95</option>
									<option id="oilType">#92</option>
									<option id="oilType">#98</option>
									<option id="oilType">柴油</option>
								</select>
							</td>
							<td>单价(元/L):<input id="fee" name="fee" type="text"  style="width: 120px;"/></td>
						</tr>
						<tr>
							<td>加油量(L): &nbsp;&nbsp;<input id="oilNum" name="oilNum" type="text" style="width: 120px;" /></td>
							<td>金额(元):&nbsp;&nbsp;&nbsp;<input id="total" name="total" type="text"  style="width: 120px;"/></td>
						</tr>
						<tr>
							<td>加油者姓名:<input id="userName" name="userName" type="text" style="width: 120px;" /></td>
							<span id="error"></span>
						</tr>
					</tbody>
				</table>
				<div class="widget-bottom"> 
					<center>
						<input id="fuelAdd" class="btn btn-primary pull-center" type="button" value="保 存" onclick="check()" />&nbsp;
						<input class="btn btn-primary pull-center" type="button" value="返回" onclick="reback()"/>&nbsp;
					</center>
				</div>
			</div>
			<!-- /widget-content -->
		</div>

	</form>
</body>
</html>