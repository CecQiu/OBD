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
		$("#che_gall").bind("change",function() {
			var checkResult = $("#che_gall").is(':checked');
			$("input[name=che_group]").each(function() {
				$(this).prop("checked",checkResult);
			});
		});
		
		$("input[name=che_group]").bind("change",function() {
			if(!$(this).is(":checked")) {
				$("#che_gall").prop("checked", false);
			} else {
				var flag = true;
				$("input[name=che_group]").each(function() {
					if(!$(this).is(":checked")) {
						flag = false;
						return;
					}
				});
				$("#che_gall").prop("checked", flag);
			}
		});
	});
	
	function groupSetCheck() {
		var groupNum=$("#groupNum").val();
		if(groupNum==''){
			alert("请选择分组.");
			return false;
		} 
		alert($("#groupNum").find("option:selected").text());
		var len = $("input[name=che_group]:checked").length; 
		alert("设备选择个数:"+len);
		if(len==0){
			alert("请选择1个以上设备.");
			return false;
		}
		var obdListStr="";
		$("input[name=che_group]:checked").each(function() {
			obdListStr += $(this).val() + ',';
		});
		obdListStr = obdListStr.substring(0,obdListStr.length-1);
		alert(obdListStr);
		if(window.confirm('请再次确认!')){
            return true;
         }else{
            return false;
        }
		/* return true; */
	}
</script>
</head>
<body>
	<form id="myForm" name="myForm" action="obdGroupSet_set.do" method="post">
		<ul class="breadcrumb">
			<li><i class="icon-home icon-2x"></i></li>
			<li>当前位置：${currentPosition}&nbsp;&gt;&nbsp;obd分组设置</li>
		</ul>
		<div class="widget widget-table">
			<div class="widget-header">
				<i class="icon-th-list"></i>
				<h5>obd分组设置</h5>
			</div>
			<div>
			<table class="table table-striped table-bordered table-condensed table-hover sortable">
				<tr>
					<th>分组：</th>
					<td>
						<select name="obdGroup.groupNum" id="groupNum" style="width: 100px;">
							<option value="">全部</option>
							<c:forEach items="${obdGroups}" var="item" varStatus="status">
								<option value="${item.groupNum}">'${item.groupName}'</option>
							</c:forEach>
						</select>
					</td>
				</tr>
			</table>
			</div>
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
						<th width="5%">全选&nbsp;<input id="che_gall" type="checkbox" /></th>
						<th width="10%">设备号</th>
						<th width="10%">表面号</th>
						<th width="10%">分组编号</th>
					</tr>
				</thead>
			</table>
			<div class="widget-content" style="min-height:150px;max-height: 500px;overflow-x: scroll;overflow-y:auto;">
				<table class="table table-striped table-bordered table-condensed table-hover sortable" style="overflow-x: scroll">
					<tbody>
						<c:forEach items="${obdStockInfos}" var="item" varStatus="status">
							<tr>
								
								<th width="5%"><input name="che_group" type="checkbox" value="${item.obdSn}" /></th>
								<td width="10%">${item.obdSn }</td>
								<th width="10%">${item.obdMSn }</th>
								<th width="15%">${item.groupNum }</th>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
			<!-- /widget-content -->
			<div class="widget-bottom">
				<center>
					<input class="btn btn-s-md btn-success" type="submit"  onclick="return groupSetCheck()" value="确定" />&nbsp;
					<input class="btn btn-danger btn-s-md" type="button" onclick="doGoBack()" value="返回"/>
				</center>
			</div>
		</div>
	</form>
</body>
</html>