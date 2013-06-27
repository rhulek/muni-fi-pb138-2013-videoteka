<%-- 
    Document   : _heading
    Created on : 24.6.2013, 19:43:06
    Author     : hulek
    Doc        : Pomocny soubor, ktery je urcen pro includovani.
                 Dokument obsahuje prvky zahlavi.
                 Volani lze parametrizovat parametry "type" a "menuItemActive"
    @param String type Parametr ridi presnou podobu vykreslovani HTML komponent. Nabyva hodnot: "compact" | ""
    @param String menuItemActive ridi zobrezeni aktivni polozky hlavniho horizontalniho menu v zahlavi. Nabyva hodnot: "catalog". Mozno nadale rozsirovat.
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<c:choose>
    <c:when test="${param.type=='compact'}">
        <div id="heading" class="compact">
            <div class="content">
                <a href="${pageContext.request.contextPath}/category" id="logo-link">
                <img src="${pageContext.request.contextPath}/resources/images/videoteka-logo.png" alt="Videotéka" /></a>
                <div id="navcontainer">
                    <ul id="navlist">
                        <li <% if(request.getParameter("menuItemActive").equals("catalog") ) {%> class="active" <%}%>  ><a href="${pageContext.request.contextPath}/category">Katalog</a></li>
<!--                        <li <% if(request.getParameter("menuItemActive").equals("setup") ) {%> class="active" <%}%>  ><a href="#">Nastavení</a></li>
                        <   li <% if(request.getParameter("menuItemActive").equals("help") ) {%> class="active" <%}%>  ><a href="#">Nápověda</a></li>-->
                    </ul>
                </div>
            </div>
        </div>
    </c:when>
    <c:otherwise>
        <div id="heading">
            <div class="content">
                <a href="${pageContext.request.contextPath}/category" id="logo-link">
                <img src="${pageContext.request.contextPath}/resources/images/videoteka-logo.png" alt="Videotéka" /></a>
                <span id="slogan">Správa domácí videotéky / fonotéky</span>
                <img src="${pageContext.request.contextPath}/resources/images/banner.png" alt="Banner" id="banner-img" />
            </div>
        </div>        
    </c:otherwise>
</c:choose>

