<%@ include file="/WEB-INF/views/include/include_main.jsp"%>

<p class="body">
	<div id="NEW_ACCOUNT" class="notes">
		To add a new Account, click here:
		<a href="javascript: editAccount('');"> Add Account </a>
	</div>

	<c:if test="${not empty errMsg}">
		<span class="error">${errMsg}</span>
	</c:if>

	<table style="width: 10%;" id="filtersTable" class="pets">
		<tr class="smallTextBold">
			<td width="20%" nowrap="nowrap">
				Account Type:
				<select id="filterAccountType" name="filterAccountType" onchange="javascript: setFilters(FILTER_SET, this, '', '');">
					<c:forEach items="${refAccountTypesList}" var="accTypeVar">
						<option value="${accTypeVar.id}" ${accTypeVar.id==accountFilters.accountTypeId?'selected="selected"':''}>
							${accTypeVar.description}
						</option>
					</c:forEach>
				</select>
			</td>
			<td width="20%" nowrap="nowrap">
				Bank Name:
				<select id="filterBank" name="filterBank" onchange="javascript: setFilters(FILTER_SET, '', this, '');">
					<c:forEach items="${refBanksList}" var="bankVar">
						<option value="${bankVar.id}" ${bankVar.id==accountFilters.bankId?'selected="selected"':''}>
							${bankVar.description}
						</option>
					</c:forEach>
				</select>
			</td>
			<td width="20%" nowrap="nowrap">
				Account Status:
				<select id="filterAccountStatus" name="filterAccountStatus" onchange="javascript: setFilters(FILTER_SET, '', '', this);">
					<option value="">PLEASE SELECT</option>
					<c:forEach items="${accountStatusList}" var="accStatus">
						<option value="${accStatus}" ${accStatus==accountFilters.status?'selected="selected"':''}>
							${accStatus}
						</option>
					</c:forEach>
				</select>
			</td>
		</tr>
	</table>
	
	<c:if test="${ not empty accountFiltersStr }">
		<span class="error">${ accountFiltersStr }
			<a href="javascript: setFilters(FILTER_RESET, '', '', '');">
				Clear Filter and Show All Accounts
			</a>
		</span>
	</c:if>

	<display:table name="allAccountsList" class="display" pagesize="50" id="account" sort="list" defaultsort="1" defaultorder="ascending"
		export="true" requestURI="" decorator="pets.ui.mpa.decorator.AccountsDecorator">
		<display:column property="refBank.description" title="Bank Name" sortable="true" />
		<display:column property="refAccountType.description" title="Account Type" sortable="true" />
		<display:column property="description" title="Account Name" sortable="true" href="account.pets" paramId="accountId" paramProperty="id" />
		<display:column property="openingBalance" title="Opening Balance" format="{0,number,currency}" sortable="true" />
		<display:column property="currentBalance" title="Current Balance" format="{0,number,currency}" sortable="true" />
		<display:column title=" " media="html" style="width:100px;">
			<input type="button" value=" View/Update " onclick="editAccount('${account.id}');" />
		</display:column>
		<display:setProperty name="export.csv.filename" value="SelectedAccounts.csv" />
	</display:table>

<script type="text/javascript">
<!--
	function setFilters(action, selectedAccType, selectedBank, selectedAccStatus) {
		let fAccType = ''
		let fBank = ''
		let fAccSts = ''
		
		if (action == FILTER_RESET) {
			filterAccounts(action, fAccType, fBank, fAccSts)
		} else if (action == FILTER_SET) {
			if (selectedAccType != '') {
				fAccType = selectedAccType.value
				fBank = document.getElementById("filterBank").value
				fAccSts = document.getElementById("filterAccountStatus").value
			} else if (selectedBank != '') {
				fAccType = document.getElementById("filterAccountType").value
				fBank = selectedBank.value
				fAccSts = document.getElementById("filterAccountStatus").value
			} else if (selectedAccStatus != '') {
				fAccType = document.getElementById("filterAccountType").value
				fBank = document.getElementById("filterBank").value
				fAccSts = selectedAccStatus.value
			}
			
			filterAccounts(action, fAccType, fBank, fAccSts)
		}
	}
//-->
</script>
