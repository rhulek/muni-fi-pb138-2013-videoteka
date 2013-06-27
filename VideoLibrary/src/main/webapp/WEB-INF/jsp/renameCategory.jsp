<%-- 
    Document   : renameCategory
    Created on : 21.6.2013, 20:54:37
    Author     : Martin, hulek
    Doc        : View pro akci "Přejmenování kategorie"
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="header.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<t:pageTemplate>
    <jsp:attribute name="pageTitle">Přejmenování kategorie</jsp:attribute>
    <jsp:attribute name="theme">dark-theme</jsp:attribute>
    <jsp:attribute name="headingType">compact</jsp:attribute>
    <jsp:attribute name="headingMenuItemActive">catalog</jsp:attribute>
    
    <jsp:body>
        <form action="${pageContext.servletContext.contextPath}/category/rename/${oldCategoryName}" method="post">
            <div class="form-row">
                <label for="newName">Nový název</label>
                <input type="text" id="newName" name="newName" value="${oldCategoryName}"/>
            </div>
            <div class="form-row submit">
                <input type="submit" value="Uložit"/>
            </div>
        </form>
    </jsp:body>
        
</t:pageTemplate>