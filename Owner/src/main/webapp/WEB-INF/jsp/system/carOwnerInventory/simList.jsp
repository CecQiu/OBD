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
<title>营销管理 -流量卡管理</title>
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

function myFormReset(){
	$("#obdSn").val("");
	$("#simId").val("");
	$("#starTime").val("");
	$("#endTime").val("");
}

function expor(){
	var obdSn=document.getElementById("obdSn").value;
	var simId=document.getElementById("simId").value;
	var starTime=document.getElementById("starTime").value;
	var endTime=document.getElementById("endTime").value;
	var url="${basePath}/admin/carOwnerInventory_simMsgExport.do?starTime="+starTime+"&endTime="+endTime+"&simStockInfo.obdSn="+obdSn+"&simStockInfo.simId="+simId;
	window.location.href=url;
}
</script>
</head>
<body>
	<form id="myForm" class="myForm" action="${basePath}/admin/carOwnerInventory_getSimInfo.do" method="post">
		<ul class="breadcrumb">
			<li><i class="icon-home icon-2x"></i></li>
			<li>当前位置：库存管理 &gt; 流量卡管理</li>
		</ul>
		<div class="widget widget-table">
			<div class="widget-content">
				<table class="pn-ftable table-condensed" border="0" cellpadding="10">
					<tbody>
						<tr>
							<th>二维码：</th>
							<td class="pn-fcontent"><input type="text" size="24"
								id="obdSn" name="simStockInfo.obdSn"
								value="${simStockInfo.obdSn}" /></td>
							<th>流量卡号：</th>
							<td class="pn-fcontent"><input type="text" size="24"
								id="simId" name="simStockInfo.simId"
								value="${simStockInfo.simId}" /></td>
						</tr>
						<tr>
							<th>开始时间</th>
							<td class="pn-fcontent">
								<input type="text" value="${starTime}" class="Wdate" readonly="readonly" name="starTime" id="starTime" onclick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss',maxDate:'%y-%M-%d %H:%m:%s'})"/>
							</td>
							<th>结束时间</th>
							<td class="pn-fcontent">
								<input type="text" value="${endTime}" class="Wdate" readonly="readonly" name="endTime" id="endTime"  onclick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss',maxDate:'%y-%M-%d %H:%m:%s'})" />
							</td>
						</tr>
					</tbody>
				</table>
				<div class="widget-bottom">
					<center>
						<input class="btn btn-s-md btn-success" type="submit" value="查 询" />&nbsp;
						<input class="btn btn-danger pull-center" type="button" value="重置" onclick="myFormReset();" />&nbsp;
						<button type="button" class="btn btn-primary" onclick="expor()">导出</button>	 
					</center>
				</div>
			</div>
			<!-- /widget-content -->
		</div>

		<div class="widget-content">
			<table class="table table-hover">
				<tr style="background-color: #c4e3f3;">
					<td>序号</td>
					<td>设备号</td>
					<td>流量卡号</td>
					<td>是否超流量</td>
					<td>当月已使用流量</td>
					<td>总流量</td>
					<td>创建时间</td>
				</tr>
				<s:iterator value="simList" status="st">
					<tr>
						<td class="license" id="stockId"><s:property value="stockId" />
						</td>
						<td class="obdSn" id="obdSn"><s:property value="obdSn" /></td>
						<td class="simNo"><s:property value="simId" /></td>
						<td class="flowFlag"><s:property value="flowFlag" /></td>
						<td class="flowUse">
						${(flowUse+tempFlowUse)} KB 
						=> 
						<fmt:formatNumber type="number" pattern="0.00" maxFractionDigits="2" value='${(flowUse+tempFlowUse)/1024}'>
						</fmt:formatNumber>
						MB 
						=>
						<fmt:formatNumber type="number" pattern="0.00" maxFractionDigits="2" value='${(flowUse+tempFlowUse)/1024/1024}'>
						</fmt:formatNumber>GB</td>
						<td class="totalFlow"><s:property value="totalFlow" /></td>
						<td class="createTime"><s:date name="createTime"
								format="yyyy-MM-dd HH:mm:ss" /></td>
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