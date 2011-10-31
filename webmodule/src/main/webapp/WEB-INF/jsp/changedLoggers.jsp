<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Logger Setup - Results</title>
</head>

<body>
	Please choose the logger and the level:
	<form method="post">
		<table CELLPADDING="0" CELLSPACING="0" BORDER="0">
			<tr>
				<td COLSPAN="2"><h2>Enable Disable Logger</h2>
				</td>
			</tr>
			<tr>
				<td>The following Logger's were set to level: ${level}<br>
				
				<c:forEach var="logger" items="${changedLoggers}">
					${logger}<br>
				</c:forEach>
				
					</td>
			</tr>
			<tr>
				<td><a HREF="${pageContext.request.contextPath}/admin/logger">Return to list</a></td>
			</tr>
		</table>
	</FORM>
</body>
</html>