<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"
%><html>
<head>
	<title><fmt:message key="home.title"/></title>
	<link rel="shortcut icon" href="${pageContext.request.contextPath}/img/favicon.ico" type="image/x-icon" />
</head>

<body>
<fmt:message key="home.welcome"/><br/>

<br/><br/>
<a href="${pageContext.request.contextPath}/admin/users">usuarios</a><br/>
<br/><br/>
<a href="${pageContext.request.contextPath}/j_spring_security_logout"><fmt:message key="home.logout"/></a><br/>


<a href="${pageContext.request.contextPath}?locale=es"><fmt:message key="locale.spanish"/></a> | 
<a href="${pageContext.request.contextPath}?locale=en"><fmt:message key="locale.english"/></a>
</body>

</html>