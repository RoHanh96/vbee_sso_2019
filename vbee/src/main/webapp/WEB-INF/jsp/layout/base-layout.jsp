<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">
	<link rel="icon" type="image/png" href="/img/logo-vadi.png" />
<title>Vadi CMS</title>
<!-- Custom fonts for this template-->
<link href="/vendor/font-awesome/css/font-awesome.min.css"
	rel="stylesheet" type="text/css">
<!-- Custom styles for this template-->
<link href="http://s1.store.baonoivn.xyz/assets/css/sb-admin.css" rel="stylesheet">
<!-- <link href="/css/nav-tabs.css" rel="stylesheet"> -->

<!-- Bootstrap Core CSS -->
<link
	href="https://gitcdn.github.io/bootstrap-toggle/2.2.2/css/bootstrap-toggle.min.css"
	rel="stylesheet">
<link href="/vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">
<link href="/vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">
<!-- Core plugin JavaScript-->
<script src="/vendor/jquery/jquery.min.js"></script>
<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<script src="http://momentjs.com/downloads/moment-with-locales.min.js"></script>
<script src="/vendor/jquery-easing/jquery.easing.min.js"></script>
<script src="//cdn.ckeditor.com/4.9.0/full/ckeditor.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.0.0/sockjs.min.js"></script>
<script src="http://s1.store.baonoivn.xyz/assets/js/stomp.min.js"></script>
<script src="http://s1.store.baonoivn.xyz/assets/js/jquery.cookie.js"></script>
<script src="/js/cookie.js"></script>

<script
	src="https://gitcdn.github.io/bootstrap-toggle/2.2.2/js/bootstrap-toggle.min.js"></script>
<script src="http://s1.store.baonoivn.xyz/assets/js/bootstrap-tagsinput.js"></script>
<script src="/js/bootstrap-notify.js"></script>
</head>
<body id="page-top">
	<div id="wrapper">
		<tiles:insertAttribute name="navigation" />
		<div class="content-wrapper">
			<div class="container-fluid">
				<tiles:insertAttribute name="body" />
			</div>
		</div>

		<tiles:insertAttribute name="footer" />
	</div>
</body>
</html>