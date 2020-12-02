<%@ include file="/WEB-INF/views/include/include_main.jsp"%>

<p class="body">
<c:if test="${not empty errMsg}">
	<span class="error">${errMsg}</span>
</c:if>

Filter Category List By:
<select id="filterCategoryType" name="filterCategoryType" onchange="reloadFilter(this);">
	<c:forEach items="${refCategoryTypesList}" var="catTypeVar">
		<option value="${catTypeVar.id}" ${catTypeVar.id==filterCategoryType?'selected="selected"':''}>${catTypeVar.description}</option>
	</c:forEach>
</select>

<c:if test="${filterCategoryType != 'Please Select' && filterCategoryType != ''}">
	<br>
	<span class="error">Filter Currently Applied: [Category Type]
		<a href="javascript: reloadFilter('')">
			Clear Filter and Show All Categories
		</a>
	</span>
</c:if>

<div style="width: 30%">
	<display:table name="refCategoriesList" class="display" pagesize="500" id="category" sort="list" defaultsort="1" 
		defaultorder="ascending" export="true" requestURI="">
		<display:column property="refCategoryType.description" title="Category Type" sortable="true" />
		<display:column property="description" title="Category Name" sortable="true" />
		<display:setProperty name="export.csv.filename" value="SelectedCategories.csv" />
	</display:table>
</div>

<script type="text/javascript">
<!--
	function reloadFilter(selected) {
		if (selected == null || selected == '') {
			window.location = 'categories.pets';
		} else {
			window.location = 'categories.pets?categoryTypeId=' + selected.value;
		}
	}
//-->
</script>