<%-- 
    Document   : _footer
    Created on : 23.6.2013, 15:38:14
    Author     : hulek
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<div id="footer">
    <div class="content">
        <img src="${pageContext.request.contextPath}/resources/images/dvojice.png" alt="" id="footer-decor-img" />
        <div class="block1">
            <h3>Info - included</h3>
            <p>Školní projekt, který vznikl na  půdě FI.MUNI.cz<br/>PB138 (jaro2013)</p>
        </div>
        <div class="block2">
            <h3>Autoři</h3>
            <p>
                Richard Hůlek<br/>
                Ondřej Vojtíšek<br/>
                Milan Vláčil<br/>
                Martin Vavrušák
            </p>
        </div>
        <div class="block3">
            <h3>Odkazy</h3>
            <p>PB138 - Moderní značkovací jazyky a jejich aplikace<br/>
                Fakulta informatiky MU, Brno</p>
        </div>
        <c:choose>
            <c:when test="${false}">false</c:when>
            <c:otherwise>other</c:otherwise>
        </c:choose>
    </div>
</div>