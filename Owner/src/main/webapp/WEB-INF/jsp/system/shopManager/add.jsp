<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html>
<head>
<title>add</title>
<jsp:include page="../../include/common.jsp" />
<script type="text/javascript" src="${basePath}/js/jsAddress.js"></script>
<script language="javascript">
	$().ready(function () {
		
		addressInit('province', 'city', 'area', '${shopInfo.province}', '${shopInfo.city}', '${shopInfo.area}');
		
		<c:forEach items="${shopInfos}" var="item">
			addShop('${item.shopId}','${item.shopName}');
		</c:forEach>

		$("#myForm").validate();
	});
	
	function addShop(id,name){
		if($("#selectUser").val()==""){
			alert("请先选择用户");
			var inp = document.getElementById("shopTr"+id);
	        if(inp != "undefined" && inp != ""){
	    		inp.checked = false;
	    	}
			return false;
		}
		var val = document.getElementById("selectUser");
		var role =getNameRole(val.options[val.selectedIndex].title,0);
		if(role == 3){
			$("#shops").find("div").each(function(index,ele){
				//alert(ele.id);
				removeShop(ele.id);
			});
		}
    	var inHtml = ' <div class="btn-group" id="'+id+'"><button type="button" '
    			+ 'class="btn btn-default" onclick="removeShop('+ id +')">'
       			+ name
        		+ '<span class="trash"></span></button></div>';
        $("#shops").append(inHtml);
        $("#shopIds").val($("#shopIds").val() + id + ";");
        var inp = document.getElementById("shopTr"+id);
        if(inp != "undefined" && inp != ""){
    		inp.checked = true;
    	}
    }
    
    function removeShop(id){
    	$("#"+id).remove();
    	var shopids = $("#shopIds").val();
    	var shops = shopids.split(";");
    	shopids = "";
    	for(var i=0;i<shops.length;i++){
    		var shop = shops[i];
    		if(shop!=id && shop!=""){
    			shopids = shop + ";";
    		}
    	}
    	$("#shopIds").val(shopids);
    	var inp = document.getElementById("shopTr"+id);
    	if(inp != "undefined" && inp != "" && inp != null){
    		inp.checked = false;
    	}
    }
    
    function changeDiv(ckb,id,name){
    	if(ckb.checked){
    		addShop(id, name);
    	} else {
    		removeShop(id);
    	}
    }
    
    
    function searchShop(currentPage){
    	var pr = document.getElementById("province").value;
    	var city = document.getElementById("city").value;
    	var area = document.getElementById("area").value;
    	if(pr == "" || city == '' || area=="") {
    		return;
    	}
    	if(currentPage == "" || currentPage == undefined) {
    		currentPage = 1;
    	}
    	$("#shopContent").html('');
    	$("#shopContent").load("adminForShop_getShop.do", {'shopInfo.province': pr,
			'shopInfo.city':city,'shopInfo.area':area,
			'pager.currentPage':currentPage}, function(){setIFrameHeight();});
    }
	function setIFrameHeight() {
		//var mainheight = $("#myForm").height();
		var mainheight = document.body.scrollHeight;
		if (mainheight < 580) {
			mainheight = 580;
		}
		parent.document.all("rightFrame").style.height = mainheight + "px";
	}
    
    function setRealName(val){
    	$("#adminName").val(getNameRole(val.options[val.selectedIndex].title,1));
    }
    
    function setRole(val){
    	role = val;
    }
    
    function changeBiggest(){
    	var biggest = document.getElementById("topage").innerHTML;
    	var jumpInput = $("[name='pager.currentPage']").val();
        var jumpInputNum = parseInt(jumpInput,10);
    	if(isNaN(jumpInputNum)||jumpInputNum>biggest){
    		$("[name='pager.currentPage']").val(1);
    	};
    }
    
    function getNameRole(val, type){
    	var arr = val.split(",");
    	if(type == 1){
    		return arr[0];
    	} else {
    		return arr[1];
    	}
    }
</script>
</head>
<body>
	<form id="myForm" style="min-height: 580px;" name="myForm" method="post" action="adminForShop_save.do">
		<ul class="breadcrumb">
			<li><i class="icon-home icon-2x"></i></li>
			<li>当前位置：${currentPosition}&nbsp;>&nbsp;添加</li>
		</ul>
		<div class="widget widget-table">
			<div class="widget-content">
				<table class="pn-ftable table-bordered">
					<tbody>
						<tr>
							<th>选择管理用户:</th>
							<td class="pn-fcontent">
								<select name="admin.id" id="selectUser" style="width: 200px;" onchange="setRealName(this)">
									<option value="" title="">-选择管理用户-</option>
									<c:forEach items="${admins}" var="item">
										<option value="${item.id}" title="${item.name}<c:forEach items="${item.roles}" var="role">,${role.id}</c:forEach>">
												${item.username}
										</option>
									</c:forEach>
								</select>
							</td>
							<th>真实姓名:</th>
							<td class="pn-fcontent">
								<input id="adminName" name="admin.name" readonly="readonly" value="${admin.name}" style="width: 200px;"
									maxlength="20" type="text" />
							</td>
						</tr>
						<tr>
							<th>管理彩点:</th>
							<td class="pn-fcontent" colspan="3">
								<input type="hidden" name="shopids" value="" id="shopIds"/>
								<div style="margin:5px;width: 100%;height:100px;border:thin;border-color: black;" id="shops">
								</div>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			<!-- /widget-content -->
		</div>
		<div class="widget widget-edit">
			<div class="widget-content" id="shopContent">
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
							<th></th>
							<th>彩站账号</th>
							<th>彩站名称</th>
							<th>所属省</th>
							<th>所属市</th>
							<th>所属地区</th>
							<th>彩站地址</th>
						</tr>
					</thead>
					<tbody id="tbody">
						<c:forEach items="${list}" var="item" varStatus="status">
							<tr>
								<td><input style="width: 20px" type="checkbox" id="shopTr${item.shopId}" onclick="changeDiv(this,'${item.shopId}','${item.shopName}')"/>选择</td>
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
					<div class="paginations pull-right">
						<ul class="pagination pagination-sm pull-left">
							<li><a href="javascript:void(0);" <c:if test="${pager.hasFirst==false}">disabled</c:if>" <c:if test="${pager.hasFirst!=false}">onclick="searchShop(1)"</c:if>>首页</a> </li>
							<li><a href="javascript:void(0);" <c:if test="${pager.hasPrevious==false}">disabled</c:if>" <c:if test="${pager.hasPrevious!=false}">onclick="searchShop(${pager.currentPage-1})"</c:if>>上一页</a> </li>
							<li><a href="javascript:void(0);" <c:if test="${pager.hasNext==false}">disabled</c:if>" <c:if test='${pager.hasNext!=false}'>onclick="searchShop(${pager.currentPage+1})"</c:if>>下一页</a> </li>
							<li><a href="javascript:void(0);" <c:if test="${pager.hasLast==false}">disabled</c:if>" <c:if test="${pager.hasLast!=false}">onclick="searchShop(${pager.totalPage})"</c:if>>末页</a> </li>
						</ul>
						<div class="toPage pull-left"> 
							&emsp;第<span id="cupage">${pager.currentPage}</span>页&emsp;(共<span id="tosize">${pager.totalSize}</span>条,共<span id="topage">${pager.totalPage}</span>页)&emsp;
						                    ，跳到
						     <input type="text" id="child.currentPage" name="pager.currentPage" onKeypress="if (event.keyCode < 48 || event.keyCode > 57) event.returnValue = false;" size="2" value="${pager.currentPage}" onblur="changeBiggest()" onfocus="this.select();"/>
						                    页&emsp;
						      <a class="btn btn-default btn-sm" href="javascript:searchShop(-1);">跳转</a>
						</div>
					</div>
				</div>
				<div class="widget-bottom">
					<center>
						<a onclick="$(this).parents('form').submit();" href="javascript:;" class="btn btn-primary">保 存</a>
						<a href="javascript:window.location.href='adminForShop_list.do';" class="btn btn-danger">返 回</a>
					</center>
				</div>
			</div>
		</div>
	</form>
</body>
</html>