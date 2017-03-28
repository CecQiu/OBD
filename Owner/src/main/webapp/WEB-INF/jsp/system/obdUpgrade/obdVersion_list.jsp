<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
<jsp:include page="../../include/common.jsp" />

<script language="javascript">

	function getRowIndex(obj){
		get("pager.rowIndex").value=obj.rowIndex;
	}
	
	
	function myFormReset(){
		$("#obdSn").val("");
		$("#obdMSn").val("");
		$("#version").val("");
		$("#updateFlag").val("");
		$("#starTime").val("");
		$("#endTime").val("");
		$("#qtype").val("0");
		$("#firmType").val("");
		$("#firmVersion").val("");
	}
	
	function exportExcel(){
		var uresultIds = $('#uresultIds').val();
		if(!$.trim(uresultIds)){
			alert('没有查询结果！');
			return;
		}
		
		//window.location.href="${basePath}/admin/obdUpgradeMsg_uresultExcel.do?uresultIds="+uresultIds;
		$("#idsForm").submit();
	}
	
	function formCheck(){
		var obdSn=$("#obdSn").val().trim();
		var obdMSn=$("#obdMSn").val().trim();
		var qtype=$("#qtype").val();
		var version=$("#version").val();
		var updateFlag=$("#updateFlag").val();
		var firmVersion=$("#firmVersion").val();
		var firmType=$("#firmType").val();
		var starTime=$("#starTime").val();
		var endTime=$("#endTime").val();
		
		if(obdSn=='' && obdMSn=='' &&  version=='' && updateFlag=='' && firmVersion=='' && firmType=='' && starTime=='' && endTime ==''){
			if (confirm("麻烦先输入查询参数,比如开始时间和结束时间？")) {
		
			}else{
				return false;
			}
			//alert("请输入查询参数,若查询最新记录,请选择查询时间,加上版本号亦可.");
		}
		if (qtype == '1') {
			alert("查询时间段内有哪些设备升级过，只需要输入开始时间和结束时间和版本号即可.");
			if (starTime == '' || endTime == '') {
				alert("查询时间段内有哪些设备升级过，开始时间和结束时间不能为空.");
				return false;
			}
		}
		return true;
	}
</script>
</head>
<body>
	<form name="idsForm" id="idsForm" action="${basePath}/admin/obdUpgradeMsg_uresultExcel.do" method="post">
		<input type="hidden" value="${uresultIds}" id="uresultIds" name='uresultIds'/>
	</form>
	<form name="myForm" id="myForm" action="obdUpgradeMsg_query.do;" method="post" onsubmit="return formCheck();">
		<ul class="breadcrumb">
			<li><i class="icon-home icon-2x"></i></li>
			<li>当前位置：${currentPosition}>obd远程升级详情</li>
		</ul>
		<div class="widget widget-table">
			<div class="widget-content">
				<table class="pn-ftable table-condensed" border="0" cellpadding="10">
					<tbody>
						<tr>
							<th>设备号:</th>
							<td class="pn-fcontent">
								 <input type="text" size="24" id="obdSn" name="obdSn" value="${obdSn}" />
							</td>
							<th>表面号</th>
							<td class="pn-fcontent">
								<input type="text" size="24" id="obdMSn" name="obdMSn" value="${obdMSn}" />
							</td>
						</tr>
						<tr>
							<th>版本号</th>
							<td class="pn-fcontent">
								<input type="text" size="24"  id="version" name="version" value="${version}" />
							</td>
							<th></th>
							<td class="pn-fcontent">
							</td>
						</tr>
						<tr>
							<th>固件类型:</th>
							<td class="pn-fcontent">
								<select name="firmType" id="firmType" style="width: 200px;">
									<option value="">全部</option>
									<option value="0" <c:if test="${firmType=='0'}">selected</c:if>>0-APP</option>
									<option value="1" <c:if test="${firmType=='1'}">selected</c:if>>1-IAP</option>
								</select>
							</td>
							<th>固件版本号:</th>
							<td class="pn-fcontent">
								<input type="text" size="24" id="firmVersion" name="firmVersion" value="${firmVersion}" />
							</td>
						</tr>
						<tr>
							<th>远程升级结果:</th>
							<td class="pn-fcontent">
								<select name="updateFlag" id="updateFlag" style="width: 200px;">
									<option value="">全部</option>
									<option value="00" <c:if test="${updateFlag=='00'}">selected</c:if>>升级成功00</option>
									<option value="01" <c:if test="${updateFlag=='01'}">selected</c:if>>写flash错误01</option>
									<option value="02" <c:if test="${updateFlag=='02'}">selected</c:if>>校验码错误02</option>
									<option value="03" <c:if test="${updateFlag=='03'}">selected</c:if>>升级数据包有问题03</option>
									<option value="04" <c:if test="${updateFlag=='04'}">selected</c:if>>升级数据包大小有问题04</option>
									<option value="05" <c:if test="${updateFlag=='05'}">selected</c:if>>超时失败05</option>
									<option value="06" <c:if test="${updateFlag=='06'}">selected</c:if>>其他06</option>
								</select>
							</td>
							<th>查询方式:</th>
							<td class="pn-fcontent">
								<select name="qtype" id="qtype" style="width: 200px;">
									<option value="0" <c:if test="${qtype=='0'}">selected</c:if>>查询所有0</option>
									<option value="1" <c:if test="${qtype=='1'}">selected</c:if>>查询最新1</option>
								</select>
							</td>
						</tr>
						<tr>
							<th>开始时间</th>
							<td class="pn-fcontent">
								<input type="text" value="${starTime}" class="Wdate" readonly="readonly" name="starTime" id="starTime" onclick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss',maxDate:'%y-%M-%d %H:%m:%s'})"/>
							</td>
							<th>结束时间</th>
							<td class="pn-fcontent">
								<input type="text" value="${endTime}" class="Wdate" readonly="readonly" name="endTime" id="endTime"  onclick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss',maxDate:'%y-%M-%d %H:%m:%s'})" />
							</td>
						</tr>
					</tbody>
				</table>
				<div class="widget-bottom">
					<center>
						<input class="btn btn-s-md btn-success" type="submit" value="查 询" />&nbsp;
						<input class="btn btn-danger pull-center" type="button" value="重置" onclick="myFormReset();"/>&nbsp;
						<input class="btn btn-primary pull-center" type="button" value="导出" onclick="exportExcel();"/>&nbsp;
					</center>
				</div>
			</div>
			<!-- /widget-content -->
		</div>
		<div class="widget widget-table">
			<div class="widget-header">
				<i class="icon-th-list"></i>
				<h5>obd远程升级详情列表</h5>
			</div>
			<!-- /widget-header -->
			<div class="widget-content widget-list">
				<table class="table table-striped table-bordered table-condensed table-hover sortable">
					<thead>
						<tr>
							<th>序号</th>
							<th>设备号</th>
							<th>版本号</th>
							<th>固件版本号</th>
							<th>固件类型</th>
							<th>升级结果</th>
							<th>创建时间</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${obdVersionList}" var="item" varStatus="status">
							<tr onclick="getRowIndex(this)">
								<td>${item.id}</td>
								<td>${item.obdSn}</td>
								<td>${item.version}</td>
								<td>${item.firmVersion}</td>
								<c:choose>
								   <c:when test="${item.firmType==0}">  
								    	<td>APP</td>   
								   </c:when>
								   <c:when test="${item.firmType==1}">  
								    	<td>IAP</td>   
								   </c:when>
								   <c:otherwise> 
								   		<td></td> 
								   </c:otherwise>
								</c:choose>
								
								<td>${item.updateFlag}</td>
								<td><fmt:formatDate value="${item.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
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
</body>
</html>