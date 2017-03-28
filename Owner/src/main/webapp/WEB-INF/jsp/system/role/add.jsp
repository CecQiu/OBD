<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html>
<head>
<title></title>
<link rel="stylesheet" type="text/css"
	href="${basePath}/css/bootstrap.min.css" />
<link rel="stylesheet" type="text/css" href="${basePath}/css/style.css" />
<link rel="stylesheet" type="text/css"
	href="${basePath}/theme/${session.theme}/main.css" />
<link type="text/css" rel="stylesheet" href="${basePath}/css/dtree.css" />
<script language="javascript" src="${basePath}/js/jquery.min.js"></script>
<script type="text/javascript" src="${basePath}/js/util.js"></script>
<script type="text/javascript" src="${basePath}/js/dtree.js"></script>
<script type="text/javascript">
	function check() {
		if (get("role.name").value == "") {
			alert("角色名称不能为空，请重新输入");
			get("role.name").focus();
			return;
		}
		document.myForm.submit();
	}
</script>
</head>
<body>

	<form id="myForm" name="myForm" method="post" action="role_save.do">
		<ul class="breadcrumb">
			<li><i class="icon-home icon-2x"></i></li>
			<li>当前位置：${currentPosition}&nbsp;>&nbsp;添加角色权限</li>
		</ul>
		<div class="widget widget-edit">
			<div class="widget-content">
				<table class="pn-ftable table-bordered table-condensed" border="0" cellpadding="10">
					<tbody>
						<tr>
							<th>角色名称：</th>
							<td class="pn-fcontent"><input type="text" name="role.name" /></td>
							<td class="pn-info"></td>
						</tr>
						<tr>
							<th>备&nbsp;&nbsp;&nbsp;&nbsp;注：</th>
							<td class="pn-fcontent">
							<textarea name="role.remark" rows="3" cols="50"></textarea>
							</td>
							<td class="pn-info"></td>
						</tr>
						<tr>
							<th>角色权限：</th>
							<td class="pn-fcontent">
								<div class="dtree">
									<script type="text/javascript">
										var dtree = new Array();
										var i = 1;
										<c:forEach items="${list}" var="module">
										dtree[i] = new Array();
										dtree[i][0] = "${module.id}";
										dtree[i][1] = "${module.parent==null?0:module.parent.id}";
										dtree[i][2] = "${module.name}";
										i++;
										</c:forEach>
										d = new dTree('d');
										d.config.check = true;
										d.config.inputName = "modules";
										d.add(0, -1, "功能菜单");
										for ( var i = 1; i < dtree.length; i++) {
											d.add(dtree[i][0], dtree[i][1],
													dtree[i][2]);
										}
										document.write(d);
										d.openAll();
									</script>
								</div>
							</td>
							<td class="pn-info"></td>
						</tr>
					</tbody>
				</table>
				<div class="widget-bottom">
					<center>
						<input class="btn btn-primary pull-center" type="button" onclick="check()" value="保 存" />&nbsp;
						<input class="btn btn-danger pull-center" type="button" value="返 回" onclick="history.go(-1);"/>	
					</center>
				</div>
			</div>
		</div>
	</form>
</body>
</html>