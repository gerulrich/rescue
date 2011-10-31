<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%><%@ taglib
	prefix="c" uri="http://java.sun.com/jsp/jstl/core"%><%@taglib
	uri="http://www.springframework.org/tags/form" prefix="spring"%><html>

<body>

	<c:url var="backUrl" value="/admin/users/" />
	<p><a href="${backUrl}">Volver a usuarios</a></p>
	<c:url var="urlForm" value="/admin/user/${user.id}" />

	<table>
		<tr>
			<td>Nombre de usuario</td>
			<td><c:out value="${user.username}"></c:out></td>
		</tr>
		<tr>
			<td>Nombre a visualizar</td>
			<td><c:out value="${user.displayName}"></c:out></td>
		</tr>
		<tr>
			<td>Habilitado</td>
			<td><c:out value="${user.account.enabled}"></c:out></td>
		</tr>
		<tr>
		<td>Cuenta bloqueada</td>
			<td><c:out value="${user.account.locked}"></c:out></td>
		</tr>
	</table>

</body>


</html>