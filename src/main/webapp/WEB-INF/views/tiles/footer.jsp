<%@ include file="/WEB-INF/views/include/include_main.jsp"%>

<br>
<br>
<div class="smallText" align=center>
	<c:out value="${sessionScope.petsCopyright}" />
</div>
<div class="footer" align=center>
	build
	<c:out value="${sessionScope.petsVersion}" />
</div>
