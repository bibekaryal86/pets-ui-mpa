<%@ include file="/WEB-INF/views/include/include_main.jsp"%>

<p class="body">
<c:if test="${not empty errMsg}">
	<span class="error">${errMsg}</span>
	<br>
</c:if>

<c:choose>
	<c:when test="${showAllReports eq true}">
		<jsp:include page="report_current_balances.jsp" />
		<jsp:include page="report_cash_flows.jsp" />
		<jsp:include page="report_categories.jsp" />
	</c:when>
	<c:when test="${showCurrentBalancesReport eq true}">
		<jsp:include page="report_current_balances.jsp" />
	</c:when>
	<c:when test="${showCashFlowsReport eq true}">
		<jsp:include page="report_cash_flows.jsp" />
	</c:when>
	<c:when test="${showCategoriesReport eq true}">
		<jsp:include page="report_categories.jsp" />
	</c:when>
</c:choose>
