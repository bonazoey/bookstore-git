<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@page import="book.BookVO"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>도서관리 시스템</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/common.js" defer></script>
<script src="${pageContext.request.contextPath}/js/jquery-3.6.3.min.js"></script> 
<script src="https://cdn.jsdelivr.net/npm/jquery-validation@1.19.0/dist/jquery.validate.js"></script>
<script src="https://cdn.jsdelivr.net/npm/jquery-validation@1.19.0/dist/additional-methods.js"></script>
<script src="${pageContext.request.contextPath}/js/fileupload_validate.js"></script>
<style>
	.btn_log {
		display: flex;
		justify-content: right;
		gap: 10px;
		margin: 10px;
	}
</style>
</head>
<body>
	<div class="btn_log">
		<c:choose>
			<c:when test="${sessionScope.mvo == null }">
				<button type="button" id="login" name="login" class="btn btn-success" onclick="location.href='login'">로 그 인</button>
				<button type="button" id="register" name="register" class="btn btn-warning" onclick="">회원가입</button>
			</c:when>
			<c:otherwise>
				${sessionScope.mvo.name}님 환영합니다람쥐!
				<button type="button" id="logout" name="logout" class="btn btn-success" onclick="location.href='logout'">로그아웃</button>
			</c:otherwise>
		</c:choose>
	</div>