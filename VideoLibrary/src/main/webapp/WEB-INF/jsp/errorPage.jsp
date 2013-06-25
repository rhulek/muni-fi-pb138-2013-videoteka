<%-- 
    Document   : errorPage
    Created on : 17.6.2013, 16:43:18
    Author     : Martin
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
        <a href="<c:url value="${msg}"/>"> odkaz </a><br/>
        <a href="file://localhost/D:/Fotky/bonsai_by_johnbruk03_normalni_verze.JPG"> vod </a>
    </jsp:body>
        
</t:pageTemplate>
