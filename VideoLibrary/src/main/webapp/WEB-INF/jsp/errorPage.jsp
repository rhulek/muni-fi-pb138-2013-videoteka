<%-- 
    Document   : errorPage
    Created on : 17.6.2013, 16:43:18
    Author     : Martin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Jejda! V aplikaci došlo k cyhbě!</h1>
        
        <p> <c:out value="${msg}"/> </p>
        <a href="<c:url value="${msg}"/>"> odkaz </a><br/>
        <a href="file://localhost/D:/Fotky/bonsai_by_johnbruk03_normalni_verze.JPG"> vod </a>
        
    </body>
</html>
