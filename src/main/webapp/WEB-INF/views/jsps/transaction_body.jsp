<%@ include file="/WEB-INF/views/include/include_main.jsp" %>

<p class="body">
<form:form method="post" modelAttribute="transactionModel">
	<form:errors path="errMsg" cssClass="error" element="div" />
	<c:if test="${not empty errMsg}">
		<span class="error">${errMsg}</span>
	</c:if>
	
	<table style="width: 10%" id="transactionsInputTable" class="pets">
		<thead>
			<tr>
				<th colspan="3">Transaction Details</th>
			</tr>					
		</thead>
		<tbody>
			<tr>
				<td width="10%" nowrap="nowrap">Transaction Type: <span class="error">*</span>
					<br>
					<form:select path="transaction.refTransactionType.id" onchange="changesForTxnTypeSelection(this, '');">
						<form:options items="${refTransactionTypesList}" itemValue="id" itemLabel="description"/>
					</form:select>
					<br>
					<form:errors path="transaction.refTransactionType.id" cssClass="error" />
				</td>
				<td width="10%" nowrap="nowrap">Category Type: <span class="error">*</span>
					<br>
					<form:select path="transaction.refCategory.refCategoryType.id" 
								onchange="updateCategoryByType(this, '', 'transaction.refCategory.id', 'false');">
						<form:options items="${refCategoryTypesList}" itemValue="id" itemLabel="description"/>
					</form:select>
					<br>
					<form:errors path="transaction.refCategory.refCategoryType.id" cssClass="error" />
				</td>
				<td width="10%" nowrap="nowrap">Category Name: <span class="error">*</span>
					<br>
					<form:select path="transaction.refCategory.id">
						<form:options items="${refCategoriesList}" itemValue="id" itemLabel="description"/>
					</form:select>
					<br>
					<form:errors path="transaction.refCategory.id" cssClass="error" />
				</td>
			</tr>
			<tr>
				<td width="10%" nowrap="nowrap">Account Name : <span class="error">*</span>
					<br>
					<c:choose>
						<c:when test="${transactionModel.transaction.account.status eq 'CLOSED'}">
							${transactionModel.transaction.account.description}
							(Closed)
						</c:when>
						<c:otherwise>
							<form:select path="transaction.account.id">
								<form:options items="${accountsList}" itemValue="id" itemLabel="description"/>
							</form:select>
						</c:otherwise>
					</c:choose>
					<br>
					<form:errors path="transaction.account.id" cssClass="error" />
				</td>
				<td width="10%" nowrap="nowrap">Transfer To :
					<br>
					<c:choose>
						<c:when test="${transactionModel.transaction.account.status eq 'CLOSED' 
										|| transactionModel.transaction.trfAccount.status eq 'CLOSED'}">
							${transactionModel.transaction.trfAccount.description}
							<c:if test="${transactionModel.transaction.trfAccount.status eq 'CLOSED'}">
								(Closed)
							</c:if>
						</c:when>
						<c:otherwise>
							<form:select path="transaction.trfAccount.id" disabled="true">
								<form:options items="${accountsList}" itemValue="id" itemLabel="description"/>
							</form:select>
						</c:otherwise>
					</c:choose>
					<br>
					<form:errors path="transaction.trfAccount.id" cssClass="error" />
				</td>
				<td></td>
			</tr>
			<tr>
				<td width="10%" nowrap="nowrap">Transaction Date : <span class="error">*</span>
					<br>
					<form:input path="transaction.date" size="20" maxLength="10" readonly="readonly" onclick="setDefaultDate();"/>
					<img src="static/images/calendar.jpeg" width="15" height="14" alt="Calendar" border="0" 
																		onclick="showCalendarControl('transaction.date');"/>
					<br>
					<form:errors path="transaction.date" cssClass="error" />
				</td>
				<td width="10%" nowrap="nowrap">Transaction Amount : <span class="error">*</span>
					<br>
					<form:input path="transaction.amount" size="20" maxLength="99"/>
					<br>
					<form:errors path="transaction.amount" cssClass="error" />
				</td>
				<td></td>
			</tr>
			
			<tr>
				<td width="10%" nowrap="nowrap">Merchant Name : <span class="error">*</span>
					<br>
					<form:select path="transaction.refMerchant.id">
						<form:options items="${refMerchantsList}" itemValue="id" itemLabel="description"/>
					</form:select>
					<br>
					<form:errors path="transaction.refMerchant.id" cssClass="error" />
				</td>
				<td colspan="2" width="10%" nowrap="nowrap">
					<span class="notes">OR</span>&nbsp; Enter New Merchant Name :
					<br>
					<form:input path="newMerchant" size="60" maxLength="60"/>
					<br>
					<form:errors path="newMerchant" cssClass="error" />
				</td>
			</tr>
			<tr>
				<td width="10%" nowrap="nowrap">
					Necessary: <span class="error">*</span>
					Yes:
					<form:radiobutton path="transaction.necessary" value="true"/>
					No:
					<form:radiobutton path="transaction.necessary" value="false"/>
					<br>
					<form:errors path="transaction.necessary" cssClass="error" />
				</td>
				<td width="10%" nowrap="nowrap">Regular : <span class="error">*</span>
					Yes:
					<form:radiobutton path="transaction.regular" value="true"/>
					No:
					<form:radiobutton path="transaction.regular" value="false"/>
					<br>
					<form:errors path="transaction.regular" cssClass="error" />
				</td>
				<td></td>
			</tr>
			<tr>	
				<td colspan="3">Transaction Description :
					<br>
					<form:textarea class="textArea" path="transaction.description" maxLength="999" 
							onblur="inputTextToUpper(this);"/>
				</td>
			</tr>
			<tr>
				<td colspan="3">
					<br>
					<c:choose>
						<c:when test="${transactionModel.userAction eq 'ADD'}">
							<input type="button" value="ADD" onclick="submitTransaction(this, '');"/>
							<input type="button" value="CLEAR" onclick="window.location.href='transaction.pets'"/>
						</c:when>
					</c:choose>
					<c:choose>
						<c:when test="${transactionModel.userAction eq 'UPDATE' && transactionModel.transaction.account.status eq 'ACTIVE'}">
							<input id="UPDATE_BUTTON" type="button" value="UPDATE" onclick="submitTransaction(this, '');"/>
							<input id="DELETE_BUTTON1" type="button" value="DELETE" onclick="submitTransaction(this, '');"/>
						</c:when>
					</c:choose>
					<a href="javascript: window.location.href='transactions.pets';">
						[ All Transactions List ]
					</a>
					<c:choose>
						<c:when test="${transactionModel.fromAccountPage eq true}">
							<a href="javascript: window.location.href='account.pets?accountId=${transactionModel.transaction.account.id}'">
								[ Account Details ]
							</a>
							<a href="javascript: window.location.href='accounts.pets';">
								[ All Accounts List ]
							</a>
						</c:when>
						<c:when test="${transactionModel.fromMerchantPage eq true}">
							<a href="javascript: window.location.href='merchant.pets?merchantId=${transactionModel.transaction.refMerchant.id}'">
								[ Merchant Details ]
							</a>
							<a href="javascript: window.location.href='merchants.pets';">
								[ All Merchants List ]
							</a>
						</c:when>
					</c:choose>
				</td>
			</tr>
		</tbody>
	</table>
	
	<div id="CONFIRM_DELETE" style = "display:none" class="error">
		Are you sure you want to delete this Transaction?
		<br>
		This action cannot be undone!!!
		<br>
		<input type="button" id="DELETE_BUTTON2" value=DELETE onclick="submitTransaction(this, 'CONFIRMED');" />
		<input type="button" value="CANCEL" onclick="window.location.reload()"/>
	</div>
	
	<form:hidden path="transaction.id"/>
	<form:hidden path="userAction" />
	<form:hidden path="fromAccountPage" />
	<form:hidden path="fromMerchantPage" />
	<form:hidden path="transaction.account.status" />
	
</form:form>
	
<script type="text/javascript">
<!--
	var refTxnTypeId = '${sessionScope.refTxnTypeId}';
	if (refTxnTypeId == '') {
		refTxnTypeId = document.getElementById('transaction.refTransactionType.id').value;
		
		if (refTxnTypeId != '') {
			changesForTxnTypeSelection('', refTxnTypeId);
		}
	}
	
	var refCatTypeId = document.getElementById('transaction.refCategory.refCategoryType.id').value
	var refCatId = document.getElementById('transaction.refCategory.id').value;
	
	function changesForTxnTypeSelection(txnType, txnTypeIdValue) {
		if (txnTypeIdValue == TRANSACTION_TYPE_ID_TRANSFER 
				|| txnType.value == TRANSACTION_TYPE_ID_TRANSFER) {
			document.getElementById('transaction.trfAccount.id').disabled=false;
			document.getElementById('transaction.refMerchant.id').disabled=true;
			document.getElementById('newMerchant').disabled=true;
			document.getElementById('transaction.refCategory.refCategoryType.id').value = CATEGORY_TYPE_ID_TRANSFER;
			
			if (refCatId == '') {
				updateCategoryByType('', CATEGORY_TYPE_ID_TRANSFER, 'transaction.refCategory.id', 'false');
			}
		} else if (txnTypeIdValue == TRANSACTION_TYPE_ID_INCOME 
				|| txnType.value == TRANSACTION_TYPE_ID_INCOME) {
			document.getElementById('transaction.trfAccount.id').disabled=true;
			document.getElementById('transaction.refMerchant.id').disabled=false;
			document.getElementById('newMerchant').disabled=false;
			document.getElementById('transaction.refCategory.refCategoryType.id').value = CATEGORY_TYPE_ID_INCOME;

			if (refCatId == '') {
				updateCategoryByType('', CATEGORY_TYPE_ID_INCOME, 'transaction.refCategory.id', 'false');
			}
		} else {
			document.getElementById('transaction.trfAccount.id').disabled=true;
			document.getElementById('transaction.refMerchant.id').disabled=false;
			document.getElementById('newMerchant').disabled=false;
			
			if (refCatTypeId == '') {
				document.getElementById('transaction.refCategory.refCategoryType.id').value = '';
				updateCategoryByType('', '', 'transaction.refCategory.id', 'false');
			}
		}
	}
	
	function setDefaultDate() {
		if($('transaction.date').value=='') {
			$('transaction.date').value=formatDate(new Date());
		}
	}
	
	function submitTransaction(button, confirmDelete) {
		$('userAction').value = button.value;
		button.disabled=true;
		
		if (button.value == USER_ACTION_DELETE) {
			if (confirmDelete == '') {
				document.getElementById('CONFIRM_DELETE').style.display  = 'block';
				document.getElementById('UPDATE_BUTTON').disabled = true;
				document.getElementById('DELETE_BUTTON1').disabled = true;
				return false;
			}
		}
		
		var inputAmount = document.getElementById('transaction.amount').value;
		if (isNaN(inputAmount)) {
			alert('Invalid Input for Transaction Amount! Only Numbers and Decimal Allowed! Please Try Again!!!')
			button.disabled = false;
			return;
		} else {
			var outputAmount = inputAmount.replace('-', '');
			$('transaction.amount').value = outputAmount;
		}
		
		$('transactionModel').submit();		
	}
	
//-->	
</script>