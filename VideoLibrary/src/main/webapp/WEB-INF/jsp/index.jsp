<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="header.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<t:pageTemplate>
    <jsp:attribute name="pageTitle">Přehled kategorií</jsp:attribute>
    <jsp:attribute name="theme">dark-theme</jsp:attribute>
    <jsp:attribute name="headingType">compact</jsp:attribute>
    <jsp:attribute name="headingMenuItemActive">catalog</jsp:attribute>
    <jsp:attribute name="scndMenuItemActive">categoryShowAll</jsp:attribute>
    
    <jsp:body>
        
        <div class="item-container">
            <c:forEach items="${categoriesList}" var="item">
                <div class="item category">
                    <h3><a href="${pageContext.servletContext.contextPath}/category/${item}"><c:out value="${item}" /></a></h3>
                    <div class="action-btns">
                        <span class="action-btn delete"><a href="${pageContext.servletContext.contextPath}/category/delete/${item}">Smazat</a></span>
                        <span class="action-btn rename"><a href="${pageContext.servletContext.contextPath}/category/showRenameForm/${item}">Přejmenovat</a></span>
                    </div>
                </div>
            </c:forEach>
    </jsp:body>
        
</t:pageTemplate>