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
	<title>Đăng ký người dùng</title>
</head>
<body>
	<form:form method="POST" modelAttribute="userRegistrationAdmin" class="form-signin">
	 <h2 class="form-signin-heading">Đăng ký thông tin</h2>
	 
            <spring:bind path="username">
                <div class="form-group ${status.error ? 'has-error' : ''}">
                    <form:input type="text" path="username" class="form-control"
                                autofocus="true" placeholder="Tên đăng nhập"></form:input>
                    <form:errors path="username" class="has-error"></form:errors>
                </div>
            </spring:bind>

            <spring:bind path="password">
                <div class="form-group ${status.error ? 'has-error' : ''}">
                    <form:input type="password" path="password" class="form-control" placeholder="Mật khẩu"></form:input>
                    <form:errors path="password" class="has-error"></form:errors>
                </div>
            </spring:bind>

            <spring:bind path="email">
                <div class="form-group ${status.error ? 'has-error' : ''}">
                    <form:input type="text" path="email" class="form-control"
                                placeholder="Email"></form:input>
                    <form:errors path="email" class="has-error"></form:errors>
                </div>
            </spring:bind>
            
            <spring:bind path="role_id">
            	<label>Role</label>
                <div class="form-group ${status.error ? 'has-error' : ''}">
                     <form:select path="role" items="${roles}" itemValue="id" itemLabel="name"></form:select>
                </div>
            </spring:bind>
            <button class="btn btn-lg btn-primary btn-block" type="submit">Đăng ký</button>     
	</form:form>

</body>
</html>
