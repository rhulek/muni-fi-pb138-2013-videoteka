<%-- 
    Document   : index
    Created on : 17.6.2013, 16:43:18
    Author     : Martin, hulek
    Doc        : View pro akci "Vítejte"
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="header.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<t:pageTemplate>
    <jsp:attribute name="pageTitle">Vítejte</jsp:attribute>
    <jsp:attribute name="theme">dark-theme</jsp:attribute>
    <jsp:attribute name="headingType">compact</jsp:attribute>
    
    <jsp:body>

        <h2><c:out value="${msg}"/> </h2>
        
        <%-- <a href="${baseURL}/category/showAll"> Zpět na seznam kategorií </a> --%>
    </jsp:body>
        
</t:pageTemplate>
