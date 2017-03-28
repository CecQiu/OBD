<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
<jsp:include page="../../include/common.jsp" />

<style type="text/css">
.query_hint {
	border: 5px solid #939393;
	width: 250px;
	height: 80px;
	line-height: 55px;
	padding: 0 20px;
	position: absolute;
	left: 50%;
	margin-left: -140px;
	top: 50%;
	margin-top: -40px;
	font-size: 15px;
	color: #333;
	font-weight: bold;
	text-align: center;
	background-color: #f9f9f9;
}
</style>

<script language="javascript">

	function getRowIndex(obj){
		get("pager.rowIndex").value=obj.rowIndex;
	}
	
	function exportExcel(){
		var obdSn=$("#obdSn").val();
		var obdMSn=$("#obdMSn").val();
		var type=$("#type").val();
		var version=$("#version").val();
		var auditState=$("#auditState").val();
		var success=$("#success").val();
		var sendedCount=$("#sendedCount").val();
		//var valid=$("#valid").val();
		var startTime=$("#startTime").val();
		var endTime=$("#endTime").val();
		
		
		window.location.href="${basePath}/admin/obdBatchSet_exportExcel.do?obdSn="+obdSn+
				"&obdMSn="+obdMSn+
				"&type="+type+
				"&version="+version+
				"&auditState="+auditState+
				"&success="+success+
				"&sendedCount="+sendedCount+
				"&valid="+1+
				"&startTime="+startTime+
				"&endTime="+endTime;
	}
	
	function myFormReset(){
		$("#obdSn").val("");
		$("#obdMSn").val("");
		$("#type").val("");
		$("#version").val("");
		$("#auditState").val("");
		$("#success").val("");
		$("#sendedCount").val("");
		//$("#valid").val("");
		$("#startTime").val("");
		$("#endTime").val("");
		
	}
	
	//普通用户删除
	function del(id){
		var obdSN = $("#"+id).html();
		if(confirm("你要删除的设备是:"+obdSN+",导出数据了吗？确定要删除这条吗？")) {
			$.post('${basePath}/admin/obdUpgradeMsg_del.do',{'id':id},function (data){
				if(data != ''){
					if(confirm(data)) {
						window.location.reload();
					}else
						window.location.reload();
				}else{
					alert('删除失败！');
				}
			});
		}else{
			return false;
		}
	}
	
	
	
	//管理员删除
	function adminDel(id){
		var obdSN = $("#"+id).html();
		if(confirm("你要删除的设备是:"+obdSN+",导出数据了吗？确定要删除这条吗？")) {
			$.post('${basePath}/admin/obdBatchSet_adminDel.do',{'id':id},function (data){
				if(data != ''){
					if(confirm(data)) {
						window.location.reload();
					}else
						window.location.reload();
				}else{
					alert('删除失败！');
				}
			});
		}else{
			return false;
		}
	}
	
	function delAll(){
		var version = $("#version").val();
		/* var upgradeSetIds=$('#upgradeSetIds').val();
		if(upgradeSetIds==''){
			alert("没有要删除的记录.");
			return false;
		} */
		if(version == ''){
			alert("输入待删除的版本号.");
			return false;
		}
		
		if(confirm("确定要删除版本号为:"+version+"--的所有有效且未升级成功的记录么?")){
			//确定
		}else{
			return false;
		}

		$.ajaxSetup({   
		    async : false  
		}); 
		if(confirm("导出数据了吗？确定要删除版本号:"+version+"的所有记录？")) {
			$("#query_hint").css('display','block'); 
			$.post('${basePath}/admin/obdUpgradeMsg_delAll.do',{upgradeSetIds:$('#upgradeSetIds').val(),'version':version},function (data){
				$("#query_hint").css('display','none');
				if(data != ''){
					if(confirm(data)) {
						window.location.reload();
					}else
						window.location.reload();
				}else{
					alert('删除失败！');
				}
			});
			$("#query_hint").css('display','none');
		}else{
			return false;
		}
	}
	
	function adminDelAll(){
		var version = $("#version").val();
		if(version == ''){
			alert("输入待删除的版本号.");
			return false;
		}
		
		if(confirm("确定要删除版本号为:"+version+"--的所有有效且未升级成功的记录么?")){
			//确定
		}else{
			return false;
		}

		$.ajaxSetup({   
		    async : false  
		}); 
		if(confirm("导出数据了吗？确定要删除版本号:"+version+"的所有记录？")) {
			$("#query_hint").css('display','block'); 
			$.post('${basePath}/admin/obdBatchSet_adminDelAll.do',{'version':version},function (data){
				$("#query_hint").css('display','none');
				if(data != ''){
					if(confirm(data)) {
						window.location.reload();
					}else
						window.location.reload();
				}else{
					alert('删除失败！');
				}
			});
			$("#query_hint").css('display','none');
		}else{
			return false;
		}
	}
	
	function aduitShow(){
		$("#aduitWin").show();
	}
	
	function auditsave(){
		if($('#obdversion').val().trim()!=""){
			$.post('${basePath}/admin/obdBatchSet_audit.do',{version:$('#obdversion').val(),auditState:$('#auditState2').val()},function (data){
				if(data == '0'){
					if(confirm("审核成功")) {
						window.location.reload();
					}else
						window.location.reload();
				}else if(data =="1"){
					alert('该版本号没有待审核记录.');
				}				
			});
		}
		else
			alert("版本号不能为空!");
	}
	function reback(){
		$("#aduitWin").hide();
	}
	
	function formCheck(){
		var sendedCount=$("#sendedCount").val();
		var reg =  new RegExp("^[0-9]*$");
		if(sendedCount!=''){
			if(!reg.test(sendedCount)){
				alert("下发次数输入框请输入阿拉伯数字.");
				return false;
			}
		}
		
		var startTime=$("#startTime").val();
		var endTime=$("#endTime").val();
		if(startTime!='' && endTime!=''){
			//查询只能查询一周的记录
			var a = startTime.split(" ");     
			var b = a[0].split("-");     
			var c = a[1].split(":");     
			var oldTime = new Date(b[0], b[1]-1, b[2], c[0], c[1], c[2]);  

			var aa = endTime.split(" ");     
			var bb = aa[0].split("-");     
			var cc = aa[1].split(":");     
			var newTime = new Date(bb[0], bb[1]-1, bb[2], cc[0], cc[1], cc[2]);  
			var days = parseInt((newTime.getTime()-oldTime.getTime()) / (1000 * 60 * 60 * 24));
			if(newTime<=oldTime){
				alert("结束时间不能大于开始时间.");
				return false;
			}
		}
		
		return true;
	}
	
</script>
</head>
<body>
	<form name="myForm" id="myForm"
		action="obdBatchSet_query.do" method="post"
		onsubmit="return formCheck();">
		<ul class="breadcrumb">
			<li><i class="icon-home icon-2x"></i></li>
			<li>当前位置：${currentPosition}>设备批量设置信息</li>
		</ul>
		<div class="widget widget-table">
			<div class="widget-content">
				<table class="pn-ftable table-condensed" border="0" cellpadding="10">
					<tbody>
						<tr>
							<th>设备号:</th>
							<td class="pn-fcontent"><input type="text" size="24"
								id="obdSn" name="obdSn" value="${obdSn}" />
							</td>
							<th>表面号</th>
							<td class="pn-fcontent">
								<input type="text" size="24" id="obdMSn" name="obdMSn" value="${obdMSn}" />
							</td>
						</tr>
						<tr>
							<th>版本号</th>
							<td class="pn-fcontent"><input type="text" size="24"
								id="version" name="version"
								value="${version}" />
							</td>
							<th>类型</th>
							<td class="pn-fcontent">
								<select name="type" id="type" style="width: 200px;">
									<option value="">全部</option>
									<option value="address" <c:if test="${type=='address'}">selected</c:if>>地址修改</option>
								</select>
							</td>
						</tr>
						<tr>
							<th>审核状态</th>
							<td class="pn-fcontent">
								<select name="auditState" id="auditState" style="width: 200px;">
									<option value="">全部</option>
									<option value="0" <c:if test="${auditState=='0'}">selected</c:if>>未审核</option>
									<option value="1" <c:if test="${auditState=='1'}">selected</c:if>>通过</option>
									<option value="-1" <c:if test="${auditState=='-1'}">selected</c:if>>不通过</option>
								</select>
							</td>
							<th>下发次数(大于等于):</th>
							<td class="pn-fcontent">
								<input type="text" size="24" id="sendedCount" name="sendedCount" value="${sendedCount}" />
							</td>
						</tr>
						<tr>
							<th>成功:</th>
							<td class="pn-fcontent">
								<select name="success" id="success" style="width: 200px;">
									<option value="">全部</option>
									<option value="0" <c:if test="${success=='0'}">selected</c:if>>否</option>
									<option value="1" <c:if test="${success=='1'}">selected</c:if>>是</option>
								</select>
							</td>
							<th></th>
							<td class="pn-fcontent">
							</td>
						</tr>
						
						<tr>
							<th>开始时间</th>
							<td class="pn-fcontent"><input type="text"
								value="${startTime}" class="Wdate" readonly="readonly"
								name="startTime" id="startTime"
								onclick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss',maxDate:'%y-%M-%d %H:%m:%s'})" />
							</td>
							<th>结束时间</th>
							<td class="pn-fcontent"><input type="text"
								value="${endTime}" class="Wdate" readonly="readonly"
								name="endTime" id="endTime"
								onclick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss',maxDate:'%y-%M-%d %H:%m:%s'})" />
							</td>
						</tr>
					</tbody>
				</table>
				<div class="widget-bottom">
					<center>
						<input class="btn btn-s-md btn-success" type="submit" value="查 询" />&nbsp;
						<input class="btn pull-center  btn-info" type="button" value="重置" onclick="myFormReset();" />&nbsp; 
						<input class="btn btn-primary pull-center " type="button" value="导出" onclick="exportExcel();" />&nbsp; 
						<!-- <a class="btn btn-warning pull-center " type="button" onclick="delAll();">删除</a>&nbsp;  -->
						<input class="btn btn-primary pull-center " type="button" value="审核" onclick="aduitShow();" />&nbsp;
						<c:if test="${fn:contains(session.operator.role.name,'管理员')}">
							<a class="btn btn-warning pull-center " type="button" onclick="adminDelAll();">管理员删除</a>&nbsp; 
						</c:if>
					</center>
				</div>
			</div>
			<!-- /widget-content -->
		</div>
		<div class="widget widget-table">
			<div class="widget-header">
				<i class="icon-th-list"></i>
				<h5>批量设置信息</h5>
			</div>
			<!-- /widget-header -->
			<div class="widget-content widget-list" style="overflow-x: scroll;">
				<table
					class="table table-striped table-bordered table-condensed table-hover sortable">
					<thead>
						<tr>
							<th>序号</th>
							<th>设备号</th>
							<th>类型</th>
							<th>版本号</th>
							<th>审核人</th>
							<th>审核时间</th>
							<th>审核结果</th>
							<th>下发次数</th>
							<th>成功</th>
							<th>导入时间</th>
							<th>更新时间</th>
							<th>有效</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${obdBatchSets}" var="item" varStatus="status">
							<tr onclick="getRowIndex(this)">
								<td>${item.id}</td>
								<td id="${item.id}">${item.obdSn}</td>
								<td>
									<c:choose>  
									   <c:when test="${item.type=='address'}">地址修改</c:when>  
									   <c:otherwise></c:otherwise>  
									</c:choose>
								</td>
								<td>${item.version}</td>
								<td>${item.auditOper}</td>
								<td><fmt:formatDate value="${item.auditTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
								<c:choose>
									<c:when test="${item.auditState==0}"><td style="color: black;">未审核</td></c:when>
									<c:when test="${item.auditState==1}"><td style="color: red;">通过</td></c:when>
									<c:when test="${item.auditState==-1}"><td style="color: blue;">不通过</td></c:when>
									<c:otherwise><td></td></c:otherwise>
								</c:choose>
								<td>${item.sendedCount}</td>
								<c:choose>
									<c:when test="${item.success==0}"><td style="color: purple;">未成功</td></c:when>
									<c:when test="${item.success==1}"><td style="color: red;">成功</td></c:when>
									<c:otherwise><td></td></c:otherwise>
								</c:choose>

								<td><fmt:formatDate value="${item.createTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
								<td><fmt:formatDate value="${item.updateTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
								<c:choose>
									<c:when test="${item.valid==0}"><td style="color: purple;">无效</td></c:when>
									<c:when test="${item.valid==1}"><td style="color: red;">有效</td></c:when>
									<c:otherwise><td></td></c:otherwise>
								</c:choose>
								
								<td>
									<a href="javascript:void();" onclick="adminDel('${item.id}')" style="color: red;">删除</a>
									<%-- <a href="javascript:void();" onclick="del(${item.id})">删除</a>&nbsp;&nbsp; --%>
									<%-- <c:if test="${session.operator.role.id==3}">
										<a href="javascript:void();" onclick="adminDel('${item.id}')" style="color: red;">管理员删除</a>
									</c:if> --%>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				<div class="widget-bottom">
					<jsp:include page="../../include/pager.jsp" />
				</div>
			</div>
			<!-- /widget-content -->
		</div>
	</form>
	<div id="aduitWin" class="widget"
		style="display: none; POSITION: absolute; left: 50%; top: 50%; width: 400px; height: 100px; margin-left: -200px; margin-top: -150px; border: 1px solid #888; background-color: white; text-align: center">
		<form id="newsForm" method="post" action="" style="margin-top: 25px;">
			版本号：<input type="text" id="obdversion" size="20px"></input> 审核结果：<select
				id="auditState2">
				<option value="1">通过</option>
				<option value="-1">不通过</option>
			</select>
		</form>

		<button type="button" class="btn btn-primary"
			style="margin-top: 20px;" onclick="auditsave()">审核</button>
		<button type="button" class="btn btn-primary"
			style="margin-top: 20px;" onclick="reback()">取消</button>
	</div>

	<div id="query_hint" class="query_hint" style = "display:none;">
		<img src="../images/wait.gif" />正在执行，请稍等．．．
	</div>
</body>
</html>