<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8" language="java"
	pageEncoding="UTF-8" isErrorPage="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
<title>车辆监控 - 加油管理</title>
<jsp:include page="../../include/common.jsp" />
<script>
$(function(){
	
});
function del(){
	var infoId=null;
	var length=$("input[name=checkbox]:checked").length;
	if(length==0){
		alert("请选择要删除的数据！");
		return ;
	}
	$("input:checkbox").each(function(index){ 
		if(this.checked){
			if(infoId==null){
				infoId=this.value;
			}else{
				infoId=infoId+","+this.value;
			}
			
		}
	});
	url="${basePath}/admin/carOwnerManager_delAddoil.do?idArr="+infoId;
	window.location.href=url;
	
}
function add(){
	var url="${basePath}/admin/carOwnerManager_addoilJsp.do";
	document.getElementById("fuelAdd").style.display="";
	document.getElementById("iframe").src=url;
}
function hideIframe(){
	$("#iframe").html('');
	$("#fuelAdd").hide();
}
</script>
</head>
<body>
<form id="myForm" class="myForm" action="${basePath}/admin/carOwnerManager_fuelManagement.do" method="post">
	<ul class="breadcrumb">
		<li><i class="icon-home icon-2x"></i></li>
		<li>当前位置：车辆监控  &gt; 加油管理</li>
	</ul>
	<h3><c:if test="${shopName != null && shopName != ''}">欢迎${shopName }</c:if></h3>
	<div class="widget">
		<div class="widget-content" style="line-height: 20px">
			车牌号码：<input type="text" id="license" value="${license}" name="license"/>
			加油站名称：<input type="text" id="gasStation" value="${gasStation}" name="gasStation"/>
			加油者姓名：<input type="text" id="userName" value="${yName}" name="userName"/>
			<br /><br />
			加油时间：<input type="text" value="${starTime}" class="Wdate" readonly="readonly" name="starTime" id="starTime" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'%y-%M-%d'})" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			&nbsp;&nbsp;&nbsp;&nbsp;到&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" value="${endTime}" class="Wdate" readonly="readonly" name="endTime" id="endTime"  onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'%y-%M-%d'})" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			&nbsp;&nbsp;&nbsp;&nbsp;<button type="submit" class="btn btn-primary">查询</button>
			<br /><br />
			<button type="button" class="btn btn-primary" onclick="add()">新增</button>
			<button type="button" class="btn btn-primary" onclick="del()">删除</button>
		</div>
		<div class="widget-content">
			<table class="table table-hover">
			  <tr style="background-color: #c4e3f3;">
			  	<td>选择</td><td>车牌号</td><td>加油时间</td><td>加油站名称</td><td>加油站地址</td><td>油料标号</td><td>单价(元/L)</td><td>加油量(L)</td><td>金额(元)</td><td>加油者姓名</td>
			  </tr>
			  <s:iterator value="carAddoilLsit" >
			  <tr>
			  	<td width="10%" height="26" align="center"> 
					<input type="checkbox" name="checkbox" id="checkbox" value="<s:property value="id"></s:property>"/> 
				</td>
				<td>
			  	  	<s:property value="license"></s:property>
			  	</td>
			  	<td>
			  	  	<s:property value="addTime"></s:property>
			  	</td>
			  	<td>
			  	  	<s:property value="gasStation"></s:property>
			  	</td>
			  	<td>
			  	  	<s:property value="gasStationAdd"></s:property>
			  	</td>
			  	<td>
			  	  	<s:property value="oilType"></s:property>
			  	</td>
			  	<td>
			  	  	<s:property value="fee"></s:property>
			  	</td>
			  	<td>
			  	  	<s:property value="oilNum"></s:property>
			  	</td>
			  	<td>
			  	  	<s:property value="total"></s:property>
			  	</td>
			  	<td>
			  	  	<s:property value="yName"></s:property>
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
	<div id="fuelAdd" class="widget" style="display:none; POSITION:absolute; left:50%; top:50%; width:550px; height:350px; margin-left:-300px; margin-top:-200px; border:1px solid #888; background-color:white; text-align:center">
	<iframe id="iframe" width='550px' height='350px'></iframe>
	</div>
</body>
</html>