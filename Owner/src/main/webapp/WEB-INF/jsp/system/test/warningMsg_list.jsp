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
	
	function myFormReset(){
		$("#obdSn").val("");
		$("#messageType").val("");
		$("#remark").val("");
		$("#company").val("");
		$("#starTime").val("");
		$("#endTime").val("");
	}
	
	
	function formCheck(){
		var obdSn=$("#obdSn").val().trim();
		var comman=$("#messageType").val();
		var remark=$("#remark").val();
		var company=$("#company").val();
		var starTime=$("#starTime").val();
		var endTime=$("#endTime").val();
		
		
		if(obdSn==''){
			alert("设备号不能为空.");
			return false;
		}
		
		/* if(starTime=='' || endTime ==''){
			alert("开始时间和结束时间不能为空.");
			return false;
		} */
		if(starTime!='' && endTime !=''){
			//查询只能查询一周的记录
			var a = starTime.split(" ");     
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
			/* if(days > 31){
				alert("日期范围应在31天内.");
				return false;
			} */
		}
	}

</script>
</head>
<body>
	<form name="myForm" id="myForm" action="warningMsg_query.do" method="post" onsubmit="return formCheck();">
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
								 <input type="text" size="24" id="obdSn" name="warningMessage.obdSn" value="${warningMessage.obdSn}" />
							</td>
							<th>公司</th>
							<td class="pn-fcontent">
								<select name="warningMessage.company" id="company" style="width: 200px;">
									<option value="">全部</option>
									<option value="0" <c:if test="${warningMessage.company=='0'}">selected</c:if>>亿讯0</option>
									<option value="1" <c:if test="${warningMessage.company=='1'}">selected</c:if>>号百1</option>
								</select>
							</td>
						</tr>
						<tr>
							<th>报警类型 </th>
							<td class="pn-fcontent">
								<select name="warningMessage.messageType" id="messageType" style="width: 200px;">
									<option value="">全部</option>
									<option value="11" <c:if test="${warningMessage.messageType=='11'}">selected</c:if>>非法启动11</option>
									<option value="12" <c:if test="${warningMessage.messageType=='12'}">selected</c:if>>异常震动12</option>
									<option value="14" <c:if test="${warningMessage.messageType=='14'}">selected</c:if>>疲劳驾驶预警14</option>
									<option value="15" <c:if test="${warningMessage.messageType=='15'}">selected</c:if>>车辆震动提醒15</option>
									<option value="16" <c:if test="${warningMessage.messageType=='16'}">selected</c:if>>点火启动提醒16</option>
									<option value="17" <c:if test="${warningMessage.messageType=='17'}">selected</c:if>>故障提醒17</option>
									<option value="21" <c:if test="${warningMessage.messageType=='21'}">selected</c:if>>急变速21</option>
									<option value="27" <c:if test="${warningMessage.messageType=='27'}">selected</c:if>>超速报警27</option>
									<option value="30" <c:if test="${warningMessage.messageType=='30'}">selected</c:if>>怠速30</option>
									<option value="00" <c:if test="${warningMessage.messageType=='00'}">selected</c:if>>疲劳驾驶解除00</option>
									<option value="51" <c:if test="${warningMessage.messageType=='51'}">selected</c:if>>进区域报警51</option>
									<option value="52" <c:if test="${warningMessage.messageType=='52'}">selected</c:if>>出区域报警52</option>
									<option value="53" <c:if test="${warningMessage.messageType=='53'}">selected</c:if>>进出区域报警53</option>
								</select>
							</td>
							<th>推送状态</th>
							<td class="pn-fcontent">
								<select name="warningMessage.remark" id="remark" style="width: 200px;">
									<option value="">全部</option>
									<option value="0" <c:if test="${warningMessage.remark=='0'}">selected</c:if>>成功0</option>
									<option value="-1" <c:if test="${warningMessage.remark=='-1'}">selected</c:if>>失败-1</option>
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
				<h5>短信推送</h5>
			</div>
			<!-- /widget-header -->
			<div class="widget-content widget-list">
				<table class="table table-striped table-bordered table-condensed table-hover sortable">
					<thead>
						<tr>
							<th>序号</th>
							<th>设备号</th>
							<th>预警类型</th>
							<th>内容</th>
							<th>公司</th>
							<th>推送状态</th>
							<th>创建时间</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${warningMsgs}" var="item" varStatus="status">
							<tr>
								<td>${item.id}</td>
								<td>${item.obdSn}</td>
								<td>${item.messageType}</td>
								<td>${item.messageDesc}</td>
								<td>${item.company}</td>
								<td>${item.remark}</td>
								<td><fmt:formatDate value="${item.messageTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
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