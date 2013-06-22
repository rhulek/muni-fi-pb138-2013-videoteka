<%-- 
    Document   : deleteCategoryConfirm
    Created on : 21.6.2013, 22:46:57
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
        <h1>Smazání kategorie - varování!</h1>
        <p>Skutečně si přejete smazat kategorii <c:out value="${categoryName}"/>? </p>
        <p>Budou smazány i všechny položky kategorie. Tuto operaci nelze vrátit zpět!</p>
        
        <form action="${pageContext.servletContext.contextPath}/category/delete/${categoryName}" method="POST">
            <input type="hidden" value="delete" name="delete" id="delete"/>
            <input type="submit" value="Ano SMAZAT"/>
            <a href="${pageContext.servletContext.contextPath}/category/showAll"> <input type="button" value="Probůh to ne! Rychle pryč!"/> </a>
        </form>
        
        
    </body>
</html>
