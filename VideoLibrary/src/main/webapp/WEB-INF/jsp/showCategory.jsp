<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="header.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<t:pageTemplate>
    <jsp:attribute name="pageTitle">Detail kategorie</jsp:attribute>
    <jsp:attribute name="theme">dark-theme</jsp:attribute>
    <jsp:attribute name="headingType">compact</jsp:attribute>
    <jsp:attribute name="headingMenuItemActive">catalog</jsp:attribute>
    
    <jsp:body>
        <c:set var="catName" value="${category.name}" scope="page"/>
        <h1><c:out value="${catName}"/></h1>
        
        <table border="1">
            <c:forEach items="${category.mediums}" var="med">
            <tr>
                <td> 
                    <form action="${pageContext.servletContext.contextPath}/medium/delete" id="deleteBackingForm" method="POST">
                        <input type="text" name="mediumID" id="mediumID" value="${med.value.id}"/>
                        <input type="text" name="categoryName" id="categoryName" value="${category.name}"/>
                        <input type="submit" value="Smazat"/>
                    </form>
                
                </td>
                <td>

                <c:out value="${med.value.id}">Tady by melo byt ID kterte je ale null</c:out></td>
                            
                <c:forEach items="${med.value.movies}" var="movie">
                    <td><c:out value="${movie.name}"/></td>
                </c:forEach>
            </tr>
            </c:forEach>
        </table>
        
        <form action="${pageContext.servletContext.contextPath}/medium/delete" id="deleteBackingForm" method="POST">
            <input type="hidden" name="mediumID" id="mediumID" value="666"/>
            <input type="hidden" name="categoryName" id="categoryName" value="7777"/>
        </form>
        
        <%-- <c:set var="preselected" value="${category.name}" scope="application"/> --%>
        <!--
            Preselected je priznak, ktery rika kontroleru jestli jsme prisli z nejake kategorie a tedy jestli parametr v URL je validni
            nazev kategorie a ta ma byt predvyplnena ve formulari k vytvoreni noveho media.
        -->
        <a href="${pageContext.servletContext.contextPath}/medium/addMedium/${catName}?preselected=true"> Pridat medium </a><br/><br/>
        <a href="${pageContext.servletContext.contextPath}/category/showAll"> Zpet na seznam kategorii </a>
    </jsp:body>
        
</t:pageTemplate>