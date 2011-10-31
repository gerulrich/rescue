<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"
%><%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><html>
<head>
	<title><fmt:message key="exception.caught.title" /></title>
</head>
<body>
	<p><fmt:message key="exception.caught.description"/></p>
	<div id="errors">
		<pre>${exception}</pre>
	</div>
	<p><fmt:message key="exception.caught.spring.mvc"/></p>
</body>
</html>