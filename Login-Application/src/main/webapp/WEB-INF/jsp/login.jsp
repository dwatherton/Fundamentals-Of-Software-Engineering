<%@ page session="false"%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:page>
	<jsp:attribute name="title">Login</jsp:attribute>
	<jsp:body>

		<form class="form-container" action="${pageContext.request.contextPath}/Login" method="post">
			<section class="form">

				<h3 class="form-header">Login</h3>

				<div class="form-group row">
					<label class="col-sm-3 col-form-label">User ID: </label>
					<div class="col-sm-9">
						<input class="form-control" type="text" name="userID" placeholder="User ID">
					</div>
				</div>

				<div class="form-group row">
					<label class="col-sm-3 col-form-label">Password: </label>
					<div class="col-sm-9">
						<input class="form-control" type="password" name="password" placeholder="Password">
					</div>
				</div>

				<input class="form-button" type="submit" name="login" value="Login">

			</section>
		</form>

	</jsp:body>
</t:page>
