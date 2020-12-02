<%@ include file="/WEB-INF/views/include/include_main.jsp"%>

<p class="body">
<form:form method="POST" action="import.pets" enctype="multipart/form-data">
	<c:if test="${not empty errMsg}">
		<span class="error">${errMsg}</span>
	</c:if>
	
	<p class="notes">MAKE SURE TO CLEAR ALL FORMATTING IN THE CSV FILE BEFORE UPLOADING TO AVOID CSV PARSE ERROR</p>
	
	<table>
		<thead>
			<tr>
				<th>Import to Database</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td>Select Table Name:</td>
				<td>
					<select name = "name" onchange="showInstructions(this);">
						<option value = "" selected >PLEASE SELECT</option>
						<c:forEach items="${namesList}" var="name">
							<option value = "${ name }" ${name==selectedName?'selected="selected"':''} >${ name }</option>
						</c:forEach>
         			</select>
       			</td>
			</tr>
			<tr>
				<td>Select a file to upload</td>
				<td><input type="file" name="file" /></td>
			</tr>
			<tr>
				<td><input type="submit" value="Submit" /></td>
			</tr>
		</tbody>
	</table>
	
	<table>
		<tbody>
			<tr class="notes">
				<td id="CATEGORY_INSTRUCTIONS" style = "display:none">
					EXPECTED HEADERS: [CATEGORY_TYPE_NAME, CATEGORY_NAME]
				</td>
				<td id="ACCOUNT_INSTRUCTIONS" style = "display:none">
					EXPECTED HEADERS: [BANK_NAME, ACCOUNT_TYPE_NAME, ACCOUNT_NAME, OPENING_BALANCE, STATUS]
				</td>
				<td id="TRANSACTION_INSTRUCTIONS" style = "display:none">
					EXPECTED HEADERS: [DESCRIPTION, REGULAR, NECESSARY, DATE, AMOUNT, TRANSACTION_TYPE_NAME, CATEGORY_NAME, ACCOUNT_NAME, TRF_ACCOUNT_NAME, MERCHANT_NAME]
				</td>
			</tr>
		</tbody>
	</table>
	
	<br>
	<c:if test="${not empty errors}">
		<span class="error">
			ERRORS:
			<br>
			<c:forEach items="${errors}" var="error">
				${error}
				<br>
			</c:forEach>
		</span>
	</c:if>
</form:form>

<script type="text/javascript">
<!--
	var selectionValue = '${selectedName}';
	showInstructions('', selectionValue);

	function showInstructions(selection, selectionValue) {
		if(selection.value == 'CATEGORY' || selectionValue == 'CATEGORY') {
			document.getElementById('CATEGORY_INSTRUCTIONS').style.display  = 'block';
			document.getElementById('ACCOUNT_INSTRUCTIONS').style.display  = 'none';
			document.getElementById('TRANSACTION_INSTRUCTIONS').style.display  = 'none';
		} else if(selection.value == 'ACCOUNT' || selectionValue == 'ACCOUNT') {
			document.getElementById('CATEGORY_INSTRUCTIONS').style.display  = 'none';
			document.getElementById('ACCOUNT_INSTRUCTIONS').style.display  = 'block';
			document.getElementById('TRANSACTION_INSTRUCTIONS').style.display  = 'none';
		} else if(selection.value == 'TRANSACTION' || selectionValue == 'TRANSACTION') {
			document.getElementById('CATEGORY_INSTRUCTIONS').style.display  = 'none';
			document.getElementById('ACCOUNT_INSTRUCTIONS').style.display  = 'none';
			document.getElementById('TRANSACTION_INSTRUCTIONS').style.display  = 'block';
		} else {
			document.getElementById('CATEGORY_INSTRUCTIONS').style.display  = 'none';
			document.getElementById('ACCOUNT_INSTRUCTIONS').style.display  = 'none';
			document.getElementById('TRANSACTION_INSTRUCTIONS').style.display  = 'none';
		}
	}
//-->
</script>
