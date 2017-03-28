<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.util.ArrayList"%>
<%@page import="java.net.URLDecoder"%>
<%@ page contentType="text/html; charset=UTF-8" language="java"
	pageEncoding="UTF-8" isErrorPage="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
<title>营销管理 -业务统计</title>
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
<script language="javascript" src="${basePath}/js/curentTime.js"></script>
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
	var starTime=document.getElementById("starTime").value;
	var endTime=document.getElementById("endTime").value;
	var url="${basePath}/admin/carOwnerMarketing_obdExport.do?starTime="+starTime+"&endTime="+endTime;
	window.location.href=url;
}
$(document).ready(function() {
 //   $('input[name="endTime"]').val(Data());
});

</script>
</head>
<body>
	<form id="myForm" class="myForm" action="${basePath}/admin/carOwnerMarketing_businessStatistics.do" method="post">
	<ul class="breadcrumb">
		<li><i class="icon-home icon-2x"></i></li>
		<li>当前位置：营销管理  &gt; 业务统计</li>
	</ul>
	<h3><c:if test="${shopName != null && shopName != ''}">欢迎${shopName }</c:if></h3>
		<div class="widget">
		<div class="widget-content" style="line-height: 20px">
			日期&nbsp;&nbsp;<input type="text" value="${starTime}" class="Wdate" readonly="readonly" name="starTime" id="starTime" onclick="WdatePicker({maxDate:'%y-%M-%d'})"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			到&nbsp;&nbsp;<input type="text" value="${endTime}" class="Wdate" readonly="readonly" name="endTime" id="endTime"  onclick="WdatePicker({maxDate:'%y-%M-%d'})" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			&nbsp;&nbsp;&nbsp;&nbsp;
			<button type="submit" class="btn btn-primary">查询</button>
			<button type="button" class="btn btn-primary" onclick="expor()">导出</button>			
			<br /><br />
			
			<p>
			OBD设备总数:&nbsp;&nbsp;<span style="color:red">			
			<s:property value="#attr.businessStatistList[0].obdNumber"/>
			</span> &nbsp;&nbsp;&nbsp;&nbsp;
			车主总数:&nbsp;&nbsp; <span style="color:red"><s:property value="#attr.businessStatistList[0].userNumber"/></span>
			</p>
			 
		</div>
		<div class="widget-content">
			<table class="table table-hover">
			  <tr style="background-color: #c4e3f3;">
			  	<td>日期</td><td>OBD设备总数</td><td>新激活设备</td><td>活跃设备</td><td>已绑定设备</td>
			  </tr>
			  <s:iterator value="businessStatistList" status="st">
			  <tr>
			  	  <td width="10%" height="26" align="center"> 
					<s:property value="date"></s:property>
				  </td> 			 
			  	  <td class="obdNumber" id="obdNumber">
			  	  	<s:property value="obdNumber"></s:property>	  	
			  	  </td>
			  	  <td class="obdNew" id="obdNew">
			  	  	<s:property value="obdNew"></s:property>
			  	  </td>
			  	  <td class="obdActive">
			  	  	<s:property value="obdActive"></s:property>
			  	  </td>
			  	    <td class="obdBind">
			  	  	<s:property value="obdBind"></s:property>
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