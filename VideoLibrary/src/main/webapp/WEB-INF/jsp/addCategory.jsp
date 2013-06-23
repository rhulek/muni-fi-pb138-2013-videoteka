<%-- 
    Document   : renameCategory
    Created on : 21.6.2013, 20:54:37
    Author     : Martin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="header.jsp" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Category renaming</h1>
        <form action="${pageContext.servletContext.contextPath}/category/addCategory" method="post">
            <input type="text" id="categoryName" name="categoryName"/>
            <input type="submit" value="odeslat"/>
        </form>
        <br/>
        <a href="${pageContext.servletContext.contextPath}/category/showAll"> Zpet na seznam kategorii </a>
    </body>
</html>