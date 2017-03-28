<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html>
<head>
<title>add</title>
<jsp:include page="../../include/common.jsp" />
<script type="text/javascript" src="${basePath}/js/jsAddress.js"></script>
<script language="javascript">
	$().ready(function () {
		$("#myForm").validate({
	        rules: {
	            'info.systemName': {
	                required:true,
	                maxlength_T:20
	            },'info.minSysVersion':{
	            	required:true,
	            	maxlength_T:20
	            },'info.appVersion':{
	            	required:true,
	            	maxlength_T:20
	            },'info.updateContent':{
	            	maxlength_T:100
	            },'info.downloadUrl':{
	            	url:true
	            },'app':{
	            	isInstall:true
	            }
	        }
	    });
	});
	
	function resetForm(){
		var val = $('input[name="loadType"]:checked').val();
		if(val == 'local') {
			$("#upload").show();
			$("#myForm").attr('action','appUpload_upload.do');
			$("#urls").hide();
		} else {
			$("#upload").hide();
			$("#myForm").attr('action','appUpload_save.do');
			$("#urls").show();
		}
	}
	
	function check() {
		var val = $('input[name="loadType"]:checked').val();
		if(val == 'local') {
			if($("#app")=='') {
				alert("请选择要上传的APP安装包");
				return;
			}
		} else {
			if($("#urls")=='') {
				alert("填写下载路径");
				return;
			}
		}
		$("#myForm").submit();
	}
</script>
</head>
<body>
	<form id="myForm" name="myForm" method="post" action="appUpload_save.do" enctype="multipart/form-data">
		<ul class="breadcrumb">
			<li><i class="icon-home icon-2x"></i></li>
			<li>当前位置：${currentPosition}&nbsp;>&nbsp;添加APP版本信息</li>
		</ul>
		<div class="widget widget-edit">
			<div class="widget-content">
				<table class="pn-ftable table-bordered" cellpadding="5px">
					<tbody>
						<tr>
							<th>系统名称:</th>
							<td class="pn-fcontent">
								<input type="text" style="width: 500px" id="systemName" name="info.systemName" maxlength="20" value="${info.systemName}"/>
								<font color="red">*</font>
							</td>
						</tr>
						<tr>
							<th>系统最低版本:</th>
							<td class="pn-fcontent">
								<input id="minSysVersion" style="width: 500px" name="info.minSysVersion" value="${info.minSysVersion}" maxlength="15" type="text" />
								<font color="red">*</font>
							</td>
						</tr>
						<tr>
							<th>APP版本:</th>
							<td class="pn-fcontent">
								<input type="text" style="width: 500px" id="appVersion"  name="info.appVersion" maxlength="10" value="${info.appVersion}"/>
							</td>
						</tr>
						<tr id="urls">
							<th>下载路径:</th>
							<td class="pn-fcontent">
								<input type="text" maxlength="100" style="width: 500px" name="info.downloadUrl" id="downloadUrl" />
							</td>
						</tr>
						<tr>
							<th>更新内容:</th>
							<td class="pn-fcontent">
								<textarea rows="4" style="width: 500px" name="info.updateContent" id="updateContent">${info.updateContent}</textarea>
							</td>
						</tr>
						<tr>
							<th>选择上传方式:</th>
							<td class="pn-fcontent">
								<input type="radio" id="local" name="loadType" value="local" onchange="resetForm();"/>上传到本地
								<input type="radio" id="remote" name="loadType" value="remote" checked="checked" onchange="resetForm();"/>已上传到远程
							</td>
						</tr>
						<tr style="display: none;" id="upload">
							<th>上传文件:</th>
							<td class="pn-fcontent">
								<input type="file" style="width: 500px" id="app" name="app"/>
							</td>
						</tr>
					</tbody>
				</table>
				<div class="widget-bottom">
					<center>
						<a onclick="check();" href="javascript:;" class="btn btn-primary">保 存</a>
						<a href="javascript:window.location.href='appUpload_list.do';" class="btn btn-danger">返 回</a>
					</center>
				</div>
			</div>
		</div>
	</form>
</body>
</html>