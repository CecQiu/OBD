<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
    	<jsp:include page="../../include/common.jsp" />
		<script type="text/javascript">
		function check() {
			if(get("startTime1").value == "" && get("endTime1").value == "" ) {
				alert(" 请选择时间范围 ！ ");
				return false;
				  }
		    if(get("startTime1").value != "" && get("endTime1").value != "" ) {
			    if(get("startTime1").value > get("endTime1").value) {
			      	alert("开始时间必须早于或等于结束时间  ");
			    	return false;
			    }
			}    
		    else {
		      	return true;
		    }  	
		}
		function reback(){
			window.location="systemLog_list.do";
		}
		</script>
	</head>
	<body>
	<form style="min-height: 580px;" name="myForm" id="myForm" method="post" action="systemLog_delSystemLog.do" onsubmit="return check();">
		<ul class="breadcrumb">
			<li><i class="icon-home icon-2x"></i></li>
			<li>当前位置：${currentPosition}</li>
		</ul>
		<div class="widget widget-table">
			<div class="widget-content">
				<table class="pn-ftable table-condensed" border="0" cellpadding="10">
					<tbody>
						<tr>
							<th>选择时间范围：</th>
							<td class="pn-fcontent">
								<input style="width: 200px;" size="22" type="text" name="startTime1" value="${startTime1 }" class="Wdate" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})""/> - <input style="width: 200px;" size="22" type="text" name="endTime1" value="${endTime1 }"class="Wdate" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"/>
							</td>
						</tr>
					</tbody>
				</table>
				
				<div class="widget-bottom">
					<center>
						<input class="btn btn-primary pull-center" type="submit" value="删 除" />&nbsp;
						<input class="btn btn-success pull-center" type="button" value="返 回" onclick="reback()"/>&nbsp;
					</center>
				</div>
			</div>
			<!-- /widget-content -->
		</div>
	</form>
	</body>
</html>