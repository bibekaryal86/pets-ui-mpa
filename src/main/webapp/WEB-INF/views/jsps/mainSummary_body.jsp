<%@ include file="/WEB-INF/views/include/include_main.jsp"%>

<p class="body">
<form:form method="get" modelAttribute="main">

	<c:if test="${not empty errMsg}">
		<span class="error">${errMsg}</span>
	</c:if>
	
	<div id="NEW_TRANSACTION" class="notes">
		To add a new Transaction, click here:
		<a href="javascript: editTransaction('', '', '');">
			Add Transaction
		</a>
	</div>
	<br>
	<div id="REPORT_ALL" class="notes">
		To view different reports, click here:
		<a href="report_all.pets">
			Reports
		</a>
	</div>

</form:form>
