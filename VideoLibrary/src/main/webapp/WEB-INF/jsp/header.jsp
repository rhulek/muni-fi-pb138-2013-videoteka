<%-- 
    Document   : header
    Created on : 17.6.2013, 16:43:18
    Author     : Martin
    Doc        : Pomocný soubor ur?ený k includovaní - sdílené deklarace
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:set var="baseURL" value="${pageContext.servletContext.contextPath}" scope="page"/>