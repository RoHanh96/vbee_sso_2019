<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!-- Breadcrumbs-->
<ol class="breadcrumb breadcrumb-top">
	<li class="breadcrumb-item"><a href="/dashboard">Trang chủ</a></li>
	<li class="breadcrumb-item"><a href="/newspapers">Danh sách
			bài báo</a></li>
	<li class="breadcrumb-item active">Tạo mới bài báo</li>
</ol>

<!-- <div class="form-group row">
	<label for="example-text-input" class="col-2 col-form-label">Hoạt
		động</label>
	<div class="col-10">
		<input checked data-toggle="toggle" type="checkbox">
	</div>
</div> -->
<div class="form-group row">
	<label for="example-text-input" class="col-2 col-form-label">Chuyên
		mục</label>
	<div class="col-3">
		<select id="category" class="form-control form-control-sm">
			<c:forEach items="${categories}" var="category">
				<option value="${category.id}">${category.name}</option>
			</c:forEach>
		</select>
	</div>
</div>
<div class="form-group row">
	<label for="example-text-input" class="col-2 col-form-label">Đầu
		báo</label>
	<div class="col-3">
		<select id="website" class="form-control form-control-sm">
			<c:forEach items="${websites}" var="website">
				<option value="Vadi News">Vadi News</option>
				<option value="${website.id}">${website.name}</option>
			</c:forEach>
		</select>
	</div>
</div>
<div class="form-group row">
	<label for="example-text-input" class="col-2 col-form-label">Đường dẫn (URL)</label>
	<div class="col-10">
		<input class="form-control article_url" type="text" maxlength="500">
	</div>
</div>
<div class="form-group row">
	<label for="example-text-input" class="col-2 col-form-label">Ảnh
		đại diện</label>
	<div class="col-10">
		<div class="alert alert-danger article_image_alert_size none-display">
			Ảnh phải có dung lượng nhỏ hơn 1MB.</div>
		<div class="alert alert-danger article_image_alert_empty none-display">
			Bạn chưa chọn ảnh đại diện.</div>
		<input type="hidden" id="article_id" value="${article.id}" /> <img src="${article.picture}" alt="" class="img-rounded img-responsive"
		 id="image-render" style="width: 140px; height: 90px" />
		<div class="btn btn-default btn-image-upload">
			<i class="fa fa-camera">&nbsp Chọn ảnh để upload</i>
		</div>
		<input type="file" accept="image/jpeg,image/pjpeg,image/gif,image/png" class="input-image sc-visuallyhidden" onchange="readURL(this);" />

	</div>
</div>
<div class="form-group row">
	<label for="example-text-input" class="col-2 col-form-label">Tiêu
		đề</label>
	<div class="col-10">
		<div class="alert alert-danger article_title_alert none-display">
			Không được để trống.</div>
		<input class="form-control article_title" type="text" maxlength="500">
	</div>
</div>
<div class="form-group row">
	<label for="example-text-input" class="col-2 col-form-label">Lời
		dẫn</label>
	<div class="col-10">
		<div class="alert alert-danger article_lead_alert none-display">
			Không được để trống.</div>
		<textarea class="form-control article_lead" rows="3"></textarea>
	</div>
</div>
<div class="form-group row">
	<label for="example-text-input" class="col-2 col-form-label">Nội
		dung</label>
	<div class="col-10">
		<div class="alert alert-danger article_content_alert none-display">
			Không được để trống.</div>
		<textarea name="editor" id="editor" rows="10" cols="80">

           </textarea>
	</div>
</div>

<div class="form-group row">
	<label for="example-text-input" class="col-2 col-form-label">Tags</label>
	<div class="col-10">
		<input type="text" data-role="tagsinput" class="article_tags" />
	</div>
</div>

<div class="form-group">
	<button class="btn btn-danger btn-back">Hủy</button>
	<button class="btn btn-success btn-add">Thêm mới và chuẩn hóa</button>
	<div class="btn btn-warning btn-normalize none-display">Kiểm tra
		từ chuẩn hóa</div>
	<button class="btn btn-primary btn-synthesis none-display">Tổng
		hợp</button>
</div>
<script src="/js/ckeditor4-config.js"></script>
<script src="/js/preview.js"></script>
<%@ include file="preview.jsp"%>