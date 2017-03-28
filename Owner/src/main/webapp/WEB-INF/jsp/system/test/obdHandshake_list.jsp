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
		$("#obdMSn").val("");
		$("#startTime").val("");
		$("#endTime").val("");
		
		$("#gpsModule").val("");
		$("#accelerator3D").val("");
		$("#carFaultCode").val("");
		$("#wifiSet").val("");
	}
	
	
	function formCheck(){
		var obdSn=$("#obdSn").val().trim();
		var obdMSn=$("#obdMSn").val().trim();
		var startTime=$("#startTime").val();
		var endTime=$("#endTime").val();
		
		var gpsModule=$("#gpsModule").val();
		var accelerator3D=$("#accelerator3D").val();
		var carFaultCode=$("#carFaultCode").val();
		var wifiSet=$("#wifiSet").val();
		
		/* if(obdSn!='' && obdMSn!=''){
			alert("设备号和表面码只能输入一个.");
			return false;
		} */
		if(startTime=='' && endTime ==''){
			alert("开始时间和结束时间不能为空.");
			return false;
		}
		
		if(obdSn!=''){
			$("#obdSn").val(obdSn.trim());
		}
		if(obdMSn!=''){
			$("#obdMSn").val(obdMSn.trim());
		}
		
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
		if(days > 31){
			alert("日期范围应在31天之内.");
			return false;
		}
		
	}
	
	function exportExcel(){
		
		var obdSn=$("#obdSn").val().trim();
		var obdMSn=$("#obdMSn").val().trim();
		var startTime=$("#startTime").val();
		var endTime=$("#endTime").val();
		
		var gpsModule=$("#gpsModule").val();
		var accelerator3D=$("#accelerator3D").val();
		var carFaultCode=$("#carFaultCode").val();
		var wifiSet=$("#wifiSet").val();
		
		/* if(obdSn!='' && obdMSn!=''){
			alert("设备号和表面码只能输入一个.");
			return false;
		} */
		if(startTime=='' || endTime==''){
			alert('请选择时间段,请勿导出多天的记录.');
			return;
		} 
		
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
		if(days > 31){
			alert("日期范围应在31天之内.");
			return false;
		}
		
		window.location.href="${basePath}/admin/obdHandshake_exportExcel.do?obdSn="+obdSn+
				"&obdMSn="+obdMSn+"&startTime="+startTime+"&endTime="+endTime+"&gpsModule="+gpsModule+
				"&accelerator3D="+accelerator3D+"&carFaultCode="+carFaultCode+"&wifiSet="+wifiSet;  
		
	}

</script>
</head>
<body>
	<form name="myForm" id="myForm" action="obdHandshake_list.do" method="post" onsubmit="return formCheck();">
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
								 <input type="text" size="24" id="obdSn" name="obdSn" value="${obdSn}" />
							</td>
							<th>表面号</th>
							<td class="pn-fcontent">
								<input type="text" size="24" id="obdMSn" name="obdMSn" value="${obdMSn}" />
							</td>
						</tr>
						<tr>
                           <th>GPS模块</th> 
                           <td class="pn-fcontent">
                           	  <select name="gpsModule" id="gpsModule" style="width: 100px;">
								<option value="">请选择</option>
								<option value="0" <c:if test="${gpsModule=='0'}">selected</c:if>>正常0</option>
								<option value="1" <c:if test="${gpsModule=='1'}">selected</c:if>>异常1</option>	
							  </select>
                           </td>
                           <th>3D加速器传感器</th> 
                           <td class="pn-fcontent">
                           	  <select name="accelerator3D" id="accelerator3D" style="width: 100px;">
								<option value="">请选择</option>
								<option value="0" <c:if test="${accelerator3D=='0'}">selected</c:if>>正常0</option>
								<option value="1" <c:if test="${accelerator3D=='1'}">selected</c:if>>异常1</option>	
							  </select>
                           </td>                                        
                       </tr>
						<tr>
                           <th>车辆故障码</th> 
                           <td class="pn-fcontent">
                           	  <select name="carFaultCode" id="carFaultCode" style="width: 100px;">
								<option value="">请选择</option>
								<option value="0" <c:if test="${carFaultCode=='0'}">selected</c:if>>无0</option>
								<option value="1" <c:if test="${carFaultCode=='1'}">selected</c:if>>有1</option>	
							  </select>
                           </td>
                           <th>Wifi设置</th> 
                           <td class="pn-fcontent">
                           	  <select name="wifiSet" id="wifiSet" style="width: 100px;">
								<option value="">请选择</option>
								<option value="0" <c:if test="${wifiSet=='0'}">selected</c:if>>开0</option>
								<option value="1" <c:if test="${wifiSet=='1'}">selected</c:if>>关1</option>	
							  </select>
                           </td>                                       
                       </tr>
						<tr>
							<th>开始时间</th>
							<td class="pn-fcontent">
								<input type="text" value="${startTime}" class="Wdate" readonly="readonly" name="startTime" id="startTime" onclick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss',maxDate:'%y-%M-%d %H:%m:%s'})"/>
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
		<div class="widget widget-table" style="overflow-x: scroll;">
			<div class="widget-header">
				<i class="icon-th-list"></i>
				<h5>obd握手包信息</h5>
			</div>
			<!-- /widget-header -->
			<div class="widget-content widget-list">
				<table class="table table-striped table-bordered table-condensed table-hover sortable">
					<thead>
						<tr>
							<!-- <th>序号</th> -->
							<th>设备号</th>
							<th>固件版本号</th>
							<th>唤醒方式</th>
							<th>离线数据</th>
							<th>ECU通讯协议</th>
							<th>车辆故障码</th>
							<th>启动方式</th>
							<th>前次休眠原因</th>
							<th>上电号</th>
							<th>前次上电号</th>
							<th>创建时间</th>
							<th>GPS模块</th>
							<th>EFPROM</th>
							<th>3D加速器传感器</th>
							<th>Wifi设置</th>
							<th>GPS设置</th>
							<th>GPS数据格式</th>
							<th>离线心跳设置</th>
							<th>注册网络</th>
							<th>网络信号强度</th>
							<th>蓄电池电压情况</th>
							<th>发动机水温</th>
							<th>非法启动探测设置</th>
							<th>非法震动探测设置</th>
							<th>蓄电电压异常报警设置</th>
							<th>发动机水温高报警设置</th>
							<th>车辆故障报警设置</th>
							<th>超速报警设置</th>
							<th>电子围栏报警设置</th>
							<th>VIN码</th>
							<th>蓄电池电压</th>
							<th>电压类型</th>
							<th>流量统计值</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${obdHandShakes}" var="item" varStatus="status">
							<tr>
								<%-- <td>${item.id}</td> --%>
								<td>${item.obdSn}</td>
								<td>${item.firmwareVersion}</td>
								<td>${item.wakeUp}</td>
								<td>${item.hasOffData}</td>
								<td>${item.ecu}</td>
								<td>${item.carFaultCode}</td>
								
								<td>
									<c:choose>
										<c:when test="${item.startMode==0}">0-点火</c:when>
										<c:when test="${item.startMode==1}">1-震动</c:when>
										<c:otherwise></c:otherwise>
									</c:choose>
								</td>
								
								<td>${item.lastSleep}</td>
								<td>${item.upElectricNo}</td>
								<td>${item.lastUpElectricNo}</td>
								<td><fmt:formatDate value="${item.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
								<td>${item.gpsModule}</td>
								<td>${item.efprom}</td>
								<td>${item.accelerator3D}</td>
								
								<td>
									<c:choose>
										<c:when test="${item.wifiSet==0}">0-开</c:when>
										<c:when test="${item.wifiSet==1}">1-关</c:when>
										<c:otherwise></c:otherwise>
									</c:choose>
								</td>
								
								<td>
									<c:choose>
										<c:when test="${item.gpsSet==0}">0-开</c:when>
										<c:when test="${item.gpsSet==1}">1-关</c:when>
										<c:otherwise></c:otherwise>
									</c:choose>
								</td>
								<td>${item.gpsDataFormat}</td>
								
								<td>
									<c:choose>
										<c:when test="${item.offHeartSet==0}">0-无</c:when>
										<c:when test="${item.offHeartSet==1}">1-设置</c:when>
										<c:otherwise></c:otherwise>
									</c:choose>
								</td>
								
								<td>${item.regNet}</td>
								<td>${item.netSinal}</td>
								<td>${item.voltStatus}</td>
								<td>${item.engineWater}</td>
								
								<td>
									<c:choose>
										<c:when test="${item.illegalStartSet==0}">0-开启</c:when>
										<c:when test="${item.illegalStartSet==1}">1-关闭</c:when>
										<c:otherwise></c:otherwise>
									</c:choose>
								</td>
								
								<td>
									<c:choose>
										<c:when test="${item.illegalShockSet==0}">0-开启</c:when>
										<c:when test="${item.illegalShockSet==1}">1-关闭</c:when>
										<c:otherwise></c:otherwise>
									</c:choose>
								</td>
								
								<td>
									<c:choose>
										<c:when test="${item.voltUnusualSet==0}">0-开启</c:when>
										<c:when test="${item.voltUnusualSet==1}">1-关闭</c:when>
										<c:otherwise></c:otherwise>
									</c:choose>
								</td>
								
								<td>
									<c:choose>
										<c:when test="${item.engineWaterWarnSet==0}">0-开启</c:when>
										<c:when test="${item.engineWaterWarnSet==1}">1-关闭</c:when>
										<c:otherwise></c:otherwise>
									</c:choose>
								</td>
								
								<td>
									<c:choose>
										<c:when test="${item.carWarnSet==0}">0-开启</c:when>
										<c:when test="${item.carWarnSet==1}">1-关闭</c:when>
										<c:otherwise></c:otherwise>
									</c:choose>
								</td>
								
								<td>
									<c:choose>
										<c:when test="${item.overSpeedWarnSet==0}">0-开启</c:when>
										<c:when test="${item.overSpeedWarnSet==1}">1-关闭</c:when>
										<c:otherwise></c:otherwise>
									</c:choose>
								</td>
								
								<td>
									<c:choose>
										<c:when test="${item.efenceWarnSet==0}">0-开启</c:when>
										<c:when test="${item.efenceWarnSet==1}">1-关闭</c:when>
										<c:otherwise></c:otherwise>
									</c:choose>
								</td>
								
								<td>${item.vinCode}</td>
								<td>${item.volt}</td>
								<td>${item.voltType}</td>
								<td>${item.flowCounter}</td>
								
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