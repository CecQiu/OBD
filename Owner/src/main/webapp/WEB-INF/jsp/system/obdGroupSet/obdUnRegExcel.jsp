<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html>
<head>
<title>未激活设备设置默认分组-->EXCEL导入</title>
<jsp:include page="../../include/common.jsp" />
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

</script>
</head>
<body>
	<form id="myForm" name="myForm" method="post" action="${basePath}/admin/obdGroup_obdUnRegExcel.do"
		enctype="multipart/form-data">
		<ul class="breadcrumb">
			<li><i class="icon-home icon-2x"></i></li>
			<li>当前位置：${currentPosition}&nbsp;>&nbsp;未激活设备设置默认分组——>EXCEL升级导入</li>
		</ul>
		<div class="widget widget-table">
			<div class="widget-header">
				<i class="icon-th-list"></i>
				<h5>obd分组信息</h5>
			</div>
			<div>
			<table class="table table-striped table-bordered table-condensed table-hover sortable">
				<tr>
					<th>编号：</th>
					<td>${obdGroup.groupNum }</td>
					<td width="15%"/>
					<th>名称：</th>
					<td>${obdGroup.groupName }</td>
				</tr>
				<tr>
					<th>经度：</th>
					<td>${obdGroup.longitude}</td>
					<td width="15%"/>
					<th>纬度：</th>
					<td>${obdGroup.latitude}</td>
				</tr>
				<tr>
					<th>半径：</th>
					<td>${obdGroup.radius}</td>
					<td width="15%"/>
					<th>创建时间：</th>
					<td><fmt:formatDate value="${obdGroup.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				</tr>
				<tr>
					<th>备注：</th>
					<td colspan="4" style="text-align: left;">
						<textarea rows="3" cols="80" readonly="readonly">${obdGroup.remark }</textarea>
					</td>
				</tr>
			</table>
			</div>
		</div>
		<div class="widget widget-edit">
			<div class="widget-content">
				<table  align="center">
					<tbody>
						<tr>
							<th>文件：</th>
							<td class="pn-fcontent">
								<input type="file" id="obdexcel" name="obdexcel" required/>
							</td>
						</tr>
					</tbody>
				</table>
				<input name="obdGroup.id" value="${obdGroup.id }" style ="display:none;"/>
				<br />
				<div class="widget-bottom">
					<center>
						<input class="btn btn-primary pull-center" id="editUserbt" type="submit" value="保 存" />&nbsp;
						<input class="btn btn-primary pull-center" type="button" value="返回" onclick="javascript:window.history.back('-2');"/>&nbsp;
					</center>
				</div>
			</div>
		</div>

	</form>
</body>
</html>