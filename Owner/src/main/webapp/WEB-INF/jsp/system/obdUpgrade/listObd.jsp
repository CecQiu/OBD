<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html>
<head>
<jsp:include page="../../include/common.jsp" />
<title></title>
<script language="javascript">	
	$().ready(function () {
		$("#che_all").bind("change",function() {
			var checkResult = $("#che_all").is(':checked');
			$("input[name=che_su]").each(function() {
				if($(this).prop("disabled")){
					$(this).attr("checked",false);
				}else{
					$(this).attr("checked",checkResult);
				}
			});
		});
		
		$("input[name=che_su]").bind("change",function() {
			if(!$(this).is(":checked")) {
				$("#che_all").attr("checked", false);
			} else {
				var flag = true;
				$("input[name=che_su]").each(function() {
					if(!$(this).is(":checked")) {
						flag = false;
						return;
					}
				});
				$("#che_all").attr("checked", flag);
			}
		});
	});
	
	function pushMsg() {
		var obdVersion=$("#version").text();
		var obdListStr="";
		$("input[name=che_su]:checked").each(function() {
			obdListStr += $(this).val() + ',';
		});
		obdListStr = obdListStr.substring(0,obdListStr.length-1);
		alert(obdListStr);
		if(window.confirm('请再次确认!')){
           // return true;
         }else{
            return false;
        }
		$.ajax({
		    type: 'POST',
		    url: "${basePath}/admin/upgrade_upgrade.do" ,
		    data: {'obdListStr':obdListStr,'obdVersion':obdVersion} ,
		    dataType: 'json',
		    success: function(data){
		    	if(data.status=="success"){
		    		alert("远程升级推送成功.");
		    		//doGoBack();//刷新页面
		    	}else{
		    		alert("远程升级推送失败.");
		    	}
		    } ,
		    error : function(){
		    	alert("系统异常,请联系管理员.");
		    }
		});
	}
	
	function myFormReset(){
		$("#obdSn").val("");
		$("#obdMSn").val("");
		$("#stockState").val("");
		$("#groupNum").val("");
	}
</script>
</head>
<body>
	<form id="myForm" name="myForm" action="odbUpgrade_listPushObd.do" method="post">
		<ul class="breadcrumb">
			<li><i class="icon-home icon-2x"></i></li>
			<li>当前位置：${currentPosition}&nbsp;&gt;&nbsp;推送</li>
		</ul>
		<div class="widget widget-table">
			<div class="widget-header">
				<i class="icon-th-list"></i>
				<h5>obd设备升级文件信息</h5>
			</div>
			<div>
			<table class="table table-striped table-bordered table-condensed table-hover sortable">
				<tr>
					<th>文件名：</th>
					<td>${obdUpgrade.fileName }</td>
					<td width="15%"/>
					<th>版本号：</th>
					<td id='version'>${obdUpgrade.version }</td>
				</tr>
				<tr>
					<th>文件大小：</th>
					<td>${obdUpgrade.size }字节</td>
					<td width="15%"/>
					<th>上传时间：</th>
					<td>${obdUpgrade.createTime }</td>
				</tr>
				<tr>
					<th>审核人员：</th>
					<td>${obdUpgrade.auditOper }</td>
					<td width="15%"/>
					<th>审核时间：</th>
					<td>${obdUpgrade.auditTime }</td>
				</tr>
				<tr>
					<th>审核结果：</th>
					<c:choose>
					   <c:when test="${obdUpgrade.auditState== '1'}"> 
					    	<td>审核通过</td>   
					   </c:when>
					   <c:when test="${obdUpgrade.auditState== '2'}"> 
					    	<td>审核不通过</td>   
					   </c:when>
					   <c:otherwise>
					   		<td>未审核</td>
					   </c:otherwise>
					</c:choose>
					<td width="15%"/>
					<th>固件推送状态：</th>
					<c:choose>
					   <c:when test="${obdUpgrade.auditSend== '1'}"> 
					    	<td>推送成功</td>   
					   </c:when>
					   <c:when test="${obdUpgrade.auditSend== '2'}"> 
					    	<td>推送失败</td>   
					   </c:when>
					   <c:otherwise>
					   		<td>未推送</td>
					   </c:otherwise>
					</c:choose>
				</tr>
				<tr>
					<th>备注：</th>
					<td colspan="4" style="text-align: left;">
						<textarea rows="3" cols="80" readonly="readonly">${obdUpgrade.memo }</textarea>
					</td>
				</tr>
			</table>
			</div>
		</div>
		<div class="widget widget-table">
			<div class="widget-content">
				<div class="widget-header">
					<i class="icon-th-list"></i>
					<h5>设备筛选</h5>
				</div>
				<table class="pn-ftable table-condensed" border="0" cellpadding="10">
					<tbody>
						<tr>
							<th>设备号:</th>
							<td class="pn-fcontent">
								 <input type="text" size="24" id="obdSn" name="obdStockInfo.obdSn" value="${obdStockInfo.obdSn}" />
							</td>
							<th>设备注册号:</th>
							<td class="pn-fcontent">
								<input type="text" size="24"  id="obdMSn" name="obdStockInfo.obdMSn" value="${obdStockInfo.obdMSn}" />
							</td>
						</tr>
						<tr>
							<th>分组编号: </th>
							<td class="pn-fcontent">
								<select name="obdStockInfo.groupNum" id="groupNum">
									<option value="">全部</option>
									<c:forEach items="${obdGroups}" var="item" varStatus="status">
										<option value="${item.groupNum}" <c:if test="${item.groupNum==obdStockInfo.groupNum}">selected</c:if>>'${item.groupName}'</option>
									</c:forEach>
								</select>
							</td>
							<th>在线状态: </th>
							<td class="pn-fcontent">
								<select name="obdStockInfo.stockState" id="stockState" style="width: 200px;">
									<option value="">全部</option>
									<option value="01" <c:if test="${obdStockInfo.stockState=='01'}">selected</c:if>>在线01</option>
									<option value="00" <c:if test="${obdStockInfo.stockState=='00'}">selected</c:if>>离线00</option>
								</select>
							</td>
						</tr>
					</tbody>
				</table>
				<div style="display:none;">
					<input name="upId" value="${obdUpgrade.id }"/> 
				</div>
				<div class="widget-bottom">
					<center>
						<input class="btn btn-s-md btn-success" type="submit" value="查 询" />&nbsp;
						<input class="btn btn-danger pull-center" type="button" value="重置" onclick="myFormReset();"/>&nbsp;
					</center>
				</div>
			</div>
			<!-- /widget-content -->
		</div>
		
		<div class="widget widget-table">
			<div class="widget-header">
				<i class="icon-th-list"></i>
				<h5>obd设备列表</h5>
			</div>
			<!-- /widget-header -->
			<table class="table table-striped table-bordered table-condensed table-hover sortable">
				<thead>
					<tr>
						<th width="5%">全选&nbsp;<input id="che_all" type="checkbox" /></th>
						<th width="10%">设备号</th>
						<th width="10%">设备注册号</th>
						<th width="10%">二维码</th>
						<th width="10%">分组编号</th>
						<th width="10%">创建时间</th>
						<th width="10%">设备在线状态</th>
					</tr>
				</thead>
			</table>
			<div class="widget-content" style="min-height:150px;max-height: 50%;overflow-x: scroll;overflow-y:auto;">
				<table class="table table-striped table-bordered table-condensed table-hover sortable" style="overflow-x: scroll">
					<tbody>
						<c:forEach items="${obdStockInfos}" var="item" varStatus="status">
							<tr>
								<c:if test="${item.stockState == '00'}">
									<th width="5%"><input name="che_su" type="checkbox" disabled="disabled"/></th>
								</c:if>
								<c:if test="${item.stockState == '01'}">
									<th width="5%"><input name="che_su" type="checkbox" value="${item.obdSn}" /></th>
								</c:if>
								
								<th width="10%">${item.obdSn }</th>
								<th width="10%">${item.obdMSn }</th>
								<th width="10%">${item.obdId }</th>
								<th width="10%">${item.groupNum }</th>
								<th width="10%">${item.startDate }</th>
								<td width="10%">${item.stockState }</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
			<div class="widget-bottom">
				<jsp:include page="../../include/pager.jsp" />
			</div>
			
			<!-- /widget-content -->
			<div class="widget-bottom">
				<center>
					<input class="btn btn-s-md btn-success" type="button" onclick="pushMsg()" value="推送" />&nbsp;
					<input class="btn btn-danger btn-s-md" type="button" onclick="doGoBack()" value="返回"/>
				</center>
			</div>
		</div>
	</form>
</body>
</html>