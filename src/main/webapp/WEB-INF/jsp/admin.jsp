<!DOCTYPE HTML>
<html lang="en">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Expires" content="Sat, 01 Dec 2001 00:00:00 GMT">

<title>News Manager</title>
<link href="Static/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="Static/css/Css.css">
<link href="Static/css/style.css" rel="stylesheet">
<link href="Static/images/icon.png" rel="icon" />

<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js"></script>
<!-- jQuery -->
<script src="Static/js/jquery.min.js"></script>
<!-- Bootstrap Core JavaScript -->
<script src="Static/js/bootstrap.min.js"></script>
<script type="text/javascript" src="Static/ckeditor"></script>

<!--[if lt IE 9]>
		<script src="static/js/html5shiv.min.js"></script>
		<script src="static/js/respond.min.js"></script>
	<![endif]-->
</head>
<body>
		<div class="container header" style="padding:0;">
			<!--<ul>
				<li><a href="#"><i class="fa fa-user-plus"
						aria-hidden="true"></i> Sign Up</a></li>
				<li><a href="#"><i class="fa fa-sign-in" aria-hidden="true"></i>
						Login</a></li>
			</ul>-->
			<img src="Static/images/banner.jpg" style="height: auto; width: 100%;" />

		</div>

	<div class="container" style="padding:0;">
		<nav class="navbar navbar-inverse navbar-static-top" style="margin:0;">

			<!-- Nav -->
			<div class="navbar-header">
				<button type="button" class="navbar-toggle" data-toggle="collapse"
					data-target="#menu">
					<span class="sr-only">Toggle navigation</span> <span
						class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>

			</div>
			<!-- Nav collapse -->
			<div class="collapse navbar-collapse" id="menu">
				<ul class="nav navbar-nav">
					<li class="active"><a class="navbar-brand" href="home"><span
							class="glyphicon glyphicon-send"></span> Tin tức</a></li>
					<li class="nav-item"><a class="nav-link" href="new-task">Thêm
							Tin tức</a></li>

					<li class="nav-item"><a class="nav-link" href="all-tasks">All
							Tin Tức</a></li>
					<li class="nav-item"><a class="nav-link" href="xem"
						target="_blank">Xem tin tức</a></li>
					<li class="nav-item"><a href="/logout" target="_blank"><span
							class="nav-link glyphicon glyphicon-off"></span> Đăng xuất</a></li>
				</ul>

			</div>
			<!-- /.navbar-collapse -->

		</nav>
	</div>

	<c:choose>
		<c:when test="${mode == 'MODE_HOME'}">
			<div class="container" id="homeDiv" style="background:white;">
				<div class="jumbotron text-center" style="background:white;">
					<h1>Welcome to News Manager</h1>
					<h3 id="clock"></h3>
				</div>
			</div>
			<script>
				setInterval(displayTime, 1000);
				function displayTime() {
					var d = new Date();
					document.getElementById("clock").innerHTML = d
							.toLocaleString();
				}
			</script>
		</c:when>
		<c:when test="${mode == 'MODE_TINTUC'}">
			<div class="container text-center " id="tasksDiv" style="background:white;">
				<h3>My News</h3>
				<hr>
				<div class="table-responsive">
					<table class="table table-striped table-bordered text-left">
						<thead>
							<tr>
								<th>Id</th>
								<th>Tên tin tức</th>
								<th>Hình ảnh</th>
								<th>Nội dung</th>
								<th>Ngày đăng</th>
								<th></th>
								<th></th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="tintuc" items="${tintucs}">
								<tr>
									<td>${tintuc.id}</td>
									<td>${tintuc.name}</td>
									<td><a href="${tintuc.hinhanh}" target="_blank">Link
											hình ảnh</a></td>
									<td class="rut-gon-nd">${tintuc.noidung}</td>
									<td><fmt:formatDate pattern="HH:MM dd/MM/yyyy"
											value="${tintuc.thoigian}" /></td>
									<td><a href="update-task?id=${tintuc.id}"><span><i
												class="glyphicon glyphicon-pencil" aria-hidden="true" style="color: black"></i></span></a></td>
									<td><a href="delete-task?id=${tintuc.id}"><span><i
												class="glyphicon glyphicon-trash" aria-hidden="true" style="color: black"></i></span></a></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		</c:when>
		<c:when test="${mode == 'MODE_NEW'|| mode == 'MODE_UPDATE'}">
			<div class="container "  style="background:white;">
				<h3 style="text-align: center;">Add News</h3>
				<hr>
				<div class="row">
					<div class="col-lg-8 offset-2 col-lg-offset-2">

						<form action="save-task" method="post"
							enctype="multipart/form-data" runat="server">
							<div class="card">
								<div class="card-body">
									<div class="form-group">
										<input type="hidden" class="form-control" name="id"
											value="${tintuc.id}">
									</div>
									<div class="form-group">
										<label for="Name">Name:</label> <input type="text"
											class="form-control" id="Name" name="Name"
											value="${tintuc.name}">
									</div>

									<fieldset class="form-group">

										<img style="max-width:100%;height:auto;"
											src="" alt="Image" class="card-img-top"
											id="blah" />
									</fieldset>

									<div class="form-group">
										<label for="hinhanh">Hình ảnh:</label> <input type="file"
											class="form-control" id="hinhanh" name="hinhanh"
											value="${tintuc.hinhanh}">
									</div>
									<div class="form-group">
										<label for="noidung">Nội dung:</label>
										<textarea class="form-control" id="noidung" name="noidung">${tintuc.noidung}</textarea>
									</div>

									<button type="submit" class="btn btn-primary" value="Save">Submit</button>
								</div>
							</div>
						</form>
					</div>
				</div>
			</div>
		</c:when>
	</c:choose>


	<script type="text/javascript">
		$(document).ready(function() {
			$('#blah').hide();
			function readURL(input) {

				if (input.files && input.files[0]) {
					var reader = new FileReader();

					reader.onload = function(e) {
						$('#blah').attr('src', e.target.result);
					}

					reader.readAsDataURL(input.files[0]);
				}
			}

			$("#hinhanh").change(function() {
				readURL(this);
				$('#blah').show();
			});
		});
	</script>


	<script src="ckeditor/ckeditor.js"></script>
	<script src="ckfinder/ckfinder.js"></script>
	<script>
		CKEDITOR
				.replace(
						'noidung',
						{
							filebrowserBrowseUrl : 'ckfinder/ckfinder.html',
							filebrowserImageBrowseUrl : 'ckfinder/ckfinder.html?type=Images',
							filebrowserFlashBrowseUrl : 'ckfinder/ckfinder.html?type=Flash',
							filebrowserUploadUrl : 'ckfinder/core/connector/java/connector.java?command=QuickUpload&type=Files',
							filebrowserImageUploadUrl : 'ckfinder/core/connector/java/connector.java?command=QuickUpload&type=Images',
							filebrowserFlashUploadUrl : 'ckfinder/core/connector/java/connector.java?command=QuickUpload&type=Flash'
						});
	</script>




<div class="container" style="padding-left:0;">
	<footer class="py-5 bg-primary">

		<div class="school" style="color:white;margin:20px;">
			<p>
				4th International Conference on Green Technology and Sustainable
				Development <br> HCMC University of Technology and Education
			</p>
			<p>
				<strong>Add:</strong> No 1 Vo Van Ngan Street, Linh Chieu Ward, Thu
				Duc District, Ho Chi Minh City<br> <strong>Tel:</strong>
				(+84.8) 37 221 223<br> <strong>Ext:</strong> 8161 or 8443<br>
				<strong>E-mail:</strong> gtsd2018@hcmute.edu.vn
			</p>
		</div>

	</footer>
</div>
</body>
</html>