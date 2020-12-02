<%@ include file="/WEB-INF/views/include/include_main.jsp"%>

<p class="body">
<form:form method="post" modelAttribute="accountModel">
	<form:errors path="errMsg" cssClass="error" element="div" />
	<c:if test="${not empty errMsg}">
		<span class="error">${errMsg}</span>
	</c:if>
	
	<c:if test="${(accountModel.userAction eq 'UPDATE' || accountModel.userAction eq 'DELETE') 
					&& accountModel.account.status eq 'ACTIVE'}">
		<div id="NEW_TRANSACTION" class="notes">
			To add a new Transaction, click here:
			<a href="javascript: editTransaction('', '${ accountModel.account.id }', '');">
				Add Transaction
			</a>
		</div>
	</c:if>
	
	<table style="width: 25%" id="accountsInputTable" class="pets">
		<thead>
			<tr>
				<th colspan="2">Account Details</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td nowrap="nowrap">Account Type : <span class="error">*</span>
					<br> 
					<form:select path="account.refAccountType.id">
						<form:options items="${refAccountTypesList}" itemValue="id" itemLabel="description" />
					</form:select> 
					<br> 
					<form:errors path="account.refAccountType.id" cssClass="error" />
				</td>
				<td nowrap="nowrap">Bank Name : <span class="error">*</span> 
					<br>
					<form:select path="account.refBank.id">
						<form:options items="${refBanksList}" itemValue="id" itemLabel="description" />
					</form:select> 
					<br> 
					<form:errors path="account.refBank.id" cssClass="error" />
				</td>

			</tr>
			<tr>
				<td nowrap="nowrap">Account Status : <span class="error">*</span>
					<br> 
					<form:select path="account.status">
						<form:option value="" label="PLEASE SELECT" />
						<form:options items="${accountStatusList}" />
					</form:select> 
					<c:choose>
						<c:when test="${accountModel.account.status eq 'CLOSED'}">
							<input id="REOPEN_BUTTON" type="button" value="REOPEN" onclick="submitAccount(this, '');" />
						</c:when>
					</c:choose>
					<br> 
					<form:errors path="account.status" cssClass="error" />
				</td>
				<td nowrap="nowrap">
				</td>
			</tr>
			<tr>
				<td nowrap="nowrap">Opening Balance : <span class="error">*</span>
					<br> 
					<c:out value="$ " /> 
					<form:input path="account.openingBalance" size="8" maxlength="10" /> 
					<br>
					<form:errors path="account.openingBalance" cssClass="error" />
				</td>
				<td width="10%" nowrap="nowrap">Current Balance : <span class="error">*</span> 
					<br> 
					<c:out value="$ " /> 
					<form:input path="account.currentBalance" size="8" maxlength="10" disabled="true" /> 
				</td>
			</tr>
			<tr>
				<td colspan="2" nowrap="nowrap">Account Name : <span class="error">*</span> 
					<br> 
					<form:input path="account.description" size="50" maxlength="99" /> 
					<br>
					<form:errors path="account.description" cssClass="error" />
				</td>
			</tr>
			<tr>
				<td colspan="2"><br> 
					<c:choose>
						<c:when test="${accountModel.userAction eq 'ADD'}">
							<input type="button" value="ADD" onclick="submitAccount(this, '');" />
							<input type="button" value="CLEAR" onclick="window.location.href='account.pets'"/>
						</c:when>
					</c:choose> 
					<c:choose>
						<c:when test="${(accountModel.userAction eq 'UPDATE' || accountModel.userAction eq 'DELETE') 
										&& accountModel.account.status eq 'ACTIVE'}">
								<input id="UPDATE_BUTTON" type="button" value="UPDATE" onclick="submitAccount(this, '');" />
								<input id="DELETE_BUTTON1" type="button" value="DELETE" onclick="submitAccount(this, '');" />
						</c:when>
					</c:choose>
					<a href="javascript: window.location.href='accounts.pets'">
						[ All Accounts List ]
					</a>
					<a href="javascript: window.location.href='transactions.pets'">
						[ All Transactions List ]
					</a>
				</td>
			</tr>
		</tbody>
	</table>
	
	<div id="CONFIRM_DELETE" style = "display:none" class="error">
		Are you sure you want to delete this Account?
		<br>
		This will also delete all transactions for this account!!
		<br>
		This action cannot be undone!!!
		<br>
		<input type="button" id="DELETE_BUTTON2" value=DELETE onclick="submitAccount(this, 'CONFIRMED');" />
		<input type="button" value="CANCEL" onclick="window.location.reload()"/>
	</div>
	
	<jsp:include page="transactions_list.jsp" />

	<form:hidden path="userAction" />
	<form:hidden path="account.id" />

</form:form>

<script type="text/javascript">
<!--
	changeAccountBalances();
	
	function changeAccountBalances() {
		var accountTypeId = document.getElementById('account.refAccountType.id').value;
		var openingBalance = document.getElementById('account.openingBalance').value;
		var currentBalance = document.getElementById('account.currentBalance').value;
		
		if (ACCOUNT_TYPES_LOAN_ACCOUNTS.includes(accountTypeId)) {
			var newOpeningBalance = openingBalance * -1;
			var newCurrentBalance = currentBalance * -1;
			
			document.getElementById('account.openingBalance').value = newOpeningBalance;
			document.getElementById('account.currentBalance').value = newCurrentBalance;
		}
	}

	function submitAccount(button, confirmDelete) {
		$('userAction').value = button.value;
		button.disabled = true;

		if (button.value == USER_ACTION_DELETE) {
			if (confirmDelete == '') {
				document.getElementById('CONFIRM_DELETE').style.display  = 'block';
				document.getElementById('UPDATE_BUTTON').disabled = true;
				document.getElementById('DELETE_BUTTON1').disabled = true;
				return false;
			}
		} else if (button.value == USER_ACTION_REOPEN) {
			$('userAction').value = USER_ACTION_UPDATE;
			$('account.status').value = ACCOUNT_STATUS_ACTIVE;
		}

		var inputOpeningBalance = document.getElementById('account.openingBalance').value;
		if (isNaN(inputOpeningBalance)) {
			alert('Invalid Input for Opening Balance! Only Numbers and Decimal Allowed! Please Try Again!!!')
			button.disabled = false;
			return;
		} else {
			var outputOpeningBalance = inputOpeningBalance.replace('-', '');
			$('account.openingBalance').value = outputOpeningBalance;
		}

		$('accountModel').submit();
	}
//-->
</script>
