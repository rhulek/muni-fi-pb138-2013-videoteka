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
    <jsp:attribute name="pageTitle">Add medium</jsp:attribute>
    <jsp:attribute name="theme">dark-theme</jsp:attribute>
    <jsp:attribute name="headingType">compact</jsp:attribute>
    <jsp:attribute name="headingMenuItemActive">catalog</jsp:attribute>
    <jsp:attribute name="scndMenuItemActive">mediumAdd</jsp:attribute>
    
    <jsp:body>
        Context path: <c:out value="${pageContext.servletContext.contextPath}"/><br/>
        <div>
            <form:form action="${pageContext.servletContext.contextPath}/medium/addMedium" method="POST" modelAttribute="newMedium">
            
                ID media: <form:input path="id"/><br/>

                <c:forEach items="${newMedium.movies}" var="mov" varStatus="status">
                    Film: <%-- <form:input path="movies[${status.index}].name"/> --%> <!--Tento pristup pouziva pevne dany list, da se udelat i dynamicky: AutoPopulatingList-->
                    <input name="movies[${status.index}].name"/>
                </c:forEach>
                <br/>

                <%-- Jmeno kategorie: <form:input path="category.name"/> <c:out value="${newMedium.category.name}"> je to null </c:out><br/> --%>
                Typ media: <form:input path="type"/><br/>
                <br/>

                Predvolena kategorie (toto prijde smazat - pouze trace): <c:out value="${newMedium.category.name}"> je to null </c:out><br/>
                <form:select path="category" >
                    <form:option value="${selectedCategory}" label="${selectedCategory}"/>
                    <form:options items="${categories}" />
                </form:select>

                <input type="submit" value="Pridat"/>
            </form:form>
        </div>
    </jsp:body>
        
</t:pageTemplate>
