<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="header.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<t:pageTemplate>
    <jsp:attribute name="pageTitle">${movieName}</jsp:attribute>
    <jsp:attribute name="theme">dark-theme</jsp:attribute>
    <jsp:attribute name="headingType">compact</jsp:attribute>
    <jsp:attribute name="headingMenuItemActive">catalog</jsp:attribute>
    
    <jsp:body>
        <div class="item-container">
            <b>Kategorie: </b> ${category}<br/> 
            <b>Medium ID: </b> ${mediumId}<br/><br/>
            <form:form action="${pageContext.servletContext.contextPath}/movie/save" method="POST">
                
            <table>
            <c:forEach var="entry" items="${metaInfo}">
                <tr>
                    <td>
                <label for="${entry.key}">${entry.key}</label>
                    </td>
                    <td>
                    <input name="${entry.value}" value="${entry.value}"/>
                    </td>
                </tr>
            </c:forEach>
            </table>
            
              <div class="form-row submit">
                    <input type="submit" value="Uložit"/>
              </div>
             </form:form>
        </div>
    </jsp:body>
        
</t:pageTemplate>