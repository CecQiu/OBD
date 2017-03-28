<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8" language="java"
	pageEncoding="UTF-8" isErrorPage="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
<title>车主管理 - 车主查询功能</title>
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
function query(type){
	var obdSN=$("#obdSn").val().trim();
	if(obdSN == "" || obdSN == undefined || obdSN == null){
		alert("设备号不能为空");
		return false;
	}
	var hrefStr = '${basePath}/admin/redisDataControlAction.do';
	var hrefString = hrefStr + "?obdSn="+obdSN + "&operType="+type;
	window.location.href = hrefString;
}
function resetValue() {
	$("#obdSn").val("");
	$("#connectTime").val("");
}

</script>
</head>
<body>
<form id="myForm" class="myForm" action="${basePath}/testOBD/redisDataControlAction.do" method="post">
	<ul class="breadcrumb">
		<li><i class="icon-home icon-2x"></i></li>
		<li>当前位置：监控管理  &gt; 操作redis数据</li>
	</ul>
	<h3><c:if test="${shopName != null && shopName != ''}">欢迎${shopName }</c:if></h3>
	<div class="widget">
		<div class="widget-content" style="line-height: 20px">
			设备号：<input type="text" name="obdSn" id="obdSn" value="${obdSn }"/>
			<button type="button" class="btn btn-success" onclick="resetValue();">重 置</button>
		</div>
		<div class="widget-content" style="line-height: 15px;font-size: 15px">
			结果：${result }
		</div>
		<div class="widget-content">
			<table class="table table-hover">
			  <tr style="background-color: #c4e3f3;">
			  	<td width="100px">类目</td>
			  	<td width="140px">操作</td>
			  </tr>
			  
			  <tr>
			  	<td>设备TTL时间</td>
			  	<td>
			  	<input type="hidden" value="0" name="operType"/>
			  	<button type="button" class="btn-primary" style="margin-left:20px;margin-right: 30px;" onclick="query(0);">查询</button>&nbsp;
			  	<button type="button" class="btn-danger">删除</button>
			  	</td>
			  </tr>
			  <tr>
			  	<td>疲劳驾驶时间参数</td>
			  	<td>
			  	<button type="button" class="btn-primary" style="margin-left:20px;margin-right: 30px;" onclick="query(1);">查询</button>&nbsp;
			  	<button type="button" class="btn-danger">删除</button>
			  	</td>
			  </tr>
			  <tr>
			  	<td>疲劳驾驶状态数据</td>
			  	<td>
			  	<button type="button" class="btn-primary" style="margin-left:20px;margin-right: 30px;" onclick="query(2);">查询</button>&nbsp;
			  	<button type="button" class="btn-danger">删除</button>
			  	</td>
			  </tr>
			  <tr>
			  	<td>告警推送状态数据</td>
			  	<td>
			  	<button type="button" class="btn-primary" style="margin-left:20px;margin-right: 30px;" onclick="query(3);">查询</button>&nbsp;
			  	<button type="button" class="btn-danger">删除</button>
			  	</td>
			  </tr>
			  <tr>
			  	<td>上次上电号数据</td>
			  	<td>
			  	<button type="button" class="btn-primary" style="margin-left:20px;margin-right: 30px;" onclick="query(4);">查询</button>&nbsp;
			  	<button type="button" class="btn-danger">删除</button>
			  	</td>
			  </tr>
			  <tr>
			  	<td>行程统计数据</td>
			  	<td>
			  	<button type="button" class="btn-primary" style="margin-left:20px;margin-right: 30px;" onclick="query(5);">查询</button>&nbsp;
			  	<button type="button" class="btn-danger">删除</button>
			  	</td>
			  </tr>
			  <tr>
			  	<td>设备当前连接主机</td>
			  	<td>
			  	<button type="button" class="btn-primary" style="margin-left:20px;margin-right: 30px;" onclick="query(6);">查询</button>&nbsp;
			  	<button type="button" class="btn-danger">删除</button>
			  	</td>
			  </tr>
			  <tr>
			  	<td>设备最新位置数据</td>
			  	<td>
			  	<button type="button" class="btn-primary" style="margin-left:20px;margin-right: 30px;"  onclick="query(7);">查询</button>&nbsp;
			  	<button type="button" class="btn-danger">删除</button>
			  	</td>
			  </tr>
			  <tr>
			  	<td>设备最新初始化数据</td>
			  	<td>
			  	<button type="button" class="btn-primary" style="margin-left:20px;margin-right: 30px;" onclick="query(8);">查询</button>&nbsp;
			  	<button type="button" class="btn-danger">删除</button>
			  	</td>
			  </tr>
			   <tr>
			  	<td>设备最新上线大概时间</td>
			  	<td>
			  	<button type="button" class="btn-primary" style="margin-left:20px;margin-right: 30px;" onclick="query(9);">查询</button>&nbsp;
			  	<button type="button" class="btn-danger">删除</button>
			  	</td>
			  </tr>
			  <tr>
			  	<td>请求离线数据TTL时间</td>
			  	<td>
			  	<button type="button" class="btn-primary" style="margin-left:20px;margin-right: 30px;" onclick="query(10);">查询</button>&nbsp;
			  	<button type="button" class="btn-danger">删除</button>
			  	</td>
			  </tr>
			</table>
			<div class="widget-bottom">
				&nbsp;<span class="pull-right">版权所有：广州华工信息软件有限公司</span>
			</div>
		</div>
	</div>
	</form>
	<div class="widget-bottom">
		<jsp:include page="../../include/pager.jsp" />
	</div>
<div id="editUser" class="widget" style="display:none; POSITION:absolute; left:50%; top:50%; width:450px; height:300px; margin-left:-300px; margin-top:-100px; border:1px solid #888; background-color:white; text-align:center">
	<iframe id="iframe" width='450px' height='300px'></iframe>
</div>
</body>
</html>