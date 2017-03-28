
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html xmlns="http://www.w3.org/1999/html">
<head>
<title>车主通系统平台登录</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href="${basePath}/css/bootstrap.min.css" />
<link rel="stylesheet" type="text/css" href="${basePath}/css/login.css" />
<link rel="stylesheet" type="text/css" href="${basePath}/css/style.css" />
<link id="css_main" rel="stylesheet" type="text/css" href="${basePath}/theme/${session.theme}/main.css" />
<style type="text/css">
</style>
<script src="${basePath}/js/jquery.min.js"></script>
<script type="text/javascript">
    window.onload = function ()
    {
        if(window.parent.length>0)
            window.parent.location=location;
    }
    var pwdInpu,nameInput;
    
    function check(){
    	pwdInput = $("#password");
        nameInput = $("#username");
    	if(nameInput.val() == ""){
            nameInput.focus();
            $(".warm-info").html("<font color='red'>请输入用户名！</font>");
        }
        else if(pwdInput.val() == ""){
            pwdInput.focus();
            $(".warm-info").html("<font color='red'>请输入密码！</font>");
        }
        else{
            $("#loginform").submit();
        }
    }
    
    $(function(){
        document.onkeydown = function(e){
            var ev = document.all ? window.event : e;
            if(ev.keyCode==13) {
            	check();
            }
        };
        
    });
</script>
</head>
<body class="loginpage">
	<div class="loginbox">            
        <form id="loginform" class="form-vertical" name="loginForm" action="login.do" method="post">
            <div class="formtitle">
            	<%-- <img src="${basePath}/images/logo.png" /> --%><font size="6">车主通系统平台管理系统</font>
            </div>
            <div class="control-group form-actions" style="padding-top:30px;">
                <div class="controls">
                    <div class="main_input_box">
                        <span class="add-on bg_lr"><i class="icon-user"></i></span><input type="text" placeholder="用户名" name="admin.username" value="" id="username" />
                    </div>
                </div>
            </div>
            <div class="control-group">
                <div class="controls">
                    <div class="main_input_box">
                        <span class="add-on bg_lg"><i class="icon-lock"></i></span><input type="password" placeholder="密码" name="admin.password" id="1" value=""/>
                    </div>
                </div>
                <div class="info-box" <c:if test="${not empty message}">style="display:block;"</c:if>>
                    <span class="warm-info">
                    	<c:choose>
							<c:when test="${not empty message}">
								<font color='red'>${message}</font>
							</c:when>
							<c:otherwise>
								<font color='red'>${SPRING_SECURITY_LAST_EXCEPTION.message}</font>
							</c:otherwise>
						</c:choose>
                    </span>
                </div>
            </div>
            <div class="form-bottom">
                <a href="javascript:void(0);" class="btn btn-primary" onclick="check()" />登&nbsp;&nbsp;&nbsp;&nbsp;录</a>
            </div>
        </form>
    </div>
    <div class="info-box">
        <div class="system-info">
        	车主通系统平台<br />
			版权所有：广州华工信息软件有限公司
        </div>
    </div>
</body>
</html>