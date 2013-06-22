<%@page contentType="text/html" pageEncoding="UTF-8" language="java"%>
<%@include file="header.jsp" %>

<!DOCTYPE html>
<html>
    <head>
        <title>JSP Page</title>
    </head>
    <body>
        <c:set var="catName" value="${category.name}" scope="page"/>
        <h1><c:out value="${catName}"/></h1>
        
        <table border="1">
            <c:forEach items="${category.mediums}" var="med">
            <tr>
                <td> <a href="${pageContext.servletContext.contextPath}/deleteMedium/${catName}/${med.value.id}"> Smazat </a> </td>
                <td>

                <c:out value="${med.value.id}">Tady by melo byt ID kterte je ale null</c:out></td>
                            
                <c:forEach items="${med.value.movies}" var="movie">
                    <td><c:out value="${movie.name}"/></td>
                </c:forEach>
            </tr>
            </c:forEach>
        </table>
        
        <a href="${pageContext.servletContext.contextPath}/medium/addMedium/${catName}"> Pridat medium </a><br/><br/>
        <a href="${pageContext.servletContext.contextPath}/category/showAll"> Zpet na seznam kategorii </a>
        
        
    </body>
</html>
