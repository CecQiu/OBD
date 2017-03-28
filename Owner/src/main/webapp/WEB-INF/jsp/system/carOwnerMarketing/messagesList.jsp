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
<title>营销管理 -推送消息</title>
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
$(document).ready(function() {
	$('input[name="messageTime"]').val(Data());
});
function show(){
	document.getElementById("msgAddDiv").style.display="";
}
function checkMsg(){
	var messageText=document.getElementById("messageText").value;
	if(messageText!=null&&messageText.replace(/(^s*)|(s*$)/g, "").length>0){
		return true;
	}else{
		 $('#messageTextError')[0].innerHTML = "<font color=red size=2>消息内容不能为空</font>";
		return false;
	}	
}
</script>
</head>
<body>
	<form id="myForm" class="myForm" action="${basePath}/admin/carOwnerMarketing_messageInfo.do" method="post">
	<ul class="breadcrumb">
		<li><i class="icon-home icon-2x"></i></li>
		<li>当前位置：营销管理  &gt; 推送消息</li>
	</ul>
	<h3><c:if test="${shopName != null && shopName != ''}">欢迎${shopName }</c:if></h3>
		<div class="widget">
		<div class="widget-content" style="line-height: 20px">
		</div>&nbsp;&nbsp;
		<button type="button" class="btn btn-primary" onclick="show()">发消息</button>	 
		<div class="widget-content">
			<table class="table table-hover">
			  <tr style="background-color: #c4e3f3;">
			  	<td>推送时间</td><td>推送内容</td><td>接收对象</td>
			  </tr>
			  <s:iterator value="msglist" status="st">
			  <tr>
			  	  <td width="10%" height="26" align="center" > 
					<s:property value="messageTime"></s:property>
				  </td> 			 
			  	  <td >
			  	  	<s:property value="messageText"></s:property>	  	
			  	  </td>
			  	  <td >
			  	  	<s:property value="messageRec"></s:property>
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
<div id="msgAddDiv" class="widget" style="display:none; POSITION:absolute; left:50%; top:50%; width:450px; height:230px; margin-left:-300px; margin-top:-100px; border:1px solid #888; background-color:white; text-align:center">
<form action="${basePath}/admin/carOwnerMarketing_addMsg.do" method="post">
	<table class="table table-hover">
		<tbody>
			<tr>
			<td>接收对象:<select id="messageRec" name="messageRec">
				<option>所有车主</option>
				<option>部分车主</option>
			</select></td>
			<td>
			推送时间：
			<input type="text" name="messageTime" id="messageTime" onclick="WdatePicker({maxDate:'%y-%M-%d'})"class="Wdate" readonly="readonly"/></td>
			</tr>			
			<tr>
			<td colspan="2">&nbsp;&nbsp;&nbsp;&nbsp;内容:&nbsp;&nbsp;&nbsp;&nbsp;<textarea rows="5" cols="50" id="messageText" name="messageText"></textarea></td>
			</tr>
			<tr><td><span id="messageTextError"></span></td></tr>
		</tbody>
	</table>
	<button type="submit" class="btn btn-primary" onclick="return checkMsg()">发布</button>
	<button type="button" class="btn btn-primary" onclick="window.location.href='${basePath}/admin/carOwnerMarketing_messageInfo.do'">取消</button>
</form>		  
</div>
	
</body>
</html>