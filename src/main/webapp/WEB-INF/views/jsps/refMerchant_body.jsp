<%@ include file="/WEB-INF/views/include/include_main.jsp"%>

<p class="body">
<c:if test="${not empty errMsg}">
	<span class="error">${errMsg}</span>
</c:if>

<div id="NEW_TRANSACTION" class="notes">
	To add a new Transaction, click here:
	<a href="javascript: editTransaction('', '', '${merchant.id}');">
		Add Transaction
	</a>
</div>

<table style="width: 25%" id="merchantsTable" class="pets">
	<thead>
		<tr>
			<th colspan="2">Merchant Details</th>
		</tr>
	</thead>
	<tbody>
		<tr>
			<td colspan="2" nowrap="nowrap">Merchant Name : <span class="error">*</span> 
				<br>
				<input id="UPDATEMERCHANT_99" size="50" maxlength="99" value="${ merchant.description }">
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<c:choose>
					<c:when test="${ merchant.notEditable eq true }">
						<input id="UPDATE_BUTTON" type="button" value="UPDATE" disabled="disabled" />
					</c:when>
					<c:otherwise>
						<input id="UPDATE_BUTTON" type="button" value="UPDATE" onclick="editRefMerchant(this, '${merchant.id}', '${merchant.description}', '99');" />
					</c:otherwise>
				</c:choose>
				<a href="javascript: window.location.href='merchants.pets'">
					[ All Merchants List ]
				</a>
				<a href="javascript: window.location.href='transactions.pets'">
					[ All Transactions List ]
				</a>
			</td>
		</tr>
	</tbody>
</table>

<jsp:include page="transactions_list.jsp" />
