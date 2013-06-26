<%-- 
    Document   : addMedium
    Created on : 22.6.2013, 20:15:49
    Author     : Martin
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="header.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<t:pageTemplate>
    <jsp:attribute name="pageTitle">Přidat médium</jsp:attribute>
    <jsp:attribute name="theme">dark-theme</jsp:attribute>
    <jsp:attribute name="headingType">compact</jsp:attribute>
    <jsp:attribute name="headingMenuItemActive">catalog</jsp:attribute>
    <jsp:attribute name="scndMenuItemActive">mediumAdd</jsp:attribute>
    
    <jsp:body>
        <div>
            <form:form action="${pageContext.servletContext.contextPath}/medium/addMedium" method="POST" modelAttribute="newMedium">
            
                <div class="form-row">
                    <label for="id">ID média</label>
                    <form:input path="id" />
                </div>
                
                <c:forEach items="${newMedium.movies}" var="mov" varStatus="status">
                    <div class="form-row">
                        <label for="movies[${status.index}].name">Film</label>
                        <input name="movies[${status.index}].name"/>
                    </div>
                </c:forEach>
                
                <div class="form-row">
                    <label for="type">Typ média</label>
                    <form:input path="type"/>
                </div>
                
                <div class="form-row">
                    <label for="category">Kategorie</label>
                    <form:select path="category" >
                        <form:option value="${selectedCategory}" label="${selectedCategory}"/>
                        <form:options items="${categories}" />
                    </form:select>
                </div>

                <div class="form-row submit">
                    <input type="submit" value="Uložit"/>
                </div>
                
            </form:form>
        </div>
    </jsp:body>
        
</t:pageTemplate>
