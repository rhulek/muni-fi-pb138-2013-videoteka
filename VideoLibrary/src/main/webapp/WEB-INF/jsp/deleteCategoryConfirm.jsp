<%-- 
    Document   : deleteCategoryConfirm
    Created on : 21.6.2013, 22:46:57
    Author     : Martin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="header.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<t:pageTemplate>
    <jsp:attribute name="pageTitle">Smazání kategorie - varování!</jsp:attribute>
    <jsp:attribute name="theme">dark-theme</jsp:attribute>
    <jsp:attribute name="headingType">compact</jsp:attribute>
    <jsp:attribute name="headingMenuItemActive">catalog</jsp:attribute>
    
    <jsp:body>
        <p>Skutečně si přejete smazat kategorii <c:out value="${categoryName}"/>? </p>
        <p>Budou smazány i všechny položky kategorie. Tuto operaci nelze vrátit zpět!</p>
        
        <form action="${pageContext.servletContext.contextPath}/category/delete/${categoryName}" method="POST">
            <input type="hidden" value="delete" name="delete" id="delete"/>
            <input type="submit" value="Ano SMAZAT"/>
        </form>
    </jsp:body>
        
</t:pageTemplate>