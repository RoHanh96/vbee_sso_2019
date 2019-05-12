<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!--Modal preview-->
<div id="preview" class="modal fade" role="dialog">
	<div class="modal-dialog modal-lg">
		<!-- Modal content-->
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title">
					Kiểm tra từ chuẩn hóa <span class="title"></span>
				</h4>
				<button type="button" class="close" data-dismiss="modal">&times;</button>
			</div>
			<div class="modal-body">
				<h3 hidden="true" class="notify-preview">Không có từ nào sai
					chính tả !!!</h3>
				<table class="table table-hover">
					<thead>
						<tr>
							<th>#</th>
							<th>Cụm từ</th>
							<th>Cách đọc</th>
						</tr>
						<tr class="parent" style="display: none">
							<td class="doubt" style="display: none"></td>
							<td class="nsw_precise" style="display: none"></td>
							<td class="order"></td>
							<td class="word show-more"></td>
							<td class="expandation edit"></td>
							<td class="expandation-form">
								<div class="form form-inline">
									<div class="form-group">
										<input type="text" class="form-control expandation-text">
									</div>
									<div class="btn btn-primary expandation-save">Lưu</div>
								</div>
							</td>
							<td><i class="fa fa-pencil-square-o pull-right edit"
								aria-hidden="true" title="sửa"></i></td>
							<td class="action-show-more"><i
								class="fa fa-chevron-circle-right pull-right show-more show-more-icon"
								aria-hidden="true" title="xem thêm"></i></td>
							<td>
								<div class="check-complete">
									<button class="btn btn-primary mark-check">Đánh dấu</button>
									<input type="hidden" class="status" value="unchecked" />
									<div class="checked" style="display: none;">
										<span class="fa fa-check text-success">&nbsp; Đã kiểm
											tra </span>
									</div>
								</div>
							</td>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">
					Hủy</button>
				<c:choose>
					<c:when test="${article.numDoubt == '0'}">
						<button class="btn btn-success">
							<span class="fa fa-check">&nbsp; Đã hoàn thành</span>
						</button>
					</c:when>
					<c:otherwise>
						<button class="btn btn-success" id="btn-completed">Hoàn
							thành kiểm tra</button>
					</c:otherwise>
				</c:choose>

				<button type="button" class="btn btn-primary save-preview"
					data-dismiss="modal">Lưu thay đổi</button>
			</div>
		</div>

	</div>
</div>

