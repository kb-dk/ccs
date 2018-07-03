<!DOCTYPE html>
<html lang="en"
      xmlns:spring="http://www.springframework.org/tags"
      xmlns:jsp="http://java.sun.com/JSP/Page"
        xmlns="http://www.w3.org/1999/xhtml">

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<head>
    <title>Hello Workflow</title>
</head>
<body>
Hello CCS!
</body>
</html>
<!--
<html lang="en"
      xmlns:s="http://www.springframework.org/tags"
      xmlns:jsp="http://java.sun.com/JSP/Page"
      xmlns:c="http://java.sun.com/jsp/jstl/core"
      xmlns="http://www.w3.org/1999/xhtml">
<c:set var = "disable" value = "false"/>
<head>
    <title>CCS Workflow</title>
    <jsp:include page="includes/head.jsp"/>
    <meta http-equiv="Refresh" content="5">
</head>

<body>
<jsp:include page="includes/topBar.jsp">
    <jsp:param name="page" value="workflow"/>
</jsp:include>
<div class="jumbotron text-center">
    <h1>CCS-Workflow</h1>
</div>
<div id="main" class="container">
    <p><b>Current state:</b> ${workflow.getState()}</p>
    <p><b>Next run:</b> ${workflow.getNextRunDate()}</p>

    <table class="table table-striped">
        <thead>
        <tr>
            <th>Name of step</th>
            <th>State</th>
            <th>Time for last run (in millis)</th>
            <th>Results for last run</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${workflow.getSteps()}" var="step">
            <c:if test="${step.getStatus() eq 'Running'}">
                <c:set var = "disable" value = "true"/>
            </c:if>
            <tr>
                <td>${step.getName()}</td>
                <td>${step.getStatus()}</td>
                <td>${step.getTimeForLastRun()}</td>
                <td>${step.getResultOfLastRun()}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>


    <h2></h2>
    <form action="${pageContext.request.contextPath}/workflow/run" method="post">
        <button type="submit" class="btn btn-success" id="runWorkflow" <c:if test="${disable eq true}">disabled</c:if>>Run now</button>
    </form>
    <div>
    </div>
</div>
</body>
</html>
-->

