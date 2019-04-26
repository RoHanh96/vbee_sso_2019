<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html lang="en">
<head>
    <title>Authentication Service</title>
</head>
<body>
<form method="POST" action="/login">
	<h2>${error}</h2>
    <h2>Log in</h2>
    <input name="username" type="text" placeholder="Username"
           autofocus="true"/>
    <input name="password" type="password" placeholder="Password"/>
    
    <br/>
    <button type="submit">Log In</button>
</form>
</body>
</html>