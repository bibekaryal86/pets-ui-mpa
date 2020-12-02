<%@ include file="/WEB-INF/views/include/include_main.jsp"%>

<p class="body">
<table id="cashFlowsHeaderTable" class="pets">
	<thead>
		<tr>
			<th>Cash Flows: ${ selectedYear }</th>
		</tr>
	</thead>
</table>

<c:choose>
	<c:when test="${showYearSelection eq true}">
		<span class="sectionTitle">Year:</span> 
		<select id="SELECT_YEAR" onchange="reloadCashFlowsReport(this);">
			<option value="">Please Select</option>
			<c:forEach items="${years}" var="year">
	   			<option value="${year}" ${year==selectedYear?'selected="selected"':''}>${year}</option>
	  		</c:forEach>
		</select>
		<c:if test="${showClearSelection eq true}">
			<a href="javascript: reloadCashFlowsReport('');">Go to Current Year Report</a>
		</c:if>
	</c:when>
	<c:otherwise>
		Click here to see reports for previous years
		<a href="report_cashflows.pets">Cash Flows Report</a>
	</c:otherwise>
</c:choose>

<div style="width: 40%">
	<display:table name="cashFlows" class="display" id="cashFlow" sort="list" defaultsort="2" 
		defaultorder="ascending" export="${showYearSelection}" requestURI="">
		<display:column property="monthToSort" media="html" class="hidden" headerClass="hidden"/>
		<display:column property="month" media="csv" title="Month" sortable="true" sortProperty="monthToSort"/>
		<display:column media="html" title="Month" sortable="true" sortProperty="monthToSort">
			<a href="javascript: setFiltersCashFlows('${ cashFlow.monthBeginDate }', '${ cashFlow.monthEndDate }');">
				${ cashFlow.month }
			</a>
		</display:column>
		<display:column property="totalIncomes" title="Incomes" format="{0,number,currency}" sortable="true"/>
		<display:column property="totalExpenses" title="Expenses" format="{0,number,currency}" sortable="true"/>
		<display:column property="netSavings" title="Gain/Loss" format="{0,number,currency}" sortable="true"/>
		<display:setProperty name="export.csv.filename" value="SelectedCashFlowsReport.csv" />
	</display:table>
</div>

<script type="text/javascript">
<!--
	function setFiltersCashFlows(beginDate, endDate) {
		filterTransactions(FILTER_SET, '', '', '', '', beginDate, endDate, '', '', ' ', ' ', ' ', ' ', '');
	}
//-->
</script>
