<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="header.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<t:pageTemplate>
    <jsp:attribute name="pageTitle">Vítejte</jsp:attribute>
    <jsp:attribute name="theme">dark-theme</jsp:attribute>
    <jsp:attribute name="headingType">compact</jsp:attribute>
    
    <jsp:body>
        <p>Pokračujte výběrem konkrétní akce.</p>
        <h2> h2 nadpis: <c:out value="${msg}"/> </h2>
        file://d:/IMG_0011.JPG
        <a href="${msg}">${msg}</a>
    </jsp:body>
        
</t:pageTemplate>
