<%@tag description="Page Template" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@attribute name="title" required="false" %>

<!DOCTYPE html>
<html>

<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<!-- Allow for each jsp view using this tag to append it's name to the title of "Login Application | TITLE" -->
	<title>Login Application |
		<c:out value="${!empty title ? title : ''}" />
	</title>
	<!-- Using Bootstrap CDN for the quick prototyping of page styling and design -->
	<link href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css" rel="stylesheet">
	<style>
		<%@include file="../jsp/css/style.css"%>

	</style>
	<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
	<!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
        <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
        <![endif]-->
	<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
	<script src="//maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>
</head>

<body>
	<div class="container">

		<div class="header">

			<ul class="nav nav-pills pull-right">
				<%-- Set variable 'uri' for checking the servlet jsp view that the user is currently on --%>
				<c:set var="uri" value="${requestScope['javax.servlet.forward.request_uri']}" />
				<%-- Show Home as active if uri ends with "/Home" --%>
				<li<c:if test="${fn:endsWith(uri,'Home')}"> class="active"</c:if>><a href="${pageContext.request.contextPath}/Home">Home</a></li>

					<%-- Change upper right context menu depending on if the user is logged in or not --%>
					<c:choose>
						<c:when test="${!empty account}">
							<%-- When user is logged in show Home as active (done above) and Logout as inactive --%>
							<li><a href="${pageContext.request.contextPath}/Logout">Logout</a></li>
						</c:when>
						<c:otherwise>
							<%-- When user is not logged in show Register as active if uri ends with "/Register" and Login as active if uri ends with "/Login" --%>
							<li<c:if test="${fn:endsWith(uri,'Register')}"> class="active"</c:if>><a href="${pageContext.request.contextPath}/Register">Register</a></li>
								<li<c:if test="${fn:endsWith(uri,'Login')}"> class="active"</c:if>><a href="${pageContext.request.contextPath}/Login">Login</a></li>
						</c:otherwise>
					</c:choose>
			</ul>

			<h3 class="text-muted">Login Application</h3>


			<!-- TEMPORARY - FOR TESTING JSP VARIABLE VALUES - SIMPLY UN-COMMENT THESE OUT FOR DEBUGGING -->
                <section class="TEMP">
                    <c:if test="${!empty title}"> <c:out value="Title: ${title}"/> <br/> </c:if>
                    <c:if test="${!empty account}"> <c:out value="Account: ${account}"/> <br/> </c:if>
                    <c:if test="${empty account}"> <c:out value="Account: null"/> <br/> </c:if>
                    <c:if test="${!registered}"> <c:out value="Registered: null"/> <br/> </c:if>
                    <c:if test="${registered}"> <c:out value="Registered: ${registered}"/> <br/> </c:if>
                    <c:if test="${!loggedin}"> <c:out value="Logged In: null"/> <br/> </c:if>
                    <c:if test="${loggedin}"> <c:out value="Logged In: ${loggedin}"/> <br/> </c:if>
                    <c:if test="${!empty registrationFieldEmpty}"> <c:out value="Registration Field Empty: ${registrationFieldEmpty}"/> <br/> </c:if>
                    <c:if test="${empty registrationFieldEmpty}"> <c:out value="Registration Field Empty: null"/> <br/> </c:if>
                    <c:if test="${!empty loginFieldEmpty}"> <c:out value="Login Field Empty: ${loginFieldEmpty}"/> <br/> </c:if>
                    <c:if test="${empty loginFieldEmpty}"> <c:out value="Login Field Empty: null"/> <br/> </c:if>
                    <c:if test="${!empty uri}"> <c:out value="URI: ${uri}"/> <br/> </c:if>
                </section>
            



		</div>

		<br>

		<div class="jumbotron" id="main">

			<jsp:doBody />

		</div>

	</div>
</body>

</html>
