<%-- 
    Document   : _scndMenu
    Created on : 24.6.2013, 19:42:08
    Author     : hulek
    Doc        : Pomocny soubor, ktery je urcen pro includovani.
                 Dokument obsahuje prvky navigacniho menu urceneho do praveho sloupce.
                 Volani lze parametrizovat parametrem "menuItemActive"
    @param String menuItemActive ridi zobrezeni aktivni polozky menu.
        Nabyva hodnot: "categoryShowAll" | "categoryAdd" | "mediumAdd" | "search" | "export" | "import".
        Mozno nadale rozsirovat.
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<div id="navcontainer-scnd">
    <h3>Hlavní nabídka</h3>
    <ul id="navlist-scnd">
        
        <li <% if(request.getParameter("menuItemActive").equals("categoryShowAll") ) {%> class="active" <%}%>  ><a href="${pageContext.servletContext.contextPath}/category/showAll">Přehled kategorií</a></li>
        <li <% if(request.getParameter("menuItemActive").equals("categoryAdd") ) {%> class="active" <%}%>  ><a href="${pageContext.servletContext.contextPath}/category/addCategory">Přidat kategorii</a></li>
        <li <% if(request.getParameter("menuItemActive").equals("mediumAdd") ) {%> class="active" <%}%>  ><a href="${pageContext.servletContext.contextPath}/medium/addMedium/empty">Přidat medium</a></li>
        <li <% if(request.getParameter("menuItemActive").equals("search") ) {%> class="active" <%}%>  ><a href="${pageContext.servletContext.contextPath}/search">Hledat film</a></li>
        <li <% if(request.getParameter("menuItemActive").equals("export") ) {%> class="active" <%}%>  ><a href="${pageContext.servletContext.contextPath}/file/export">Export</a></li>
        <li <% if(request.getParameter("menuItemActive").equals("import") ) {%> class="active" <%}%>  ><a href="${pageContext.servletContext.contextPath}/file/import">Import</a></li>
        
      
    </ul>
</div>