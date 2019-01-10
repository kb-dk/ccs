<!DOCTYPE html>
<html lang="en"
      xmlns:s="http://www.springframework.org/tags"
      xmlns:jsp="http://java.sun.com/JSP/Page"
      xmlns:c="http://java.sun.com/jsp/jstl/core"
      xmlns="http://www.w3.org/1999/xhtml">

<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var = "disable" value = "false"/>
<head>
    <title>CCS Report</title>
  <jsp:include page="includes/head.jsp" />
</head>

<body>
<jsp:include page="includes/topBar.jsp">
    <jsp:param name="page" value="report"/>
</jsp:include>
<div class="jumbotron text-center">
    <h1>Cumulus Crowd Service backflow report</h1>
</div>
<div id="main" class="container">
  <p><b>Number of entries returned to Cumulus:</b> ${count}</p>
  <p><b>Date interval:</b> ${fromDateTime} <b>to</b> ${toDateTime}</p>

  <form action="${pageContext.request.contextPath}/report/run" method="post">
    <div>
      <label for="fromDate">Start date</label>
      <input type="date" id="fromDate" name="fromDate" value="${fromDate}" />
    </div>
    <div>
      <label for="toDate">End date</label>
      <input type="date" id="toDate" name="toDate" value="${toDate}" />
    </div>
  
    <button type="submit" name="sendMail" value="false" class="btn btn-success">Check date</button>
    <button type="submit" name="sendMail" value="true" class="btn btn-success">Send mail</button>
  </form>
</div>
</body>
</html>
