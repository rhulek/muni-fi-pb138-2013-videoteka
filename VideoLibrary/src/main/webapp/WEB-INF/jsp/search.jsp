<%-- 
    Document   : search
    Created on : 23.6.2013, 14:23:10
    Author     : Martin
--%>

<%-- 
    Document   : renameCategory
    Created on : 21.6.2013, 20:54:37
    Author     : Martin
--%>


<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="header.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<t:pageTemplate>
    <jsp:attribute name="pageTitle">Hledání!</jsp:attribute>
    <jsp:attribute name="theme">dark-theme</jsp:attribute>
    <jsp:attribute name="headingType">compact</jsp:attribute>
    <jsp:attribute name="headingMenuItemActive">catalog</jsp:attribute>
    <jsp:attribute name="scndMenuItemActive">search</jsp:attribute>
    
    <jsp:body>
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
    </jsp:body>
        
</t:pageTemplate>