<%@ page session="false"%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:page>
    <jsp:attribute name="title">Login</jsp:attribute>
    <jsp:body>

        <form action="${pageContext.request.contextPath}/Login" method="post">

            <table>
                <tr>User ID: </tr>
                <input type="text" name="userID">
                <br/>

                <tr>Password: </tr>
                <input type="password" name="password">
                <br/>

                <tr>Name: </tr>
                <input type="text" name="name">
                <br/>

                <tr>Security Question </tr>
                <select multiple="true" size="2" name="securityQuestion">
                    <option value="What is your mothers maiden name?">What is your mothers maiden name?</option>
                    <option value="What city were you born in?">What city were you born in?</option>
                </select>
                <br/>

                <input type="submit" name="submit" value="Submit" />
            </table>

        </form>

    </jsp:body>
</t:page>