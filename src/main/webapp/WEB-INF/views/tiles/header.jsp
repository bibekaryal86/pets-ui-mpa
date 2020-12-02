<%@ include file="/WEB-INF/views/include/include_main.jsp"%>

<table class="header">
	<tbody>
		<tr>
			<td nowrap="nowrap" width="5%"><img src="static/images/pets.gif" id="petsLogo" alt="PETS" border="0" /> 
				<!-- <br> --> 
				<span class="error"> 
					<c:out value="${sessionScope.userFullName}" />
				</span> 
				<span class="notes"> 
					<var id="currentDate"></var>
				</span>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<ul id="nav">
					<li><a href="main.pets">Main Summary</a></li>
					<li><a href="transactions.pets">Transactions</a></li>
					<li><a href="accounts.pets">Accounts</a></li>
					<li><a href="merchants.pets">Merchants</a></li>
					<li><a href="categories.pets">Categories</a></li>
					<li><a href="#">Reports</a>
						<ul>
							<li><a href="report_all.pets">All Current Reports</a></li>
							<li><a href="report_currentbalances.pets">Current Balances</a></li>
							<li><a href="report_cashflows.pets">Cash Flows</a></li>
							<li><a href="report_categories.pets">Categories</a></li>
						</ul>
					</li>
					<li><a href="import.pets">Import</a></li>
					<li><a href="logoff.pets">Log Off</a></li>
				</ul>
			</td>
		</tr>
	</tbody>
</table>
