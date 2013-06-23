<%-- 
    Document   : insertCode
    Created on : 17.6.2013, 21:01:44
    Author     : Martin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="${pageContext.request.contextPath}/resources/css/styles.css" rel="stylesheet" type="text/css"/>
        <title>Videotéka - autorizace aplikace</title>
    </head>
    <body>
        <div id="wrapper">
            <div id="heading">
                <div class="content">
                    <a href="${pageContext.request.contextPath}/category" id="logo-link">
                    <img src="${pageContext.request.contextPath}/resources/images/videoteka-logo.png" alt="Videotéka" /></a>
                    <span id="slogan">Správa domácí videotéky / fonotéky</span>
                    <img src="${pageContext.request.contextPath}/resources/images/banner.png" alt="Banner" id="banner-img" />
                </div>
            </div>
                <div id="main" class="light-theme">
                <div class="content">
                    <h1>Autorizace aplikace<br/>pro přístup k úložišti Google Drive</h1>
                    <p>Pro správný chod aplikace videotéky je potřeba tuto aplikaci autorizovat vůči úložišti Google Drive.
                        <br/>
                        V nově otevřeném okně prohlížeče autorizujte aplikaci a zde vložte vygenerovaný bezpečnostní kód.</p>
                    <form name="authCodeForm" action="autorize" method="POST" >
                        <input name="authCode" type="text" class="auth-code-form-input"/>
                        <input type="submit" value="Autorizovat aplikaci"/>
                    </form>
                </div>
            </div>
            <div id="footer">
                <div class="content">
                    <img src="${pageContext.request.contextPath}/resources/images/dvojice.png" alt="" id="footer-decor-img" />
                    <div class="block1">
                        <h3>Info</h3>
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
                </div>
            </div>
            
        </div>
    </body>
</html>
