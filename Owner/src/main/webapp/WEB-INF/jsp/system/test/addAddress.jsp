<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html>
<head>
<title>OBD设备EXCEL导入</title>
<jsp:include page="../../include/common.jsp" />
<style type="text/css">
.query_hint {
	display: none;   
    position: absolute;   
    top: 0%;   
    left: 0%;   
    width: 100%;   
    height: 100%;   
    background-color: gray;   
    z-index:1001;   
    -moz-opacity: 0.8;   
    opacity:.80;   
    filter: alpha(opacity=88); 
    font-size: 15px;
    font-weight: bold;
	text-align: center;
	line-height: 200px;
    color: red;
}
</style>
<script language="javascript">
$(document).ready(function() {
	$("#myForm").validate({
		rules: {
            'obdexcel' : {
                addOBD: true
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
});

function showWait(){
	var excelInput=$("#addressExcel").val();
	var version=$("#version").val();
	console.info(excelInput):
		
	if(excelInput==''||excelInput==undefined || excelInput==null){
		alert("请上传文件.");
		return false;
	}
	if(version==''){
		alert("版本号不能为空.");
		return false;
	}
	
	$("#query_hint").css('display','block');
	return true;
}
</script>
</head>
<body>
	<form id="myForm" name="myForm" method="post" action="${basePath}/admin/batchSet_excel.do"
		enctype="multipart/form-data">
		<ul class="breadcrumb">
			<li><i class="icon-home icon-2x"></i></li>
			<li>当前位置：${currentPosition}&nbsp;>&nbsp;OBD地址参数EXCEL导入</li>
		</ul>
		<div class="widget widget-edit">
			<div class="widget-content">
				<table  align="center">
					<tbody>
						<tr>
							<th>文件</th>
							<td class="pn-fcontent">
								<input type="file" id="addressExcel" name="addressExcel" required/>
							</td>
						</tr>
						<tr>
							<th>版本号</th>
							<td class="pn-fcontent">
								<input name="version" id="version" value="${version}"/>
							</td>
						</tr>
					</tbody>
				</table>
				<br />
				<div class="widget-bottom">
					<center>
						<input class="btn btn-primary pull-center" id="editUserbt" type="submit" value="保 存" onclick="showWait()"/>&nbsp;
						<input class="btn btn-primary pull-center" id='bt' type="button" value="返回" onclick="javascript:window.history.back('-2');"/>&nbsp;
					</center>
				</div>
			</div>
		</div>

	</form>
	
	<div id="query_hint" class="query_hint">
		<img src="../images/wait.gif" />正在上传，请稍等．．．
	</div>
</body>
</html>