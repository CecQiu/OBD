<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html>
<head>
<title>修改栅栏区域</title>
<script language="javascript" src="${basePath}/js/jquery.min.js"></script>
<jsp:include page="../../include/common.jsp" />
<script language="javascript">
function reback(){	
	window.parent.hideIframe();//隐藏
	//清空表单内容
	$("#mebUserForm :input").not(":button, :submit, :reset, :hidden").val("").removeAttr("checked").remove("selected");//核心
}

function update(){
 	var obdSn = $('#obdSn').text();
 	var valid = $('input[name=valid]:checked').val();
 	$.post('${basePath}/admin/carLine_update.do',{obdSn:obdSn,valid:valid},function(data){
 		if(data == 'ok'){
 			alert('修改成功！');
 		}else if(data == 'fail'){
 			alert('修改失败！');
 		}else{
 			alert('未知错误！');
 		}
 		window.parent.hideIframe();
 		window.parent.location.reload();
 	});
}

$(document).ready(function() {
	 $("select[@name=city] option").each(function(){
		   /* if($(this).val() == 111){
		    $(this).remove();
		   } */
		   if(this.value=='${mebUser.city}'){
			   this.selected="selected";
		   }
	});
	 $("select[@name=letter] option").each(function(){
		   /* if($(this).val() == 111){
		    $(this).remove();
		   } */
		   if(this.value=='${mebUser.letter}'){
			   this.selected="selected";
		   }
	});
});
</script>
</head>
<body>
	<form id="mebUserForm" >
		<ul class="breadcrumb">
			<li><i class="icon-home icon-2x"></i></li>
			<li>当前位置：${currentPosition}&nbsp;>&nbsp;修改栅栏区域</li>
		</ul>
		 <div class="widget widget-edit">
			<div class="widget-content">
			<c:forEach items="${map }" var="item" >
				<table  align="center">
					<tbody>
						<tr>
							<th>手机号码</th>
							<td class="pn-fcontent">
							<span id="mobileNumber">${item.key.mobileNumber }</span>
							</td>
						</tr>
						<tr>
							<th>智能盒激活码</th>
							<td class="pn-info">
							<span id="obdSn">${item.key.obdSN }</span>
							</td>
						</tr>
						<tr>
							<th>车辆编号</th>
							<td class="pn-info">
							<span id="carId">${item.key.carId }</span>
							</td>
						</tr>
						<tr>
							<th>车牌号码</th>						
							<td class="pn-info">
							<span id="license">${item.key.license }</span>
							</td>					
						</tr>
						<tr>
							<th>是否设定</th>
							<td class="pn-info">
								<c:if test="${empty item.value}">
									否    <a href="#" onclick="window.parent.location.href='${basePath}/admin/carLine_carTravelArea.do?obdSn=${item.key.obdSN}'">去设定</a>
								</c:if>
								<c:if test="${!empty item.value}">
									是
								</c:if>
							</td>
						</tr>
						<tr>
							<th>是否有效</th>
							<td class="pn-info" style="color: red;">
								<c:if test="${empty item.value}">
								无效<input type="radio" name="valid" value="1" checked="checked"/>
								</c:if>
								<c:if test="${ item.value.valid == 1}">
								有效<input type="radio" name="valid" value="0"/>
								无效<input type="radio" name="valid" value="1" checked="checked"/>
								</c:if>
								<c:if test="${!empty item.value && item.value.valid == 0}">
								有效<input type="radio" name="valid" value="0" checked="checked"/>
								无效<input type="radio" name="valid" value="1"/>
								</c:if>
							</td>
						</tr>
					</tbody>
				</table>
				
				<div class="widget-bottom">
					<input type="hidden" name="mebUser.regUserId" value="iiiii9" />  
					<center>
						<c:if test="${!empty item.value}">
							<input class="btn btn-primary pull-center" id="editUserbt" type="button" value="更 新" onclick="update()" />&nbsp;
						</c:if>
						<input class="btn btn-primary pull-center" type="button" value="返 回" onclick="reback()"/>&nbsp;
					</center>
				</div>
			</c:forEach>
			</div>
			<!-- /widget-content -->
		</div>
	</form>
</body>
</html>