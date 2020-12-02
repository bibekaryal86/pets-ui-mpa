function getOrSetOrResetSessionAttribute(getOrSetOrReset, attributeName, attributeValue) {
	new Ajax.Request('servlet/SessionHelperServlet', {
		method : 'post',
		parameters : {
			getOrSetOrReset : getOrSetOrReset,
			attributeName : attributeName,
			attributeValue : attributeValue
		}
	});
}

function updateCategoryByType(catTypeId, catTypeIdValue, catId, usedInTxnsOnly) {
	if (catTypeIdValue == '') {
		catTypeIdValue = catTypeId.value;
	}
	new Ajax.Updater(catId, 'servlet/LookupRefCategoriesByType', {
		parameters : {
			categoryTypeId : catTypeIdValue,
			usedInTxnsOnly : usedInTxnsOnly
		}
	});
}

function deleteUserTransaction(button, id, date) {
	button.disabled = true;

	var answer = confirm('Are you sure you want to delete transaction from date '
			+ formatDate(date) + '?');
	
	if (!answer) {
		button.disabled = false;
		return false;
	} else {
		new Ajax.Request('servlet/DeleteTransaction', {
			method : 'post',
			parameters : {
				transactionId : id,
				transactionDate : date
			},
			onSuccess : function(transport) {
				var response = transport.responseText || SOMETHING_WENT_WRONG;
				
				if (response == GENERIC_SUCCESS_MESSAGE) {
					location.reload();
				} else {
					alert(response);
					button.disabled = false;
				}
			},
			onFailure : function() {
				alert('Something went wrong...');
				button.disabled = false;
			}
		});
	}
}

function editRefMerchant(button, merchantId, merchantName, rowNum) {
	button.disabled = true;
	var answer;
	var action;
	
	if (button.id.includes(USER_ACTION_UPDATE)) {
		answer = document.getElementById('UPDATEMERCHANT_'+rowNum).value;
		action = USER_ACTION_UPDATE;
	} else if (button.id.includes(USER_ACTION_DELETE)) {
		action = USER_ACTION_DELETE;
	}
	
	new Ajax.Request('servlet/RefMerchantServlet', {
		method : 'post',
		parameters : {
			merchantAction : action,
			merchantId : merchantId,
			merchantNameOld : merchantName,
			merchantNameNew : answer
		},
		onSuccess : function(transport) {
			var response = transport.responseText || SOMETHING_WENT_WRONG;

			if (response == GENERIC_SUCCESS_MESSAGE) {
				location.reload();
			} else {
				alert(response);
				button.disabled = false;
			}
		},
		onFailure : function() {
			alert(SOMETHING_WENT_WRONG);
			button.disabled = false;
		}
	});
}

function filterTransactions(action, txnType, catType, cat, acc, txnDateF,
		txnDateT, txnAmtF, txnAmtT, nTxnY, nTxnN, rTxnY, rTxnN, txnMerchant) {
	new Ajax.Request('servlet/FilterTransactions', {
		method : 'post',
		parameters : {
			setOrReset : action,
			fTxnType : txnType,
			fCatType : catType,
			fCat : cat,
			fAcc : acc,
			fTxnDateFrom : txnDateF,
			fTxnDateTo : txnDateT,
			fTxnAmtFrom : txnAmtF,
			fTxnAmtTo : txnAmtT,
			fNecTxn : nTxnY + "_" + nTxnN,
			fRegTxn : rTxnY + "_" + rTxnN,
			fTxnMerchant : txnMerchant
		},
		onSuccess : function(transport) {
			window.location = 'transactions.pets';
		},
		onFailure : function() {
			alert('Something went wrong...');
			location.reload();
		}
	});
}

function filterAccounts(action, accType, bank, accSts) {
	new Ajax.Request('servlet/FilterAccounts', {
		method : 'post',
		parameters : {
			setOrReset : action,
			fAccType : accType,
			fBank : bank,
			fAccSts : accSts
		},
		onSuccess : function(transport) {
			window.location = 'accounts.pets';
		},
		onFailure : function() {
			alert(SOMETHING_WENT_WRONG);
			location.reload();
		}
	});
}

function initPetsHome(showInput, showLoadingError) {
	new Ajax.Request('servlet/InitPetsHome', {
		method: 'post',
		onSuccess : function(transport) {
			if (transport.responseText === GENERIC_SUCCESS_MESSAGE) {
				showInput();
			} else {
				showLoadingError(transport.responseText);
			}
		},
		onFailure : function(transport) {
			showLoadingError(SOMETHING_WENT_WRONG);
		}
	})
}

function test() {
	new Ajax.Request('servlet/Test', {
		method : 'post'
	});
}
