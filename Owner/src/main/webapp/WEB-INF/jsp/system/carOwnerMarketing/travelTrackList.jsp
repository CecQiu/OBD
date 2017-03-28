<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.util.ArrayList"%>
<%@ page contentType="text/html; charset=UTF-8" language="java"
	pageEncoding="UTF-8" isErrorPage="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<title>营销管理 -行程记录统计</title>
<link rel="stylesheet" type="text/css"
	href="${basePath}/css/bootstrap.min.css" />
<link rel="stylesheet" type="text/css" href="${basePath}/css/style.css" />
<link id="css_main" rel="stylesheet" type="text/css"
	href="${basePath}/theme/${session.theme}/main.css" />
<style type="text/css">
h3 {
	margin-bottom: 20px;
	text-align: center;
}

.page-content-wrapper {
	width: 75%;
	margin: 0 auto 35px;
	height: 50px;
	background: #f2f2f2;
	border-radius: 10px;
	text-align: center;
	color: #626262;
	line-height: 50px;
	border: 1px solid #cacaca;
}

.midnav {
	width: 80%;
	text-align: center;
	margin: 0 auto 30px;
}

.midnav li {
	min-width: 22%;
	margin: 17px 10px 0px;
	position: relative;
	display: inline-block;
	text-align: center;
}

.midnav li a {
	padding: 10px 9px 5px;
	display: block;
	font-weight: bold;
	outline: none;
	white-space: nowrap;
	color: #626262;
	border-radius: 3px;
	text-shadow: 0px 1px #FFF;
	border: 1px solid #D5D5D5;
	background: -moz-linear-gradient(center top, #FFF 0%, #F4F4F4 100%)
		repeat scroll 0% 0% transparent;
	box-shadow: 0px 1px 2px #EEE;
	margin-bottom: 20px;
}

.midnav li a.active {
	border: 1px solid #999;
}

.midnav li img {
	padding-right: 10px;
}
</style>
<script language="javascript" src="${basePath}/js/curentTime.js"></script>
<script language="javascript" src="${basePath}/js/jquery.min.js"></script>
<script language="javascript"
	src="${basePath}/js/plugins/My97DatePicker/WdatePicker.js"></script>
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

function tlReset() {
	$("#obdSn").val("");
	$("#obdMSn").val("");
	//$("#mobileNumber").val("");
	//$("#license").val("");
}

$(document).ready(function() {
	//$('input[name="endTime"]').val(Data());
});
function expor(){
	var obdSn=$("#obdSn").val();
	var obdMSn=$("#obdMSn").val();
	//var mobileNumber=$("#mobileNumber").value;
	//var license=#("#license").value;
	var starTime=$("#starTime").val();
	var endTime=$("#endTime").val();
	if(obdSn=='' && obdMSn==''){
		alert("设备号和表面号不能同时为空.");
		return;
	}
	//var url="${basePath}/admin/carOwnerMarketing_trackExport.do?starTime="+starTime+"&endTime="+endTime+"&obdSn="+obdSn+"&obdMSn="+obdMSn+"&mobileNumber="+mobileNumber+"&license="+encodeURI(encodeURI(license));
	var url="${basePath}/admin/carOwnerMarketing_trackExport.do?starTime="+starTime+"&endTime="+endTime+"&obdSn="+obdSn+"&obdMSn="+obdMSn;
	window.location.href=url;
}
</script>
</head>
<body>
	<form id="myForm" class="myForm"
		action="${basePath}/admin/carOwnerMarketing_travelTrack.do"
		method="post">
		<ul class="breadcrumb">
			<li><i class="icon-home icon-2x"></i></li>
			<li>当前位置：车辆监控 &gt; 行程记录统计</li>
		</ul>
		
		<div class="widget">
			<div class="widget-content" style="line-height: 20px">
				<table class="pn-ftable table-condensed" border="0" cellpadding="10">
					<tbody>
						<tr>
							<th>设备号</th>
							<td class="pn-fcontent"><input type="text" id="obdSn" name="obdSn" value="${obdSn}" /></td>
							<th>表面号</th>
							<td class="pn-fcontent"><input type="text" id="obdMSn" name="obdMSn" value="${obdMSn}" /></td>
						</tr>
						<tr>
							<th>导入开始时间</th>
							<td class="pn-fcontent">
								<input type="text" class="Wdate" readonly="readonly" value="${starTime}" name="starTime" id="starTime" onclick="WdatePicker({maxDate:'%y-%M-%d'})"/>
							</td>
							<th>导入结束时间</th>
							<td class="pn-fcontent">
								<input type="text" class="Wdate" readonly="readonly" value="${endTime}" name="endTime" id="endTime"  onclick="WdatePicker({maxDate:'%y-%M-%d'})" />
							</td>
						</tr>

					</tbody>
				</table>
				<div class="widget-bottom">
					<center>
						<input class="btn btn-s-md btn-success" type="submit" value="查询" />&nbsp;
						<input class="btn pull-center  btn-info" type="button" value="重置"
							onclick="tlReset();" />&nbsp; <input
							class="btn btn-primary pull-center" type="button" value="导出"
							onclick="expor();" />&nbsp;
					</center>
				</div>
			</div>
			<div class="widget-content">
				<table class="table table-hover">
					<tr style="background-color: #c4e3f3;">
						<td>设备编号</td>
						<td>里程</td>
						<td>日期</td>
					</tr>
					<s:iterator value="dataTrack" status="st">
						<tr>
							<td width="10%" height="26" align="center"><s:property value="obdSn" /></td>
							<td class="distance"><fmt:formatNumber
									value="${distance/100}" pattern="##.##" minFractionDigits="2"></fmt:formatNumber>
							</td>
							<td class="travelEnd"><a
								href="${basePath}/admin/carOwnerMarketing_trackData.do?obdSn=<s:property value="obdSn"/>&insesrtTime=<s:date name="date1" format="yyyy-MM-dd"/>">
									<s:date name="date1" format="yyyy-MM-dd" /> <!--<s:date name="insesrtTime"  format="yyyy-MM-dd HH:mm:ss"/>-->
							</a></td>
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