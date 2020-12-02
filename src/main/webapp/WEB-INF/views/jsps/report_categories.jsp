<%@ include file="/WEB-INF/views/include/include_main.jsp"%>

<p class="body">
<table id="categoriesHeaderTable" class="pets">
	<thead>
		<tr>
			<th>Categories: ${ selectedYear }</th>
		</tr>
	</thead>
</table>

<c:choose>
	<c:when test="${showYearSelection eq true}">
		<span class="sectionTitle">Year:</span> 
		<select id="SELECT_YEAR" onchange="reloadCategoriesReport(this);">
			<option value="">Please Select</option>
			<c:forEach items="${years}" var="year">
	   			<option value="${year}" ${year==selectedYear?'selected="selected"':''}>${year}</option>
	  		</c:forEach>
		</select>
		<c:if test="${showClearSelection eq true}">
			<a href="javascript: reloadCategoriesReport('');">Go to Current Year Report</a>
		</c:if>
	</c:when>
	<c:otherwise>
		Click here to see reports for previous years
		<a href="report_categories.pets">Categories</a>
	</c:otherwise>
</c:choose>

<div id="REPORT_BOTH" style="width: 40%">
	<display:table name="categoryTypes" class="display" id="categoryType" sort="list" defaultsort="1" 
		defaultorder="ascending" requestURI="">
		<display:column title="Category Types" sortable="true">
			<a href="javascript: setFiltersCategories('${ categoryType.refCategoryType.id }', '', '${ selectedYear }');">
				${ categoryType.refCategoryType.description }
			</a>
		</display:column>
		<display:column property="totalRefCategoryType" title="Category Types" format="{0,number,currency}" sortable="true" />
		<display:column title="Categories">
			<c:forEach items="${categoryType.reportCategories}" var="catType">
	   			<a href="javascript: setFiltersCategories('', '${ catType.refCategory.id }', '${ selectedYear }');">
					${ catType.refCategory.description }
				</a>
	   			<fmt:formatNumber value="${ catType.totalRefCategory }" maxFractionDigits="2" type="currency" /> 
	   			<br>
	  		</c:forEach>
		</display:column>
	</display:table>
</div>

<c:if test="${showYearSelection eq true}">
	<div id="REPORT_CATTYPE" style="width: 20%; float: left">
		<display:table name="categoryTypes" class="display" id="categoryType" sort="list" defaultsort="1" 
			defaultorder="ascending" export="true" requestURI="">
			<display:column property="refCategoryType.description" media="csv" title="Category Types" sortable="true" />
			<display:column media="html" title="Category Types" sortable="true">
				<a href="javascript: setFiltersCategories('${ categoryType.refCategoryType.id }', '', '${ selectedYear }');">
					${ categoryType.refCategoryType.description }
				</a>
			</display:column>
			<display:column property="totalRefCategoryType" title="Total" format="{0,number,currency}" sortable="true" />
			<display:setProperty name="export.csv.filename" value="SelectedCatTypeReport.csv" />
		</display:table>
	</div>
	
	<div id="REPORT_CAT" style="width: 20%; float: left">
		<display:table name="categories" class="display" id="category" sort="list" defaultsort="1" 
			defaultorder="ascending" export="true" requestURI="">
			<display:column property="refCategory.description" media="csv" title="Category" sortable="true" />
			<display:column media="html" title="Category" sortable="true">
				<a href="javascript: setFiltersCategories('', '${ category.refCategory.id }', '${ selectedYear }');">
					${ category.refCategory.description }
				</a>
			</display:column>
			<display:column property="totalRefCategory" title="Total" format="{0,number,currency}" sortable="true" />
			<display:setProperty name="export.csv.filename" value="SelectedCategoryReport.csv" />
		</display:table>
	</div>
</c:if>

<div style="clear: both;"></div>

<script type="text/javascript">
<!--
	function setFiltersCategories(catTypeId, catId, selectedYear) {
		filterTransactions(FILTER_SET, '', catTypeId, catId, '', selectedYear+'-01-01', selectedYear+'-12-31', '', '', ' ', ' ', ' ', ' ', '');
	}
//-->
</script>
