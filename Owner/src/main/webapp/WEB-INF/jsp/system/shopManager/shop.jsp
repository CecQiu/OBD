<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
    <table class="table table-bordered">
		<thead>
			<tr>
				<td colspan="7" style="padding:3px;background-color: #EEEEEE;padding-left: 15px">
					<b>选择省市区：</b>
					<select style="width: 150px" name="shopInfo.province" id="province"></select>&emsp;
					<select style="width: 150px" id="city" name="shopInfo.city"></select>&emsp;
					<select style="width: 150px" id="area" name="shopInfo.area"></select>
					<input type="button" class="btn btn-info" value="查找" onclick="searchShop()"/>
				</td>
			</tr>
			<tr>
				<th>序列号</th>
				<th>彩站账号</th>
				<th>彩站名称</th>
				<th>所属省</th>
				<th>所属市</th>
				<th>所属地区</th>
				<th>彩站地址</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${list}" var="item" varStatus="status">
				<tr>
					<td width="100px"><input style="width: 20px" type="checkbox" id="shopTr${item.shopId}" 
						onclick="changeDiv(this,'${item.shopId}','${item.shopName}')"/>
					${status.count }
					</td>
					<td>${item.accountId}</td>
					<td>${item.shopName}</td>
					<td>${item.province}</td>
					<td>${item.city}</td>
					<td>${item.area}</td>
					<td>${item.shopAddress}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="widget-bottom">
		<jsp:include page="../../include/areapager.jsp" />
	</div>
	<div class="widget-bottom">
		<center>
			<a onclick="$(this).parents('form').submit();" href="javascript:;" class="btn btn-primary">保 存</a>
			<a href="javascript:window.location.href='adminForShop_list.do';" class="btn btn-danger">返 回</a>
		</center>
	</div>
	<%-- <div class="widget widget-table">
		<!-- /widget-header -->
		<div class="widget-content widget-list">
			<table class="table table-striped table-bordered table-condensed table-hover sortable">
				<thead>
					<tr>
						<th>序列号</th>
						<th>彩站账号</th>
						<th>彩站名称</th>
						<th>所属省</th>
						<th>所属市</th>
						<th>所属地区</th>
						<th>彩站地址</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${list}" var="item" varStatus="status">
						<tr>
							<td width="100px"><input style="width: 20px" type="checkbox" id="shopTr${item.shopId}" 
								onclick="changeDiv(this,'${item.shopId}','${item.shopName}')"/>
							${status.count }
							</td>
							<td>${item.accountId}</td>
							<td>${item.shopName}</td>
							<td>${item.province}</td>
							<td>${item.city}</td>
							<td>${item.area}</td>
							<td>${item.shopAddress}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<div class="widget-bottom">
				<jsp:include page="../../include/pager.jsp" />
			</div>
		</div>
		<!-- /widget-content -->
	</div><c:forEach items="${list}" var="item">
	<tr>
		<td width="100px"><input style="width: 20px" type="checkbox" id="shopTr${item.shopId}" onclick="changeDiv(this,'${item.shopId}','${item.shopName}')"/></td>
		<td>${item.accountId}</td>
		<td>${item.shopName}</td>
		<td>${item.province}</td>
		<td>${item.city}</td>
		<td>${item.area}</td>
		<td>${item.shopAddress}</td>
	</tr>
</c:forEach> --%>
<script>
	//document.getElementById("cupage").innerHTML = '${pager.currentPage}';
	//document.getElementById("topage").innerHTML = '${pager.totalPage}';
	//document.getElementById("tosize").innerHTML = '${pager.totalSize}';
	$().ready(function () {
		addressInit('province', 'city', 'area', '${shopInfo.province}', '${shopInfo.city}', '${shopInfo.area}');
	});
</script>