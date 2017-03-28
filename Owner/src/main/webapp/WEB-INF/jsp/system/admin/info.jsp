<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html>
<head>
<title>查看用户信息</title>
<jsp:include page="../../include/common.jsp" />
<script language="javascript">
	if ('${message}' != '') {
		alert('${message}');
	}
</script>
</head>
<body>
	<form id="myForm" method="post" action="admin_updatemyself.do">
		<ul class="breadcrumb">
			<li><i class="icon-home icon-2x"></i></li>
			<li>当前位置：${currentPosition}&nbsp;>&nbsp;查看用户信息</li>
		</ul>
		<div class="widget widget-edit">
			<div class="widget-content">
				<table class="pn-ftable table-bordered table-condensed" border="0" cellpadding="10">
					<tbody>
						<tr>
							<th>登&nbsp;&nbsp;录&nbsp;&nbsp;名</th>
							<td class="pn-fcontent">${admin.username}</td>
							<td class="pn-info"></td>
						</tr>
						<tr>
							<th>真实姓名</th>
							<td class="pn-fcontent">${admin.name}</td>
							<td class="pn-info"></td>
						</tr>
						<tr>
							<th>性&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;别</th>
							<td class="pn-fcontent">${admin.sex}</td> 
							<td class="pn-info"></td>
						</tr>
						<tr>
							<th>邮&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;箱</th>
							<td class="pn-fcontent">${admin.email}</td>
							<td class="pn-info"></td>
						</tr>
						<tr>
							<th>手&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;机</th>
							<td class="pn-fcontent">${admin.phone}</td>
							<td class="pn-info"></td>
						</tr>
						<tr>
							<th>身份证号</th>
							<td class="pn-fcontent">${admin.idCard}</td>
							<td class="pn-info"></td>
						</tr>
						<tr>
							<th>启&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;用</th>
							<td class="pn-fcontent">
								<c:choose>
									<c:when test="${admin.valid==1}">是</c:when>
									<c:otherwise>否</c:otherwise>
								</c:choose>
							</td>
							<td class="pn-info"></td>
						</tr>
						<tr>
							<th>创建时间</th>
							<td class="pn-fcontent">
								<fmt:formatDate value="${admin.createTime}" pattern="yyyy-MM-dd HH:mm:ss" />
							</td>
								
							<td class="pn-info"></td>
						</tr>
					</tbody>
				</table>
				<div class="widget-bottom">
					<a href="javascript:history.go(-1);"
						class="btn btn-danger pull-right">返 回</a> 
				</div>
			</div>
			<!-- /widget-content -->
		</div>

	</form>
</body>
</html>