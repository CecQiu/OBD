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
   var maintainTime=document.getElementById("carMaintain.maintainTime").value;
   var flag=true;
   if(maintainTime==null||maintainTime.replace(/(^s*)|(s*$)/g, "").length ==0){	
	   $('#error')[0].innerHTML = "<font color=red size=2>上次保养时间不能为空 !</font>";
	   flag=false;
   }
		   
   if(flag==true){
		  alert("修改成功 ！");
		  $('#fuelAdd').submit();
		 window.parent.hideIframe();
	}	   
   
 
}
function reback(){
	var url="carOwnerService_maintainSearch.do";
	window.location.href=url;
	window.parent.hideIframe();
}
$(document).ready(function() {
	 $("select[@name=carMaintain.maintainStatus] option").each(function(){
		 
		   if(this.value=='${carMaintain.maintainStatus}'){
			   this.selected="selected";
		   }
	});
});
</script>
</head>
<body>
	<form id="fuelAdd" method="post" action="carOwnerService_maintainEdit.do">
		<ul class="breadcrumb">
			<li><i class="icon-home icon-2x"></i></li>
			<li>当前位置：${currentPosition}&nbsp;>&nbsp;修改保养信息</li>
		</ul>
		<div class="widget widget-edit">
			<div class="widget-content">
				<table  class="table table-hover"  align="center">
					<tbody>
						<tr>
							<td>二&nbsp;维&nbsp;码:&nbsp;
							<input type="text" name="carMaintain.obdSn" value="${carMaintain.obdSn}" style="width: 120px;" readonly="readonly"/>
							</td>
							<td>保养时间:&nbsp;&nbsp;<input value="${carMaintain.maintainTime}" id="carMaintain.maintainTime" name="carMaintain.maintainTime" type="text"  readonly="readonly" style="width: 120px;" class="Wdate" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'%y-%M-%d'})" /></td>
						</tr>
						<tr>
							<td>
							保养状态: <select name="carMaintain.maintainStatus" style="width: 120px;" >
										<option id="maintainStatus" value="00">无需保养</option>
										<option id="maintainStatus" value="01">需要保养</option>
										<option id="maintainStatus" value="02">逾期保养</option>
									</select>
							</td>
						</tr>
							<input type="hidden" name="carMaintain.id" value="${carMaintain.id}" />
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