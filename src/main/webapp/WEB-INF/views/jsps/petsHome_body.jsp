<%@ include file="/WEB-INF/views/include/include_main.jsp"%>

<p class="sectionTitle">
	Welcome to PETS - Personal Expenses Tracking System
</p>

<c:if test="${not empty errMsg}">
	<span class="error">${errMsg}</span>
</c:if>

<form action="home.pets" method="post">

	<div id="LOADING" class="notes" align=center>
		APP IS LOADING! PLEASE WAIT! 
		<br><br> 
		<img src="static/images/loading.gif" id="loadinggif" alt="App is Loading" border="0" />
	</div>

	<div id="LOADING_ERROR" class="error" align=center style="display: none;">
		Something went wrong! 
		<br>
		<var id="LOADING_ERROR_RESPONSE"></var>
		<br> 
		Please try again in 30 seconds!
	</div>

	<div id="LOADED" align=center style="display: none;">
		<table class="pets">
			<thead>
				<tr>
					<th><c:out value="Please Log In" /></th>
				</tr>
			</thead>
			<tbody>
				<tr class="odd">
					<td>
						<c:out value="User Name:" /> 
						<br> 
						<input type="text" name="username">
					</td>
				</tr>
				<tr class="even">
					<td>
						<c:out value="Password:" /> 
						<br> 
						<input type="password" name="password">
					</td>
				</tr>
				<tr class="odd">
					<td style="text-align: center;">
						<input type="submit" value="Sign In">
					</td>
				</tr>
			</tbody>
		</table>
	</div>

</form>

<script type="text/javascript">
<!--
	window.onload = setTimeout(function() {
		init();
	}, 1000);

	function showInput() {
		document.getElementById('LOADING').style.display = 'none';
		document.getElementById('LOADED').style.display = 'block';
	}

	function showLoadingError(response) {
		document.getElementById('LOADING_ERROR_RESPONSE').innerHTML = response;
		document.getElementById('LOADING_ERROR').style.display = 'block';
	}

	function init() {
		// Call this when loading the page
		// this will create an instance of google app engines for both database and service
		// this is better because it will create instance of both database and service
		// unlike when logging in a user because that will first create instance of service, then create instance of database
		// so this will save time

		if (window.location.search) {
			showInput();
		} else {
			initPetsHome(showInput, showLoadingError);
		}
	}
//-->
</script>
