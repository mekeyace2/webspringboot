<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="cr" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>cdn 서버 이미지 리스트 목록</title>
<link rel="stylesheet" href="../css/bootstrap.css">
<script src="../js/bootstrap.js?v=1"></script>
</head>
<body>
<form id="frm" method="post" action="./cdn_delete.do">
<table class="table table-bordered">
<thead>
<tr align="center">
<th><input type="checkbox"></th>
<th>이미지</th>
<th>사용자 파일명</th>
<th>개발자 파일명</th>
<th>API 파일명</th>
</tr>
</thead>
<tbody id="ls">
<cr:forEach var="fdata" items="${all}">
<tr align="center">
<td><input type="checkbox" name="cbox" value="${fdata.AIDX}" class="ck"></td>
<td>
<img src="http://172.30.1.22:8080/cdn/image/${fdata.API_FILE}" style="width:100px;">
</td>
<td>${fdata.ORI_FILE}</td>
<td><a href="${fdata.FILE_URL}" target="_new">${fdata.NEW_FILE}</a></td>
<td>${fdata.API_FILE}</td>
</tr>
</cr:forEach>
</tbody>
</table>
</form>
<button type="button" class="btn btn-danger" id="btn">선택삭제</button>
</body>
<script type="module">
import {cdn_lists} from "./cdn.js";
document.querySelector("#btn").addEventListener("click",function(){
	new cdn_lists().cdn_listdel();
});
</script>
</html>