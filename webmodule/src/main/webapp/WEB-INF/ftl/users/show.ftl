<@layout.main>
<table>
	<tr>
		<td>Nombre de usuario</td>
		<td>${user.username}</td>
	</tr>
	<tr>
		<td>Nombre a visualizar</td>
		<td>${user.displayName}</td>
	</tr>
	<tr>
		<td>Habilitado</td>
		<td><#if user.account.enabled>Yes<#else>No</#if></td>
	</tr>
	<tr>
		<td>Cuenta bloqueada</td>
		<td><#if user.account.locked>Yes<#else>No</#if></td>
	</tr>
</table>
</@layout.main>