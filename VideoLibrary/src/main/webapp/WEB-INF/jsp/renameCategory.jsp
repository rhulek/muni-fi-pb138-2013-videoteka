<%-- 
    Document   : renameCategory
    Created on : 21.6.2013, 20:54:37
    Author     : Martin
--%>


<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="header.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<t:pageTemplate>
    <jsp:attribute name="pageTitle">Přejmenování kategorie</jsp:attribute>
    <jsp:attribute name="theme">dark-theme</jsp:attribute>
    <jsp:attribute name="headingType">compact</jsp:attribute>
    <jsp:attribute name="headingMenuItemActive">catalog</jsp:attribute>
    
    <jsp:body>
        <form action="${pageContext.servletContext.contextPath}/category/rename/${oldCategoryName}" method="post">
            <input type="text" id="newName" name="newName" value="${oldCategoryName}"/>
            <input type="submit" value="odeslat"/>
        </form>
        <br/>
        <a href="${pageContext.servletContext.contextPath}/category/showAll"> Zpet na seznam kategorii </a>
    </jsp:body>
        
</t:pageTemplate>