<%@ page session="false"%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:page>
	<jsp:attribute name="title">Welcome!</jsp:attribute>
	<jsp:body>

		<c:choose>
		
			<c:when test="${registered}">
			
				<h3>You have successfully Registered and been Logged In!</h3>
				
			</c:when>
			
			<c:when test="${loggedin}">
			
				<h3>You have successfully Logged In!</h3>
				
			</c:when>
			
			<c:otherwise>
			
				<h3>Welcome to the Login Web Application!</h3>
				
			</c:otherwise>
			
		</c:choose>

	</jsp:body>
</t:page>
