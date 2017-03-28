<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html>
<head>
<title>OBD设备EXCEL导入</title>
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
	<form id="myForm" name="myForm" method="post" action="${basePath}/admin/carOwnerInventory_obdExcel.do"
		enctype="multipart/form-data">
		<ul class="breadcrumb">
			<li><i class="icon-home icon-2x"></i></li>
			<li>当前位置：${currentPosition}&nbsp;>&nbsp;OBD设备EXCEL导入</li>
		</ul>
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
				<br />
				<div class="widget-bottom">
					<center>
						<input class="btn btn-primary pull-center" id="editUserbt" type="submit" value="保 存" />&nbsp;
						<input class="btn btn-primary pull-center" type="button" value="返回" onclick="javascript:window.history.back('-1');"/>&nbsp;
					</center>
				</div>
			</div>
		</div>

	</form>
</body>
</html>