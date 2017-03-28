<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
<title></title>
<jsp:include page="../../include/common.jsp" />
<script language="javascript">
function check(){
// 利用正则表达式对输入数据匹配
   var violationTime=document.getElementById("violationTime").value;
   var ViolationDesc=document.getElementById("ViolationDesc").value;
   var flag=true;
   if(violationTime==null||violationTime.replace(/(^s*)|(s*$)/g, "").length ==0){	
	   $('#error')[0].innerHTML = "<font color=red size=2>违章时间不能为空 !</font>";
	   flag=false;
   }else{
	   if(ViolationDesc==null||ViolationDesc.replace(/(^s*)|(s*$)/g, "").length ==0){	
		   $('#error')[0].innerHTML = "<font color=red size=2>违章内容不能为空 !</font>";
		   flag=false;
	   }else{
		   if(flag==true){
				  alert("新增成功 ！");
				  $('#fuelAdd').submit();
				 window.parent.hideIframe();
			  }
	   }
   }
 
  
}
function reback(){
	var url="carOwnerService_peccancySearch.do";
	window.location.href=url;
	window.parent.hideIframe();
}
</script>
</head>
<body>
	<form id="fuelAdd" method="post" action="carOwnerService_peccancyAdd.do">
		<ul class="breadcrumb">
			<li><i class="icon-home icon-2x"></i></li>
			<li>当前位置：${currentPosition}&nbsp;>&nbsp;新增违章信息</li>
		</ul>
		<div class="widget widget-edit">
			<div class="widget-content">
				<table  class="table table-hover"  align="center">
					<tbody>
						<tr>
							<td>二&nbsp;维&nbsp;码:&nbsp;
							<select style="width: 120px;" name="obdSN" id="obdSn">
							<s:iterator value="obdSnList" var="st">
								<option id="obdSN"><s:property value="st"/></option>							
							</s:iterator>
							</select>
							</td>
							<td>违章时间:&nbsp;&nbsp;<input id="violationTime" name="violationTime" type="text"  readonly="readonly" style="width: 120px;" class="Wdate" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'%y-%M-%d'})" /></td>
						</tr>
						<tr>
							<td>违章内容: <input type="text" style="width: 120px;" name="ViolationDesc" id="ViolationDesc"/></td>
							<td>&nbsp;扣&nbsp;&nbsp;&nbsp;&nbsp;分&nbsp;:&nbsp;
								<select style="width: 120px;" name="penaltyPoints">
									<option id="penaltyPoints" value="1">1分</option>
									<option id="penaltyPoints" value="2">2分</option>
									<option id="penaltyPoints" value="3">3分</option>
									<option id="penaltyPoints" value="4">4分</option>
									<option id="penaltyPoints" value="5">5分</option>
									<option id="penaltyPoints" value="6">6分</option>
									<option id="penaltyPoints" value="7">7分</option>
									<option id="penaltyPoints" value="8">8分</option>
									<option id="penaltyPoints" value="9">9分</option>
									<option id="penaltyPoints" value="10">10分</option>
									<option id="penaltyPoints" value="11">11分</option>
									<option id="penaltyPoints" value="12">12分</option>
							</select>
							</td>
						</tr>
							<span id="error"></span>
					</tbody>
				</table>
				<div class="widget-bottom"> 
					<center>
						<input id="fuelAdd" class="btn btn-primary pull-center" type="button" value="保 存" onclick="check()" />&nbsp;
						<input class="btn btn-primary pull-center" type="button" value="返回" onclick="reback()"/>&nbsp;
					</center>
				</div>
			</div>
			<!-- /widget-content -->
		</div>

	</form>
</body>
</html>