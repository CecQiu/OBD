<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script language="javascript">
function gotoPage(pageNo) {
	if(pageNo>0)
		get("pager.currentPage").value = pageNo;
	try{
		document.myForm.submit();
	}
	catch(e)
	{
		if(parent.document)
		{
			if(pageNo>0)
				parent.document.getElementById("parent.currentPage").value=	pageNo;	
			else
				parent.document.getElementById("parent.currentPage").value = document.getElementById("child.currentPage").value;
			parent.document.myForm.submit();
		}
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
	
<div class="fb-foot">
	<div class="pagination alternate f_r">
		
			 <ul class="f_l">
				<li style="float:left;">
					<a <c:if test="${pager.hasFirst!=false}">href="javascript:gotoPage(1);"</c:if>><img src="${basePath}/images/first.png" /></a>
				</li>
				<li style="float:left;">
					<a <c:if test="${pager.hasPrevious!=false}">href="javascript:gotoPage(${pager.currentPage-1});"</c:if>><img src="${basePath}/images/prev.png" /></a>
				</li>
				 <li style="float:left;">
					<a <c:if test="${pager.hasNext!=false}">href="javascript:gotoPage(${pager.currentPage+1});"</c:if>><img src="${basePath}/images/next.png" /></a>
				 </li>
				  <li style="float:left;">
					<a <c:if test="${pager.hasLast!=false}">href="javascript:gotoPage(${pager.totalPage});"</c:if>><img src="${basePath}/images/last.png" /></a>
				</li>
			</ul>
			 <div class="toPage f_l" >
				
				<span>第&nbsp;${pager.currentPage}&nbsp;页&nbsp;(共&nbsp;${pager.totalSize}&nbsp;条，&nbsp;${pager.totalPage}&nbsp;页)，跳转到第
					<input type="text" id="child.currentPage" name="pager.currentPage" onKeypress="if (event.keyCode < 48 || event.keyCode > 57) event.returnValue = false;" size="2" value="${pager.currentPage}" onblur="changeBiggest()" onfocus="this.select();" class="page"/> 页
				</span>
				
				<a href="javascript:gotoPage(-1);"><img src="${basePath }/images/jump.png"/></a>
			</div>
	</div>
</div> 