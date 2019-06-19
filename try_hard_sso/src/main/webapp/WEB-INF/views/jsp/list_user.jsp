<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<link href="/resources/css/bootstrap.min.css" rel="stylesheet">
		<link href="/resources/css/bootstrap.css" rel="stylesheet">
	    <link href="/resources/css/common.css" rel="stylesheet">
		<title>Users List</title>
	</head>
	<body>
		<div class="generic-container">
			<div class="authbar">
				<span>Xin chào admin <strong>${userLogined.getUsername()}</strong>.</span> 
				<span class="floatRight">
					<a href="<c:url value='/newuser' />" class="btn btn-success custom-width">Thêm người dùng</a>
					<a href="<c:url value="/logout" />" class="btn btn-danger custom-width">Đăng xuất</a>
				</span>
			</div>
			<div class="panel panel-default">
				  <!-- Default panel contents -->
			  	<div class="panel-heading"><span class="lead">Danh sách người dùng </span></div>
				<table class="table table-hover">
		    		<thead>
			      		<tr>
			      			<th>ID</th>
					        <th>Username</th>
					        <th>Email</th>
					        <th>Role</th>
						</tr>
			    	</thead>
		    		<tbody>
					<c:forEach items="${listUser}" var="user">
						<tr>
							<td>${user.getId()}</td>
							<td>${user.getUsername()}</td>
							<td>${user.getEmail()}</td>
							<td>${user.getRole().getName()}</td>
								<td><a href="<c:url value='/edit-user-${user.getId()}' />" class="btn btn-success custom-width">Sửa</a></td>
								<td><a href="<c:url value='/delete-user-${user.getId()}' />" class="btn btn-danger custom-width" onclick="return confirm('Bạn chắc chắn muốn xóa người dùng này chứ?')">Xóa</a></td>
						</tr>
					</c:forEach>
		    		</tbody>
		    	</table>
		    	</div>
		    </div>
	</body>
</html>