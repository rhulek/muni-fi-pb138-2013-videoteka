<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="header.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Welcome to Spring Web MVC project</title>
    </head>

    <body>
        <p>Hello! This is the default welcome page for a Spring Web MVC project.</p>
        <p><i>To display a different welcome page for this project, modify</i>
            <tt>index.jsp</tt> <i>, or create your own welcome page then change
                the redirection in</i> <tt>redirect.jsp</tt> <i>to point to the new
                welcome page and also update the welcome-file setting in</i>
            <tt>web.xml</tt>.</p>
        
        
        <br/>
        <br/>
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
        <td><a href="${pageContext.servletContext.contextPath}/category/addCategory"> Přidat kategorii TODO </a></td>
    </body>
</html>
