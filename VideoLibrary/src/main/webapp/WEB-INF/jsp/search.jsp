<%-- 
    Document   : search
    Created on : 23.6.2013, 14:23:10
    Author     : Martin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="header.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<t:pageTemplate>
    <jsp:attribute name="pageTitle">Vyhledat film</jsp:attribute>
    <jsp:attribute name="theme">dark-theme</jsp:attribute>
    <jsp:attribute name="headingType">compact</jsp:attribute>
    <jsp:attribute name="headingMenuItemActive">catalog</jsp:attribute>
    <jsp:attribute name="scndMenuItemActive">search</jsp:attribute>
    
    <jsp:body>
        <h2><c:out value="${msg}"/></h2>
        <form:form action="${pageContext.servletContext.contextPath}/search" modelAttribute="searchBean" method="POST">
            <div class="form-row">
                <label for="movieName">Zadejte název filmu</label>
                <input type="text" name="movieName"/>
            </div>
            
            <div class="form-row">
                <label for="categoryName">Hledat v kategorii</label>
                <form:select path="categoryName">
                <form:option value="" label="-- Hledat ve všech --"/>
                <form:options items="${categoriesList}" />
            </form:select>
            </div>
            
            <div class="form-row submit">
                <input type="submit" value="Hledat">
            </div>
            
        </form:form>
       
        <div class="item-container">
        
            
            <c:forEach items="${foundMediums}" var="medium">
                
                    <h3><c:out value="${medium.id}"/></h3>
                    <p>Kategorie: <a href="${pageContext.servletContext.contextPath}/category/${medium.category.name}"><c:out value="${medium.category.name}"/></a></p>

                    <ul>
                    <c:forEach items="${medium.movies}" var="movie">
                        <li><c:out value="${movie.name}"/></li>
                    </c:forEach>
                    </ul>
            </c:forEach>
            
        </div>
        
    </jsp:body>
        
</t:pageTemplate>