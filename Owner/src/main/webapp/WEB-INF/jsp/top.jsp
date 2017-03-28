<%@ page contentType="text/html; charset=UTF-8" language="java"
	pageEncoding="UTF-8" isErrorPage="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<style>
.newClass{
    background: none repeat scroll 0% 0% white;
    color: white;
    transition: all 0.15s ease-in-out 0s;
    box-shadow: 1px 0px 1px #2A2A2A inset, -1px 0px 1px #2A2A2A inset;
    text-color:red;
}
</style>
<div id="newMessageDIV" style="display:none"></div>
<input type="hidden" id="count"/>
<input type="hidden" id="hasNew"/>
<input type="hidden" id="newClass"/>
<div class="navbar-header">
	<a href="#"><img src="${basePath}/images/index_02.png" height="50px" /></a>
</div>
<div class="navbar-collapse">
	<ul class="nav navbar-nav navbar-right">
		<li><a href="sys_index.do" style="color: white;">
		<i class="icon-home"><img src="${basePath}/images/index_07.png"/></i>&nbsp;&nbsp;返回首页
		</a></li>
		<li><a href="#" style="color: white;"><c:if test="${not empty session.operator}">
				<i class="icon-user"><img src="${basePath}/images/index_05.png"/></i>&nbsp;&nbsp;员工姓名${request.shopManageRole }：【${session.operator.name}】
			</c:if></a></li>
		<%-- 
		<li><a href="#" class="dropdown-toggle theme-toggle" onclick='javascript:$(".theme-menu").toggle();'><i class="icon-adjust"></i>&nbsp;&nbsp;更换主题</a>
        <ul class="dropdown-menu theme-menu">
            <li>
              <div class="panel-group" id="accordion">
                <div class="panel panel-default">
                  <div class="panel-heading">
                    <h4 class="panel-title"> <a data-toggle="collapse" data-toggle="collapse" data-parent="#accordion" href="#collapseOne"> 选择主题 </a> </h4>
                  </div>
                  <div id="collapseOne" class="panel-collapse collapse in">
                    <div class="panel-body">
                      <select class="set-theme" onchange='setTheme(this.value)'>
                        <option value="amsterdam" <c:if test="${session.theme=='amsterdam'}">selected="selected"</c:if>>Amsterdam</option>
                        <option value="dandelion" <c:if test="${session.theme=='dandelion'}">selected="selected"</c:if>>Dandelion</option>
                        <option value="pinsupreme" <c:if test="${session.theme=='pinsupreme'}">selected="selected"</c:if>>Pinsupreme</option>
                      </select>
                    </div>
                  </div>
                </div>
              </div>
            </li>
            <!-- <li><a href="javascript:void(0);" class="save-theme" onclick="saveTheme()">保存</a></li>
            <li><a href="#" class="reset-theme">还原</a></li> -->
          </ul>
        </li> --%>
		<li><a href="#" data-toggle="dropdown" data-target="#" style="color: white;"
			id="dLabel"><i class="icon-signout"><img src="${basePath}/images/index_09.png"/></i>&nbsp;&nbsp;退出系统</a>
			<ul class="dropdown-menu text-center" role="menu"
				aria-labelledby="dLabel">
				<li>确认退出吗?</li>
				<li>
					<button class="btn">返 回</button>
					<button onclick="javascript:window.location.href='admin_logout.do';" class="btn btn-primary">退 出</button>
				</li>
			</ul></li>
	</ul>
</div>