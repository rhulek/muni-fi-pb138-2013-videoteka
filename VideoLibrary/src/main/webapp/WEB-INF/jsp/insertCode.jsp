<%-- 
    Document   : insertCode
    Created on : 17.6.2013, 21:01:44
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
        <title>Please insert authentication code.</title>
    </head>
    <body>
        <h1>Please insert authentication code: </h1>
        <form name="authCodeForm" action="autorize" method="POST">
            <input name="authCode" type="text"/>
            <input type="submit" value="Odeslat"/>
        </form>
        
    </body>
</html>
