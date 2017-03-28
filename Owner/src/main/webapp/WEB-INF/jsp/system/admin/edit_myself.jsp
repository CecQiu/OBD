<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html>
<head>
<title>查看修改个人信息</title>
<jsp:include page="../../include/common.jsp" />
<script language="javascript">
	
	$().ready(function() {
		if ('${message}' != '') {
			alert('${message}');
		}
		$("#myForm").validate({
			rules: {
				 'admin.phone': {
			    	mobile:true
			    },'admin.email': {
			    	email:true
			    }
			  }
			});
	});
</script>
</head>
<body>
	<form id="myForm" method="post" action="admin_updatemyself.do">
		<ul class="breadcrumb">
			<li><i class="icon-home icon-2x"></i></li>
			<li>当前位置：${currentPosition}&nbsp;>&nbsp;查看修改个人信息</li>
		</ul>
		<div class="widget widget-edit">
			<div class="widget-content">
				<table class="pn-ftable table-bordered table-condensed" border="0" cellpadding="10">
					<tbody>
						<tr>
							<th>登&nbsp;&nbsp;录&nbsp;&nbsp;名</th>
							<td class="pn-fcontent"><input type="text" style="width: 200px;"
								name="admin.username" value="${admin.username}"
								readonly="readonly" required maxlength="20"/></td>
							<td class="pn-info"></td>
						</tr>
						<tr>
							<th>真实姓名</th>
							<td class="pn-fcontent"><input type="text" name="admin.name"  style="width: 200px;"
								value="${admin.name}" readonly="readonly" required maxlength="20"/></td>
							<td class="pn-info"></td>
						</tr>
						<tr>
							<th>密&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;码</th>
							<td class="pn-fcontent"><input type="password" style="width: 200px;"
								name="admin.password" id="password" maxlength="32"/>（若不修改则留空）</td>
							<td class="pn-info"></td>
						</tr>
						<tr>
							<th>重复密码</th>
							<td class="pn-fcontent"><input type="password" style="width: 200px;"
								name="admin.password2" equalTo="#password" /></td>
							<td class="pn-info"></td>
						</tr>
						<tr>
							<th>性&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;别</th>
							<td class="pn-fcontent"><label class="inline"> <input
									type="radio" name="admin.sex" value="男" checked /><i
									class="icon-male"></i></label> <label class="inline"> <input
									type="radio" name="admin.sex" value="女"
									<c:if test="${admin.sex=='女'}">checked</c:if> /><i
									class="icon-female"></i></label></td>
							<td class="pn-info"></td>
						</tr>
						<tr>
							<th>邮&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;箱</th>
							<td class="pn-fcontent"><input type="text" style="width: 200px;"
								name="admin.email" value="${admin.email}" maxlength="50"/></td>
							<td class="pn-info"></td>
						</tr>
						<tr>
							<th>手&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;机</th>
							<td class="pn-fcontent"><input type="text" style="width: 200px;"
								name="admin.phone" value="${admin.phone}" maxlength="20" /></td>
							<td class="pn-info"></td>
						</tr>
						<tr>
							<th>身份证号</th>
							<td class="pn-fcontent">
								<input style="width: 200px;" type="text" name="admin.idCard" value="${admin.idCard}" maxlength="20"/></td>
							<td class="pn-info"></td>
						</tr>
						<tr>
							<th>用户角色</th>
							<td class="pn-fcontent">
								<c:forEach items="${roleList}" var="item">
									<input style="width: 30px; border: 0;" type="radio" name="roles" value="${item.id}"
					                    <c:if test="${admin.role.id==item.id}">checked</c:if> />${item.name}
	                        	</c:forEach>
	                        </td>
	                        <td class="pn-info"></td>
						</tr>
					</tbody>
				</table>
				<div class="widget-bottom">
					<input type="hidden" name="admin.id" value="${admin.id}" /> 
					<input type="hidden" name="admin.createTime" value="${admin.createTime}" />
					<input type="hidden" name="admin.lastIp" value="${admin.lastIp}" />
					<input type="hidden" name="admin.lastTime" value="${admin.lastTime}" /> 
					<input type="hidden" name="admin.valid" value="${admin.valid}" /> 
					<center>
						<input class="btn btn-primary pull-center" type="submit" value="保 存" />&nbsp;
					</center>
				</div>
			</div>
			<!-- /widget-content -->
		</div>

	</form>
</body>
</html>