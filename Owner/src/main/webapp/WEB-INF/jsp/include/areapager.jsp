<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script language="javascript">
function gotoPage(pageNo) {
	if(pageNo>0)
		$("[name='pager.currentPage']").attr("value",pageNo);
	else
		pageNo = $("#currentPage").val();
	try{
		searchShop(pageNo);
	}
	catch(e)
	{
			if(pageNo > 0){
				$("#currentPage",window.parent.document).val(pageNo);
			}
			var $myForm = $("#myForm",window.parent.document);
			searchShop(pageNo);
	}
}

function changeBiggest(){
	var biggest = ${pager.totalPage};
	var jumpInput = $("[name='pager.currentPage']").val();
    var jumpInputNum = parseInt(jumpInput,10);
	if(isNaN(jumpInputNum)||jumpInputNum>biggest){
		$("[name='pager.currentPage']").val(1);
	};
}
</script>
<div class="paginations pull-right">
	<ul class="pagination pagination-sm pull-left">
		<li><a href="javascript:void(0);" <c:if test="${pager.hasFirst==false}">disabled</c:if> <c:if test="${pager.hasFirst!=false}">onclick="gotoPage(1)"</c:if>>首页</a> </li>
		<li><a href="javascript:void(0);" <c:if test="${pager.hasPrevious==false}">disabled</c:if> <c:if test="${pager.hasPrevious!=false}">onclick="gotoPage(${pager.currentPage-1})"</c:if>>上一页</a> </li>
		<li><a href="javascript:void(0);" <c:if test="${pager.hasNext==false}">disabled</c:if> <c:if test='${pager.hasNext!=false}'>onclick="gotoPage(${pager.currentPage+1})"</c:if>>下一页</a> </li>
		<li><a href="javascript:void(0);" <c:if test="${pager.hasLast==false}">disabled</c:if> <c:if test="${pager.hasLast!=false}">onclick="gotoPage(${pager.totalPage})"</c:if>>末页</a> </li>
	</ul>
	<div class="toPage pull-left"> 
		&nbsp;&nbsp;第&nbsp;${pager.currentPage}&nbsp;页&nbsp;(共&nbsp;${pager.totalSize}&nbsp;条，&nbsp;${pager.totalPage}&nbsp;页)&nbsp;&nbsp;
	                    ，跳转到第
	     <input type="text" id="child.currentPage" name="pager.currentPage" onKeypress="if (event.keyCode < 48 || event.keyCode > 57) event.returnValue = false;" size="2" value="${pager.currentPage}" onblur="changeBiggest()" onfocus="this.select();"/>
	                    页&nbsp;&nbsp;
	      <a class="btn btn-default btn-sm" href="javascript:gotoPage(-1);" style="border-color:#ADADAD">跳转</a>&nbsp;&nbsp;&nbsp;
	      <input type="hidden" name="pager.rowIndex" size="2" value="${pager.rowIndex}"/>
	</div>
</div>

