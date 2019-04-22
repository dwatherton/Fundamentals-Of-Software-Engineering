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
	<!-- Allow for each jsp view using this tag to append it's own name to the title, "Login Application | TITLE" -->
	<title>Login Application | <c:out value="${!empty title ? title : ''}" /> </title>
	<!-- Using Bootstrap CDN for quick prototyping and page styling/design -->
	<link href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css" rel="stylesheet">
	<!-- Include my own personal stylesheet, style.css, for all jsp views using this tag -->
	<style> <%@include file="../jsp/css/style.css"%> </style>
	<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
	<!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"> </script>
        <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"> </script>
        <![endif]-->
	<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"> </script>
	<script src="//maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"> </script>
</head>

<body>
	<div class="container">

		<div class="header">

			<ul class="nav nav-pills pull-right">
				<%-- Set variable 'uri' for checking the servlet jsp view that the user is currently on --%>
				<c:set var="uri" value="${requestScope['javax.servlet.forward.request_uri']}" />
				<%-- Show Home as the active nav button if uri ends with "/Home" --%>
				<li <c:if test="${fn:endsWith(uri,'Home')}"> class="active" </c:if>> <a href="${pageContext.request.contextPath}/Home">Home</a> </li>
					<%-- Change upper right nav buttons depending on if the user is logged in or not --%>
					<c:choose>
						<c:when test="${!empty account}">
							<%-- When user is logged in show Home as the active nav button (done above) and Logout as an inactive nav button --%>
							<li> <a href="${pageContext.request.contextPath}/Logout">Logout</a> </li>
						</c:when>
						<c:otherwise>
							<%-- When user is not logged in show Register as active nav button if uri ends with "/Register", and show Login as active nav button if uri ends with "/Login" or "/ForgotPassword" --%>
							<li <c:if test="${fn:endsWith(uri,'Register')}"> class="active" </c:if>> <a href="${pageContext.request.contextPath}/Register">Register</a> </li>
							<li <c:if test="${fn:endsWith(uri,'Login') || fn:endsWith(uri,'ForgotPassword')}"> class="active" </c:if>> <a href="${pageContext.request.contextPath}/Login">Login</a> </li>
						</c:otherwise>
					</c:choose>
			</ul>

			<h3 class="text-muted">Login Application</h3>

		</div>

		<br>

		<div class="jumbotron" id="main">

			<jsp:doBody />

		</div>

	</div>
</body>

</html>
