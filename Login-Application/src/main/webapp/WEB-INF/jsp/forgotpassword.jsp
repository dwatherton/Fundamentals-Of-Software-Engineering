<%@ page session="false"%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:page>
	<jsp:attribute name="title">Forgot Password</jsp:attribute>
	<jsp:body>

        <c:choose>
        
			<c:when test="${forgotPasswordFieldEmpty}">
			
				<h3 class="error">Please provide information for ALL fields below!!!</h3>
				
			</c:when>
			
			<c:when test="${incorrectRecoveryInfo}">
			
			    <h3 class="error">Some or all of the information provided was incorrect, Please try again!!!</h3>
			
			</c:when>
			
			<c:when test="${passwordsDoNotMatch}">
			
			    <h3 class="error">The passwords entered do NOT match, Please try again!!! </h3>    
			
			</c:when>
			
		</c:choose>
		
		<form class="form-container" action="${pageContext.request.contextPath}/ForgotPassword" method="post">
				
		    <section class="form">

		    <h3 class="form-header">Forgot Password</h3>

			<div class="form-group row">
				<label class="col-sm-3 col-form-label">User ID: </label>
				<div class="col-sm-9">
					<input class="form-control" type="text" name="userID" placeholder="User ID">
				</div>
			</div>

			<div class="form-group row">
				<label class="col-sm-3 col-form-label">Security Question: </label>
				<div class="col-sm-9">
					<select class="form-control" name="securityQuestion">
						<option value="What is your mothers maiden name?">What is your mothers maiden name?</option>
						<option value="What city were you born in?">What city were you born in?</option>
					</select>
				</div>
			</div>

			<div class="form-group row">
				<label class="col-sm-3 col-form-label">Security Answer: </label>
				<div class="col-sm-9">
					<input class="form-control" type="text" name="securityAnswer" placeholder="Security Answer">
				</div>
			</div>
			
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">New Password: </label>
				<div class="col-sm-9">
					<input class="form-control" type="password" name="newPassword" placeholder="New Password">
				</div>
			</div>
			
			<div class="form-group row">
				<label class="col-sm-3 col-form-label">Confirm Password: </label>
				<div class="col-sm-9">
					<input class="form-control" type="password" name="confirmPassword" placeholder="Confirm Password">
				</div>
			</div>

			<input class="form-button" type="submit" name="resetPassword" value="Reset Password">
			
		    </section>
    			    
        </form>

	</jsp:body>
</t:page>
