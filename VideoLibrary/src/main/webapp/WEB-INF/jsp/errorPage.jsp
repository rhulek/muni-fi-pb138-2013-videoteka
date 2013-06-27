<%-- 
    Document   : errorPage
    Created on : 17.6.2013, 16:43:18
    Author     : Martin
    Doc        : View pro akci "Chybová hláška"
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="header.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<t:pageTemplate>
    <jsp:attribute name="pageTitle">Jejda! V aplikaci došlo k cyhbě!</jsp:attribute>
    <jsp:attribute name="theme">dark-theme</jsp:attribute>
    <jsp:attribute name="headingType">compact</jsp:attribute>
    
    <jsp:body>
        <p> <c:out value="${msg}"/> </p>
    </jsp:body>
        
</t:pageTemplate>
