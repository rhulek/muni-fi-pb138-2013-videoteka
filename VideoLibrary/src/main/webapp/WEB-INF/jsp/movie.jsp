<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="header.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<t:pageTemplate>
    <jsp:attribute name="pageTitle">${movieName} - Detail filmu</jsp:attribute>
    <jsp:attribute name="theme">dark-theme</jsp:attribute>
    <jsp:attribute name="headingType">compact</jsp:attribute>
    <jsp:attribute name="headingMenuItemActive">catalog</jsp:attribute>
    
    <jsp:body>
        <div class="item-container">
            Název: ${movieName}<br/> 
            Kategorie: ${category}<br/> 
            Id média: ${mediumId}<br/>
            
            <c:forEach var="entry" items="${metaInfo}">
                ${entry.key}: ${entry.value} <br/>
            </c:forEach>

        </div>
    </jsp:body>
        
</t:pageTemplate>