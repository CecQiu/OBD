<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8" language="java"
	pageEncoding="UTF-8" isErrorPage="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
<title>车主服务 - 故障提醒</title>
<jsp:include page="../../include/common.jsp" />
<script>
$(function(){
	
});
function hideIframe(){
	$("#iframe").html('');
	$("#editUser").hide();
}
function add(){
	var url="carOwnerService_faultAddJsp.do";
	document.getElementById("editUser").style.display="";
	document.getElementById("iframe").src=url;
}
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
				infoId=infoId+","+this.value
			}
			
		}
	});
	url="carOwnerService_faultDel.do?idArr="+infoId;
	window.location.href=url;
	
}
function edit(){
	var length=$("input[name=checkbox]:checked").length;
	if(length!=1){
		alert("请选择要修改的一条数据！");
		return ;
	}else{
		var value=$("input[name=checkbox]:checked").val();
		var url="carOwnerService_faultEditJsp.do?id="+value;
		document.getElementById("editUser").style.display="";
		document.getElementById("iframe").src=url;
	}
	
}
</script>
</head>
<body>
<form id="myForm" class="myForm" action="carOwnerService_faultSearch.do" method="post">
	<ul class="breadcrumb">
		<li><i class="icon-home icon-2x"></i></li>
		<li>当前位置：车主服务  &gt; 故障提醒</li>
	</ul>
	<h3><c:if test="${shopName != null && shopName != ''}">欢迎${shopName }</c:if></h3>
	<div class="widget">
		<div class="widget-content" style="line-height: 20px">
			手机号码：<input type="text" id="mobileNumber" value="${mobileNumber}" name="mobileNumber"/>
			车牌号码：<input type="text" id="license" value="${license}" name="license"/>
			二维码：<input type="text" id="obdSN" value="${obdSN}" name="obdSN"/>
			<br /><br />
			发生时间：<input type="text" value="${starTime}" class="Wdate" readonly="readonly" name="starTime" id="starTime" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'%y-%M-%d'})" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			&nbsp;到&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" value="${endTime}" class="Wdate" readonly="readonly" name="endTime" id="endTime"  onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'%y-%M-%d'})" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			&nbsp;&nbsp;&nbsp;&nbsp;<button type="submit" class="btn btn-primary">查询</button>
			<br /><br />
			<button type="button" class="btn btn-primary" onclick="add()">新增</button>
			<button type="button" class="btn btn-primary" onclick="edit()">修改</button>
			<button type="button" class="btn btn-primary" onclick="del()">删除</button>
		</div>
		<div class="widget-content">
			<table class="table table-hover">
			  <tr style="background-color: #c4e3f3;">
			  	<td>选择</td><td>姓名</td><td>性别</td><td>手机号码</td><td>车牌号码</td><td>二维码</td><td>故障码</td><td>故障内容</td><td>发生时间</td>
			  </tr>
			  <s:iterator value="faultList" >
			  <tr>
			  <td width="10%" height="26" align="center"> 
					<input type="checkbox" name="checkbox" id="checkbox" value="<s:property value="id"></s:property>"/> 
				</td>
				<td>
			  	  	<s:property value="userName"></s:property>
			  	</td>
			  	<td>
			  	  	<s:property value="sex"></s:property>
			  	</td>
			  	<td>
			  	  	<s:property value="mobileNumber"></s:property>
			  	</td>
			  	<td>
			  	  	<s:property value="license"></s:property>
			  	</td>
			  	<td>
			  	  	<s:property value="obdSN"></s:property>
			  	</td>
			  	<td>
			  	  	<s:property value="fault_code"></s:property>
			  	</td>
			  	<td>
			  	  	<s:property value="fault_comment"></s:property>
			  	</td>
			  	<td>
			  	  	<fmt:formatDate value="${create_time}" type="both" />
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
	<div id="editUser" class="widget" style="display:none; POSITION:absolute; left:50%; top:50%; width:450px; height:250px; margin-left:-300px; margin-top:-200px; border:1px solid #888; background-color:white; text-align:center">
	<iframe id="iframe" width='450px' height='250px'></iframe>
	</div>
</body>
</html>