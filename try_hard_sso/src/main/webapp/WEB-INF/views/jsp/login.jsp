<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html lang="en">
<head>
    <title></title>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link href="/resources/css/bootstrap.min.css" rel="stylesheet">
    <link href="/resources/css/common.css" rel="stylesheet">
	<title>Authentication Service</title>
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
   <h4 class="text-center"><a id="registration" >Tạo tài khoản</a></h4>
</form>
</body>
<script>
	var url_string = window.location.href; //window.location.href
	var url = new URL(url_string);
	var c = url.searchParams.get("callbackUrl");
	console.log(c);
	var error = url.searchParams.get("error");
	document.getElementById("callbackUrl").value = c;
	document.getElementById("error").innerHTML = error;
	document.getElementById("registration").href = "/registration?callbackUrl="+c;
</script>
</html>