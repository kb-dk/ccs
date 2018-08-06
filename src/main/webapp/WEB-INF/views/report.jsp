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
</head>
<body>
<div class="jumbotron text-center">
    <h1>Cumulus Crowd Service backflow report</h1>
</div>
<div id="main" class="container">
    <p><b>Number of entries returned to Cumulus:</b> ${count}</p>
    <p><b>Date interval:</b> ${fromDate} <b>to</b> ${toDate}</p>

    <!--form action="${pageContext.request.contextPath}/workflow/run" method="post">
        <button type="submit" class="btn btn-success" id="runWorkflow" <c:if test="${disable eq true}">disabled</c:if>>Run now</button>
    </form-->
</div>
</body>
</html>
