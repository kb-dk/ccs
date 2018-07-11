<%@ page import="dk.kb.ccs.SendMail" %>
<%@ page import="org.springframework.beans.factory.annotation.Value" %>
<!DOCTYPE html>
<html lang="en"
      xmlns:spring="http://www.springframework.org/tags"
      xmlns:jsp="http://java.sun.com/JSP/Page"
      xmlns="http://www.w3.org/1999/xhtml">

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<head>
    <title>CCS start page</title>
    <jsp:include page="includes/head.jsp"/>
</head>
<body>
<jsp:include page="includes/topBar.jsp">
    <jsp:param name="page" value="workflow"/>
</jsp:include>
<div class="jumbotron text-center">
    <h1>Cumulus Crowd Service</h1>
</div>
<div id="main" class="container">
    <p><b>Næste planlagte opdatering:</b> </p>


    <table class="table table-striped">
        <thead>
        <tr>
            <th>Add status here</th>
        </tr>
        </thead>
    </table>

</div>
</body>
</html>