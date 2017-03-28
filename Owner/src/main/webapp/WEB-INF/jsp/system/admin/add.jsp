<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<title>add</title>
    <jsp:include page="../../include/common.jsp" />
<script>
	function changeOrg() {
		var obj = arguments[0];
		var val = obj.value;

		var areaSel = document.getElementById("areaSel");
		areaSel.options.length = 0;//删除所有选项option
		areaSel.options.add(new Option("请选择", "")); //这个兼容IE与firefox

		var customSel = document.getElementById("customSel");
		customSel.options.length = 0;//删除所有选项option
		customSel.options.add(new Option("请选择", "")); //这个兼容IE与firefox

		if (val == "") {
			return;
		}

		$.post('admin_changeOrg.do', {
			orgid : val
		}, function(data) {
			//alert(data);
			if (data.msg) {
				var arr = data.msg;
				var len = arr.length;

				for ( var i = 0; i < len; i++) {
					//添加一个选项
					//obj.add(new Option(arr.name,arr.id));    //这个只能在IE中有效
					areaSel.options.add(new Option(arr[i].name, arr[i].id)); //这个兼容IE与firefox
					//当请求第一个时，同时加载营业厅数据
					//if(i==0)
					//{
					// changeArea(areaSel);
					//}

				}
			} else {
				alert("系统提示：程序内部错误!");
			}
		}, 'json');
	}

	function changeArea() {
		var obj = arguments[0];
		var val = obj.value;
		var customSel = document.getElementById("customSel");
		customSel.options.length = 0;//删除所有选项option

		if (val == "") {
			customSel.options.add(new Option("请选择", "")); //这个兼容IE与firefox
			return;
		}

		$.post('admin_changeArea.do', {
			areaId : val
		}, function(data) {
			//alert(data);
			if (data.msg) {
				var arr = data.msg;
				var len = arr.length;
				if (len == 0) {
					customSel.options.add(new Option("请选择", "")); //这个兼容IE与firefox
				} else {
					for ( var i = 0; i < arr.length; i++) {
						//添加一个选项
						//obj.add(new Option(arr.name,arr.id));    //这个只能在IE中有效
						customSel.options
								.add(new Option(arr[i].name, arr[i].id)); //这个兼容IE与firefox
					}
				}

			} else {
				alert("系统提示：程序内部错误!");
			}
		}, 'json');
	}
	$().ready(function() {
		$("#myForm").validate({
			 rules: {
				 'admin.phone': {
			    	mobile:true
			    },'admin.email': {
			    	email:true
			    },'admin.idCard': {
			    	UID:true
			    }
			  }
			});
	});
</script>
</head>
<body>
	<form id="myForm" name="myForm" method="post" action="admin_save.do">
		<ul class="breadcrumb">
			<li><i class="icon-home icon-2x"></i></li>
			<li>当前位置：${currentPosition}&nbsp;>&nbsp;添加系统管理员</li>
		</ul>
		<div class="widget widget-edit">
			<div class="widget-content">
				<table class="pn-ftable table-bordered table-condensed" border="0" cellpadding="10">
					<tbody>
						<tr>
							<th>登&nbsp;&nbsp;录&nbsp;&nbsp;名</th>
							<td class="pn-fcontent"><input type="text" style="width: 200px;"
								name="admin.username" required maxlength="20" /></td>
							<td class="pn-info"><c:if test="${!empty usernameIsExists}">
									<label for="admin.username" generated="true" class="error">${usernameIsExists}</label>
								</c:if></td>
						</tr>
						<tr>
							<th>真实姓名</th>
							<td class="pn-fcontent"><input style="width: 200px;" type="text" name="admin.name" required maxlength="20"/></td>
							<td class="pn-info"></td>
						</tr>
						<tr>
							<th>密&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;码</th>
							<td class="pn-fcontent"><input style="width: 200px;" type="password" id="password"
								name="admin.password" minlength="6" required maxlength="32"/></td>
							<td class="pn-info"></td>
						</tr>
						<tr>
							<th>重复密码</th>
							<td class="pn-fcontent"><input style="width: 200px;" type="password"
								name="admin.password2" equalTo="#password"
								minlength="6" /></td>
							<td class="pn-info"></td>
						</tr>
						<tr>
							<th>性&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;别</th>
							<td class="pn-fcontent"><label class="inline"> <input
									 type="radio" name="admin.sex"
									value="男" checked />男<i class="icon-male"></i></label> <label
								class="inline"> <input 
									type="radio" name="admin.sex" value="女" />女<i
									class="icon-female"></i></label></td>
							<td class="pn-info"></td>
						</tr>
						<tr>
							<th>邮&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;箱</th>
							<td class="pn-fcontent"><input type="text" style="width: 200px;"
								name="admin.email" maxlength="50" /></td>
							<td class="pn-info"></td>
						</tr>
						<tr>
							<th>手&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;机</th>
							<td class="pn-fcontent"><input style="width: 200px;" type="text"
								name="admin.phone" maxlength="20" /></td>
							<td class="pn-info"></td>
						</tr>
						<tr>
							<th>身份证号</th>
							<td class="pn-fcontent">
								<input style="width: 200px;" type="text" name="admin.idCard" maxlength="18"/></td>
							<td class="pn-info"></td>
						</tr>
						<tr>
							<th>用户角色：</th>
							<td class="pn-fcontent">
								<c:forEach items="${roleList}" var="item">
									<input style="width: 30px; border: 0;" type="radio" name="admin.role.id" value="${item.id}" />${item.name}
								</c:forEach>
							</td>
							<td class="pn-info"></td>
						</tr>
						<tr>
							<th>是否禁用</th>
							<td class="pn-fcontent"><label class="inline"> <input
									 type="radio" name="admin.valid"
									value="1" checked />否
							</label> <label class="inline"> <input
									 type="radio" name="admin.valid"
									value="0" />是
							</label></td>
							<td class="pn-info"></td>
						</tr>
					</tbody>
				</table>
				<div class="widget-bottom">
					<center>
						<input class="btn btn-primary pull-center" type="submit" value="保存" />&nbsp;
						<input class="btn btn-danger pull-center" type="button" value="返回" onclick="history.go(-1);"/>	
					</center>
				</div>
			</div>
			<!-- /widget-content -->
		</div>
	</form>
</body>
</html>