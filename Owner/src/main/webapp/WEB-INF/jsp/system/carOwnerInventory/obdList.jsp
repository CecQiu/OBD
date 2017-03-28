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
<title>库存管理 -OBD设备管理</title>
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

});
function forwardUrl(uri) {
	window.location.href = uri;
	
}

function test(){
	var url="${basePath}/admin/carOwnerInventory_test.do";
	window.location.href=url;
}

function myFormReset(){
	$("#stockState").val("");
	$("#obdSn").val("");
	$("#obdMSn").val("");
	$("#valid").val("1");
}


function obdImport(){
	window.location.href="${basePath}/admin/carOwnerInventory_addOBD.do";
}
</script>
</head>
<body>
	<form id="myForm" class="myForm" action="${basePath}/admin/carOwnerInventory_query.do" method="post">
	<ul class="breadcrumb">
		<li><i class="icon-home icon-2x"></i></li>
		<li>当前位置：库存管理  &gt; OBD设备</li>
	</ul>
	<div class="widget widget-table">
			<div class="widget-content">
				<table class="pn-ftable table-condensed" border="0" cellpadding="10">
					<tbody>
						<tr>
							<th>设备号:</th>
							<td class="pn-fcontent">
								 <input type="text" size="24" id="obdSn" name="obdSn" value="${obdSn}" />
							</td>
							<th>表面号：</th>
							<td class="pn-fcontent">
								 <input type="text" size="24" id="obdMSn" name="obdMSn" value="${obdMSn}" />
							</td>
						</tr>
						<tr>
							<th>设备在线状态:</th>
							<td class="pn-fcontent">
								<select id="stockState" name="stockState">
									<option value="">全部</option>
									<option value="01"  <c:if test="${stockState=='01'}">selected</c:if>>在线</option>
									<option value="00"  <c:if test="${stockState=='00'}">selected</c:if>>离线</option>
								</select>
							</td>
							<th>绑定状态:</th>
							<td class="pn-fcontent">
								<select id="valid" name="valid">
									<option value="">全部</option>
									<option value="1"  <c:if test="${valid=='1'}">selected</c:if>>激活1</option>
									<option value="0"  <c:if test="${valid=='0'}">selected</c:if>>解绑0</option>
								</select>	
							</td>
						</tr>
					</tbody>
				</table>
				<div class="widget-bottom">
					<center>
						<input class="btn btn-s-md btn-success" type="submit" value="查 询" />&nbsp;
						<input class="btn btn-danger pull-center" type="button" value="重置" onclick="myFormReset();"/>&nbsp;
						<input class="btn btn-danger pull-center" type="button" value="Excel导入" onclick="obdImport();"/>&nbsp;
					</center>
				</div>
			</div>
			<!-- /widget-content -->
		</div>
		<div class="widget-content">
			<table class="table table-hover">
			  <tr style="background-color: #c4e3f3;">
			  	<td>设备号</td><td>二维码</td><td>表面号</td><td>wifi</td><td>gps</td><td>分组</td><td>在线状态</td><td>激活状态</td><td>激活时间</td><td>解绑时间</td>
			  </tr>
			   <s:iterator value="obdList" status="st">
			  <tr>
			  	  <td id="obdSn">
			  	  	<s:property value="obdSn"></s:property>
			  	  </td>
			  	  <td>
			  	  	<s:property value="obdId"></s:property>
			  	  </td>
			  	    <td>
			  	  	<s:property value="obdMSn"></s:property>
			  	  </td>
			  	  <td>
			  	  	<s:property value="wifiState"></s:property>
			  	  </td>
			  	  <td>
			  	  	<s:property value="gpsState"></s:property>
			  	  </td>
			  	  <td>
			  	  	<s:property value="groupNum"></s:property>
			  	  </td>
			  	  <td>
			  	  	<c:if test="${stockState=='00'}">
			  	  	<span>设备下线</span>
			  	  	</c:if>
			  	  	<c:if test="${stockState=='01'}">
			  	  	<span style="color:red">设备上线</span>
			  	  	</c:if>
			  	  </td>	
			  	  <td>
			  	  	<s:property value="valid"></s:property>
			  	  </td>	
			  	  <td>
			  	  	<s:property value="startDate"></s:property>
			  	  </td>	
			  	  <td>
			  	  	<s:date name="updateTime" format="yyyy-MM-dd HH:mm:ss"/>
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
	</form>
</body>
</html>