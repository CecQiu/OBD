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
	if("${result}"=="SUCCESS"){
		alert("参数配置成功！");
	}
	
	function getRowIndex(obj){
		get("pager.rowIndex").value=obj.rowIndex;
	}
	
	function myFormReset(){
		$("#obdSn").val("");
		$("#comman").val("");
		$("#packetType").val("");
		$("#starTime").val("");
		$("#endTime").val("");
	}
	
	function obdMsgShow(packet,obdMsg){
		alert(packet+"\n"+obdMsg);
		/* var url="${basePath}/admin/obdMsg_showMsg.do?obdPacket.msg="+encodeURI(obdMsg,'utf8');
		//window.open(url,"",'width=500px,height=300,toolbar =no, menubar=no, scrollbars=no, resizable=no, location=no, status=no');
		$("#iframe").html('');
		document.getElementById("iframe").src=url;
		document.getElementById("obdMsg").style.display=""; */
	}

	/* function hideIframe(){
		$("#iframe").html('');
		$("#obdMsg").hide();
	} */
	
	function formCheck(){
		var obdSn=$("#obdSn").val().trim();
		var comman=$("#comman").val();
		var packetType=$("#packetType").val();
		var starTime=$("#starTime").val();
		var endTime=$("#endTime").val();
		if(obdSn=='' && comman=='' && packetType=='' && starTime=='' && endTime ==''){
			alert("请输入查询参数.");
			return false;
		}
	}

</script>
</head>
<body>
	<form name="myForm" id="myForm" action="obdMsg_query.do" method="post" onsubmit="return formCheck();">
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
								 <input type="text" size="24" id="obdSn" name="obdPacket.obdSn" value="${obdPacket.obdSn}" />
							</td>
							<th></th>
							<td class="pn-fcontent"></td>
						</tr>
						<tr>
							<th>报文类型 </th>
							<td class="pn-fcontent">
								<select name="obdPacket.comman" id="comman" style="width: 200px;">
									<option value="">全部</option>
									<option value="0001" <c:if test="${obdPacket.comman=='0001'}">selected</c:if>>设备初始化0001</option>
									<option value="0002" <c:if test="${obdPacket.comman=='0002'}">selected</c:if>>位置数据上传0002</option>
									<option value="0003" <c:if test="${obdPacket.comman=='0003'}">selected</c:if>>行程记录上传0003</option>
									<option value="0004" <c:if test="${obdPacket.comman=='0004'}">selected</c:if>>设备数据上传0004</option>
									<option value="0005" <c:if test="${obdPacket.comman=='0005'}">selected</c:if>>设备请求数据帧/ACK应答0005</option>
									<option value="8001" <c:if test="${obdPacket.comman=='8001'}">selected</c:if>>主动设置8001</option>
									<option value="8002" <c:if test="${obdPacket.comman=='8002'}">selected</c:if>>服务器请求数据8002</option>
									<option value="8003" <c:if test="${obdPacket.comman=='8003'}">selected</c:if>>服务响应设备请求8003</option>
								</select>
							</td>
							<th>上传/下发报文 </th>
							<td class="pn-fcontent">
								<select name="obdPacket.packetType" id="packetType" style="width: 200px;">
									<option value="">全部</option>
									<option value="0" <c:if test="${obdPacket.packetType=='0'}">selected</c:if>>上传-未拆包0</option>
									<option value="1" <c:if test="${obdPacket.packetType=='1'}">selected</c:if>>上传-拆包1</option>
									<option value="2" <c:if test="${obdPacket.packetType=='2'}">selected</c:if>>下发2</option>
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
				<h5>报文信息</h5>
			</div>
			<!-- /widget-header -->
			<div class="widget-content widget-list">
				<table class="table table-striped table-bordered table-condensed table-hover sortable">
					<thead>
						<tr>
							<th>编&nbsp;&nbsp;号</th>
							<th>设备号</th>
							<th>命令字</th>
							<th>报文</th>
							<th>转后报文</th>
							<th>内容</th>
							<th>是否拆包</th>
							<th>创建时间</th>
							<th>操&nbsp;&nbsp;作</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${obdPackets}" var="item" varStatus="status">
							<tr onclick="getRowIndex(this)">
								<td>${item.id}</td>
								<td>${item.obdSn}</td>
								<td>${item.comman}</td>
								<td>${item.packetData}</td>
								<td>${item.apacketData}</td>
								<td>${item.msg}</td>
								<td>${item.packetType}</td>
								<td><fmt:formatDate value="${item.insertTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
								<td>
									<input class="btn btn-s-md btn-success" type="button" value="查看" onclick="obdMsgShow('${item.packetData}','${item.msg}')"/>
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
	<!-- <div id="obdMsg" class="widget" style="display:none; POSITION:absolute; left:50%; top:50%; width:450px; height:300px; margin-left:-300px; margin-top:-100px; border:1px solid #888; background-color:white; text-align:center">
		<iframe id="iframe" width='450px' height='300px'></iframe>
	</div> -->
</body>
</html>