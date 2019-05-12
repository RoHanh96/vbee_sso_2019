<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script src="/js/article-search.js"></script>
<!-- Breadcrumbs-->
<ol class="breadcrumb breadcrumb-top">
	<li class="breadcrumb-item"><a href="/dashboard">Trang chủ</a></li>
	<li class="breadcrumb-item active">Danh sách bài báo</li>
</ol>
<div class="btn-add-news">
	<a class="a-text-normal" href="/newspapers/create"><button
			class="btn btn-primary">
			<i class="fa fa-plus fa-3"></i> Thêm bài báo mới
		</button></a>
</div>
<!-- Example DataTables Card-->
<div class="card mb-3">
	<div class="card-header">
		<i class="fa fa-table"></i> Dữ liệu báo
	</div>
	<div class="card-body">
		<div class="row">
			<div class="col-sm-12 col-md-1">
				<label>Số lượng </label> <label><select
					name="dataTable_length" id="data_article_length"
					class="form-control form-control-sm"><option value="10">10</option>
						<option value="25">25</option>
						<option value="50">50</option>
						<option value="100">100</option></select> </label>
			</div>
			<div class="col-sm-12 col-md-2">
				<label>Chuyên mục </label> <label><select
					name="dataTable_length" id="category_filter"
					class="form-control form-control-sm">
						<option value="0">Tổng hợp</option>
						<c:forEach items="${categories}" var="category">
							<option value="${category.id}">${category.name}</option>
						</c:forEach>
				</select> </label>
			</div>
			<div class="col-sm-12 col-md-2">
				<label>Nguồn báo </label> <label><select
					name="dataTable_length" id="website_filter"
					class="form-control form-control-sm">
						<option value="0">Tổng hợp</option>
						<option value="Vadi News">Vadi News</option>
						<c:forEach items="${websites}" var="website">
							<option value="${website.name}">${website.name}</option>
						</c:forEach>
				</select> </label>
			</div>
			<div class="col-sm-12 col-md-2">
				<label>Trạng thái </label> <label><select
					name="dataTable_length" id="synthesis_filter"
					class="form-control form-control-sm">
						<option value="0">Tất cả</option>
						<option value="2">Đã tổng hợp</option>
						<option value="1">Đang tổng hợp</option>
						<option value="3">Tổng hợp lỗi</option>
				</select> </label>
			</div>
			<div class="col-sm-12 col-md-2">
				<label>Tìm kiếm </label> <label><input
					id="data_article_search" class="form-control form-control-sm"
					placeholder="Tìm kiếm theo tiêu đề" type="search"></label>
			</div>
			<div class="col-sm-12 col-md-2">
				<label>Sắp xếp theo </label> <label><select
					name="dataTable_length" id="sort"
					class="form-control form-control-sm">
						<option value="<publicDate">Thời điểm xuất bản: Mới nhất</option>
						<option value="<totalChoose">Số lượng tương tác: Nhiều
							nhất</option>
						<option value=">totalChoose">Số lượng tương tác: Ít nhất</option>
						<option value="<listeningRate">Tỷ lệ nghe: Nhiều nhất</option>
						<option value=">listeningRate">Tỷ lệ nghe: Ít nhất</option>
				</select> </label>
			</div>
		</div>
		<div class="table-responsive" id="table">
			<table class="table table-bordered" width="100%" cellspacing="0">
				<thead>
					<tr>
						<th>Id</th>
						<th>Ảnh</th>
						<th>Tiêu đề</th>
						<th>Chuyên mục</th>
						<th>Nguồn báo</th>
						<th style="max-width: 100px">Thời điểm xuất bản</th>
						<th>Trạng thái</th>
						<th style="max-width: 100px">Số lượng tương tác</th>
						<th>Tỷ lệ nghe</th>
						<th>Hành động</th>
					</tr>
					<tr class="article_col_clone none-display">
						<td class="article_id"></td>
						<td class="article_picture"><img alt=""
							class="link_picture img-responsive"
							style="width: 140px; height: 90px;" src=""></td>
						<td class="article_title"></td>
						<td class="article_category"></td>
						<td class="article_source"></td>
						<td class="article_publicDate"></td>
						<td class="article_synthesisType"></td>
						<td class="article_totalChoose"></td>
						<td class="article_listeningRate"></td>
						<td class="article_action"><a class="a-text-normal btn-edit"
							href="">
								<button class="btn btn-success">Sửa</button>
								<span class="fa fa-comment"></span> <span class="num">0</span>
						</a><br> <a class="a-text-normal btn-advertisements" href=""><button
									class="btn btn-primary">Lập lịch quảng cáo</button></a> <br>
							<button class="btn btn-default btn-action-active">Tắt
								hoạt động</button></td>
						<td class="article_id none-display"></td>
						<td class="article_status none-display"></td>
					</tr>
				</thead>
				<tbody>

				</tbody>
			</table>
		</div>
	</div>
	<div class="row">
		<div class="col-lg-12">
			<div class="center" style="display: none" id="no-results">
				Không có dữ liệu để hiển thị</div>
			<div class="clear"></div>
			<!-- /.col-lg-12 -->
		</div>
	</div>
	<!-- /.row -->
	<div class="row">
		<div class="col-lg-12">
			<div id="loadingSpinner"
				class="loadingSpinnerRhs loadingSpinner none-display"
				style="height: 152px; width: 52px; left: 511px;" data-ux-degraded="">&nbsp;</div>
		</div>
		<div class="clear"></div>
		<!-- /.col-lg-12 -->
	</div>
	<!-- /.row -->
	<div class="row">
		<div class="col-lg-12" id="paging" style="display: none">
			<div class="pagnHy">
				<div id="prevPage">
					<span class="pagnPP inline"> <img alt="previous"
						src="/img/previous-page.png" class="imgPrevious img-page"> <span
						class="pagnPrevString">Trang trước</span>
					</span>
				</div>

				<div class="pagns"></div>
				<div id="nextPage">
					<span class="pagnNP inline"> <span class="pagnNextString">Trang
							sau</span> <img alt="next" src="/img/next-page.png"
						class="imgNext img-page">
					</span>
				</div>

			</div>
		</div>
		<div class="clear"></div>
		<span class="pagnLink none-display pagn-clone inline pagnNumber"></span>
		<div class="clear"></div>
		<!-- /.col-lg-12 -->
	</div>
	<!-- /.row -->
	<!--  ./paging -->
</div>

