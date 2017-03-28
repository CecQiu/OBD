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
	url="${basePath}/admin/carOwnerManager_delBusManagement.do?idArr="+infoId;
	window.location.href=url;
	
}
function add(){
	var url="${basePath}/admin/carOwnerManager_addBusManagementJsp.do";
	document.getElementById("addBusManagement").style.display="";
	document.getElementById("iframe").src=url;
}
function hideIframe(){
	$("#iframe").html('');
	$("#addBusManagement").hide();
}
function accept(id,license){
	var url="${basePath}/admin/carOwnerManager_acceptBusManagementJsp.do?carDispatchId="+id+"&license="+encodeURI(encodeURI(license));
	document.getElementById("addBusManagement").style.display="";
	document.getElementById("addBusManagement").style.height="280px";
	document.getElementById("iframe").src=url;
}
</script>
</head>
<body>
<form id="myForm" class="myForm" action="${basePath}/admin/carOwnerManager_busManagement.do" method="post">
	<ul class="breadcrumb">
		<li><i class="icon-home icon-2x"></i></li>
		<li>当前位置：车辆监控  &gt; 加油管理</li>
	</ul>
	<h3><c:if test="${shopName != null && shopName != ''}">欢迎${shopName }</c:if></h3>
	<div class="widget">
		<div class="widget-content" style="line-height: 20px">
			车牌号码：<input type="text" id="license" value="${license}" name="license"/>
			司机姓名：<input type="text" id="userName" value="${userName}" name="userName"/>
			状态：<select id="status" name="status">
					<option value="01">进行中</option>
					<option value="00">已收车</option>
				</select>
			<br /><br />
			出车时间：<input type="text" value="${drivingOutBeginTime}" class="Wdate" readonly="readonly" name="drivingOutBeginTime" id="drivingOutBeginTime" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'%y-%M-%d'})" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			到&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" value="${drivingOutEndTime}" class="Wdate" readonly="readonly" name="drivingOutEndTime" id="drivingOutEndTime"  onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'%y-%M-%d'})" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			&nbsp;&nbsp;&nbsp;&nbsp;
			收车时间：<input type="text" value="${offRunningBeginTime}" class="Wdate" readonly="readonly" name="offRunningBeginTime" id="offRunningBeginTime" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'%y-%M-%d'})" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			&nbsp;&nbsp;&nbsp;&nbsp;
			到&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" value="${offRunningEndTime}" class="Wdate" readonly="readonly" name="offRunningEndTime" id="offRunningEndTime"  onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'%y-%M-%d'})" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			&nbsp;&nbsp;&nbsp;&nbsp;
			<button type="submit" class="btn btn-primary">查询</button>
			<br /><br />
			<button type="button" class="btn btn-primary" onclick="add()">新增</button>
			<button type="button" class="btn btn-primary" onclick="del()">删除</button>
		</div>
		<div class="widget-content">
			<table class="table table-hover">
			  <tr style="background-color: #c4e3f3;">
			  	<td>选择</td><td>车牌号码</td><td>司机姓名</td><td>电话</td><td>出车时间</td><td>出车地点</td><td>收车时间</td><td>收车地点</td><td>状态 </td><td>操作</td>
			  </tr>
			  <s:iterator value="carDispatchList" >
			  <tr>
			  	<td align="center"> 
					<input type="checkbox" name="checkbox" id="checkbox" value="<s:property value="id"></s:property>"/> 
				</td>
				<td>
			  	  	<s:property value="license"></s:property>
			  	</td>
			  	<td>
			  	  	<s:property value="userName"></s:property>
			  	</td>
			  	<td>
			  	  	<s:property value="mobileNumber"></s:property>
			  	</td>
			  	<td>
			  	  	<s:property value="drivingOutTime"></s:property>
			  	</td>
			  	<td>
			  	  	<s:property value="drivingOutAddress"></s:property>
			  	</td>
			  	<td>
			  	  	<s:property value="offRunningTime"></s:property>
			  	</td>
			  	<td>
			  	  	<s:property value="offRunningAddress"></s:property>
			  	</td>
			  	<td>
			  		<c:if test="${status=='01'}">
			  			<span>进行中</span>
			  		</c:if>
			 		<c:if test="${status=='00'}">
			  			<span>已收车</span>
			  		</c:if>
			  	</td>
			  	<td>
			  		<c:if test="${status=='01'}">
			  			<button type="button" onclick="accept('<s:property value="id"></s:property>','<s:property value="license"></s:property>')">收车</button>
			  		</c:if>			  	  	
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
	<div id="addBusManagement" class="widget" style="display:none; POSITION:absolute; left:50%; top:50%; width:550px; height:280px; margin-left:-300px; margin-top:-200px; border:1px solid #888; background-color:white; text-align:center">
	<iframe id="iframe" width='550px' height='280px'></iframe>
	</div>
</body>
</html>