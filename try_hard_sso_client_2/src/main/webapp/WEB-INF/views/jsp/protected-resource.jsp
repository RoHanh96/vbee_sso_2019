<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html lang="en">
<head>
    <title>Protected Resource Service</title>
</head>
<body>
    <h2>User id: <%= session.getAttribute("userId") %></h2>
    <h2>User name: <%= session.getAttribute("userName") %></h2>
    
    <a href="/logout">Logout</a>
    <a href="/updateInfo">Update</a>
</body>
</html>