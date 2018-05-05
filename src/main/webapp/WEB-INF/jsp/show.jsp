<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="tintuc" value="${requestScope.tintuc }" />
<%@ include file="header.jsp"%>

<div class="panel panel-success notification-page" style="width:100%;">

	<div class="panel-heading">
		<h3 class="panel-title"><b>CHI TIẾT TIN TỨC</b></h3>
	</div>
	<div class="panel-body">
			<p class="media-heading" style="font-size: 17px"><strong>${tintuc.name }</strong></p>
			<i>Đăng tải ngày ${tintuc.getDateString() } bởi Admin</i>
			<br><br>
			<p style="text-indent: 20px; font-size: 16px">${tintuc.noidung }</p>
			<p><b><a href="${tintuc.hinhanh }" target="_blank">Hình ảnh đính kèm</a></b></p>
	</div>
</div>
</div>
<div class="col-md-4">
	<div class="panel panel-primary">
		<div class="panel-heading">
			<h3 class="panel-title">
				<a href="list-notifications"
					style="color: white; text-decoration: none;"><b>TIN MỚI NHẤT</b></a>
			</h3>
		</div>
		<div class="panel-body media sidebar-list" id="listnews">
		
		<c:forEach var="tintuc" items="${requestScope.tintucs }">
			<div class="media">
				<div class="media-body">
					<div class="media-heading">
						<a href="/show?id=${tintuc.id }">${tintuc.name }</a>
					</div>
					<small><i>Đăng tải vào ${tintuc.getDateString() } bởi Admin</i></small>
				</div>
			</div>
			</c:forEach>
		</div>
	</div>
<%@ include file="footer.jsp"%>

