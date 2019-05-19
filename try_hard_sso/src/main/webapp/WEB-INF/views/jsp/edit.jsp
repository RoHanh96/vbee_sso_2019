<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link href="/resources/css/bootstrap.min.css" rel="stylesheet">
    <link href="/resources/css/common.css" rel="stylesheet">
	<title>Sửa thông tin người dùng</title>
</head>
<body>
	<form:form method="POST" modelAttribute="userInfo" class="form-signin">
	 <h2 class="form-signin-heading">Sửa thông tin</h2>
	 
	 		<spring:bind path="id">
	 			<label>ID</label>
                <div class="form-group ${status.error ? 'has-error' : ''}">
                    <form:input type="text" path="id" class="form-control"
                                autofocus="true" value="${userInfo.getId()}" readonly="true"></form:input>
                </div>
            </spring:bind>
            <spring:bind path="username">
            	<label>Tên tài khoản</label>
                <div class="form-group ${status.error ? 'has-error' : ''}">
                    <form:input type="text" path="username" class="form-control"
                                autofocus="true" value="${userInfo.getUsername()}"></form:input>
                    <form:errors path="username" class="has-error"></form:errors>
                </div>
            </spring:bind>

            <spring:bind path="email">
            	<label>Email</label>
                <div class="form-group ${status.error ? 'has-error' : ''}">
                    <form:input type="text" path="email" class="form-control"
                                value="${userInfo.getEmail()}"></form:input>
                    <form:errors path="email" class="has-error"></form:errors>
                </div>
            </spring:bind>
            
            <spring:bind path="role_id">
            	<label>Role</label>
                <div class="form-group ${status.error ? 'has-error' : ''}">
                     <form:select path="role" items="${roles}" itemValue="id" itemLabel="name"></form:select>
                </div>
            </spring:bind>
            <input name="password" type= "hidden" id="password" value="${userInfo.getPassword()}">
            <button class="btn btn-lg btn-primary btn-block" type="submit">Cập nhật</button>     
	</form:form>

</body>
</html>
