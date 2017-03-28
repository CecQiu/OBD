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
	$("#myForm").validate({
		rules: {
			'obdUpgrade.fileName' : {
				required: true,  
                maxlength: 50
			},
			'obdUpgrade.version' : {
                required: true,  
                maxlength: 20
            },  
            'updwj' : {
                required: "${editState}" == 0 ? true : false,
                obdUpgradeFile: true
            }  
        },  
        
        //指定错误信息位置  
        errorPlacement: function (error, element) {
            if (element.is(':radio') || element.is(':checkbox')) {  //如果是radio或checkbox  
                var eid = element.attr('name');                     //获取元素的name属性  
                error.appendTo(element.parent());                   //将错误信息添加当前元素的父结点后面  
            } else {
                error.insertAfter(element);  
            }  
        }
	});
	
	//表单校验
	$('#save').click(function(){
		var version=$("#version").val();
		var fileName = $("#fileName").val(); 
		var firmType=$("input[name='obdUpgrade.firmType']:checked").val();
		
		//console.info(firmType);
		if(version==''||version==null||version==undefined 
				|| fileName=='' || fileName==null || fileName==undefined 
				|| firmType==null || firmType == undefined){
			alert("文件名或版本号或文件类型不能为空.");
			return false;
		}
		
		//如果文件名不是以.BIN结尾
		/* if(!fileName.endWith(".BIN")){
			alert("固件名称必须以.BIN结尾.");
			return false;
		} */
		
		//校验文件名
		//var reg =/^(?=(?:[^-]+-){4,5}[^\.-]+\.)[-a-zA-Z\d]+\.BIN$/;
		//var reg=/^([A-Za-z0-9]+-){4,5}\d{8}\.BIN$/;
		var fileNameReg = new RegExp("^([A-Z0-9]+-)(APP|IAP)-([A-Z0-9]+-)([A-Z0-9]{4}-)([A-Z0-9]+-)?\\d{8}\\.BIN$");
		if(!fileNameReg.test(fileName)){
			alert("固件名称不符合规范,请检查.");
			return false;
		}
		
		var firmT = fileName.split("-");
		if(firmType=='0' && firmT[1]!='APP'){
			alert("固件类型错误,请上传APP固件.");
			return false;
		}
		
		if(firmType=='1' && firmT[1]!='IAP'){
			alert("固件类型错误,请上传IAP固件.");
			return false;
		}
		
		//判断版本类型是否一直
		/* var re = new RegExp("-","g");
		var btotal = fileName.match(re); */
		
		
		if(!version || version.length != 4 || (!(/^\d{4}$/).test(version))){
			alert('版本必须为：四位数字，如：0010');
			return;
		}
		
		$('#myForm').submit();
	});
});

function setName() {
	var file = $("#updwj").val();
	//console.info(file);
	
	var strFileName = getFileName(file);
	//var strFileName=file.replace(/^.+?\\([^\\]+?)(\.[^\.\\]*?)?$/gi,"$1");  //正则表达式获取文件名，不带后缀
	//var fileExt=file.replace(/.+\./,"");   //正则表达式获取后缀
	
	//文件名校验
	var fileNameReg = new RegExp("^([A-Z0-9]+-)(APP|IAP)-([A-Z0-9]+-)([A-Z0-9]{4}-)([A-Z0-9]+-)?\\d{8}\\.BIN$");
	if(!fileNameReg.test(strFileName)){
		alert("固件名称不符合规范,请检查.");
		return false;
	}
	
	$("#fileName").val(strFileName);
	var firmVersion = strFileName.split("-");
	$("#firmVersion").val(firmVersion[3]);
}

function versionCheck(){
	var version=$("#version").val();
	
	var flag = true;
	//异步判断是否存在当前版本号
	$.ajax({
		url:'${basePath}/admin/odbUpgrade_versionCheck.do',
		data:{
			'obdUpgrade.version':version
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
			alert("版本号校验异常,请稍后重试.");
			flag = false;
		}
	});
	return flag;
	 
}

String.prototype.endWith=function(endStr){
  var d=this.length-endStr.length;
  return (d>=0&&this.lastIndexOf(endStr)==d)
}


function getFileName(o){
    var pos=o.lastIndexOf("\\");
    return o.substring(pos+1);  
}
</script>
</head>
<body>
	<form id="myForm" name="myForm" method="post" action="odbUpgrade_save.do"
		enctype="multipart/form-data"
		onsubmit="return versionCheck();">
		<ul class="breadcrumb">
			<li><i class="icon-home icon-2x"></i></li>
			<li>当前位置：${currentPosition}&nbsp;>&nbsp;新增obd设备升级文件</li>
		</ul>
		<div class="widget widget-edit">
			<div class="widget-content">
			<input type="hidden" value="${obdUpgrade.id }" name="obdUpgrade.id" />
				<table  align="center">
					<tbody>
						<tr>
							<th>文件名名：</th>
							<td class="pn-fcontent">
							<input type="text" style="width: 200px;"name="obdUpgrade.fileName" 
							value="${obdUpgrade.fileName }" id="fileName" readonly="readonly"/></td>
							<td class="pn-info"></td>
						</tr>
						<tr>
							<th>版本号：</th>
							<td class="pn-fcontent"><input type="text" name="obdUpgrade.version" style="width: 200px;" 
								value="${obdUpgrade.version }" maxlength="20" id="version" /><span style="color:red;">切记格式为：0010</span></td>
							<td class="pn-info"></td>
						</tr>
						<tr>
							<th>类型：</th>
							<td class="pn-fcontent">
								APP<input type="radio" name="obdUpgrade.firmType" value="0"/>&nbsp;
								IAP<input type="radio" name="obdUpgrade.firmType" value="1"/>&nbsp;&nbsp;&nbsp;<span style="color:red;">必选</span>
							</td>
							<td class="pn-info"></td>
						</tr>
						<tr>
							<th>固件版本号：</th>
							<td class="pn-fcontent"><input type="text" name="obdUpgrade.firmVersion" style="width: 200px;" 
								value="${obdUpgrade.firmVersion }" maxlength="20" id="firmVersion" readonly="readonly"/><span style="color:red;">切记格式为：000E,由数字或英文或数字加英文组成</span></td>
							<td class="pn-info"></td>
						</tr>
						<c:if test="${editState == 1 }">
							<tr>
								<th>文件大小：</th>
								<td class="pn-fcontent">
									<input type="text" name="obdUpgrade.size" value="${obdUpgrade.size }" readonly="readonly" />
								</td>
							</tr> 
							<tr>
								<th>上传时间：</th>
								<td class="pn-fcontent">
									<input type="text" name="obdUpgrade.createTime" value="${obdUpgrade.createTime }" readonly="readonly" />
								</td>
							</tr>
						</c:if>
						<tr>
							<th>文件：</th>
							<td class="pn-fcontent">
								<input type="file" id="updwj" name="updwj" onchange="setName()" />
							</td>
						</tr>
						<tr>
							<th>备注：</th>						
							<td class="pn-info">
								<textarea rows="3" cols="28" name="obdUpgrade.memo" >${obdUpgrade.memo }</textarea>
							</td> 						
						</tr>
					</tbody>
				</table>
				<div class="widget-bottom">
					<input type="hidden" name="mebUser.regUserId" value="" />  
					<center>
						<input class="btn btn-primary pull-center" id="save" type="button" value="保 存"/>&nbsp;
						<input class="btn btn-primary pull-center" type="button" value="返回" onclick="javascript:window.history.back('-1');"/>&nbsp;
					</center>
				</div>
			</div>
			<!-- /widget-content -->
		</div>

	</form>
</body>
</html>