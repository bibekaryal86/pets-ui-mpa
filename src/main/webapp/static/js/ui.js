function inputTextToUpper(element) {
	$(element).value = $(element).value.toUpperCase();
}

function openPopupWindow(url) {
	newwindow = window
			.open(url, 'popupWindow',
					'width=1000,height=600,scrollbars=yes,toolbar=no,status=yes,resizable=yes');
	if (window.focus) {
		newwindow.focus();
	}
	return false;
}

function swapSelectCheckBox(thisChkBoxId, thatChkBoxId) {
	document.getElementById(thatChkBoxId).checked = false;
}

function closeAndRefresh() {
	opener.location.reload(true);
	self.close();
}

function editAccount(id) {
	window.location.href = 'account.pets?accountId=' + id;
}

function editTransaction(id, accountId, merchantId) {
	window.location.href = 'transaction.pets?transactionId='+id+'&accountId='+accountId+'&merchantId='+merchantId;
}

function reloadCashFlowsReport(selectedYear) {
	if (selectedYear == '') {
		window.location.href = 'report_cashflows.pets?selectedYear='+new Date().getFullYear();
	} else {
		window.location.href = 'report_cashflows.pets?selectedYear='+selectedYear.value;
	}
}

function reloadCategoriesReport(selectedYear) {
	if (selectedYear == '') {
		window.location.href = 'report_cashflows.pets?selectedYear='+new Date().getFullYear();
	} else {
		window.location.href = 'report_categories.pets?selectedYear='+selectedYear.value;
	}
}

function startTime() {
	var today = new Date();

	var month = new Array();
	month[0] = "January";
	month[1] = "February";
	month[2] = "March";
	month[3] = "April";
	month[4] = "May";
	month[5] = "June";
	month[6] = "July";
	month[7] = "August";
	month[8] = "September";
	month[9] = "October";
	month[10] = "November";
	month[11] = "December";

	var weekday = new Array(7);
	weekday[0] = "Sunday";
	weekday[1] = "Monday";
	weekday[2] = "Tuesday";
	weekday[3] = "Wednesday";
	weekday[4] = "Thursday";
	weekday[5] = "Friday";
	weekday[6] = "Saturday";

	document.getElementById('currentDate').innerHTML = weekday[today.getDay()]
			+ ", " + month[today.getMonth()] + ", "
			+ checkTime(today.getDate()) + ", " + checkTime(today.getHours())
			+ ":" + checkTime(today.getMinutes()) + ":"
			+ checkTime(today.getSeconds());

	var t = setTimeout(startTime, 500);
}

function checkTime(i) {
	if (i < 10) {
		i = "0" + i;
	}
	return i;
}

function formatDate(date) {
	var d = new Date(date), month = '' + (d.getMonth() + 1), day = ''
			+ d.getDate(), year = d.getFullYear();

	if (month.length < 2) {
		month = '0' + month;
	}
	if (day.length < 2) {
		day = '0' + day;
	}

	return [ year, month, day ].join('-');
}

function getBeginningDate(clickYear, clickMonth) {
	var d = new Date(), month = '' + (d.getMonth() + 1), year = d.getFullYear();

	if (month.length < 2)
		month = '0' + month;

	if (clickYear != '') {
		return [ year, '01', '01' ].join('-');
	} else if (clickMonth != '') {
		return [ year, month, '01' ].join('-');
	}
}
