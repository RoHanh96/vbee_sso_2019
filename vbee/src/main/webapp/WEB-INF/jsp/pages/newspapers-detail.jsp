<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!-- Breadcrumbs-->
<ol class="breadcrumb breadcrumb-top">
	<li class="breadcrumb-item"><a href="/dashboard">Trang chủ</a></li>
	<li class="breadcrumb-item"><a href="/newspapers">Danh sách
			bài báo</a></li>
	<li class="breadcrumb-item active">Xem chi tiết</li>
</ol>

<%-- <div class="form-group row">
	<label for="example-text-input" class="col-2 col-form-label">Hoạt
		động</label>
	<div class="col-10">
		<c:choose>
			<c:when test="${article.status=='0'}">
				<input checked data-toggle="toggle" type="checkbox">
			</c:when>
			<c:otherwise>
				<input data-toggle="toggle" type="checkbox">
			</c:otherwise>
		</c:choose>
	</div>
</div> --%>
<div class="form-group row">
	<label for="example-text-input" class="col-2 col-form-label">Chuyên
		mục</label>
	<div class="col-3">
		<c:choose>
			<c:when test="${article.type =='Crawler'}">
				<div class="col-10">${article.category.name}</div>
			</c:when>
			<c:otherwise>
				<select id="category" class="form-control form-control-sm">
					<c:forEach items="${categories}" var="category">
						<c:choose>
							<c:when test="${article.category.name == category.name}">
								<option value="${category.id}" selected>${category.name}</option>
							</c:when>
							<c:otherwise>
								<option value="${category.id}">${category.name}</option>
							</c:otherwise>
						</c:choose>
					</c:forEach>
				</select>

			</c:otherwise>
		</c:choose>
	</div>
</div>
<div class="form-group row">
	<label for="example-text-input" class="col-2 col-form-label">Đầu
		báo</label>
	<div class="col-10">
		<label>${article.websiteName}</label>
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
		<input type="hidden" id="article_id" value="${article.id}" /> <img
			src="${article.picture}" alt="" class="img-rounded img-responsive"
			id="image-render" style="width: 140px; height: 90px" />
		<c:if test="${article.type !='Crawler'}">
			<div class="btn btn-default btn-image-upload">
				<i class="fa fa-camera">&nbsp Chọn ảnh để upload</i>
			</div>
		</c:if>

		<form id="submit-upload-image-article">
			<input type="file"
				accept="image/jpeg,image/pjpeg,image/gif,image/png"
				class="input-image sc-visuallyhidden" onchange="readURL(this);" />
			<button class="btn btn-success" id="btn-upload" type="submit">Upload</button>
		</form>
	</div>
</div>
<div class="form-group row">
	<label for="example-text-input" class="col-2 col-form-label">Tiêu
		đề</label>
	<div class="col-10">
		<c:choose>
			<c:when test="${article.type=='Crawler'}">
				<label>${article.title}</label>
			</c:when>
			<c:otherwise>
				<div class="alert alert-danger article_title_alert none-display">
					Không được để trống.</div>
				<input class="form-control article_title" type="text"
					value='${article.title}'>
			</c:otherwise>
		</c:choose>

	</div>
</div>
<div class="form-group row">
	<label for="example-text-input" class="col-2 col-form-label">Lời
		dẫn</label>
	<div class="col-10">
		<c:choose>
			<c:when test="${article.type=='Crawler'}">
				<label>${article.lead}</label>
			</c:when>
			<c:otherwise>
				<div class="alert alert-danger article_lead_alert none-display">
					Không được để trống.</div>
				<textarea class="form-control article_lead" rows="3">${article.lead}</textarea>
			</c:otherwise>
		</c:choose>

	</div>
</div>
<div class="form-group row">
	<label for="example-text-input" class="col-2 col-form-label">Nội
		dung</label>
	<div class="col-10">
		<c:choose>
			<c:when test="${article.type=='Crawler'}">
				<textarea disabled name="content" id="editor"
					class="article_content">${article.content}</textarea>
			</c:when>
			<c:otherwise>
				<div class="alert alert-danger article_content_alert none-display">
					Không được để trống.</div>
				<textarea name="content" id="editor" rows="10" cols="80"
					class="article_content">${article.content}</textarea>
			</c:otherwise>
		</c:choose>
	</div>
</div>
<%-- <c:forEach items="${arti}"></c:forEach> --%>
<div class="form-group row">
	<label for="example-text-input" class="col-2 col-form-label">Tags</label>
	<div class="col-10">
		<c:choose>
			<c:when test="${article.type=='Crawler'}">
				<input type="text" data-role="tagsinput" class="article_tags"
					value="${tags}" disabled />
			</c:when>
			<c:otherwise>
				<input type="text" data-role="tagsinput" class="article_tags"
					value="${tags}" />
			</c:otherwise>
		</c:choose>
	</div>
</div>
<div class="form-group">
	<div class="btn btn-danger btn-back">Quay lại</div>
	<div class="btn btn-primary btn-normalize">Kiểm tra từ chuẩn hóa</div>
	<div class="btn btn-info btn-re-normalize">Chuẩn hóa lại</div>
	<c:choose>
		<c:when test="${article.type =='Crawler'}">
			<div class="btn btn-success btn-re-synthesis">Tổng hợp lại</div>
			<br />
		</c:when>
		<c:otherwise>
			<div class="btn btn-success btn-save-synthesis">Lưu và tổng hợp
				lại</div>
			<br />
		</c:otherwise>
	</c:choose>



</div>
<script src="/js/ckeditor4-config.js"></script>
<script src="/js/preview.js"></script>
<%@ include file="preview.jsp"%>