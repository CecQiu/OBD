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
	/* $(".midnav a").click(function(){
		$(".midnav .active").attr("class","btn");
		$(this).addClass("active");
		$(".page-content-wrapper").html($(this).attr("info"));
	}); */
});
function resetValue() {
	$("#obdSn").val("");
	$("#connectTime").val("");
}

</script>
</head>
<body>
<form id="myForm" class="myForm" action="${basePath}/admin/sendStatsControlAction.do" method="post">
	<ul class="breadcrumb">
		<li><i class="icon-home icon-2x"></i></li>
		<li>当前位置：监控管理  &gt; 报文发送统计</li>
	</ul>
	<h3><c:if test="${shopName != null && shopName != ''}">欢迎${shopName }</c:if></h3>
	<div class="widget">
		<div class="widget-content" style="line-height: 20px">
			日期&nbsp;&nbsp;<input type="text" class="Wdate" readonly="readonly" value="${endDate}" name="endDate" id="endDate"
			 onclick="WdatePicker({maxDate:'%y-%M-%d'})"/>&nbsp;&nbsp;&nbsp;
			记录数:&nbsp;&nbsp;<input type="text" id="count" name="count" value="${empty count ? 10:count}" size="4"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<button type="submit" class="btn btn-primary" >查 找</button>
			<button type="button" class="btn btn-success" onclick="resetValue();">重 置</button>
		</div>
		<div class="widget-content">
			<table class="table table-hover">
			  <tr style="background-color: #c4e3f3;">
			  	<td>日期</td>
			  	<td>成功</td>
			  	<td>失败</td>
			  </tr>
			  <c:forEach items="${sendStatsMap }" var="sendStatsMap">
			  <tr>
			  	  <td>
			  	  	 ${sendStatsMap.key }
			  	  </td>
			  	  <td style="color: green">
			  	  	  ${sendStatsMap.value[0] }
			  	  	  <c:if test="${(sendStatsMap.value[0]+sendStatsMap.value[1]) != 0 }">
			  	  	  (<fmt:formatNumber value=" ${sendStatsMap.value[0]*1.00/(sendStatsMap.value[0]+sendStatsMap.value[1])*100}" pattern="#0.00" />％)
			  	  	  </c:if>
			  	  </td>
			  	  <td style="color: red">
			  	  	  ${sendStatsMap.value[1] }
			  	  	  <c:if test="${(sendStatsMap.value[0]+sendStatsMap.value[1]) != 0 }">
			  	  	  (<fmt:formatNumber value=" ${sendStatsMap.value[1]*1.00/(sendStatsMap.value[0]+sendStatsMap.value[1])*100}" pattern="#0.00" />％)
			  	  	  </c:if>
			  	  </td>
			  </tr>
			  </c:forEach>
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