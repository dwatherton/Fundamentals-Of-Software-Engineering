<%@ page session="false"%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:page>
    <jsp:attribute name="title">Register</jsp:attribute>
    <jsp:body>

        <form class="form-container" action="${pageContext.request.contextPath}/Register" method="post">
            <section class="form">

                <h3 class="form-header">Register</h3>
                

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
                
				<div class="form-group row">
					<label class="col-sm-3 col-form-label">Name: </label>
					<div class="col-sm-9">
						<input class="form-control" type="text" name="name" placeholder="Name">
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

                <input class="form-button" type="submit" name="register" value="Register">
                
            </section>
        </form>

    </jsp:body>
</t:page>