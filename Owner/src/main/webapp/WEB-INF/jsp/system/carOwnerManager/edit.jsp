<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html>
<head>
<title>修改用户信息</title>
<script language="javascript" src="${basePath}/js/jquery.min.js"></script>
<jsp:include page="../../include/common.jsp" />
<script language="javascript">
function reback(){	
	window.parent.hideIframe();//隐藏
	//清空表单内容
	$("#mebUserForm :input").not(":button, :submit, :reset, :hidden").val("").removeAttr("checked").remove("selected");//核心
}

function check(){
// 利用正则表达式对输入数据匹配
   var mobileNumber=document.getElementById("mebUser.mobileNumber").value;   
   var number=document.getElementById("number").value;
   var flag=true;
   //匹配到一个非数字字符，则返回false 
   var expr =/\D/i;
   if(expr.test(mobileNumber)){	
	   $('#mobileNumberError')[0].innerHTML = "<font color=red size=2>电话号码不能为非数字!</font>";
	   flag=false;
   }
   if(mobileNumber.length!=11){	
	     $('#mobileNumberError')[0].innerHTML = "<font color=red size=2>电话号码只能为11位数字!</font>";
	     flag=false;
	 }
   if(number.length!=5){	
	     $('#numberError')[0].innerHTML = "<font color=red size=2>车牌号只能输入5位!</font>";
	     flag=false;
	 }
  var city=document.getElementById("city").value;
  var letter=document.getElementById("letter").value;
  var number=document.getElementById("number").value;
  document.getElementById("license").value=city+letter+number;
  if(flag==true){
	  alert("修改成功！");
	  $('#mebUserForm').submit();
	  window.parent.hideIframe();
	  window.parent.location.reload();
  }
  
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
	<form id="mebUserForm" method="post" action="${basePath}/admin/carOwnerManager_update.do">
		<ul class="breadcrumb">
			<li><i class="icon-home icon-2x"></i></li>
			<li>当前位置：${currentPosition}&nbsp;>&nbsp;修改用户信息</li>
		</ul>
		<div class="widget widget-edit">
			<div class="widget-content">
				<table  align="center">
					<tbody>
						<tr>
							<th>真实姓名</th>
							<td class="pn-fcontent">
							<input type="text" style="width: 200px;"name="mebUser.name" id="mebUser.name"
							value="${mebUser.name}" maxlength="20"/></td>
							<td class="pn-info"></td>
						</tr>
						<tr>
							<th>性&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;别</th>
							<td class="pn-fcontent">
								<label class="inline">
								<c:if test="${mebUser.sex=='女'}">
									男<input type="radio" name="mebUser.sex" value="男" />
									女<input type="radio" name="mebUser.sex" value="女" checked="checked" />
								</c:if>
								<c:if test="${mebUser.sex=='男'}">
									男<input type="radio" name="mebUser.sex" value="男" checked="checked"/>
									女<input type="radio" name="mebUser.sex" value="女" />
								</c:if>
								</label>
							</td>
							<td class="pn-info"></td>
						</tr>
				<%-- 		<tr>
							<th>登录密码</th>
							<td class="pn-fcontent"><input type="password" style="width: 200px;"
								name="mebUser.password" value="${mebUser.password}" maxlength="50"/></td>
							<td class="pn-info"></td>
						</tr> --%>
					<%-- 	<tr>
							<th>支付密码</th>
							<td class="pn-fcontent"><input type="password" style="width: 200px;"
								name="mebUser.payPassword" value="${mebUser.payPassword}" maxlength="50"/></td>
							<td class="pn-info"></td>
						</tr> --%>
						<tr>
							<th>手机号码</th>
							<td class="pn-fcontent">
							<input type="text" style="width: 200px;"name="mebUser.mobileNumber" id="mebUser.mobileNumber"
							value="${mebUser.mobileNumber}" maxlength="20"/>
							<span id="mobileNumberError"></span>
							</td>
						</tr>
						<tr>
							<th>智能盒激活码</th>
							<td class="pn-fcontent"><input type="text" name="mebUser.obdSN" readonly="readonly" style="width: 200px;"
								value="${mebUser.obdSN}" maxlength="20"/></td>
							<td class="pn-info"></td>
						</tr>
						<tr>
							<th>车辆编号</th>
							<td class="pn-fcontent"><input type="text" style="width: 200px;" id="mebUser.carId"
								name="mebUser.carId" value="${mebUser.carId}" maxlength="32" readonly="readonly"/></td>
							<td class="pn-info"></td>
						</tr>
						<tr>
							<th>车牌号码</th>						
							<td class="pn-info">
							<div><select style="width:60px;" id="city" name="city">
								<option id="cityCon">京</option>
								<option id="cityCon">津</option>
								<option id="cityCon">沪</option>
								<option id="cityCon">渝</option>
								<option id="cityCon">粤</option>
								<option id="cityCon">冀</option>
								<option id="cityCon">豫</option>
								<option id="cityCon">云</option>
								<option id="cityCon">辽</option>
								<option id="cityCon">黑</option>
								<option id="cityCon">湘</option>
								<option id="cityCon">皖</option>
								<option id="cityCon">鲁</option>
								<option id="cityCon">新</option>
								<option id="cityCon">苏</option>
								<option id="cityCon">浙</option>
								<option id="cityCon">赣</option>
								<option id="cityCon">鄂</option>
								<option id="cityCon">桂</option>
								<option id="cityCon">甘</option>
								<option id="cityCon">晋</option>
								<option id="cityCon">蒙</option>
								<option id="cityCon">陕</option>
								<option id="cityCon">吉</option>
								<option id="cityCon">闽</option>
								<option id="cityCon">贵</option>
								<option id="cityCon">青</option>
								<option id="cityCon">藏</option>
								<option id="cityCon">川</option>
								<option id="cityCon">宁</option>
								<option id="cityCon">琼</option>
							</select>
							<select  style="width:50px;" id="letter" name="letter">
								<option>A</option>
								<option>B</option>
								<option>C</option>
								<option>D</option>
								<option>E</option>
								<option>F</option>
								<option>G</option>
								<option>H</option>
								<option>I</option>
								<option>J</option>
								<option>K</option>
								<option>L</option>
								<option>M</option>
								<option>N</option>
								<option>O</option>
								<option>P</option>
								<option>Q</option>
								<option>R</option>
								<option>S</option>
								<option>T</option>
								<option>U</option>
								<option>V</option>
								<option>W</option>
								<option>X</option>
								<option>Y</option>
								<option>Z</option>
							</select>
							<input type="text" style="width: 82px;" id="number" value="${mebUser.number}"/>
							<span id="numberError"></span>
							<input type="hidden" style="width: 200px;" id="license" name="mebUser.license" value="${mebUser.license}"/>
							</div>
							</td> 						
						</tr>
					<%-- 	<tr>
							<th>注册途径</th>
							<td class="pn-fcontent"><input type="text" style="width: 200px;"
								name="mebUser.userType" value="${mebUser.userType}" maxlength="50"/>(1APP)</td>
							<td class="pn-info"></td>
						</tr> --%>
						<tr>
							<th>用户状态</th>
							<td class="pn-fcontent"><input type="text" style="width: 200px;" id="mebUser.valid"
								name="mebUser.valid" value="${mebUser.valid}" maxlength="20" />(1有效,0无效)</td>
							<td class="pn-info"></td>
						</tr>
					</tbody>
				</table>
				<div class="widget-bottom">
					<input type="hidden" name="mebUser.regUserId" value="${mebUser.regUserId}" />  
					<center>
						<input class="btn btn-primary pull-center" id="editUserbt" type="button" value="保 存" onclick="check()" />&nbsp;
						<input class="btn btn-primary pull-center" type="button" value="返回" onclick="reback()"/>&nbsp;
					</center>
				</div>
			</div>
			<!-- /widget-content -->
		</div>

	</form>
</body>
</html>