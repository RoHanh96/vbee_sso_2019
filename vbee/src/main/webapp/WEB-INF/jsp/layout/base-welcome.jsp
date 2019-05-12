<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>Book CMS</title>

<!-- Bootstrap Core CSS -->
<link href="/vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">


<!-- MetisMenu CSS -->
<link href="/vendor/metisMenu/metisMenu.min.css" rel="stylesheet">
<link href="/vendor/dist/css/sb-admin-2.css" rel="stylesheet">
<!-- Custom styles for this template-->
<!--   <link href="/dist/css/bootstrap.min.css" rel="stylesheet"> -->

<!-- Custom CSS -->
<!-- <link href="/dist/css/sb-admin-2.css" rel="stylesheet"> -->
<link href="/css/welcome.css" rel="stylesheet">
<link href="/css/custom.css" rel="stylesheet">
<link rel="stylesheet"
	href="http://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">

<!-- Morris Charts CSS -->
<link href="/vendor/morrisjs/morris.css" rel="stylesheet">

<!-- Custom Fonts -->
<link href="/vendor/font-awesome/css/font-awesome.min.css"
	rel="stylesheet" type="text/css">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">

<!-- jQuery -->
<script src="/vendor/jquery/jquery.min.js"></script>
<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>

<!-- Bootstrap Core JavaScript -->
<script src="/vendor/bootstrap/js/bootstrap.min.js"></script>

<!-- Core plugin JavaScript-->
  <script src="/vendor/jquery-easing/jquery.easing.min.js"></script>
  
<!-- Metis Menu Plugin JavaScript -->
<script src="/vendor/metisMenu/metisMenu.min.js"></script>

<!-- Morris Charts JavaScript -->
<script src="/vendor/raphael/raphael.min.js"></script>
<!-- <script src="/vendor/morrisjs/morris.min.js"></script>
<script src="/data/morris-data.js"></script> -->

<!-- Custom Theme JavaScript -->
<script src="/vendor/dist/js/sb-admin-2.js"></script>
<script src="/js/bootstrap-notify.js"></script>
<script src="/js/application.js"></script>
</head>
<body>
	<div id="wrapper">
		<tiles:insertAttribute name="body" />
		<tiles:insertAttribute name="footer" />
	</div>
</body>
</html>