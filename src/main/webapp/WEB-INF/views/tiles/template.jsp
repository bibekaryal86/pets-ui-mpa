<%@ include file="/WEB-INF/views/include/include_main.jsp"%>

<html>
<head>
<link rel="shortcut icon" type="image/x-icon" href="static/images/favicon.png" />
<tiles:insertAttribute name="include" />
<title><tiles:getAsString name="title" /></title>
</head>

<body onload="startTime();">
	<tiles:insertAttribute name="header" />
	<tiles:insertAttribute name="body" />
	<tiles:insertAttribute name="footer" />
</body>
</html>
