<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="header.jsp" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1><c:out value="${category.name}"/></h1>
        
        <table>
            <c:forEach items="${category.mediums}" var="medium">
            <tr>
                <td><c:out value="${category.id}"/></td>
                <c:forEach items="${madium.movies}" var="movie">
                    <td><c:out value="${movie.name}"/></td>
                </c:forEach>
            </tr>
            </c:forEach>
        </table>
    </body>
</html>
