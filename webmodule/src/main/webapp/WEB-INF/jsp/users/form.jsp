
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"
%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%@taglib uri="http://www.springframework.org/tags/form" prefix="spring"
%><html>

<body>

	<c:if test="${user.id == null}"><h2>Crear usuario</h2></c:if>	
	<c:if test="${user.id != null}"><h2>Pagina editar usuario ${user.displayName}</h2></c:if>
	
	
	
	<c:url var="backUrl" value="/admin/users/"/>
	<p><a href="${backUrl}">Volver</a></p>
	<c:url var="urlForm" value="/admin/user/${user.id}"/>
	
	<spring:form method="post" action="${urlForm}" commandName="user">
		<fieldset id="user">
			<legend>Datos del usuario</legend>
        	<table>
        		<tr>
            		<td><spring:label path="username">Nombre de usuario</spring:label></td>
               		<td><spring:input path="username" /></td>
				</tr>
            	<tr>
	           		<td><spring:label path="displayName">Nombre a visualizar</spring:label></td>
               		<td><spring:input path="displayName" /></td>
				</tr>
			</table>
		</fieldset>
		
		<fieldset id="account">
			<legend>Detalle de la cuenta</legend>
        		<table>
					<tr>
            			<td><spring:label path="account.enabled">Habilitado</spring:label></td>
                		<td><spring:checkbox path="account.enabled" /></td>
					</tr>
					<tr>
            			<td><spring:label path="account.locked">Cuenta bloqueada</spring:label></td>
                		<td><spring:checkbox path="account.locked" /></td>
					</tr>
				</table>
			</fieldset>
			<input type="submit" value="Guardar"/>		
    </spring:form>	
	

</body>


</html>