<%@ include file="/WEB-INF/views/include/include_main.jsp"%>

<table id="currentBalancesHeaderTable" class="pets">
	<thead>
		<tr>
			<th>Current Balances:</th>
		</tr>
	</thead>
</table>

<div style="width: 40%">
	<display:table name="currentBalances" class="display" id="currentBalance">
		<display:column title="Assets" >
			<c:if test="${ currentBalance.totalCash != null }">
				<a href="javascript: setFiltersCurrentBalances('${ currentBalance.accountTypeCashId }');">
					Cash:
				</a>
				<fmt:formatNumber value="${ currentBalance.totalCash }" maxFractionDigits="2" type="currency" /> 
			</c:if>
			<c:if test="${ currentBalance.totalCheckingAccounts != null }">
				<br>
				<a href="javascript: setFiltersCurrentBalances('${ currentBalance.accountTypeCheckingAccountsId }');">
					Checking Accounts:
				</a>
				<fmt:formatNumber value="${ currentBalance.totalCheckingAccounts }" maxFractionDigits="2" type="currency" /> 
			</c:if>
			<c:if test="${ currentBalance.totalSavingsAccounts != null }">
				<br>
				<a href="javascript: setFiltersCurrentBalances('${ currentBalance.accountTypeSavingsAccountsId }');">
					Savings Accounts:
				</a>
				<fmt:formatNumber value="${ currentBalance.totalSavingsAccounts }" maxFractionDigits="2" type="currency" /> 
			</c:if>
			<c:if test="${ currentBalance.totalInvestmentAccounts != null }">
				<br>
				<a href="javascript: setFiltersCurrentBalances('${ currentBalance.accountTypeInvestmentAccountsId }');">
					Investment Accounts: 
				</a>
				<fmt:formatNumber value="${ currentBalance.totalInvestmentAccounts }" maxFractionDigits="2" type="currency" /> 
			</c:if>
			<c:if test="${ currentBalance.totalOtherDepositAccounts != null }">
				<br>
				<a href="javascript: setFiltersCurrentBalances('${ currentBalance.accountTypeOtherDepositAccountsId }');"> 
					Other Deposit Accounts:  
				</a>
				<fmt:formatNumber value="${ currentBalance.totalOtherDepositAccounts }" maxFractionDigits="2" type="currency" /> 
			</c:if>
			<c:if test="${ currentBalance.totalAssets != null }">
				<span class="sectionTitle">
					Total Assets:  
					<fmt:formatNumber value="${ currentBalance.totalAssets }" maxFractionDigits="2" type="currency" /> 
				</span>
			</c:if>
		</display:column>
		<display:column title="Debts" >
			<c:if test="${ currentBalance.totalCreditCards != null }">
				<a href="javascript: setFiltersCurrentBalances('${ currentBalance.accountTypeCreditCardsId }');">
					Credit Cards:   
				</a>
				<fmt:formatNumber value="${ currentBalance.totalCreditCards }" maxFractionDigits="2" type="currency" /> 
			</c:if>
			<c:if test="${ currentBalance.totalLoansAndMortgages != null }">
				<br>
				<a href="javascript: setFiltersCurrentBalances('${ currentBalance.accountTypeLoansAndMortgagesId }');"> 
					Loans and Mortgages:
				</a>
				<fmt:formatNumber value="${ currentBalance.totalLoansAndMortgages }" maxFractionDigits="2" type="currency" /> 
			</c:if>
			<c:if test="${ currentBalance.totalOtherLoanAccounts != null }">
				<br>
				<a href="javascript: setFiltersCurrentBalances('${ currentBalance.accountTypeOtherLoanAccountsId }');">
					Other Loan Accounts: 
				</a>
				<fmt:formatNumber value="${ currentBalance.totalOtherLoanAccounts }" maxFractionDigits="2" type="currency" /> 
			</c:if>
			<c:if test="${ currentBalance.totalDebts != null }">
				<span class="sectionTitle">
					Total Debt:  
					<fmt:formatNumber value="${ currentBalance.totalDebts }" maxFractionDigits="2" type="currency" />
				</span>
			</c:if>
		</display:column>
		<display:column title="Net Worth" >
			<c:if test="${ currentBalance.netWorth != null }">
				<span class="sectionTitle">
					Net Worth:  
					<fmt:formatNumber value="${ currentBalance.netWorth }" maxFractionDigits="2" type="currency" />
				</span>
			</c:if>
		</display:column>
	</display:table>
</div>

<script type="text/javascript">
<!--
	function setFiltersCurrentBalances(accTypeId) {
		filterAccounts(FILTER_SET, accTypeId, '', '');
	}
//-->
</script>
