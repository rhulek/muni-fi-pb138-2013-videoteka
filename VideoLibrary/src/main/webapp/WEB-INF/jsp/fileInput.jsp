<%-- 
    Document   : fileInput
    Created on : 24.6.2013, 13:11:58
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
        <h1>Import knihovny</h1>
        <form:form action="${baseURL}/file/import" method="POST" commandName="importBean" enctype="multipart/form-data">
            
            Zvolte prosim soubor: <input type="file" name="filePath"/><br/>

            <form:radiobuttons path="importOption" items="${importBean.importOptions}"/>

            <input type="submit" value="Importovat"/>
        </form:form>
            
        <a href="${baseURL}/category/showAll"> Zpet na seznam kategorii </a>
    </body>
</html>
