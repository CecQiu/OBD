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
<script language="javascript" src="${basePath}/js/curentTime.js"></script>
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
    //$('input[name="endTime"]').val(CurentTime());
});
function check(){
	var infoType=document.getElementById("infoType").value;
	var infoTitle=document.getElementById("infoTitle").value;
	var starTime=document.getElementById("beginTime").value;
	var endTime=document.getElementById("endTime").value;
	var url="${basePath}/admin/carOwnerMarketing_queryInfomation.do?infoType="+encodeURI(encodeURI(infoType))+"&infoTitle="+encodeURI(encodeURI(infoTitle))
	+"&starTime="+starTime+"&endTime="+endTime;
	if(starTime!=null&&(starTime.replace(/(^s*)|(s*$)/g, "").length>0)){
		if(endTime!=null&&(endTime.replace(/(^s*)|(s*$)/g, "").length>0)){
			if(starTime>endTime){
				alert("发布时间有误，请重新输入！");
			}else{
				window.location.href=url;
			}
		}else{
			window.location.href=url;
		}
	}else{
		window.location.href=url;
	}
}
function hideIframe(){
	$("#iframe").html('');
	$("#editUser").hide();
}
function add(){
	//window.open("${basePath}/admin/carOwnerMarketing_add.do","",'width=500px,height=300,toolbar =no, menubar=no, scrollbars=no, resizable=no, location=no, status=no');
	var url="${basePath}/admin/carOwnerMarketing_add.do";
	document.getElementById("editUser").style.display="";
	document.getElementById("iframe").src=url;
}
function update(infoId){
	//window.open("${basePath}/admin/carOwnerMarketing_queryInfoById.do?infoId="+infoId,"",'width=500px,height=300,toolbar =no, menubar=no, scrollbars=no, resizable=no, location=no, status=no');
	var url="${basePath}/admin/carOwnerMarketing_queryInfoById.do?infoId="+infoId;
	document.getElementById("editUser").style.display="";
	document.getElementById("iframe").src=url;
}
function del(){
	var infoId=null;
	var checked=document.getElementById("checkbox").checked;
	$("input:checkbox").each(function(index){ 
		if(this.checked){
			if(infoId==null){
				infoId=this.value;
			}else{
				infoId=infoId+","+this.value
			}
			
		}
	});
	url="${basePath}/admin/carOwnerMarketing_del.do?infoArr="+infoId;
	window.location.href=url;
	
}
</script>
</head>
<body>
	<form id="myForm" class="myForm" action="${basePath}/admin/carOwnerMarketing_queryInfomation.do" method="post">
	<ul class="breadcrumb">
		<li><i class="icon-home icon-2x"></i></li>
		<li>当前位置：营销管理  &gt; 资讯管理</li>
	</ul>
	<h3><c:if test="${shopName != null && shopName != ''}">欢迎${shopName }</c:if></h3>
		<div class="widget">
		<div class="widget-content" style="line-height: 20px">
			<select name="infoType" style="20px;" id="infoType">
				<option value="01">全部用户</option>
				<option value="02">部分用户</option>
				<option value="03">置顶用户</option>
			</select>&nbsp;&nbsp;&nbsp;&nbsp;
			标题&nbsp;&nbsp;<input type="text" value="${infoTitle}" id="infoTitle" name="infoTitle" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			发布时间&nbsp;&nbsp;<input type="text" value="${starTime}" class="Wdate" readonly="readonly" name="starTime" id="beginTime" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'%y-%M-%d'})"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			到&nbsp;&nbsp;<input type="text" value="${endTime}" class="Wdate" readonly="readonly" name="endTime" id="endTime"  onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'%y-%M-%d'})"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			&nbsp;&nbsp;&nbsp;&nbsp;<button type="submit" class="btn btn-primary" >查 找</button>
			<br/><br/>
			<button type="button" class="btn btn-primary" onclick="add()">新增</button>
			<button type="button" class="btn btn-primary" onclick="del()">删除</button>
			
		</div>
		<div class="widget-content">
			<table class="table table-hover">
			  <tr style="background-color: #c4e3f3;">
			  	<td>选择</td><td>标题</td><td>概况</td><td>发布者账号</td><td>发布时间</td>
			  </tr>
			  <s:iterator value="list" status="st">
			  <tr>
			  	  <td width="10%" height="26" align="center"> 
					<input type="checkbox" name="checkbox" id="checkbox" value="<s:property value="infoId"></s:property>"/> 
				  </td> 
			  	  
		 	  	  	<c:if test="${infoRedShow=='1'}">
			  	  		<td class="infoTitle" id="infoTitle">
			  	  		 <a href="#" onclick="update(<s:property value="infoId"></s:property>)"><s:property value="infoTitle"></s:property>	</a>	  	  	
			  	  		</td>
			  	  	</c:if>
			  	 		<c:if test="${infoRedShow=='0'}"> 
			  	  		<td class="infoTitle" id="infoTitle">
			  	  		 	 <a href="#" onclick="update(<s:property value="infoId"></s:property>)" style="color: red;"><s:property value="infoTitle"></s:property>	</a>	  	  	
			  	  		</td>
			  	  </c:if>
			  	  <td class="summary" id="summary">
			  	  	<s:property value="summary"></s:property>
			  	  </td>
			  	  <td class="regUserId">
			  	  	<s:property value="regUserId"></s:property>
			  	  </td>
			  	    <td class="infoTime">
			  	  	<s:property value="infoTime"></s:property>
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
	<div id="editUser" class="widget" style="display:none; POSITION:absolute; left:50%; top:50%; width:450px; height:300px; margin-left:-300px; margin-top:-200px; border:1px solid #888; background-color:white; text-align:center">
	<iframe id="iframe" width='450px' height='300px'></iframe>
</div>
</body>
</html>