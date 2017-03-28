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
	return true;
}
</script>
</head>
<body>
	<form id="myForm" name="myForm" action="batchSet_check.do" method="post" onsubmit="return formCheck();">
		<ul class="breadcrumb">
			<li><i class="icon-home icon-2x"></i></li>
			<li>当前位置：${currentPosition}&nbsp;&gt;&nbsp;批量设置记录审核</li>
		</ul>
		<div class="widget widget-table">
			<div class="widget-header">
				<i class="icon-th-list"></i>
				<h5>批量设置记录审核</h5>
			</div>
			<div>
			<table class="table table-striped table-bordered table-condensed table-hover sortable">
				<tr>
					<th>id</th>
					<td>${batchSet.id }</td>
					<th>类型：</th>
					<td>
						<c:choose>
							<c:when test="${batchSet.type=='address'}">地址修改</c:when>
							<c:otherwise></c:otherwise>
						</c:choose>
					</td>
				</tr>
				<tr>
					<th>内容</th>
					<td>
						<textarea rows="2" cols="80" readonly="readonly">${batchSet.msg }</textarea>
					</td>
					<th>消息体</th>
					<td><textarea rows="2" cols="80" readonly="readonly">${batchSet.bodyMsg }</textarea></td>
				</tr>
				<tr>
					<th>版本</th>
					<td>${batchSet.version }</td>
					<th>有效：</th>
					<td id='version'>${batchSet.valid }
						<c:choose>
							<c:when test="${batchSet.valid==0}">无效</c:when>
							<c:when test="${batchSet.valid==1}">有效</c:when>
							<c:otherwise></c:otherwise>
						</c:choose>
					</td>
				</tr>
				<tr>
					<th>审核人</th>
					<td>${batchSet.auditOper }</td>
					<th>审核时间</th>
					<td>${batchSet.auditTime }</td>
				</tr>
				<tr>
					<th>审核状态</th>
					<td id='version'>${batchSet.auditState }
						<c:choose>
							<c:when test="${batchSet.auditState==0}">未审核</c:when>
							<c:when test="${batchSet.auditState==1}">通过</c:when>
							<c:when test="${batchSet.auditState==-1}">不通过</c:when>
							<c:otherwise></c:otherwise>
						</c:choose>
					</td>
					<th>审核意见</th>
					<td>${batchSet.auditMsg }</td>
				</tr>
				<tr>
					<th>创建时间</th>
					<td>${batchSet.createTime }</td>
					<th>更新时间</th>
					<td>${batchSet.updateTime }</td>
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
								<select name="auditState" id="auditState" style="width: 200px;">
									<option value="">未审核</option>
									<option value="1" <c:if test="${auditState=='1'}">selected</c:if>>通过</option>
									<option value="-1" <c:if test="${auditState=='-1'}">selected</c:if>>不通过</option>
								</select>
							</td>
						</tr>
						<tr>
							<th>审核意见：</th>
							<td colspan="1" style="text-align: left;">
								<textarea rows="3" cols="80" name="auditMsg" id="auditMsg">${auditMsg }</textarea>
							</td>
						</tr>
					</tbody>
				</table>
				<div style="display:none;">
					<input name="id" value="${id }"/> 
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