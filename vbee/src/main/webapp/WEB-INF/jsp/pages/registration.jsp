<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<body class="bg-dark">
	<div class="container">
		<div class="card card-register mx-auto mt-5">
			<div class="card-header">Đăng ký tài khoản</div>
			<div class="card-body">
				<form:form method="POST" modelAttribute="userForm"
					class="form-signin">
					<spring:bind path="email">
						<div class="form-group ${status.error ? 'has-error' : ''}">
							<form:input type="text" path="email" class="form-control"
								placeholder="Email" autofocus="true"></form:input>
							<form:errors path="email"></form:errors>
						</div>
					</spring:bind>

					<spring:bind path="password">
						<div class="form-group ${status.error ? 'has-error' : ''}">
							<form:input type="password" path="password" class="form-control"
								placeholder="Mật khẩu"></form:input>
							<form:errors path="password"></form:errors>
						</div>
					</spring:bind>

					<spring:bind path="passwordConfirm">
						<div class="form-group ${status.error ? 'has-error' : ''}">
							<form:input type="password" path="passwordConfirm"
								class="form-control" placeholder="Confirm your password"></form:input>
							<form:errors path="passwordConfirm"></form:errors>
						</div>
					</spring:bind>

					<button class="btn btn-primary btn-block" id="btnRegister" type="submit">Đăng
						ký</button>
				</form:form>
				<div class="text-center">
					<a class="d-block small mt-3" href="/login">Quay lại đăng nhập</a>
					<a class="d-block small" href="/forgot-password">Quên mật khẩu?</a>
				</div>
			</div>
		</div>
	</div>
</body>