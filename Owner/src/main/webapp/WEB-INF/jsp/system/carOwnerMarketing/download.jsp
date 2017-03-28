<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.hgsoft.carowner.action.*" %>
<%@ page import="com.hgsoft.common.utils.*" %>
<%@ page import="org.apache.struts2.ServletActionContext" %>
<%@ page import="java.io.*" %>
<html>
<head>
<title>ABOUT</title>
</head>
<body>
<%
    try{
    	
    	DownLoadUtil su = new DownLoadUtil();
        su.initialize(pageContext);
        su.setContentDisposition(null);
       	//String path = ServletActionContext.getServletContext().getRealPath("/")+"业务统计表.xls";
       	String path=request.getAttribute("filepath").toString();
       	String fileName=request.getAttribute("fileName").toString();
       	File f = new File(path);
        if (!f.exists()) {
%>
<%    
        } else {
            su.downloadFile(fileName);
        }
%>
 
<%
        out.clear();
        out=pageContext.pushBody();
    } catch (IOException e) {
        //System.out.println("用户取消下载!");
        //e.printStackTrace();
    } catch (Exception e) {
        //System.out.println("用户取消下载!");
        //e.printStackTrace();
    }
     
%>
</body>
</html>