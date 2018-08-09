<!DOCTYPE html>
<html lang="en"
      xmlns:s="http://www.springframework.org/tags"
      xmlns:jsp="http://java.sun.com/JSP/Page"
      xmlns:c="http://java.sun.com/jsp/jstl/core"
      xmlns="http://www.w3.org/1999/xhtml">

<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var = "disable_ccsworkflow" value = "false" />
<c:set var = "disable_mailworkflow" value = "false" />
<head>
    <title>CCS Workflow</title>
    <jsp:include page="includes/head.jsp" />
    <meta http-equiv="Refresh" content="30">
</head>
<body>
<jsp:include page="includes/topBar.jsp">
    <jsp:param name="page" value="workflow"/>
</jsp:include>
<div class="jumbotron text-center">
    <h1>Cumulus Crowd Service Workflow</h1>
</div>
<div id="main" class="container">
  <div id="ccsWorkflow" class="container">
    <p><b>Name:</b> ${ccsWorkflow.getName()}</p>
    <p><b>Current state:</b> ${ccsWorkflow.getState()}</p>
    <p><b>Next run:</b> ${ccsWorkflow.getNextRunDate()}</p>

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
        <c:forEach items="${ccsWorkflow.getSteps()}" var="step">
            <c:if test="${step.getStatus() eq 'Running'}">
                <c:set var = "disable_ccsworkflow" value = "true"/>
            </c:if>
            <tr>
                <td>${step.getName()}</td>
                <td>${step.getStatus()}</td>
                <td>${step.getExecutionTime()}</td>
                <td>${step.getResultOfLastRun()}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <form action="${pageContext.request.contextPath}/workflow/run" method="post">
        <button type="submit" name="name" value="ccsWorkflow" class="btn btn-success" <c:if test="${disable_ccsworkflow eq true}">disabled</c:if> >Run CCSWorkflow</button>
    </form>
  </div>
  <br/>
  <hr/>
  <br/>
  <div id="mailWorkflow" class="container">
    <p><b>Name:</b> ${mailWorkflow.getName()}</p>
    <p><b>Current state:</b> ${mailWorkflow.getState()}</p>
    <p><b>Next run:</b> ${mailWorkflow.getNextRunDate()}</p>

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
        <c:forEach items="${mailWorkflow.getSteps()}" var="step">
            <c:if test="${step.getStatus() eq 'Running'}">
                <c:set var = "disable_mailworkflow" value = "true"/>
            </c:if>
            <tr>
                <td>${step.getName()}</td>
                <td>${step.getStatus()}</td>
                <td>${step.getExecutionTime()}</td>
                <td>${step.getResultOfLastRun()}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <form action="${pageContext.request.contextPath}/workflow/run" method="post">
        <button type="submit" name="name" value="mailWorkflow" class="btn btn-success" <c:if test="${disable_mailworkflow eq true}">disabled</c:if> >Run MailWorkflow</button>
    </form>
  </div>
</div>
</body>
</html>
