<%-- 
    Document   : insertCode
    Created on : 17.6.2013, 21:01:44
    Author     : Martin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="header.jsp" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="${pageContext.request.contextPath}/resources/css/styles.css" rel="stylesheet" type="text/css"/>
        <title>Videotéka - autorizace aplikace</title>
    </head>
    <body>
        <div id="wrapper">
            <!-- heading -->
            <jsp:include page="_heading.jsp" ></jsp:include>
            
                <div id="main" class="light-theme">
                <div class="content">
                    <h1>Autorizace aplikace<br/>pro přístup k úložišti Google Drive</h1>
                    <p>Pro správný chod aplikace videotéky je potřeba tuto aplikaci autorizovat vůči úložišti Google Drive.
                        <br/>
                        V nově otevřeném okně prohlížeče autorizujte aplikaci a zde vložte vygenerovaný bezpečnostní kód.</p>
                    <form name="authCodeForm" action="autorize" method="POST" >
                        <label for="authCode">Autorizační kód:</label>
                               <input name="authCode" id="authCode" type="text" class="auth-code-form-input"/>
                        <input type="submit" value="Autorizovat aplikaci"/>
                    </form>
                </div>
            </div>
            
            <jsp:include page="_footer.jsp" ></jsp:include>
            
        </div>
    </body>
</html>
