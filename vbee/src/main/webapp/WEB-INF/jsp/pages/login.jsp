<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<body class="bg-dark">
	<div class="container">
		<div class="card card-login mx-auto mt-5">
			<div class="card-header">Đăng nhập vào hệ thống Quản lý tổng
				hợp sách VBEE</div>
			<div class="card-body ${error != null ? 'has-error' : ''}">
				<form action="login" method="POST">
					<span class="error-message">${message}</span>
					<div class="form-group">
						<label for="inputEmail">Email: </label> <input
							name="username" type="email" class="form-control"
							placeholder="Email" autofocus="true" />
					</div>
					<div class="form-group">
						<label for="inputPassword">Mật khẩu: </label> <input
							name="password" type="password" class="form-control"
							placeholder="Mật khẩu" />
					</div>
					<span class="error-message">${error}</span> <input type="hidden"
						name="${_csrf.parameterName}" value="${_csrf.token}" />
					<!-- <div class="form-group">
						<div class="form-check">
							<label class="form-check-label"> <input
								class="form-check-input" type="checkbox" id="rememberPassword">
								Nhớ mật khẩu
							</label>
						</div>
					</div> -->
					<button class="btn btn-primary btn-block" type="submit"
						id="btnLogin">Đăng nhập</button>
				</form>
				<!-- <div class="text-center">
					<a class="d-block small mt-3" href="/registration">Đăng ký tài
						khoản</a> <span style="display: block; height: 5px;"></span> <a
						class="d-block small" href="/forgot-password">Quên tài khoản?</a>
				</div> -->
			</div>
		</div>
	</div>
</body>