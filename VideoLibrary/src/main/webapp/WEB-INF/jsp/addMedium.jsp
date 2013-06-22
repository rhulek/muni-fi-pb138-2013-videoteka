<%-- 
    Document   : addMedium
    Created on : 22.6.2013, 20:15:49
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
        <h1>Add medium</h1>
        <form:form action="" method="POST" modelAttribute="newMedium">
            <c:forEach items="${newMedium.movies}" var="mov" varStatus="status">
                Film: <form:input path="movies[${status.index}].name"/> <!--Tento pristup pouziva pevne dany list, da se udelat i dynamicky: AutoPopulatingList-->
            </c:forEach>
            <br/>
            Jmeno kategorie: <form:input path="category.name"/> <c:out value="${newMedium.category.name}"> je to null </c:out><br/>
            Typ media: <form:input path="type"/>
            
            <input type="submit" value="Pridat"/>
        </form:form>
        
        <a href="${pageContext.servletContext.contextPath}/category/showAll"> Zpet na seznam kategorii </a>
        <!-- Tady by se dalo udelat zpet na kategorii -->
        
    </body>
</html>
