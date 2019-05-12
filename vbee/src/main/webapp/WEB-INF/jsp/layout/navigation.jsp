<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!-- Navigation-->
  <nav class="navbar navbar-expand-lg navbar-dark bg-dark fixed-top" id="mainNav">
    <a class="navbar-brand" href="/dashboard">Vadi CMS</a>
    <button class="navbar-toggler navbar-toggler-right" type="button" data-toggle="collapse" data-target="#navbarResponsive" aria-controls="navbarResponsive" aria-expanded="false" aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarResponsive">
      <ul class="navbar-nav navbar-sidenav" id="exampleAccordion">
        <li class="nav-item" data-toggle="tooltip" data-placement="right" title="Bảng điều khiển">
          <a class="nav-link" href="/dashboard">
            <i class="fa fa-fw fa-dashboard"></i>
            <span class="nav-link-text">Bảng điều khiển</span>
          </a>
        </li>
       <li class="nav-item" data-toggle="tooltip" data-placement="right" title="Bản đồ & giao thông">
          <a class="nav-link nav-link-collapse collapsed" data-toggle="collapse" href="#collapseMaps" >
            <i class="fa fa-fw fa-map-o"></i>
            <span class="nav-link-text">Bản đồ & giao thông</span>
          </a>
          <ul class="sidenav-second-level collapse" id="collapseMaps">
            <li>
              <a href="${urlMap}">Bản đồ</a>
            </li>
            <li>
              <a href="${urlEditor}">Quản lý cộng tác viên</a>
            </li>
          </ul>
        </li>
        <li class="nav-item" data-toggle="tooltip" data-placement="right" title="Báo nói">
          <a class="nav-link nav-link-collapse collapsed" data-toggle="collapse" href="#collapseNews" >
            <i class="fa fa-fw fa-newspaper-o"></i>
            <span class="nav-link-text">Báo nói</span>
          </a>
          <ul class="sidenav-second-level collapse" id="collapseNews">
            <li>
            	 <a class="nav-link nav-link-collapse collapsed" data-toggle="collapse" href="#collapseThirdNews" aria-expanded="false">
		            <span class="nav-link-text">Quản lý bài báo</span>
          		</a>
            	<ul class="sidenav-third-level collapse" id="collapseThirdNews">
		            <li>
		              <a href="/newspapers/create">Thêm bài báo mới</a>
		            </li>
		            <li>
		              <a href="/newspapers">Danh sách bài báo</a>
		            </li>
	          </ul>
            </li>
            <li>
              <a href="/advertisements">Quảng cáo</a>
            </li>
            <li>
              <a href="/sources">Danh mục báo</a>
            </li>
          </ul>
        </li>
        <li class="nav-item" data-toggle="tooltip" data-placement="right" title="Cấu hình hệ thống">
          <a class="nav-link nav-link-collapse collapsed" data-toggle="collapse" href="#collapseSystems" >
            <i class="fa fa-fw fa-wrench"></i>
            <span class="nav-link-text">Cấu hình hệ thống</span>
          </a>
          <ul class="sidenav-second-level collapse" id="collapseSystems">
            <li>
              <a href="${urlCrawler}">Trình Crawler báo</a>
            </li>
            <li>
              <a href="">Thiết lập bản đồ</a>
            </li>
             <li>
              <a href="">Kết nối text to speech</a>
            </li>
          </ul>
        </li>
        
        <li class="nav-item" data-toggle="tooltip" data-placement="right" title="Người dùng">
          <a class="nav-link nav-link-collapse collapsed" data-toggle="collapse" href="#collapseUsers" >
            <i class="fa fa-fw fa-user-o"></i>
            <span class="nav-link-text">Người dùng</span>
          </a>
          <ul class="sidenav-second-level collapse" id="collapseUsers">
            <li>
              <a href="">Danh sách quản trị viên</a>
            </li>
            <li>
              <a href="">Danh sách khách hàng</a>
            </li>
            <li>
              <a href="">Phân quyền</a>
            </li>
          </ul>
        </li>
      </ul>
      <ul class="navbar-nav sidenav-toggler">
        <li class="nav-item">
          <a class="nav-link text-center" id="sidenavToggler">
            <i class="fa fa-fw fa-angle-left"></i>
          </a>
        </li>
      </ul>
      <ul class="navbar-nav ml-auto">
        <li class="nav-item">
          <a class="nav-link" data-toggle="modal" data-target="#exampleModal">
            <i class="fa fa-fw fa-sign-out"></i>Đăng xuất</a>
        </li>
      </ul>
    </div>
  </nav>

<<%-- form id="logoutForm" method="POST" action="${contextPath}/logout">
	<input type="hidden" name="${_csrf.parameterName}"
		value="${_csrf.token}" />
</form>
<input type="hidden" value="${sessionScope.SPRING_SECURITY_CONTEXT.authentication.principal.authorities}" id="currentRoleUser"> --%>