<@layout.main template="admin">

<h2>Alta de usuario</h2>

<@spring.bind "user" />
<form action="<@spring.url '/admin/user/new'/>" method="post">
	<fieldset id="user">
		<legend>Datos del usuario</legend>
        <table>
        	<tr>
           		<td><label path="username">Email</label></td>
				<td><@spring.formInput  "user.email" /></td>
				<td><@spring.showErrors "" /></td>
			</tr>
			<tr>
				<td><label path="displayName">Nombre a visualizar</label></td>
				<td><@spring.formInput  "user.displayName" /></td>
				<td><@spring.showErrors "" /></td>
			</tr>
			<tr>
				<td><label path="displayName">Contraseña</label></td>
				<td><@spring.formPasswordInput "user.password" /></td>
				<td><@spring.showErrors "" /></td>
			</tr>
			<tr>
				<td><label path="displayName">Repetir contraseña</label></td>
				<td><@spring.formPasswordInput "user.passwordVerification" /></td>
				<td><@spring.showErrors "" /></td>
			</tr>
			<tr>
				<td><label path="roles">Roles</label></td>
				<td><@spring.formInput "user.roles" /></td>
				<td><@spring.showErrors "" /></td>
			</tr>
		</table>
	</fieldset>
	<input type="submit" value="Guardar"/>
</form>	

</@layout.main>