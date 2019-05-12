<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<link
	href='http://s1.store.baonoivn.xyz/assets/css/fullcalendar.min.css'
	rel='stylesheet' />
<link
	href='http://s1.store.baonoivn.xyz/assets/css/fullcalendar.print.min.css' />
<script src="http://s1.store.baonoivn.xyz/assets/js/fullcalendar.min.js"></script>
<script src="http://s1.store.baonoivn.xyz/assets/js/vi.js"></script>

<!-- Breadcrumbs-->
<ol class="breadcrumb breadcrumb-top">
	<li class="breadcrumb-item"><a href="/dashboard">Trang chủ</a></li>
	<li class="breadcrumb-item active">Quản lý quảng cáo</a></li>
</ol>
<input id="articleId" type="hidden" value="${article.id}" />
<div class="form-group row">
	<label for="example-text-input" class="col-2 col-form-label">Tìm
		kiếm bài báo</label>
	<div class="col-6">
		<div id="search_panel">
			<div class="input-group add-on">
				<input class="form-control" placeholder="Tìm kiếm" id="search"
					type="text">
			</div>
			<div class="dropdown-search"></div>
		</div>
	</div>
</div>

<div class="form-group row title_panel_article none-display">
	<label for="example-text-input" class="col-2 col-form-label">Tiêu
		đề báo</label>
	<div class="col-6">
		<strong><label class="article_title">${article.title}</label></strong>
	</div>
</div>

<div class="form-group row source_panel_article none-display">
	<label for="example-text-input" class="col-2 col-form-label">Nguồn
		báo</label>
	<div class="col-6">
		<strong><label class="article_source">${article.websiteName}</label></strong>
	</div>
</div>

<div class="form-group row category_panel_article">
	<label for="example-text-input" class="col-2 col-form-label">Chuyên
		mục</label>
	<div class="col-3">
		<select id="category_advertisements"
			class="form-control form-control-sm">
			<option value="0">Tổng hợp</option>
			<c:forEach items="${categories}" var="category">
				<option value="${category.id}">${category.name}</option>
			</c:forEach>
		</select>
	</div>
</div>

<div class="card mb-3">
	<div class="card-header">
		<i class="fa fa-calendar" aria-hidden="true"></i> Lập lịch quảng cáo
	</div>
	<div class="card-body">
		<div id='calendar'></div>
	</div>
</div>

<div class="form-group breadcrumb-top btn-schedule none-display">
	<button class="btn btn-danger btn-back">Quay lại</button>
	<button class="btn btn-success" id="btn-create-schedule">Lưu
		lịch mới</button>
</div>
<script src="/js/fullcalendar.js"></script>
<div id="fullCalModal" class="modal fade">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<h5 id="modalTitle" class="modal-title"></h5>
				<button type="button" class="close" data-dismiss="modal">
					<span aria-hidden="true">×</span> <span class="sr-only"></span>
				</button>
			</div>
			<div id="modalBody" class="modal-body">
				<div id="startTime"></div>
				<div id="endTime"></div>
				<div id="position"></div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">Hủy
					bỏ</button>
				<button class="btn btn-primary" id="remove-event"
					data-dismiss="modal">Xóa sự kiện</button>
			</div>
		</div>
	</div>
</div>
<div class="dropdown-article-clone">
	<a class="a_article_search">
		<div class="row">
			<div class="col-md-2 inline">
				<img alt="" src="" class="article_search_img inl"
					style="width: 50px; height: 50px" />
			</div>
			<div class="col-md-9 inline">
				<span class="article_search_title"></span>
			</div>
			<div class="clear"></div>
			<input class="article_search_id" type="hidden" /> <input
				class="article_search_source" type="hidden" />
		</div>
	</a>
</div>