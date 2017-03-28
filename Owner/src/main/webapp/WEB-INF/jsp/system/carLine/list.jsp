<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8" language="java"
	pageEncoding="UTF-8" isErrorPage="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html>
<head>
<title>车辆监控 - 行程轨迹跟踪</title>
<jsp:include page="../../include/common.jsp" />
<script>
$(function(){
	var snos ='';
	//全选
	$('input[name=allCheckObdSn]').click(function(){
		var allChecked = ($(this).attr('checked'));
		snos ='';
		$('input[name=checkObdSn]').each(function(){
			if(allChecked == 'checked'){
				$(this).attr('checked','checked');
			}else
				$(this).removeAttr('checked');
		});
		
	});
	//其他单项选
	$('input[name=checkObdSn]').click(function(){
		
	});
	$('#goSet').click(function(){
		var b = false;
		$('input[name=checkObdSn]').each(function(){
			if($(this).attr('checked') == 'checked'){
				snos += $(this).val()+',';
				b = true;
			}
		});
		if(!b){
			alert('还没有选择要进行设定的选项！');
			return;
		}
		snos = snos.substring(0, snos.length-1);
		window.location.href='${basePath}/admin/carLine_carTravelArea.do?snos='+snos;
		
	});
});

function myFormReset(){
	$("#obdSn").val("");
	$("#obdMSn").val("");
	$("#groupNum").val("");
	$("#start").val("");
	$("#end").val("");
}
</script>
</head>
<body>
 <form id="myForm" class="myForm" action="${basePath}/admin/carLine_list.do" method="post">
	<ul class="breadcrumb">
		<li><i class="icon-home icon-2x"></i></li>
		<li>当前位置：车辆监控   &gt; 行程区域设定</li>
	</ul>
	<div class="widget">
		<div class="widget widget-table">
			<div class="widget-content">
				<table class="pn-ftable table-condensed" border="0" cellpadding="10">
					<tbody>
						<tr>
							<th>设备号:</th>
							<td class="pn-fcontent">
								 <input type="text" size="24" id="obdSn" name="obdStockInfo.obdSn" value="${obdStockInfo.obdSn}" />
							</td>
							<th>表面号:</th>
							<td class="pn-fcontent">
								<input type="text" size="24"  id="obdMSn" name="obdStockInfo.obdMSn" value="${obdStockInfo.obdMSn}" />
							</td>
						</tr>
						<tr>
							<th>开始时间</th>
							<td class="pn-fcontent">
								<input type="text" value="${start}" class="Wdate" readonly="readonly" name="start" id="start" onclick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss',maxDate:'%y-%M-%d %H:%m:%s'})"/>
							</td>
							<th>结束时间</th>
							<td class="pn-fcontent">
								<input type="text" value="${end}" class="Wdate" readonly="readonly" name="end" id="end"  onclick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss',maxDate:'%y-%M-%d %H:%m:%s'})" />
							</td>
						</tr>
					</tbody>
				</table>
				<div class="widget-bottom">
					<center>
						<input class="btn btn-s-md btn-success" type="submit" value="查 询" />&nbsp;
						<input class="btn btn-danger pull-center" type="button" value="重置" onclick="myFormReset();"/>&nbsp;
					</center>
				</div>
			</div>
			<!-- /widget-content -->
		</div>
		<div class="widget-content">
			<table class="table table-hover">
			  <tr style="background-color: #c4e3f3;">
			  	<th>编&nbsp;&nbsp;号</th>
				<th>设备号</th>
				<th>表面号</th>
				<th>二维码</th>
				<th>分组编号</th>
				<th>创建时间</th>
				<td>操作</td>
				<td width="3%">批量<input type="checkbox" name="allCheckObdSn"/>&nbsp;&nbsp;<button type="button" id="goSet">设定</button></td>
			  </tr>
			  <c:forEach items="${obdStockInfos}" var="item" varStatus="status">
				<tr>
					<td>${item.stockId}</td>
					<td>${item.obdSn}</td>
					<td>${item.obdMSn}</td>
					<td>${item.obdId}</td>
					<td>${item.groupNum}</td>
					<td>${item.startDate}</td>
					<td>
						<button type="button" onclick="window.location.href='${basePath}/admin/carLine_carTravelArea.do?snos=${item.obdMSn}'">设定</button> 
					</td>
					<td><input type="checkbox" name="checkObdSn" value="${item.obdMSn}"/></td>
				</tr>
			</c:forEach>
			</table>
			<div class="widget-bottom">
				<jsp:include page="../../include/pager.jsp" />
			</div>
			<div class="widget-bottom">
				&nbsp;<span class="pull-right">版权所有：广州华工信息软件有限公司</span>
			</div>
		</div>
	</div>
 </form>
</body>
</html>