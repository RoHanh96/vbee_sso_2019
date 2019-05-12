<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
	<script src="/js/sources.js"></script>
<!-- Breadcrumbs-->
<ol class="breadcrumb breadcrumb-top">
	<li class="breadcrumb-item"><a href="/dashboard">Trang chủ</a></li>
	<li class="breadcrumb-item active">Quản lý nguồn báo</li>
</ol>
<div class="sources-row">
	<div class="row">
		<div class="col col-title">Nguồn báo</div>
		<div class="col col-title">Chuyên mục</div>
	</div>
	<div class="row">
		<div class="col col-body">
			<ul id="sortable-website" class="sortable">
					
			</ul>
		</div>
		<div class="col col-body">
			<ul id="sortable-category" class="sortable">
			</ul>
		</div>
	</div>
</div>
<input value="${userServicePath}" type="hidden" id="user_service_path"/>
<input value="${version}" type="hidden" id="version"/>
<div class="form-group">
	<button class="btn btn-danger btn-back">Quay lại</button>
	<button class="btn btn-success btn-update-source">Cập nhật nguồn báo</button>
</div>
