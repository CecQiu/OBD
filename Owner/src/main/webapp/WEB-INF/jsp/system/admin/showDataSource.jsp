<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<html>
<head>
<title></title>
    <jsp:include page="../../include/common.jsp" />
</head>
<body  style="padding-left: 10px;padding-top: 20px;" onunload="uninit();" >
	<div id="content">
		<div class="container-fluid">
			<div class="row-fluid">
				<div class="span12">
					<div class="widget-box">
					
						<div class="widget-title">
							<span class="icon"><i class="icon-th"></i> </span>
							<h5>数据库连接池监控</h5>
						</div>
					
					
					
						<div class="widget-content nopadding">
								<table class="pn-ftable table-bordered table-condensed">
									<thead>
										<tr>
											<th colspan="2">基础信息</th>
										</tr>
									</thead>
									<tbody>
									 	<tr style="height: 35px;">
											<td style="width:560px;">&nbsp;连接url(jdbcUrl):</td>
											<td style="text-align: left;">&nbsp;${comboPooledDataSource.jdbcUrl}</td>
										</tr>
										<tr style="height: 35px;">
											<td >&nbsp;用户名(user):</td>
											<td style="text-align: left;">&nbsp;${comboPooledDataSource.user}</td>
										</tr>
										<tr style="height: 35px;">
											<td >&nbsp;驱动类(driverClass):</td>
											<td style="text-align: left;">&nbsp;${comboPooledDataSource.driverClass}</td>
										</tr>
										<tr style="height: 35px;">
											<td >&nbsp;初始连接数(initialPoolSize):</td>
											<td style="text-align: left;">&nbsp;${comboPooledDataSource.initialPoolSize}</td>
										</tr>
										<tr style="height: 35px;">
											<td >&nbsp;最少连接数(minPoolSize):</td>
											<td style="text-align: left;">&nbsp;${comboPooledDataSource.minPoolSize}</td>
										</tr>
										<tr style="height: 35px;">
											<td >&nbsp;最大连接数(maxPoolSize):</td>
											<td style="text-align: left;">&nbsp;${comboPooledDataSource.maxPoolSize}</td>
										</tr>
										<tr style="height: 35px;">
											<td >&nbsp;最大空闲时间(maxIdleTime):</td>
											<td style="text-align: left;">&nbsp;${comboPooledDataSource.maxIdleTime}秒</td>
										</tr>
										<tr>
											<td colspan="2" style="text-align: center;">&nbsp;以下信息每
												<select id="chooseSel" onchange="choose(this)">
													<option selected="selected" value="5000">5</option>
													<option value="10000">10</option>
													<option value="15000">15</option>
													<option value="30000" >30</option>
													<option value="60000" >60</option>
													<option value="120000" >120</option>
												</select>秒自动刷新，已刷新<em><span id="loadSpan" style="color:red;font-weight: bold;">0</span></em>次
											</td>
										</tr>
										<tr style="height: 35px;" >
											<td >&nbsp;空闲连接数(numIdleConnectionsAllUsers):</td>
											<td style="text-align: left;color:red" id="idleTd">&nbsp;${comboPooledDataSource.numIdleConnectionsAllUsers}</td>
										</tr>
										<tr style="height: 35px;" >
											<td>&nbsp;繁忙连接数(numBusyConnectionsAllUsers):</td>
											<td style="text-align: left;color:red" id="busyTd">&nbsp;${comboPooledDataSource.numBusyConnectionsAllUsers}</td>
										</tr>
										<tr style="height: 35px;" >
											<td>&nbsp;未关闭连接数(numUnclosedOrphanedConnectionsAllUsers):</td>
											<td style="text-align: left;color:red" id="unclosedTd">&nbsp;${comboPooledDataSource.numUnclosedOrphanedConnectionsAllUsers}</td>
										</tr>
									</tbody>
								</table>
							</div>
							
							
							
					</div>
				</div>
			</div>
		</div>
	</div>
	
	
</body>
<script type="text/javascript">
    var url = "admin_showDateSource.do";
    var showDateSource_flag;
    var time=5000;
    var loadTimes = 0;
	$(function(){
		if(showDateSource_flag)
		{
			clearInterval(showDateSource_flag);
		}
		
		showDateSource_flag = setInterval(function(){
			$.post(url,{"flag":"reload"},function(data,status){
				$("#idleTd").html("&nbsp;"+data.idle);
				$("#busyTd").html("&nbsp;"+data.busy);
				$("#unclosedTd").html("&nbsp;"+data.unclosed);
				
				loadTimes++;
				$("#loadSpan").html(loadTimes);
			},'json');
			//location.reload();
		},time);
	
	});
	
	function choose(obj)
	{
		var _self = obj;
		
		time = _self.value;
		
		if(showDateSource_flag)
		{
			try{
				clearInterval(showDateSource_flag);
				
				showDateSource_flag = setInterval(function(){
					$.post(url,{"flag":"reload"},function(data,status){
						$("#idleTd").html("&nbsp;"+data.idle);
						$("#busyTd").html("&nbsp;"+data.busy);
						$("#unclosedTd").html("&nbsp;"+data.unclosed);
						
						loadTimes++;
						$("#loadSpan").html(loadTimes);
					},'json');
					//location.reload();
				},time);
			}catch(e)
			{
				
			}
		}
	}
	
	function uninit()
	{
		if(showDateSource_flag)
		{
			try{
				clearInterval(showDateSource_flag);
				showDateSource_flag = null;
				loadTimes = 0;
			}catch(e)
			{
				
			}
		}
	}
</script>
</html>