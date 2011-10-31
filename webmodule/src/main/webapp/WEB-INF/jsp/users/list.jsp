<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"
%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><html>

<body>

	<h2>Pagina <c:out value="${users.pageNumber}" /> de <c:out value="${users.totalPages}"/></h2>
	<h3>Resultados: <c:out value="${users.totalSize}" /></h3>
	<table>
		<c:forEach var="user" items="${users.list}">
			<tr>
				<td><a href="<c:url value='/admin/user/${user.id}'/>">editar</a></td>
				<td><c:out value="${user.displayName}" /></td>
			</tr>
		</c:forEach>
	</table>

</body>


</html>