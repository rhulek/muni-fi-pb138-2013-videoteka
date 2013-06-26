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
    <jsp:attribute name="pageTitle">Přidat kategorii</jsp:attribute>
    <jsp:attribute name="theme">dark-theme</jsp:attribute>
    <jsp:attribute name="headingType">compact</jsp:attribute>
    <jsp:attribute name="headingMenuItemActive">catalog</jsp:attribute>
    <jsp:attribute name="scndMenuItemActive">categoryAdd</jsp:attribute>
    
    <jsp:body>
        <form action="${pageContext.servletContext.contextPath}/category/addCategory" method="post">
            <div class="form-row">
                <label for="categoryName">Název kategorie</label>
                <input type="text" id="categoryName" name="categoryName"/>
            </div>
            <div class="form-row submit">
                <input type="submit" value="Uložit"/>
            </div>
        </form>
    </jsp:body>
        
</t:pageTemplate>