<%@ page session="false"%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:page>
	<jsp:attribute name="title">Welcome!</jsp:attribute>
	<jsp:body>

		<c:choose>
		
			<c:when test="${registered}">
			
				<h3> Welcome <c:out value="${account}"/>, you have successfully Registered and been Logged In!</h3>
				
			</c:when>
			
			<c:when test="${loggedin}">
			
				<h3> Welcome <c:out value="${account}"/>, you have successfully Logged In!</h3>
				
			</c:when>
			
			<c:otherwise>
			
				<h3>The Login Application functions are listed below:</h3>
				
				<br>
				
				<ul class="app-description">
				
				    <li><b>Register a new user</b> - to register a new user, please click on the "Register" button in the top-right navigation bar and fill out the registration form! (ALL fields are required, and the userID must be unique)!</li>
				    
				    <br>
				    
				    <li><b>Login as an existing user</b> - to login as an existing user, please click on the "Login" button in the top-right navigation bar and fill out the login form! (userID and password are required, user can click "Forgot Password?" button to reset a forgotten password)!</li>
				    
				    <br>
				    
				    <li><b>Reset a forgotten password</b> - to reset a forgotten password for an existing user, please click on the "Login" button in the top-right navigation bar and click on the "Forgot Password?" button, then fill out the forgot password form! (userID, securityQuestion, and securityAnswer are required alongside a new password entered twice)!</li>
				    
				</ul>
				
			</c:otherwise>
			
		</c:choose>

	</jsp:body>
</t:page>
