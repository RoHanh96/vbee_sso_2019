<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link href="/resources/css/bootstrap.min.css" rel="stylesheet">
    <link href="/resources/css/common.css" rel="stylesheet">
	<title>BK-SSO</title>
</head>
<body>
<form method="POST" action="/login" modelAttribute="userFormLogin" class="form-signin">
    <h2 class="form-heading">Đăng nhập</h2>
    <input name="username" type="text" class="form-control" placeholder="Tên người dùng"
           autofocus="true"/>
    <input name="password" type="password"  class="form-control" placeholder="Mật khẩu"/>
    <input name="callbackUrl" type= "hidden" id="callbackUrl">
    <h4 class="has-error" id="error"></h4>
    <br/>
    <button class="btn btn-lg btn-primary btn-block" type="submit">Đăng nhập</button>
   
</form>
</body>
<script>
	var error = url.searchParams.get("error");
	document.getElementById("error").innerHTML = error;
</script>
</html>