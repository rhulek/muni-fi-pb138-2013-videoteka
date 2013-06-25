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
        Context path: <c:out value="${pageContext.servletContext.contextPath}"/><br/>
        <table>
            <c:forEach items="${categoriesList}" var="item">
                <tr>
                    <td><a href="${pageContext.servletContext.contextPath}/category/${item}"> <c:out value="${item}" /> </a></td>
                    <td><a href="${pageContext.servletContext.contextPath}/category/delete/${item}"> Smazat </a></td>
                    <td><a href="${pageContext.servletContext.contextPath}/category/showRenameForm/${item}"> Přejmenovat </a></td>
                </tr>
            </c:forEach>
        </table>
    </jsp:body>
        
</t:pageTemplate>