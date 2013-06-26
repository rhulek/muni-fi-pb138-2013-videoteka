<%-- 
    Document   : fileInput
    Created on : 24.6.2013, 13:11:58
    Author     : Martin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="header.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<t:pageTemplate>
    <jsp:attribute name="pageTitle">Import knihovny</jsp:attribute>
    <jsp:attribute name="theme">dark-theme</jsp:attribute>
    <jsp:attribute name="headingType">compact</jsp:attribute>
    <jsp:attribute name="headingMenuItemActive">catalog</jsp:attribute>
    <jsp:attribute name="scndMenuItemActive">import</jsp:attribute>
    
    <jsp:body>
        <form:form action="${baseURL}/file/import" method="POST" commandName="importBean" enctype="multipart/form-data">
            
            <div class="form-row">
                <label for="filePath">Soubor k importu</label>
                <input type="file" name="filePath"/>
            </div>
            
            <div class="form-row radio-btns">
                <label for="importOption">Režim importu</label>
                <form:radiobuttons path="importOption" items="${importBean.importOptions}"/>
            </div>
               
            <div class="form-row submit">
                <input type="submit" value="Importovat"/>
            </div>  
            
        </form:form>
            
    </jsp:body>
        
</t:pageTemplate>