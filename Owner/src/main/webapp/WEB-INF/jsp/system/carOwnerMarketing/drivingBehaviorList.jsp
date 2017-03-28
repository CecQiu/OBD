<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.util.ArrayList"%>
<%@ page contentType="text/html; charset=UTF-8" language="java"
	pageEncoding="UTF-8" isErrorPage="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
<title></title>
<link rel="stylesheet" type="text/css"
	href="${basePath}/css/bootstrap.min.css" />
<link rel="stylesheet" type="text/css" href="${basePath}/css/style.css" />
<link id="css_main" rel="stylesheet" type="text/css" href="${basePath}/theme/${session.theme}/main.css" />
<style type="text/css">
h3 {
	margin-bottom:20px;
	text-align: center;
}

.page-content-wrapper {
	width:75%;
	margin:0 auto 35px;
	height: 50px;
	background: #f2f2f2;
	border-radius: 10px;
	text-align:center;
	color:#626262;
	line-height:50px;
	border:1px solid #cacaca;
}
.midnav {
	width:80%;
    text-align: center;
    margin:0 auto 30px;
}
.midnav li {
	min-width:22%;
    margin: 17px 10px 0px;
    position: relative;
    display: inline-block;
    text-align: center;
}
.midnav li a {
    padding: 10px 9px 5px;
    display: block;
    font-weight: bold;
    outline:none;
    white-space: nowrap;
    color: #626262;
    border-radius: 3px;
    text-shadow: 0px 1px #FFF;
    border: 1px solid #D5D5D5;
    background: -moz-linear-gradient(center top , #FFF 0%, #F4F4F4 100%) repeat scroll 0% 0% transparent;
    box-shadow: 0px 1px 2px #EEE;
    margin-bottom:20px;
}
.midnav li a.active{
	border: 1px solid #999;
}
.midnav li img {
	padding-right: 10px;
}
</style>
<script language="javascript" src="${basePath}/js/jquery.min.js"></script>
<script language="javascript" src="${basePath}/js/plugins/My97DatePicker/WdatePicker.js"></script>
<script language="javascript">
$(function(){
	/* $(".midnav a").click(function(){
		$(".midnav .active").attr("class","btn");
		$(this).addClass("active");
		$(".page-content-wrapper").html($(this).attr("info"));
	}); */
});
function forwardUrl(uri) {
	window.location.href = uri;
	
}
function expor(){
	var obdSn=document.getElementById("obdSn").value;
	var mobileNumber=document.getElementById("mobileNumber").value;
	var license=document.getElementById("license").value;
	var starTime=document.getElementById("starTime").value;
	var endTime=document.getElementById("endTime").value;
	var url="${basePath}/admin/carOwnerMarketing_drivingExport.do?starTime="+starTime+"&endTime="+endTime+"&obdSn="+obdSn+"&mobileNumber="+mobileNumber+"&license="+encodeURI(encodeURI(license));
	window.location.href=url;
}
</script>
</head>
<body>
	<form id="myForm" class="myForm" action="${basePath}/admin/carOwnerMarketing_drivingBehavior.do" method="post">
	<ul class="breadcrumb">
		<li><i class="icon-home icon-2x"></i></li>
		<li>当前位置：营销管理  &gt; 驾驶行为分析</li>
	</ul>
	<h3><c:if test="${shopName != null && shopName != ''}">欢迎${shopName }</c:if></h3>
		<div class="widget">
		<div class="widget-content" style="line-height: 20px">
			设备编号：<input type="text" id="obdSn" value="${obdSn}" name="obdSn"/>
			手机号码：<input type="text" id="mobileNumber" value="${mobileNumber}" name="mobileNumber"/>
			车牌号码：<input type="text" id="license" value="${license}" name="license"/>
			<br /><br />&nbsp;&nbsp;&nbsp;&nbsp;
			日期&nbsp;&nbsp;<input type="text" value="${starTime}" class="Wdate" readonly="readonly" name="starTime" id="starTime" onclick="WdatePicker({maxDate:'%y-%M-%d'})"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			&nbsp;&nbsp;&nbsp;&nbsp;到&nbsp;&nbsp;<input type="text" value="${endTime}" class="Wdate" readonly="readonly" name="endTime" id="endTime"  onclick="WdatePicker({maxDate:'%y-%M-%d'})" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			&nbsp;&nbsp;&nbsp;&nbsp;<button type="submit" class="btn btn-primary">查询</button>
			<button type="button" class="btn btn-primary" onclick="expor()">导出</button>	 
		</div>
		<div class="widget-content">
			<table class="table table-hover">
			  <tr style="background-color: #c4e3f3;">
			  	<td>车牌号码</td><td>设备编号</td><td>姓名</td><td>手机号码</td><td>行为发生日期</td><td>超速(次)</td><td>急减速(次)</td>
			  	<td>急加速(次)</td><td>离转速(次)</td><td>急转弯(次)</td><td>转速不匹配(次)</td><td>怠速(次)</td>
			  </tr>
			   <s:iterator value="drivingBehaviorList" status="st">
			  <tr>
			  	  <td class="license" id="license">
			  	  	<s:property value="license"></s:property>	  	
			  	  </td>
			  	  <td width="10%" height="26" align="center"> 
					<s:property value="obdSn"/>
				  </td> 			 
			  	  <td class="name" id="name">
			  	  	<s:property value="name"></s:property>
			  	  </td>
			  	  <td class="mobileNumber">
			  	  	<s:property value="mobileNumber"></s:property>
			  	  </td>
			  	    <td class="driveDate">
			  	  	<s:property value="driveDate"></s:property>
			  	  </td>
			  	   </td>
			  	    <td class="tmsSpeeding">
			  	  	<s:property value="tmsSpeeding"></s:property>
			  	  </td>	
			  	   </td>
			  	    <td class="tmsRapDec">
			  	  	<s:property value="tmsRapDec"></s:property>
			  	  </td>	
			  	   </td>
			  	    <td class="tmsRapAcc">
			  	  	<s:property value="tmsRapAcc"></s:property>
			  	  </td>	
			  	   </td>
			  	    <td class="highSpeed">
			  	  	<s:property value="highSpeed"></s:property>
			  	  </td>	
			  	   </td>
			  	    <td class="tmsSharpTurn">
			  	  	<s:property value="tmsSharpTurn"></s:property>
			  	  </td>	
			  	   </td>
			  	    <td class="notMatch">
			  	  	<s:property value="notMatch"></s:property>
			  	  </td>	
			  	  </td>
			  	    <td class="longLowSpeed">
			  	  	<s:property value="longLowSpeed"></s:property>
			  	  </td>				  
			  </tr>
			</s:iterator>
			</table>
			
			<div class="widget-bottom">
				<jsp:include page="../../include/pager.jsp" />
			</div>
			<div class="widget-bottom">
				&nbsp;<span class="pull-right">版权所有：广州华工信息软件有限公司</span>
			</div>
		</div>
	</div>
	</form>
</body>
</html>