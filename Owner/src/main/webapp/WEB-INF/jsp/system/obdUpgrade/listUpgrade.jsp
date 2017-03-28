<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8" language="java"
	pageEncoding="UTF-8" isErrorPage="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html>
<head>
<title>OBD设备升级 - 升级文件列表</title>
<jsp:include page="../../include/common.jsp" />
<script>
$(function(){
	$("#addUpgrade").click(function() {
		window.location.href="${basePath}/admin/odbUpgrade_addUpgrade.do";
	});
	
	/* $("#excelUpgrade").onclick(function() {
		window.location.href="${basePath}/admin/odbUpgrade_toExcelUpgrade.do";
	}); */
	
	$('#sure').click(function(){
		if( $('#pwd').val() == '123321'){
			$('#addUpgrade').css('display','block');
			$('#excelUpgrade').css('display','block');
		}else{
			alert('密码错误！');
		}
	});
});

function pushUpgrade(id) {
	window.location.href = "${basePath}/admin/odbUpgrade_listPushObd.do?upId=" + id;
}

function deleteUpgrade(id) {
	var firmVersion = $("#"+id).html();
	if(confirm("你要固件版本是:"+firmVersion+",确认删除？")) {
		
	}else{
		return false;
	}
	
	var flag= true;
	//查询这个版本是否有未推送成功的，且有效的待升级记录
	$.ajax({
		url:'${basePath}/admin/odbUpgrade_delectCheck.do',
		data:{
			'upId':id
		},
		type:'post',
		cache : false,
		async : false,
		dataType :'json',
		success:function(data){
			if(data.status=='success'){
				alert(data.message);
			}else{
				alert(data.message);
				alert("删除失败,请核查.");
				flag = false;
			}
		},
		error : function(){
			alert("系统异常,请稍后重试.");
			flag = false;
		}
	});
	if(!flag){
		return;
	}
	//远程删除
	window.location.href = "${basePath}/admin/odbUpgrade_deleteUpgrade.do?upId=" + id;
}

function editUpgrade(id) {
	window.location.href = "${basePath}/admin/odbUpgrade_updateUpgrade.do?upId=" + id;
}

function upgradeCheck(id) {
	//审核按钮
	window.location.href = "${basePath}/admin/odbUpgrade_toCheck.do?upId=" + id;
}

function upgradeFileSend(id) {
	//固件推送
	window.location.href = "${basePath}/admin/odbUpgrade_upgradeFileSend.do?upId=" + id;
}


function toExcelUpgrade(id) {
	//跳转上传升级列表页面
	window.location.href="${basePath}/admin/odbUpgrade_toExcelUpgrade.do?upId=" + id;
}

function upgradeObdSnSend(id) {
	$.ajax({
		url:'${basePath}/admin/odbUpgrade_upgradeObdSnsSend.do',
		data:{
			'upId':id
		},
		type:'post',
		cache : false,
		async : false,
		dataType :'json',
		success:function(data){
			if(data.status=='success'){
				alert(data.message);
			}else{
				alert(data.message);
				flag = false;
			}
		},
		error : function(){
			alert("升级列表推送异常,请稍后重试.");
		}
	});
}

function autocompleteF(){
	var name='${session.operator.username}';
	var uversion= $("#uversion").val();
	console.info(name+"---"+uversion);
	if(name===uversion){
		$("#uversion").val("");
	}
}
</script>
</head>
<body>
	<ul class="breadcrumb">
		<li><i class="icon-home icon-2x"></i></li>
		<li>当前位置：OBD设备升级   &gt; 升级文件列表</li>
	</ul>
	<form id="myForm" class="myForm" action="${basePath}/admin/odbUpgrade_listUpgrade.do">
		<div class="widget widget-table">
			<div class="widget-content">
				<table class="pn-ftable table-condensed" border="0" cellpadding="10">
					<tbody>
						<tr>
							<th>版本号：</th>
							<td class="pn-fcontent">
								 <input type="text" name="uversion" id="uversion" value="${uversion}" onchange="autocompleteF()"/>
							</td>
							<th>固件类型</th>
							<td class="pn-fcontent">
								<select name="firmType" id="firmType" style="width: 200px;">
									<option value="">全部</option>
									<option value="0" <c:if test="${firmType=='0'}">selected</c:if>>APP-0</option>
									<option value="1" <c:if test="${firmType=='1'}">selected</c:if>>IAP-1</option>
								</select>
								&nbsp;<button type="submit" class="btn btn-primary">查 找</button>
							</td>
						</tr>
						<tr>
							<th>要新增升级文件?请输入密码</th>
							<td class="pn-fcontent">
								<input type="password" id="pwd" value=""/>&nbsp;<button type="button" class="btn btn-primary" id="sure">确定</button>
							</td>
							<th></th>
							<td class="pn-fcontent">
							</td>
						</tr>					
					</tbody>
				</table>
				<div class="widget-bottom">
					<center>
						<button id="addUpgrade" type="button" class="btn btn-primary" style="display: none;">新增</button>
					</center>
				</div>
			</div>
			<div class="widget-content" style="overflow-x: scroll;">
				<table class="table table-hover">
				  <tr style="background-color: #c4e3f3;">
				  	<td>序号</td>
					<td>文件名</td>
					<td>文件大小（字节）</td>
					<td>版本</td>
					<td>类型</td>
					<td>固件版本</td>
					<td>上传时间</td>
					<td>审核人员</td>
					<td>审核时间</td>
					<td>审核结果</td>
					<td>固件推送状态</td>
					<td>操作</td>
				  </tr>
				  <c:forEach items="${list}" var="item" varStatus="status">
					<tr>
						<td>${status.count}</td>
						<td>${item.fileName}</td>
						<td>${item.size}</td>
						<td>${item.version}</td>
						<td>
							<c:choose>
								<c:when test="${item.firmType==0}">APP</c:when>
								<c:when test="${item.firmType==1}">IAP</c:when>
								<c:otherwise></c:otherwise>
							</c:choose>
						</td>
						<td id="${item.id}">${item.firmVersion}</td>
						<td><fmt:formatDate value="${item.createTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
						<td>${item.auditOper}</td>
						<td><fmt:formatDate value="${item.auditTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
						<c:choose>
						   <c:when test="${item.auditState== '1'}"> 
						    	<td style="color: red;">审核通过</td>   
						   </c:when>
						   <c:when test="${item.auditState== '2'}"> 
						    	<td>审核不通过</td>   
						   </c:when>
						   <c:otherwise>
						   		<td style="color: bule;">未审核</td>
						   </c:otherwise>
						</c:choose>
						<c:choose>
						   <c:when test="${item.auditSend== '1'}"> 
						    	<td>推送成功</td>   
						   </c:when>
						   <c:when test="${item.auditSend== '2'}"> 
						    	<td>推送失败</td>   
						   </c:when>
						   <c:otherwise>
						   		<td>未推送</td>
						   </c:otherwise>
						</c:choose>
						<td>
							<!-- <button type="button" class="btn" onclick="editUpgrade('${item.id}')" auth="auth-edit">修改</button>&nbsp;  -->
							<button type="button" class="btn" onclick="deleteUpgrade('${item.id}')" auth="auth-del">删除</button>&nbsp;
							<%-- <c:if test="${item.auditState== '1'}"> 
								<button type="button" class="btn" onclick="pushUpgrade('${item.id}');" auth="auth-push">设备升级</button>&nbsp;
							</c:if> --%>
							<c:if test="${item.auditState!= '1'}"> 
								<button type="button" class="btn" onclick="upgradeCheck('${item.id}');" auth="auth-push" >审核</button>&nbsp;
							</c:if>
							<br />
							<c:if test="${item.auditState== '1' && item.auditSend!= '1'}"> 
								<button type="button" class="btn" onclick="upgradeFileSend('${item.id}');" auth="auth-push">固件推送</button>&nbsp;
							</c:if>
							<c:if test="${item.auditState== '1'}"> 
								<button type="button" id="excelUpgrade" class="btn" onclick="toExcelUpgrade('${item.id}');" auth="auth-push">升级列表上传</button>&nbsp;
							</c:if>
							<br />
							<c:if test="${item.auditState== '1' && item.auditSend == '1'}"> 
								<button type="button" class="btn" onclick="upgradeObdSnSend('${item.id}');" auth="auth-push">升级列表推送</button>&nbsp;
							</c:if>
						</td>
					</tr>
				</c:forEach>
				</table>
			</div>
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