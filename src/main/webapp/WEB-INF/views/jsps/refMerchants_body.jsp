<%@ include file="/WEB-INF/views/include/include_main.jsp"%>

<p class="body">
<c:if test="${not empty errMsg}">
	<span class="error">${errMsg}</span>
</c:if>

<table style="width: 10%;" id="filtersTable" class="pets">
	<tr>
		<td width="20%" nowrap="nowrap">Merchant First Name: 
			|
			<a href="merchants.pets?merchantNameBeginsWith=ALL">
				ALL
			</a>  
			<c:forEach items="${allMerchantsFilterList}" var="merchant">
				<c:url value="merchants.pets" var="refMerchantsUrl">
					<c:param name="merchantNameBeginsWith" value="${merchant}" />
				</c:url>
				|
				<a href="${refMerchantsUrl}">${merchant}</a>
			</c:forEach>
			|
		</td>
	</tr>
	<tr>
		<td>-------------------------------------------------</td>
	</tr>
	<tr>
		<td width="20%" nowrap="nowrap"> 
			<a href="merchants.pets?merchantNameBeginsWith=0">Only Show Merchants Not Used In Any Transactions</a> 
		</td>
	</tr>
</table>

<c:if test="${ not empty merchantFiltersStr }">
	<span class="error">${ merchantFiltersStr }
		<a href="merchants.pets">
			Clear Filter and Show All Merchants
		</a>
	</span>
</c:if>

<div style="width: 70%">
	<display:table name="allMerchantsList" class="display" pagesize="500" id="merchant" sort="list" defaultsort="1" 
		defaultorder="ascending" export="true" requestURI="">
		<display:column property="description" title="Merchant Name" sortable="true"
						href="merchant.pets" paramId="merchantId" paramProperty="id"/>
		<c:choose>
			<c:when test="${merchant.notEditable eq true}">
				<display:column title="Update Merchant" media="html" sortable="false">
					<input type="text" id="UPDATEMERCHANT_${merchant_rowNum}" size="50" maxlength="100" disabled="disabled" 
							oninput="enableRename(this);"/>
					<input type="button" id="UPDATE_${merchant_rowNum}" value="RENAME" disabled="disabled" 
							onclick="editRefMerchant(this, '${merchant.id}', '${merchant.description}', '${merchant_rowNum}');" />
				</display:column>
				<display:column title="Delete Merchant" media="html" sortable="false">
						Select Checkbox to Confirm Delete:
						<input type="checkbox" id="DELETEMERCHANT_${merchant_rowNum}" disabled="disabled" onclick="enableDelete(this);"/>
						<input type="button" id="DELETE_${merchant_rowNum}" value="DELETE" disabled="disabled"
								onclick="editRefMerchant(this, '${merchant.id}', '${merchant.description}', '${merchant_rowNum}');" />
				</display:column>
			</c:when>
			<c:otherwise>
				<display:column title="Update Merchant" media="html" sortable="false">
					<input type="text" id="UPDATEMERCHANT_${merchant_rowNum}" size="50" maxlength="100" oninput="enableRename(this);"/>
					<input type="button" id="UPDATE_${merchant_rowNum}" value="RENAME" disabled="disabled" 
							onclick="editRefMerchant(this, '${merchant.id}', '${merchant.description}', '${merchant_rowNum}');" />
				</display:column>
				<display:column title="Delete Merchant" media="html" sortable="false">
						Select Checkbox to Confirm Delete:
						<input type="checkbox" id="DELETEMERCHANT_${merchant_rowNum}" onclick="enableDelete(this);"/>
						<input type="button" id="DELETE_${merchant_rowNum}" value="DELETE" disabled="disabled"
								onclick="editRefMerchant(this, '${merchant.id}', '${merchant.description}', '${merchant_rowNum}');" />
				</display:column>
			</c:otherwise>
		</c:choose>
		<display:setProperty name="export.csv.filename" value="SelectedMerchants.csv" />
	</display:table>
</div>

<script type="text/javascript">
<!--
	function enableRename(textbox) {
		var rowNum = textbox.id.split('_')[1];
		document.getElementById('UPDATE_'+rowNum).disabled=false;
	}
	
	function enableDelete(checkbox) {
		var rowNum = checkbox.id.split('_')[1];
		if (checkbox.checked) {
			document.getElementById('DELETE_'+rowNum).disabled=false;
		} else {
			document.getElementById('DELETE_'+rowNum).disabled=true;
		}
	}
//-->	
</script>

