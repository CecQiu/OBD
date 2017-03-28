<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<script type="text/javascript" language="javascript">
function getLeftMenu(){
	jQuery.get("sys_leftJson.do?time="+$.now(),function(data){
		if (data == null || data.length > 1000) {
				window.location.href = "admin_logout.do";
			}
			$("#nav").html("");
			if(null!=data&&data.length!=0&&data.length<1000){
				var tableStr="";
				for(var i = 0;i<data.length;i++){
					var item = data[i];
					if(item.level == 1){
						var childMenu = getLeftChildMenu(item.id,data);
						if(childMenu){
							tableStr += '<li><a class="root-menu" href="#">'+ item.name + '<i class="arrow icon-angle-left"></i></a>' + childMenu;
						}
						else{
							tableStr += '<li><a class="root-menu" href="' + item.url + '" target="rightFrame" ><i class="'+ item.image +'"></i>' + item.name + '</a>';
						}
						tableStr += '</li>';
					}
				}
				$("#nav").html(tableStr);
			}
	});
}
function getLeftChildMenu(id,data){
	var tableStr = "";
	for(var i = 0;i<data.length;i++){
		var item = data[i];
		if(data[i].parent!=null&&data[i].parent.id==id){
            var childHtml = getThridChildMenu(item.id,data);
            if(childHtml){
				tableStr += '<li style="padding:0px;"><a class="second-menu" href="#">' + item.name + '<i class="arrow icon-angle-left"></i></a>' + childHtml;
			}
			else{
				tableStr += '<li style="padding:0px;"><a class="second-menu" target="rightFrame" href="${basePath}'+item.url+'?mid='+item.id+'">'+item.name+'</a></li>';
			}
			//tableStr +='<li><a target="rightFrame" href="${basePath}'+item.url+'?mid='+item.id+'">'+item.name+'</a>'+childHtml+'</li>';
		}
	}
	if(tableStr!=""&&tableStr.length>0){
		tableStr = '<ul class="sub-menu" >' + tableStr + '</ul>';
	}
	return tableStr;
}

function getThridChildMenu(id,data){
    var tableStr = "";
    for(var i = 0;i<data.length;i++){
        var item = data[i];
        if(data[i].parent!=null&&data[i].parent.id==id){
            var childHtml = getThridChildMenu(item.id,data);

            tableStr +='<li style="padding-right: 8px;padding-bottom: 5px;"><a style="text-decoration: none;color:#848484;height: 23px;padding: 5px;padding-top: 0px;background-image:url(\'../images/bg_03.jpg\');background-size:cover;" target="rightFrame" href="${basePath}'+item.url+'?mid='+item.id+'">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'+item.name+'</a>'+childHtml+'</li>';
        }
    }
    if(tableStr!=""&&tableStr.length>0){
        tableStr = '<ul class="thrid-sub-menu" style="display:none">' + tableStr + '</ul>';
    }
    return tableStr;
}
function showThridChildMenu(obj){
	//obj.
}
$(function(){
	getLeftMenu();
	
	$("#nav a.root-menu").live("click",function()
	{
		var submenu = $(this).siblings('ul');
		var thridsubmenu = $('#nav>li>ul>li>ul.thrid-sub-menu');
		var li = $(this).parents('li');
		var chevron = $(this).find(".arrow");
		var submenus = $('#nav>li>ul.sub-menu');
		thridsubmenu.slideUp();
		submenus.slideUp();
		$('#nav>li>ul>li>ul li a').css("color","#848484").css("background-image","url(../images/bg_03.jpg)");
		$("#nav .thrid-sub-menu a.active").removeClass("active");
		if(li.hasClass('open'))
		{
			li.removeClass('open').removeClass('active');
			chevron.attr("class","arrow icon-angle-left");
		} else 
		{
			$(".arrow").attr("class","arrow icon-angle-left");
			submenu.slideDown();
			$('#nav li.open').removeClass('open');
			$("#nav li.active").removeClass('active');
			li.addClass('active').addClass('open');
			chevron.attr("class","arrow icon-angle-down");
		}
	});
	
	$("#nav a.second-menu").live("click",function()
			{
				var submenu = $(this).siblings('ul');
				var li = $(this).parents('.sub-menu li');
				var chevron = $(this).find(".arrow");
				var submenus = $('#nav>li>ul>li>ul.thrid-sub-menu');
				submenus.slideUp();
				$('#nav>li>ul>li>ul li a').css("color","#848484").css("background-image","url(../images/bg_03.jpg)");
				$("#nav .thrid-sub-menu a.active").removeClass("active");
				if(li.hasClass('open'))
				{
					/* li.removeClass('open').removeClass('active');
					chevron.attr("class","arrow icon-angle-left"); */
				} else 
				{
					$(".arrow").attr("class","arrow icon-angle-left");
					submenu.slideDown();
					$('#nav li>ul>li.open').removeClass('open');
					$("#nav li>ul>li.active").removeClass('active');
					li.addClass('active').addClass('open');
					chevron.attr("class","arrow icon-angle-down");
				}
			});
	
	$("#nav .thrid-sub-menu a").live("click",function(){
		var submenus = $('#nav>li>ul>li>ul li a');
		$("#nav .thrid-sub-menu a.active").removeClass("active");
		$(this).addClass("active");
		submenus.css("color","#848484").css("background-image","url(../images/bg_03.jpg)");
		$(this).css("color","#90daff").css("background-image","url(../images/bg_07.jpg)");
		/* onmouseover="this.style.backgroundImage=\'url(../images/bg_07.jpg)\';this.style.color=\'#90daff\'" onmouseout="this.style.backgroundImage=\'url(../images/bg_03.jpg)\';this.style.color=\'#848484\'" */
	});
	
	var width = $(window).width() - $("#page-sidebar").width() - 30;
	$("#c_right").css("width",width+"px");
	
	$("#rightFrame").load(function(){ 
		try{
            var height = $(this).contents().find("body").height();
            if(!height) height = 540;
			$(this).height(height+30);
		}
		catch(e)
		{

		}
    }); 
});

</script>
<div id="side-nav">
	<ul id="nav">
	</ul>
	<!-- <ul><li>fasdfasd</li></ul>
	<br> -->
</div>
