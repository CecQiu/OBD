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
		$("#starTime").val("");
		$("#endTime").val("");
	}
	
	
	function formCheck(){
		var obdSn=$("#obdSn").val().trim();
		var starTime=$("#starTime").val();
		var endTime=$("#endTime").val();
		if(obdSn=='' && starTime=='' && endTime ==''){
			alert("请输入查询参数.");
			return false;
		}
	}
	
</script>
</head>
<body>
	<form name="myForm" id="myForm" action="obdTravelParams_query.do" method="post" onsubmit="return formCheck();">
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
								 <input type="text" size="24" id="obdSn" name="obdTravelParams.obdSn" value="${obdTravelParams.obdSn}" />
							</td>
							<th></th>
							<td class="pn-fcontent">
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
				<h5>OBD行程参数表</h5>
			</div>
			<!-- /widget-header -->
			<div class="widget-content widget-list"  style="overflow-x: scroll;">
				<table class="table table-striped table-bordered table-condensed table-hover sortable">
					<thead>
						<tr>
							<th>序号</th>
							<th>设备号</th>
							<th>蓄电池电压低阈值</th>
							<th>蓄电池电压高阈值</th>
							
							<th>超速阈值：时速阈值km/h</th>
							<th>超速阈值：限速延迟时间阈值s</th>
							
							<th>急转弯阈值：速度阈值 km/h</th>
							<th>急转弯阈值：角度阈值 度</th>
							
							<th>急加速阈值：速度变化阈值km/h</th>
							<th>急加速阈值：时间阈值</th>
							
							<th>急减速阈值：速度变化阈值km/h</th>
							<th>急减速阈值：时间阈值</th>
							
							<th>急变道阈值：角度阈值 km/h</th>
							<th>急变道阈值：时间阈值</th>
							
							<th>发动机水温报警阈值：低 摄氏度</th>
							<th>发动机水温报警阈值：高 摄氏度</th>
							
							<th>发动机转数报警阈值 转/分钟</th>
							<th>车速转速不匹配阈值：	速度30km/h时匹配转速(转/分钟)</th>
							
							<th>车速转速不匹配阈值：	转速步进值	转/分钟</th>
							
							<th>长怠速阈值：怠速车速阈值km/h</th>
							<th>长怠速阈值：时间阈值 1分钟</th>
							
							<th>急刹车强度阈值</th>
							<th>侧翻角度阈值</th>
							
							<th>碰撞强度阈值</th>
							<th>震动报警强度阈值</th>
							
							<th>疲劳驾驶连续驾驶时间单位分</th>
							<th>疲劳驾驶休息时间,单位分</th>
							
							<th>电子围栏驶出报警坐标：经度</th>
							<th>电子围栏驶出报警坐标：纬度</th>
							
							<th>电子围栏驶入报警坐标：经度</th>
							<th>电子围栏驶入报警坐标：纬度</th>
							<th>创建时间</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${obdTravelParamsList}" var="item" varStatus="status">
							<tr>
								<td>${item.id}</td>
								<td>${item.obdSn}</td>
								<td>${item.batteryLow}</td>
								<td>${item.batteryHigh}</td>
								
								<td>${item.overSpeed}</td>
								<td>${item.limitSpeedLazy}</td>
								
								<td>${item.shuddenTurnSpeed}</td>
								<td>${item.shuddenTurnAngle}</td>
								
								<td>${item.shuddenOverSpeed}</td>
								<td>${item.shuddenOverSpeedTime}</td>
								
								
								<td>${item.shuddenLowSpeed}</td>
								<td>${item.shuddenLowSpeedTime}</td>
								
								<td>${item.shuddenChangeAngle}</td>
								<td>${item.shuddenChangeTime}</td>
								
								<td>${item.engineLowTemperature}</td>
								<td>${item.engineHighTemperature}</td>
								
								<td>${item.engineTurnsWarn}</td>
								<td>${item.speedNotMatch}</td>
								
								<td>${item.speedNotMatchStep}</td>
								<td>${item.longLowSpeed}</td>
								
								<td>${item.longLowSpeedTime}</td>
								<td>${item.shuddenBrakeStrength}</td>
								
								<td>${item.sideTurnAngle}</td>
								<td>${item.crashStrength}</td>
								
								<td>${item.shockStrength}</td>
								<td>${item.fatigueDrive}</td>
								
								<td>${item.fatigueSleep}</td>
								<td>${item.fenceInLongtitude}</td>
								
								<td>${item.fenceInLatitude}</td>
								<td>${item.fenceOutLongtitude}</td>
								
								<td>${item.fenceOutLatitude}</td>
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