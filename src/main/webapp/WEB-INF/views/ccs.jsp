<!DOCTYPE html>
<html lang="en"
      xmlns:s="http://www.springframework.org/tags"
      xmlns:jsp="http://java.sun.com/JSP/Page"
      xmlns:c="http://java.sun.com/jsp/jstl/core"
      xmlns="http://www.w3.org/1999/xhtml">

<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<head>
    <title>Cumulus Crowd Service</title>
    <jsp:include page="includes/head.jsp" />
</head>
<body>
<jsp:include page="includes/topBar.jsp">
    <jsp:param name="page" value="ginnungagap"/>
</jsp:include>
<div class="jumbotron text-center">
    <h1>Cumulus Crowd Service</h1>
    <h2>${applicationName}</h2>
    <h3>Version: ${version}</h3>
</div>
<div id="conf" class="container">
  <h3>Configuration</h3>
  <div class="container">
    <p><b>Cumulus Url:</b> ${cumulusUrl}</p>
    <p><b>Cumulus Catalog:</b> ${cumulusCatalog}</p>
    <p><b>Solr Url:</b> ${solrUrl}</p>
    <p><b>Solr FilterQuery:</b> ${solrFilterQuery}</p>
    <p><b>Solr Max Results:</b> ${solrMaxResults}</p>
    <p><b>CCS Workflow Interval (in milliseconds):</b> ${ccsWorkflowInterval}</p>
    <p><b>Mail Workflow Interval (in milliseconds):</b> ${mailWorkflowInterval}</p>
    <p><b>Mail from:</b> ${mailConf.getFrom()}</p>
    <p><b>Mail to:</b></p>
    <ul>
      <c:forEach items="${mailConf.getTo()}" var="mailTo">
        <li>${mailTo}</li>
      </c:forEach>
    </ul>
    <p><b>Report file location: </b> ${reportFile}</p>
  </div>
</div>
</body>
</html>