<%@ include file="/WEB-INF/views/include/include_main.jsp" %>

<p class="body">

<c:if test="${not empty errMsg}">
	<span class="error">${errMsg}</span>
</c:if>

<div id="NEW_TRANSACTION" class="notes">
	To add a new Transaction, click here:
	<a href="javascript: editTransaction('', '', '');">
		Add Transaction
	</a>
</div>

<table style="width: 10%;" id="filtersTable" class="pets">
	<tr class="smallTextBold">
		<td width="20%" nowrap="nowrap">
			Transaction Type
			<select id="filterTxnType" name="filterTxnType" onchange="setFilters(FILTER_SET, this);">
		    	<c:forEach items="${refTransactionTypesList}" var="txnTypeVar">
					<option value="${txnTypeVar.id}" ${txnTypeVar.id==transactionFilters.transactionTypeId?'selected="selected"':''}>
						${txnTypeVar.description}
					</option>
				</c:forEach>
		    </select>
	    </td>
		<td width="20%" nowrap="nowrap">
			Category Type
			<select id="filterCategoryType" name="filterCategoryType" onchange="setFilters(FILTER_SET, this);">
		    	<c:forEach items="${refCategoryTypesList}" var="catTypeVar">
					<option value="${catTypeVar.id}" ${catTypeVar.id==transactionFilters.categoryTypeId?'selected="selected"':''}>
						${catTypeVar.description}
					</option>
				</c:forEach>
		    </select>
	    </td>
	    <td width="20%" nowrap="nowrap">
	    	Category Name
		    <select id="filterCategory" name="filterCategory" onchange="setFilters(FILTER_SET, this);">
		    	<c:forEach items="${refCategoriesList}" var="catVar">
					<option value="${catVar.id}" ${catVar.id==transactionFilters.categoryId?'selected="selected"':''}>
						${catVar.description}
					</option>
				</c:forEach>
		    </select>
	    </td>
	</tr>
	<tr class="smallTextBold">
		<td width="20%" nowrap="nowrap">
			Account Name:
			<select id="filterUserAccount" name="filterUserAccount" onchange="setFilters(FILTER_SET, this);">
		    	<c:forEach items="${accountsList}" var="userAccVar">
					<option value="${userAccVar.id}" ${userAccVar.id==transactionFilters.accountId?'selected="selected"':''}>
						${userAccVar.description}
					</option>
				</c:forEach>
		    </select>
		</td>
		<td width="20%" nowrap="nowrap">
			Txn Amt: <input id="filterTransactionAmountFrom" name="filterTransactionAmountFrom" size="7" maxlength="10"
					value="${transactionFilters.amountFrom}" onblur="setFilters(FILTER_SET, this);" />
   			TO <input id="filterTransactionAmountTo" name="filterTransactionAmountTo" size="7" maxlength="10"
					value="${transactionFilters.amountTo}" onblur="setFilters(FILTER_SET, this);" />
		</td>
		<td width="20%" nowrap="nowrap">
			Merchant Name:
	    	<select id="filterTransactionMerchant" name="filterTransactionMerchant" onchange="setFilters(FILTER_SET, this);">
		    	<c:forEach items="${refMerchantsList}" var="merVar">
					<option value="${merVar.id}" ${merVar.id==transactionFilters.merchantId?'selected="selected"':''}>
						${merVar.description}
					</option>
				</c:forEach>
		    </select>
		</td>
	</tr>
	<tr class="smallTextBold">
		<td width="20%" nowrap="nowrap" colspan="2">
			Txn Date: 
			<input id="filterTransactionDateFrom" name="filterTransactionDateFrom" value="${transactionFilters.dateFrom}" 
						size="7" maxlength="10" onblur="setFilters(FILTER_SET, this);" />
    		TO
    		<input id="filterTransactionDateTo" name="filterTransactionDateTo" value="${transactionFilters.dateTo}" 
    					size="7" maxlength="10"  onblur="setFilters(FILTER_SET, this);"/>
   			<a href="javascript: setFilterDate('year', '');">
   				Start of the Year
			</a>
   			|
   			<a href="javascript: setFilterDate('', 'month');">
   				Start of the Month
  			</a>
			<br>
		</td>
		<td width="20%" nowrap="nowrap">
			Necessary:
			Yes:
			<input type="checkbox" id="filterNecessaryTransactionYes" value="true" ${transactionFilters.necessary==true?'checked="checked"':''}
					onclick="setFilters(FILTER_SET, this);">
			No:
			<input type="checkbox" id="filterNecessaryTransactionNo" value="true" ${transactionFilters.necessary==false?'checked="checked"':''}
					onclick="setFilters(FILTER_SET, this);">
			Regular:
			Yes:
			<input type="checkbox" id="filterRegularTransactionYes" value="true" ${transactionFilters.regular==true?'checked="checked"':''}
					onclick="setFilters(FILTER_SET, this);">
			No:
			<input type="checkbox" id="filterRegularTransactionNo" value="true" ${transactionFilters.regular==false?'checked="checked"':''}
					onclick="setFilters(FILTER_SET, this);">
		</td>
	</tr>
</table>

<c:if test="${ not empty transactionFiltersStr }">
	<span class="error">${ transactionFiltersStr }
		<a href="javascript: setFilters(FILTER_RESET, '');">
			Clear Filter and Show All Transactions
		</a>
	</span>
</c:if>

<jsp:include page="transactions_list.jsp" />
	
<script type="text/javascript">
<!--
	function setFilterDate(year, month) {
		document.getElementById('filterTransactionDateFrom').value = getBeginningDate(year, month);
		setFilters(FILTER_SET, document.getElementById('filterTransactionDateFrom'))
	}
	
	function setFiltersFromTable(elementId, elementValue) {
		document.getElementById(elementId).value = elementValue
		setFilters(FILTER_SET, document.getElementById(elementId))
	}
	
	function setFilters(action, element) {
		if (action == FILTER_RESET) {
			filterTransactions(action, '', '', '', '', '', '', '', '', '', '', '', '', '')
		} else {
			let fTxnType = document.getElementById('filterTxnType').value
			let fCatType = document.getElementById('filterCategoryType').value
			let fCat = document.getElementById('filterCategory').value
			let fAcc = document.getElementById('filterUserAccount').value
			let fTxnMer = document.getElementById('filterTransactionMerchant').value
			let fTxnDateF = document.getElementById('filterTransactionDateFrom').value
			let fTxnDateT = document.getElementById('filterTransactionDateTo').value
			let fTxnAmtF = document.getElementById('filterTransactionAmountFrom').value
			let fTxnAmtT = document.getElementById('filterTransactionAmountTo').value
			let fNTxnY = document.getElementById('filterNecessaryTransactionYes').checked
			let fNTxnN = document.getElementById('filterNecessaryTransactionNo').checked
			let fRTxnY = document.getElementById('filterRegularTransactionYes').checked
			let fRTxnN = document.getElementById('filterRegularTransactionNo').checked
			
			if (element.id == 'filterTxnType') {
				fTxnType = element.value
			} else if (element.id == 'filterCategoryType') {
				fCatType = element.value
			} else if (element.id == 'filterCategory') {
				fCat = element.value
			} else if (element.id == 'filterUserAccount') {
				fAcc = element.value
			} else if (element.id == 'filterCategoryType') {
				fCatType = element.value
			} else if (element.id == 'filterTransactionMerchant') {
				fTxnMer = element.value
			} else if (element.id == 'filterTransactionDateFrom') {
				fTxnDateF = element.value
			} else if (element.id == 'filterTransactionDateTo') {
				fTxnDateT = element.value
			} else if (element.id == 'filterTransactionAmountFrom') {
				fTxnAmtF = element.value
			} else if (element.id == 'filterTransactionAmountTo') {
				fTxnAmtT = element.value
			} else if (element.id == 'filterNecessaryTransactionYes') {
				fNTxnY = element.value
				fNTxnN = 'false'
			} else if (element.id == 'filterNecessaryTransactionNo') {
				fNTxnY = 'false'
				fNTxnN = element.value
			} else if (element.id == 'filterRegularTransactionYes') {
				fRTxnY = element.value
				fRTxnN = 'false'
			} else if (element.id == 'filterRegularTransactionNo') {
				fRTxnY = 'false'
				fRTxnN = element.value
			}
			
			filterTransactions(action, fTxnType, fCatType, fCat, fAcc, fTxnDateF, fTxnDateT, fTxnAmtF, 
					fTxnAmtT, fNTxnY, fNTxnN, fRTxnY, fRTxnN, fTxnMer);
		}
	}
//-->	
</script>
