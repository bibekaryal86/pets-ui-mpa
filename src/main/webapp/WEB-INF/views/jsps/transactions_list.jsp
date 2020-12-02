<%@ include file="/WEB-INF/views/include/include_main.jsp"%>

<display:table name="allTransactionsList" class="display" pagesize="100" id="transaction" sort="list" defaultsort="1" defaultorder="descending"
	export="true" requestURI="" decorator="pets.ui.mpa.decorator.TransactionsDecorator">
	<display:column property="date" title="Date" sortable="true" />
	<display:column property="refTransactionType.description" title="Txtn Type" sortable="true" />
	<display:column property="refCategory.refCategoryType.description" title="Category Type" sortable="true" />
	<display:column property="refCategory.description" title="Category" sortable="true" />
	<display:column property="account.description" title="Account" sortable="true" 
		href="account.pets" paramId="accountId" paramProperty="account.id" />
	<display:column property="trfAccount.description" title="Transfer To" sortable="false" 
		href="account.pets" paramId="accountId" paramProperty="trfAccount.id" />
	<display:column property="refMerchant.description" title="Merchant" sortable="true" 
		href="merchant.pets" paramId="merchantId" paramProperty="refMerchant.id" />
	<display:column property="amount" title="Amount" format="{0,number,currency}" sortable="true" />
	<display:column title="Nec" sortable="true">
		<c:choose>
			<c:when test="${transaction.necessary eq true}">
				YES
			</c:when>
			<c:otherwise>
				NO
			</c:otherwise>
		</c:choose>
	</display:column>
	<display:column title="Reg" sortable="true">
		<c:choose>
			<c:when test="${transaction.regular eq true}">
				YES
			</c:when>
			<c:otherwise>
				NO
			</c:otherwise>
		</c:choose>
	</display:column>
	<display:column title=" " media="html" style="width:160px;">
		<input type="button" value="View/Update" onclick="editTransaction('${transaction.id}', '${ accountModel.account.id }', '${ merchant.id }');" />
		<input type="button" value="Delete" onclick="deleteUserTransaction(this, '${transaction.id}','${transaction.date}');" />
	</display:column>
	<display:setProperty name="export.csv.filename" value="SelectedTransactions.csv" />
</display:table>
