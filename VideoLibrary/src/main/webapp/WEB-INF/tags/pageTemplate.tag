<%-- 
    Document   : pageTemplate
    Created on : 25.6.2013, 1:38:16
    Author     : hulek
--%>

<%@tag description="Template of common page" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@attribute name="pageTitle" required="true"%>
<%@attribute name="theme" required="true"%>
<%@attribute name="headingType" required="false"%>
<%@attribute name="headingMenuItemActive" required="false"%>
<%@attribute name="scndMenuItemActive" required="false"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <title>Videotéka - ${pageTitle}</title>
        <meta http-equiv="content-type" content="text/html; charset=utf-8" />
        <link rel="stylesheet" href="${pageContext.servletContext.contextPath}/resources/css/styles.css" type="text/css" />
    </head>

    <body>
        
        <div id="wrapper">
            <!-- heading -->
            <jsp:include page="../jsp/_heading.jsp" >
                <jsp:param name="type" value="${headingType}" />
                <jsp:param name="menuItemActive" value="${headingMenuItemActive}" />
            </jsp:include>
            
            <!-- main -->
            <div id="main" class="${theme}">
                <div class="content">
                    <div class="two-column-layout">
                        <div class="column1">
                            <div class="column-content">
                                 <jsp:include page="../jsp/_scndMenu.jsp" >
                                    <jsp:param name="menuItemActive" value="${scndMenuItemActive}" />
                                </jsp:include>
                            </div>
                        </div>
                        <div class="column2">
                            <div class="column-content">
                                <h1>${pageTitle}</h1>
                                <jsp:doBody/>
                            </div>
                        </div>
                        <hr class="cleaner"/>
                    </div> <!-- two-column-layout -->

                </div><!-- main.content -->
            </div>
                                
            <!-- footer -->
            <jsp:include page="../jsp/_footer.jsp" ></jsp:include>     
        </div>
    </body>
</html>
