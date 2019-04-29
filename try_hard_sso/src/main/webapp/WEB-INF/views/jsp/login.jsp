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
    <h2 class="form-heading">Log in</h2>
    <input name="username" type="text" class="form-control" placeholder="Username"
           autofocus="true"/>
    <input name="password" type="password"  class="form-control" placeholder="Password"/>
    <h2>${error}</h2>
    <br/>
    <button class="btn btn-lg btn-primary btn-block" type="submit">Log In</button>
   <h4 class="text-center"><a href="/registration">Create an account</a></h4>
</form>
</body>
</html>