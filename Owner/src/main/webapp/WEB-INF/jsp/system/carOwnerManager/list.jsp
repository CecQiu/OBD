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
function updateMebUser(regUserId){
	var url="${basePath}/admin/carOwnerManager_queryById.do?regUserId="+regUserId;
	//window.open(url,"",'width=500px,height=300,toolbar =no, menubar=no, scrollbars=no, resizable=no, location=no, status=no');
	$("#iframe").html('');
	document.getElementById("iframe").src=url;
	document.getElementById("editUser").style.display="";
}

function hideIframe(){
	$("#iframe").html('');
	$("#editUser").hide();
}

function deleteMebUser(regUserId){
	var url="${basePath}/admin/carOwnerManager_delete.do?regUserId="+regUserId;
	window.location.href =url;
}
</script>
</head>
<body>
<form id="myForm" class="myForm" action="${basePath}/admin/carOwnerManager_search.do" method="post">
	<ul class="breadcrumb">
		<li><i class="icon-home icon-2x"></i></li>
		<li>当前位置：车主管理  &gt; 查询功能</li>
	</ul>
	<h3><c:if test="${shopName != null && shopName != ''}">欢迎${shopName }</c:if></h3>
	<div class="widget">
		<div class="widget-content" style="line-height: 20px">
			手机号码:&nbsp;&nbsp;<input type="text" id="mobileNumber" name="mobileNumber" value="${mobileNumber}"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			车牌号码:&nbsp;&nbsp;<input type="text" id="license" name="license" value="${ license}"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			二维码:&nbsp;&nbsp;<input type="text" id="obdSN" name="obdSN" value="${ obdSN}"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<button type="submit" class="btn btn-primary" >查 找</button>
		</div>
		<div class="widget-content">
			<table class="table table-hover">
			  <tr style="background-color: #c4e3f3;">
			  	<td>车牌号</td><td>用户账号</td><td>智能盒激活码</td><td>姓名</td><td>性别</td><td>操作</td>
			  </tr>
			  <s:iterator value="list" status="st">
			  <tr>
			  	  <td id="license">
			  	  	<s:property value="license"></s:property>
			  	  </td>
			  	  <td id="mobileNumber">			  	  
			  	  	<s:property value="mobileNumber"></s:property>			  	 
			  	  </td>
			  	  <td id="obdSN">
			  	  	<s:property value="obdSN"></s:property>
			  	  </td>
			  	  <td id="name">
			  	  	<s:property value="name"></s:property>
			  	  </td>
			  	    <td id="sex">
			  	  	<s:property value="sex"></s:property>
			  	  </td>
			  	  <td>
			  	  <button type="button" onclick="updateMebUser('<s:property value="regUserId"></s:property>')">编辑</button>
			  	  <button type="button" onclick="deleteMebUser('<s:property value="regUserId"></s:property>')">删除</button>
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
<div id="editUser" class="widget" style="display:none; POSITION:absolute; left:50%; top:50%; width:450px; height:300px; margin-left:-300px; margin-top:-100px; border:1px solid #888; background-color:white; text-align:center">
	<iframe id="iframe" width='450px' height='300px'></iframe>
</div>
</body>
</html>