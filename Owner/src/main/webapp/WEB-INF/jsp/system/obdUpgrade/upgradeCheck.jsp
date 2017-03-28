<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html>
<head>
<jsp:include page="../../include/common.jsp" />
<title></title>
<script language="javascript">
function formCheck(){
	var auditState = $("#auditState").val();
	if(auditState == ""){
		alert("请选择是否通过.");
		return false;
	}
}
</script>
</head>
<body>
	<form id="myForm" name="myForm" action="odbUpgrade_check.do" method="post" onsubmit="return formCheck();">
		<ul class="breadcrumb">
			<li><i class="icon-home icon-2x"></i></li>
			<li>当前位置：${currentPosition}&nbsp;&gt;&nbsp;升级固件审核</li>
		</ul>
		<div class="widget widget-table">
			<div class="widget-header">
				<i class="icon-th-list"></i>
				<h5>obd设备升级文件信息</h5>
			</div>
			<div>
			<table class="table table-striped table-bordered table-condensed table-hover sortable">
				<tr>
					<th>文件名：</th>
					<td>${obdUpgrade.fileName }</td>
					<th>版本号：</th>
					<td id='version'>${obdUpgrade.version }</td>
				</tr>
				<tr>
					<th>文件大小：</th>
					<td>${obdUpgrade.size }字节</td>
					<th>上传时间：</th>
					<td>${obdUpgrade.createTime }</td>
				</tr>
				<tr>
					<th>类型：</th>
					<td>
						<c:choose>
							<c:when test="${obdUpgrade.firmType==0}">APP</c:when>
							<c:when test="${obdUpgrade.firmType==1}">IAP</c:when>
							<c:otherwise></c:otherwise>
						</c:choose>
					</td>
					<th>备注：</th>
					<td>
						<textarea rows="2" cols="60" readonly="readonly">${obdUpgrade.memo }</textarea>
					</td>
				</tr>
			</table>
			</div>
		</div>
		<div class="widget widget-table">
			<div class="widget-content">
				<div class="widget-header">
					<i class="icon-th-list"></i>
					<h5>审核</h5>
				</div>
				<table class="pn-ftable table-condensed" border="0" cellpadding="10">
					<tbody>
						<tr>
							<th>是否通过:</th>
							<td class="pn-fcontent">
								<select name="obdUpgrade.auditState" id="auditState" style="width: 200px;">
									<option value="">未审核</option>
									<option value="1" <c:if test="${obdUpgrade.auditState=='1'}">selected</c:if>>通过</option>
									<option value="2" <c:if test="${obdUpgrade.auditState=='2'}">selected</c:if>>不通过</option>
								</select>
							</td>
						</tr>
						<tr>
							<th>审核意见：</th>
							<td colspan="1" style="text-align: left;">
								<textarea rows="3" cols="80" name="obdUpgrade.auditMsg" id="auditMsg">${obdUpgrade.auditMsg }</textarea>
							</td>
						</tr>
					</tbody>
				</table>
				<div style="display:none;">
					<input name="upId" value="${obdUpgrade.id }"/> 
					<input name="obdUpgrade.id" value="${obdUpgrade.id }"/> 
				</div>
				<div class="widget-bottom">
					<center>
						<input class="btn btn-s-md btn-success" type="submit" value=" 确 定 " />&nbsp;
						<input class="btn btn-danger btn-s-md" type="button" onclick="doGoBack()" value="返回"/>
					</center>
				</div>
			</div>
			<!-- /widget-content -->
		</div>
	</form>
</body>
</html>