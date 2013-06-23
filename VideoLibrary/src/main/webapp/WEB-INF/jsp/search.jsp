<%-- 
    Document   : search
    Created on : 23.6.2013, 14:23:10
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
        <h1>Hledání!</h1>
        <h2><c:out value="${msg}"/></h2>
        <form:form action="${pageContext.servletContext.contextPath}/search" modelAttribute="searchBean" method="POST">
        Zadejte název filmu:
        <input type="text" name="movieName"/>
        <input type="submit" value="Hledat">
        <br/>
        
        Hledat kategorii:
            <form:select path="categoryName">
                <form:option value="" label="-- Hledat ve všech --"/>
                <form:options items="${categoriesList}" />
            </form:select>
        </form:form>
        
        <table border="1">
            <c:forEach items="${foundMediums}" var="medium">
                <tr>
                    <td><c:out value="${medium.id}"/></td>
                    <td><a href="${pageContext.servletContext.contextPath}/category/${medium.category.name}"><c:out value="${medium.category.name}"/></a></td>

                    <c:forEach items="${medium.movies}" var="movie">
                        <td><c:out value="${movie.name}"/></td>
                    </c:forEach>
                </tr>
            </c:forEach>
        </table>
        <br/>
        <a href="${pageContext.servletContext.contextPath}/category/showAll"> Zpet na seznam kategorii </a>
    </body>
</html>
