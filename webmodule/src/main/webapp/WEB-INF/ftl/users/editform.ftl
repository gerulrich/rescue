<@layout.main template="admin">

	<h2>Pagina editar usuario ${user.displayName}</h2>

	<p><a href="<@spring.url '/admin/users/'/>">Volver</a></p>

	<form action="<@spring.url '/admin/user/${user.id}'/>" method="post">
		<fieldset id="user">
			<legend>Datos del usuario</legend>
        	<table>
        		<tr>
            		<td><label path="username">Nombre de usuario</label></td>
               		<td><@spring.formInput  "user.username" /></td>
				</tr>
            	<tr>
	           		<td><label path="displayName">Nombre a visualizar</label></td>
               		<td><@spring.formInput  "user.displayName" /></td>
				</tr>
			</table>
		</fieldset>
		
		<fieldset id="account">
			<legend>Detalle de la cuenta</legend>
        		<table>
					<tr>
            			<td><label path="account.enabled">Habilitado</label></td>
                		<td><@spring.formCheckbox  "user.account.enabled" /></td>
					</tr>
					<tr>
            			<td><label path="account.locked">Cuenta bloqueada</label></td>
                		<td><@spring.formCheckbox  "user.account.locked" /></td>
					</tr>
				</table>
			</fieldset>
			<input type="submit" value="Guardar"/>		
    </form>	
	
</@layout.main>