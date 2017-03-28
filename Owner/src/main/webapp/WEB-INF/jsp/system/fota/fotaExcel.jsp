<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html>
<head>
<title>修改用户信息</title>
<jsp:include page="../../include/common.jsp" />
<script language="javascript">
	$(document).ready(function() {
		$('#save').click(function() {
			$('#myForm').submit();
		});
	});

	function fotaCheck() {
		var fileName = $("#fileName").val();
		var version = $("#version").val();
		var batchVersion = $("#batchVersion").val();
		var ftpIP = $("#ftpIP").val();
		var ftpPort = $("#ftpPort").val();
		var ftpUsername = $("#ftpUsername").val();
		var ftpPwd = $("#ftpPwd").val();
		var file = $("#obdexcel").val();

		if (fileName == '' || version == '' || batchVersion == ''
				|| ftpIP == '' || ftpPort == '' || ftpUsername == ''
				|| ftpPwd == '' || file == '') {
			alert("参数不能为空.");
			return false;
		}

		if (version.length > 50 ) {
			alert('版本长度不能大于50');
			return false;
		} 
		/* 
		if (!version ||version.length != 4 || (!(/^\d{4}$/).test(version))) {
			alert('版本必须为：四位数字，如：0000');
			return false;
		} 
		*/
		
		if (!batchVersion || (!(/^\d{4}$/).test(batchVersion))) {
			alert('批号必须为：四位数字，如：0000');
			return false;
		}
		var regex= new RegExp("(^[1-9]$)|(^[1-9][0-9]$)|(^[1-9][0-9][0-9]$)|^([1-9][0-9][0-9][0-9]$)|(^[1-6][0-5][0-5][0-3][0-5]$)");
		
		//var regex= new RegExp("/^[1-9]$|(^[1-9][0-9]$)|(^[1-9][0-9][0-9]$)|(^[1-9][0-9][0-9][0-9]$)|(^[1-6][0-5][0-5][0-3][0-5]$)/");

		var portFlag = regex.test(ftpPort);
		if (!portFlag) {
			alert('ftp端口必须为1-65535');
			return false;
		}
		var excelName = file.substring(file.lastIndexOf(".") + 1, file.length);
		if (excelName == 'xls' || excelName == 'xlsx') {

		} else {
			alert("上传excel文件格式不正确.");
			return false;
		}
		return true;
	}
</script>
</head>
<body>
	<form id="myForm" name="myForm" method="post"
		action="fotaSet_obdExcel.do" enctype="multipart/form-data"
		onsubmit="return fotaCheck();">
		<ul class="breadcrumb">
			<li><i class="icon-home icon-2x"></i></li>
			<li>当前位置：${currentPosition}&nbsp;>&nbsp;FOTA待升级列表上传</li>
		</ul>
		<div class="widget widget-edit">
			<div class="widget-content">
				<table align="center">
					<tbody>
						<tr>
							<th>版本号：</th>
							<td class="pn-fcontent"><input type="text"
								name="fotaSet.version" style="width: 200px;"
								value="${fotaSet.version }" maxlength="50" id="version" /></td>
							<td class="pn-info"></td>
						</tr>
						<tr>
							<th>上传批号：</th>
							<td class="pn-fcontent"><input type="text"
								name="fotaSet.batchVersion" style="width: 200px;"
								value="${fotaSet.batchVersion }" maxlength="20"
								id="batchVersion" /></td>
							<td class="pn-info"></td>
						</tr>
						<tr>
							<th>文件名：</th>
							<td class="pn-fcontent"><input type="text"
								style="width: 200px;" name="fotaSet.fileName"
								value="${fotaSet.fileName }" id="fileName" /></td>
							<td class="pn-info"></td>
						</tr>

						<tr>
							<th>ftp地址：</th>
							<td class="pn-fcontent"><input type="text"
								name="fotaSet.ftpIP" style="width: 200px;"
								value="${fotaSet.ftpIP }" maxlength="20" id="ftpIP" /></td>
							<td class="pn-info"></td>
						</tr>
						<tr>
							<th>ftp端口：</th>
							<td class="pn-fcontent"><input type="text"
								name="fotaSet.ftpPort" style="width: 200px;"
								value="${fotaSet.ftpPort }" maxlength="20" id="ftpPort" /></td>
							<td class="pn-info"></td>
						</tr>
						<tr>
							<th>ftp用户名：</th>
							<td class="pn-fcontent"><input type="text"
								name="fotaSet.ftpUsername" style="width: 200px;"
								value="${fotaSet.ftpUsername }" maxlength="20" id="ftpUsername" /></td>
							<td class="pn-info"></td>
						</tr>
						<tr>
							<th>ftp密码：</th>
							<td class="pn-fcontent"><input type="text"
								name="fotaSet.ftpPwd" style="width: 200px;"
								value="${fotaSet.ftpPwd }" maxlength="20" id="ftpPwd" /></td>
							<td class="pn-info"></td>
						</tr>
						<tr>
							<th>设备号列表文件：</th>
							<td class="pn-fcontent"><input type="file" id="obdexcel"
								name="obdexcel" /></td>
						</tr>
					</tbody>
				</table>
				<div class="widget-bottom">
					<!-- <input type="hidden" name="mebUser.regUserId" value="" /> -->
					<center>
						<input class="btn btn-primary pull-center" id="save" type="button"
							value="保 存" />
					</center>
				</div>
			</div>
			<!-- /widget-content -->
		</div>

	</form>
</body>
</html>