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
	if("${message}"=="deleteSuccess") {
		alert("删除成功！");
	}
	
	function del(id){
		if(confirm("确认要删除吗？")){
			var obdSn=$("#obdSn").val();
			var sended=$("#sended").val();
			var starTime=$("#starTime").val();
			var endTime=$("#endTime").val();
			//保持页数
			var currentPage=$("input[name='pager.currentPage']").val();
			var rowIndex=$("input[name='pager.rowIndex']").val();
			
			window.location.href = "obdTestSendPacket_delete.do?obdTestSendPacket.id="+id+"&obdTestSendPacket.obdSn="+obdSn+
					"&obdTestSendPacket.sended="+sended+
					"&starTime="+starTime+
					"&endTime="+endTime+
					"&pager.currentPage="+currentPage+
					"&pager.rowIndex="+rowIndex;
		}
	}
	
	function getRowIndex(obj){
		get("pager.rowIndex").value=obj.rowIndex;
		}
	function edit(id,pageSize,currentPage){
		window.location.href ="obdTestSendPacket_edit.do?obdTestSendPacket.id="+id+"&pager.pageSize="+pageSize+"&pager.currentPage="+currentPage+"&pager.rowIndex="+get("pager.rowIndex").value;
	}
	
	function myFormReset(){
		$("#obdSn").val("");
		$("#sended").val("");
		$("#starTime").val("");
		$("#endTime").val("");
	}
	
</script>
</head>
<body>
	<form name="myForm" id="myForm" action="obdTestSendPacket_list.do" method="post">
		<ul class="breadcrumb">
			<li><i class="icon-home icon-2x"></i></li>
			<li>当前位置：${currentPosition}</li>
		</ul>
		<div class="widget widget-table">
			<div class="widget-content">
				<table class="pn-ftable table-condensed" border="0" cellpadding="10">
					<tbody>
						<tr>
							<th>设备号</th>
							<td class="pn-fcontent">
								 <input type="text" size="24" id="obdSn" name="obdTestSendPacket.obdSn" value="${obdTestSendPacket.obdSn}" />
							</td>
							<th>是否下发</th>
							<td class="pn-fcontent">
								<select name="obdTestSendPacket.sended" id="sended" style="width: 200px;">
									<option value="">全部</option>
									<option value="1" <c:if test="${obdTestSendPacket.sended==1}">selected</c:if>>已下发1</option>
									<option value="0" <c:if test="${obdTestSendPacket.sended==0}">selected</c:if>>未下发0</option>
									<option value="-1" <c:if test="${obdTestSendPacket.sended==-1}">selected</c:if>>无效-1</option>
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
					</center>
				</div>
			</div>
			<!-- /widget-content -->
		</div>
		<div class="widget widget-table">
			<div class="widget-header">
				<i class="icon-th-list"></i>
				<h5>报文下发信息</h5>
			</div>
			<!-- /widget-header -->
			<div class="widget-content widget-list" style="overflow-x: scroll;">
				<table class="table table-striped table-bordered table-condensed table-hover sortable">
					<thead>
						<tr>
							<th>设备号</th>
							<th>类型</th>
							<th>下发的报文</th>
							<th>结果</th>
							<th>是否下发</th>
							<th>下发次数</th>
							<th>创建时间</th>
							<th>更新时间</th>
							<th>操作人</th>
							<th>操&nbsp;&nbsp;作</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${sendPackets}" var="item" varStatus="status">
							<tr onclick="getRowIndex(this)">
								<td width="10px">${item.obdSn}</td>
								<td width="30px">${item.typeStr}</td>
								<td>${item.msgBody}</td>
								<td>${item.result}</td>
								<td width="10px">${item.sended}</td>
								<td width="10px">${item.sendCount}</td>
								<td width="30px"><fmt:formatDate value="${item.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
								<td width="30px"><fmt:formatDate value="${item.updateTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
								<td width="15px">${item.operator}</td>
								<td width="10px">
									<a href="javascript:del('${item.id}',${pager.pageSize},${pager.currentPage});" class="btn btn-warning"><i class="icon-trash"></i>&nbsp;&nbsp;删除</a>
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
</body>
</html>