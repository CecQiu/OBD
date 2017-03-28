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
	
	function myFormReset(){
		$("#obdSn").val("");
		$("#settingMsg").val("");
		$("#type").val("");
		$("#valid").val("");
		$("#starTime").val("");
		$("#endTime").val("");
	}
	
	
	function formCheck(){
		var obdSn=$("#obdSn").val().trim();
		var settingMsg=$("#settingMsg").val().trim();
		var type=$("#type").val().trim();
		var valid=$("#valid").val().trim();
		var starTime=$("#starTime").val();
		var endTime=$("#endTime").val();
		if(obdSn=='' && settingMsg=='' && type=='' && valid=='' && starTime=='' && endTime ==''){
			alert("请输入查询参数.");
			return false;
		}
	}
	
	
	function del(id){
		if(confirm("确认要删除吗？")){
			window.location.href = "obdSetting_delete.do?obdSetting.id="+id;
		}
	}
	
	
	function msgShow(id){
		alert($("#"+id).html());
	}

</script>
</head>
<body>
	<form name="myForm" id="myForm" action="obdSetting_query.do" method="post" onsubmit="return formCheck();">
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
								 <input type="text" size="24" id="obdSn" name="obdSetting.obdSn" value="${obdSetting.obdSn}" />
							</td>
							<th>内容</th>
							<td class="pn-fcontent">
								<input type="text" size="24" id="settingMsg" name="obdSetting.settingMsg" value="${obdSetting.settingMsg}" />
							</td>
						</tr>
						<tr>
							<th>设置类型</th>
							<td class="pn-fcontent">
								<select name="obdSetting.type" id="type" style="width: 250px;">
									<option value="">全部</option>
									<option value="wifi_00" <c:if test="${obdSetting.type=='wifi_00'}">selected</c:if>>wifi开关-wifi_00</option>
									<option value="wifi_01" <c:if test="${obdSetting.type=='wifi_01'}">selected</c:if>>wifi使用时间-wifi_01</option>
									<option value="wifi_10" <c:if test="${obdSetting.type=='wifi_10'}">selected</c:if>>wifi密码-wifi_10</option>
									<option value="wifi_11" <c:if test="${obdSetting.type=='wifi_11'}">selected</c:if>>wifi名称ssid-wifi_11</option>
									<option value="wifi_12" <c:if test="${obdSetting.type=='wifi_12'}">selected</c:if>>wifi密码和名称ssid-wifi_12</option>
									<option value="gps_00" <c:if test="${obdSetting.type=='gps_00'}">selected</c:if>>gps开关-gps_00</option>
									<option value="drive_00" <c:if test="${obdSetting.type=='drive_00'}">selected</c:if>>驾驶行为设置-急加速-drive_00</option>
									<option value="drive_01" <c:if test="${obdSetting.type=='drive_01'}">selected</c:if>>驾驶行为设置-急减速-drive_01</option>
									<option value="drive_02" <c:if test="${obdSetting.type=='drive_02'}">selected</c:if>>驾驶行为设置-急转弯-drive_02</option>
									<option value="drive_03" <c:if test="${obdSetting.type=='drive_03'}">selected</c:if>>驾驶行为设置-超速-drive_03</option>
									<option value="drive_04" <c:if test="${obdSetting.type=='drive_04'}">selected</c:if>>驾驶行为设置-疲劳驾驶-drive_04</option>
									<option value="switch_00" <c:if test="${obdSetting.type=='switch_00'}">selected</c:if>>非法启动探测开关-switch_00</option>
									<option value="switch_01" <c:if test="${obdSetting.type=='switch_01'}">selected</c:if>>非法震动探测开关-switch_01</option>
									<option value="switch_02" <c:if test="${obdSetting.type=='switch_02'}">selected</c:if>>蓄电电压异常报警开关-switch_02</option>
									<option value="switch_03" <c:if test="${obdSetting.type=='switch_03'}">selected</c:if>>发动机水温高报警开关-switch_03</option>
									<option value="switch_04" <c:if test="${obdSetting.type=='switch_04'}">selected</c:if>>车辆故障报警开关-switch_04</option>
									<option value="switch_05" <c:if test="${obdSetting.type=='switch_05'}">selected</c:if>>超速报警开关-switch_05</option>
									<option value="switch_06" <c:if test="${obdSetting.type=='switch_06'}">selected</c:if>>电子围栏报警开关-switch_06</option>
									<option value="switch_07" <c:if test="${obdSetting.type=='switch_07'}">selected</c:if>>保留开关-switch_07</option>
									<option value="obdDefault_00" <c:if test="${obdSetting.type=='obdDefault_00'}">selected</c:if>>未激活设备设置默认分组-obdDefault_00</option>
									
									<option value="domain_00" <c:if test="${obdSetting.type=='domain_00'}">selected</c:if>>域白名单功能开关-domain_00</option>
									<option value="domain_01" <c:if test="${obdSetting.type=='domain_01'}">selected</c:if>>域黑名单功能开关-domain_01</option>
									<option value="domain_02" <c:if test="${obdSetting.type=='domain_02'}">selected</c:if>>禁止MAC上网-domain_02</option>
									<option value="domain_03" <c:if test="${obdSetting.type=='domain_03'}">selected</c:if>>增加多个域白名单-domain_03</option>
									<option value="domain_04" <c:if test="${obdSetting.type=='domain_04'}">selected</c:if>>删除单个域白名单-domain_04</option>
									<option value="domain_05" <c:if test="${obdSetting.type=='domain_05'}">selected</c:if>>删除所有域白名单-domain_05</option>
									<option value="domain_06" <c:if test="${obdSetting.type=='domain_06'}">selected</c:if>>增加多个域黑名单-domain_06</option>
									<option value="domain_07" <c:if test="${obdSetting.type=='domain_07'}">selected</c:if>>删除单个域黑名单-domain_07</option>
									<option value="domain_08" <c:if test="${obdSetting.type=='domain_08'}">selected</c:if>>删除所有域黑名单-domain_08</option>
									
									<option value="dmsyn_00" <c:if test="${obdSetting.type=='dmsyn_00'}">selected</c:if>>白名单同步-dmsyn_00</option>
									<option value="dmsyn_01" <c:if test="${obdSetting.type=='dmsyn_01'}">selected</c:if>>黑名单同步-dmsyn_01</option>
									
									<option value="wakeupSwitch" <c:if test="${obdSetting.type=='wakeupSwitch'}">selected</c:if>>自动唤醒开关-wakeupSwitch</option>
									<option value="wakeupTime" <c:if test="${obdSetting.type=='wakeupTime'}">selected</c:if>>自动唤醒时间-wakeupTime</option>
									<option value="portalSwitch" <c:if test="${obdSetting.type=='portalSwitch'}">selected</c:if>>portal开关-portalSwitch</option>
								</select>
							</td>
							<th>有效</th>
							<td class="pn-fcontent">
								<select name="obdSetting.valid" id="valid" style="width: 200px;">
									<option value="">全部</option>
									<option value="0" <c:if test="${obdSetting.valid=='0'}">selected</c:if>>无效0</option>
									<option value="1" <c:if test="${obdSetting.valid=='1'}">selected</c:if>>有效1</option>
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
				<h5>obd设置信息</h5>
			</div>
			<!-- /widget-header -->
			<div class="widget-content widget-list">
				<table class="table table-striped table-bordered table-condensed table-hover sortable">
					<thead>
						<tr>
							<th>序号</th>
							<th>设备号</th>
							<th>类型</th>
							<th>内容</th>
							<th>有效</th>
							<th>创建时间</th>
							<th>更新时间</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${obdSettings}" var="item" varStatus="status">
							<tr>
								<td>${item.id}</td>
								<td>${item.obdSn}</td>
								<td>
									<c:choose>
										<c:when test="${item.type=='wifi_00'}">wifi开关</c:when>
										<c:when test="${item.type=='wifi_01'}">wifi使用时间</c:when>
										<c:when test="${item.type=='wifi_10'}">wifi密码</c:when>
										<c:when test="${item.type=='wifi_11'}">wifi名称</c:when>
										<c:when test="${item.type=='wifi_12'}">wifi密码和名称</c:when>
										<c:when test="${item.type=='gps_00'}">gps开关</c:when>
										<c:when test="${item.type=='drive_00'}">驾驶行为设置-急加速</c:when>
										<c:when test="${item.type=='drive_01'}">驾驶行为设置-急减速</c:when>
										<c:when test="${item.type=='drive_02'}">驾驶行为设置-急转弯</c:when>
										<c:when test="${item.type=='drive_03'}">驾驶行为设置-超速</c:when>
										<c:when test="${item.type=='drive_04'}">驾驶行为设置-疲劳驾驶</c:when>
										<c:when test="${item.type=='switch_00'}">非法启动探测开关</c:when>
										<c:when test="${item.type=='switch_01'}">非法震动探测开关</c:when>
										<c:when test="${item.type=='switch_02'}">蓄电电压异常报警开关</c:when>
										<c:when test="${item.type=='switch_03'}">发动机水温高报警开关</c:when>
										<c:when test="${item.type=='switch_04'}">车辆故障报警开关</c:when>
										<c:when test="${item.type=='switch_05'}">超速报警开关</c:when>
										<c:when test="${item.type=='switch_06'}">电子围栏报警开关</c:when>
										<c:when test="${item.type=='switch_07'}">保留开关</c:when>
										<c:when test="${item.type=='obdDefault_00'}">未激活设备设置默认分组</c:when>
										<c:when test="${item.type=='domain_00'}">域白名单功能开关</c:when>
										<c:when test="${item.type=='domain_01'}">域黑名单功能开关</c:when>
										<c:when test="${item.type=='domain_02'}">禁止MAC上网</c:when>
										<c:when test="${item.type=='domain_03'}">增加多个域白名单</c:when>
										<c:when test="${item.type=='domain_04'}">删除单个域白名单</c:when>
										<c:when test="${item.type=='domain_05'}">删除所有域白名单</c:when>
										<c:when test="${item.type=='domain_06'}">增加多个域黑名单</c:when>
										<c:when test="${item.type=='domain_07'}">删除单个域黑名单</c:when>
										<c:when test="${item.type=='domain_08'}">删除所有域黑名单</c:when>
										<c:when test="${item.type=='dmsyn_00'}">域白名单同步</c:when>
										<c:when test="${item.type=='dmsyn_01'}">域黑名单同步</c:when>
										<c:when test="${item.type=='wakeupSwitch'}">自动唤醒开关</c:when>
										<c:when test="${item.type=='wakeupTime'}">自动唤醒时间</c:when>
										<c:when test="${item.type=='portalSwitch'}">portal开关</c:when>
										<c:otherwise></c:otherwise>
									</c:choose>
								</td>
								
								<td id='${item.id}'>${item.settingMsg}</td>
								<td>
									<c:choose>
										<c:when test="${item.valid=='0'}">0-成功或被覆盖</c:when>
										<c:when test="${item.valid=='1'}">1-未成功</c:when>
										<c:otherwise></c:otherwise>
									</c:choose>
								</td>
								<td><fmt:formatDate value="${item.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
								<td><fmt:formatDate value="${item.updateTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
								<td>
									<c:if test="${item.valid=='1'}">
										<a href="javascript:del('${item.id}',${pager.pageSize},${pager.currentPage});" class="btn btn-warning"><i class="icon-trash"></i>&nbsp;&nbsp;删除</a>
									</c:if>
									<button type="button"  class="btn" onclick="msgShow('${item.id}');" auth="auth-push">查看</button>
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