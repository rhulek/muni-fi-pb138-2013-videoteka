<%-- 
    Document   : header
    Created on : 17.6.2013, 16:43:18
    Author     : Martin, hulek
    Doc        : View pro akci "Detail kategorie!"
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="header.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<t:pageTemplate>
    <jsp:attribute name="pageTitle">${category.name} - Detail kategorie</jsp:attribute>
    <jsp:attribute name="theme">dark-theme</jsp:attribute>
    <jsp:attribute name="headingType">compact</jsp:attribute>
    <jsp:attribute name="headingMenuItemActive">catalog</jsp:attribute>
    
    <jsp:body>
        <c:set var="catName" value="${category.name}" scope="page"/>
        <div class="item-container">
            <c:forEach items="${category.mediums}" var="med">
                <div class="item">
                    <h3><c:out value="${med.value.id}"></c:out></h3>
                    <ul>
                        <c:forEach items="${med.value.movies}" var="movie">
                            <li><c:out value="${movie.name}"/></li>
                        </c:forEach>
                    </ul>
                    <div class="action-btns">
                        <form action="${pageContext.servletContext.contextPath}/medium/delete" id="deleteBackingForm" method="POST">
                            <input type="hidden" name="mediumID" id="mediumID" value="ID media"/>
                            <input type="hidden" name="categoryName" id="categoryName" value="Nazev kategorie"/>
                            <input type="submit" value="Smazat"/>
                        </form>
                    </div>
                </div>
            </c:forEach>
            
            <a href="${pageContext.servletContext.contextPath}/medium/addMedium/${catName}?preselected=true">Přidat médium</a><br/><br/>
        </div>
    </jsp:body>
        
</t:pageTemplate>