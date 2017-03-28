<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
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
		var uids = $('#fotaSetIds').val();
		if(uids != ''){
			window.location.href="${basePath}/admin/fotaSet_exportExcel.do?fotaSetIds="+uids;
		}else{
			alert('没有记录!不能导出。。。');
		}
	}
	
	function myFormReset(){
		$("#obdSn").val("");
		$("#version").val("");
		$("#batchVersion").val("");
		
		$("#fileName").val("");
		$("#ftpIP").val("");
		$("#ftpPort").val("");
		$("#valid").val("");
		$("#mifiFlag").val("");
		
		$("#starTime").val("");
		$("#endTime").val("");
	}
	
	function del(id){
		if(confirm("导出数据了吗？确定要删除这条吗？")) {
			$.post('${basePath}/admin/fotaSet_del.do',{'fotaSet.id':id},function (data){
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
		/* var fotaSetIds=$('#fotaSetIds').val();
		if(fotaSetIds==''){
			alert("没有要删除的记录.");
			return false;
		} */
		if(version == ''){
			alert("输入待删除的版本号.");
			return false;
		}
		
		if(confirm("确定要删除版本号为:"+version+"--的所有有效记录么?")){
			//确定
		}else{
			return false;
		}

		$.ajaxSetup({   
		    async : false  
		}); 
		if(confirm("导出数据了吗？确定要删除？")) {
			$("#query_hint").css('display','block'); 
			$.post('${basePath}/admin/fotaSet_delAll.do',{'fotaSet.version':version},function (data){
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
	
	function aduitUpgrade(){
		$("#aduitWin").show();
	}
	
	function auditsave(){
		if($('#obdversion').val().trim()!=""){
			$.post('${basePath}/admin/fotaSet_audit.do',{'fotaSet.batchVersion':$('#obdversion').val(),'fotaSet.auditResult':$('#auditState').val()},function (data){
				if(data == '1'){
					if(confirm("审核成功")) {
						window.location.href = "${basePath}/admin/fotaSet_fsList.do";
					}else
						window.location.href = "${basePath}/admin/fotaSet_fsList.do";
				}else if(data =="0"){
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
	
	function add() {
		//审核按钮
		window.location.href = "${basePath}/admin/fotaSet_list.do";
	}
	
</script>
</head>
<body>
	<input type="hidden" value="${fotaSetIds}" id="fotaSetIds" />
	<form name="myForm" id="myForm"
		action="fotaSet_query.do" method="post">
		<ul class="breadcrumb">
			<li><i class="icon-home icon-2x"></i></li>
			<li>当前位置：${currentPosition}>FOTA待升级列表</li>
		</ul>
		<div class="widget widget-table">
			<div class="widget-content">
				<table class="pn-ftable table-condensed" border="0" cellpadding="10">
					<tbody>
						<tr>
							<th>设备号:</th>
							<td class="pn-fcontent"><input type="text" size="24"
								id="obdSn" name="fotaSet.obdSn" value="${fotaSet.obdSn}" />
							</td>
							<th>版本号</th>
							<td class="pn-fcontent"><input type="text" size="24"
								id="version" name="fotaSet.version"
								value="${fotaSet.version}" /></td>
						</tr>
						
						<tr>
							<th>上传批号:</th>
							<td class="pn-fcontent"><input type="text" size="24"
								id="batchVersion" name="fotaSet.batchVersion" value="${fotaSet.batchVersion}" />
							</td>
							<th>ftp地址:</th>
							<td class="pn-fcontent"><input type="text" size="24"
								id="ftpIP" name="fotaSet.ftpIP" value="${fotaSet.ftpIP}" />
							</td>
						</tr>
						
						<tr>
							<th>FOTA是否下发:</th>
							<td class="pn-fcontent"><select name="fotaSet.valid"
								id="valid" style="width: 200px;">
									<option value="">全部</option>
									<option value="1"
										<c:if test="${fotaSet.valid=='1'}">selected</c:if>>是</option>
									<option value="0"
										<c:if test="${fotaSet.valid=='0'}">selected</c:if>>否</option>
							</select></td>
							<th>MIFI是否下发:</th>
							<td class="pn-fcontent"><select name="fotaSet.mifiFlag"
								id="mifiFlag" style="width: 200px;">
									<option value="">全部</option>
									<option value="1"
										<c:if test="${fotaSet.mifiFlag=='1'}">selected</c:if>>是</option>
									<option value="0"
										<c:if test="${fotaSet.mifiFlag=='0'}">selected</c:if>>否</option>
							</select></td>
						</tr>
						
						<tr>
							<th>开始时间</th>
							<td class="pn-fcontent"><input type="text"
								value="${starTime}" class="Wdate" readonly="readonly"
								name="starTime" id="starTime"
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
						<a class="btn btn-warning pull-center " type="button" onclick="delAll();">删除</a>&nbsp; 
						<input class="btn btn-primary pull-center " type="button" value="审核" onclick="aduitUpgrade();" />&nbsp;
						<input class="btn btn-primary pull-center " type="button" value="新增" onclick="add();" />&nbsp;
					</center>
				</div>
			</div>
			<!-- /widget-content -->
		</div>
		<div class="widget widget-table">
			<div class="widget-header">
				<i class="icon-th-list"></i>
				<h5>FOTA升级列表</h5>
			</div>
			<!-- /widget-header -->
			<div class="widget-content widget-list" style="overflow-x: scroll;">
				<table
					class="table table-striped table-bordered table-condensed table-hover sortable">
					<thead>
						<tr>
							<th>序号</th>
							<th>设备号</th>
							<th>版本号</th>
							<th>上传批号</th>
							<th>文件名</th>
							<th>ftp地址</th>
							<th>ftp端口</th>
							<th>用户名</th>
							<th>密码</th>
							<th>创建时间</th>
							<th>是否下发</th>
							<th>下发时间</th>
							<th>上传者</th>
							<th>审核人</th>
							<th>审核结果</th>
							<th>审核时间</th>
							<th>MIFI标识</th>
							<th>MIFI更新时间</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${fotaSetList}" var="item" varStatus="status">
							<tr onclick="getRowIndex(this)">
								<td>${item.id}</td>
								<td>${item.obdSn}</td>
								<td>${item.version}</td>
								<td>${item.batchVersion}</td>
								
								<td>${item.fileName}</td>
								<td>${item.ftpIP}</td>
								<td>${item.ftpPort}</td>
								
								<td>${item.ftpUsername}</td>
								<td>${item.ftpPwd}</td>
								<td><fmt:formatDate value="${item.createTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
								
								<c:if test="${item.valid==1}">
									<td style="color: red;">是</td>
								</c:if>
								<c:if test="${item.valid==0}">
									<td style="color: blue;">否</td>
								</c:if>
								<td><fmt:formatDate value="${item.sendTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
								<td>${item.uploadOper}</td>
								<td>${item.auditOper}</td>

								<c:if test="${item.auditResult==0}">
									<td style="color: black;">审核不通过</td>
								</c:if>
								<c:if test="${item.auditResult==1}">
									<td style="color: red;">审核通过</td>
								</c:if>
								<c:if test="${item.auditResult==null}">
									<td style="color: blue;">未审核</td>
								</c:if>
								<td><fmt:formatDate value="${item.auditTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
								
								<c:if test="${item.mifiFlag==1}">
									<td style="color: red;">是</td>
								</c:if>
								<c:if test="${item.mifiFlag==0}">
									<td style="color: blue;">否</td>
								</c:if>
								<td><fmt:formatDate value="${item.mifiTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
								<c:if test="${item.useFlag=='1'}">
									<td><a href="javascript:void();" onclick="del(${item.id})">删除</a></td>
								</c:if>
								
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
			上传批号：<input type="text" id="obdversion" size="20px"></input> 审核结果：<select
				id="auditState">
				<option value="1">通过</option>
				<option value="0">不通过</option>
			</select>
		</form>

		<button type="button" class="btn btn-primary"
			style="margin-top: 20px;" onclick="auditsave()">审核</button>
		<button type="button" class="btn btn-primary"
			style="margin-top: 20px;" onclick="reback()">取消</button>
	</div>

	<div id="query_hint" class="query_hint" style = "display:none;">
		<img src="../images/wait.gif" />正在删除，请稍等．．．
	</div>
</body>
</html>