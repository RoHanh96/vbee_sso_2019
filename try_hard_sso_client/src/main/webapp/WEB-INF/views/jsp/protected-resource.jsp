<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html lang="en">
<head>
    <title>Protected Resource Service</title>
</head>
<body>
    <h2>Hello, ${user.getUsername()}</h2>
    <h2>Email: ${user.getEmail()}</h2>
    <h2>Role: ${user.getRole().getName()}</h2>
    
    <a href="/logout">Logout</a>
</body>
</html>