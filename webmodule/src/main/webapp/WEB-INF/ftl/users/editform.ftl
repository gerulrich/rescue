<@layout.main breadcrumbs=breadcrumbs menu=menu sidebar=sidebar body=body/>

<#macro breadcrumbs>
	<li><a href="<@spring.url '/'/>">Dashboard</a></li>
	<li><a href="<@spring.url '/admin/users'/>">Usuarios</a></li>
	<li><a href="<@spring.url '/admin/user/${user.id}'/>">Editar ${user.displayName}</a></li>
</#macro>

<#macro menu>
	<@widget.menu "admin"/>
</#macro>

<#macro sidebar>
	<@widget.statistics/>
</#macro>


<#macro body>
		<div class="section">
				<div class="box">
					<div class="title">
						Editar datos del usuario "${user.displayName}"
						<span class="hide"></span>
					</div>
					<div class="content">
						<form  action="<@spring.url '/admin/user/${user.id}'/>" method="post" class="valid">
							<div class="row">
								<label>Email</label>
								<div class="right">
									<@spring.formInput  "user.username" "class='{validate:{required:true, email:true, messages:{email:\"Ingrese una direccion de E-mail valida.\", required:\"Ingrese una dirección de E-mail\"}}}'"/>
								</div>
							</div>							
							
							<div class="row">
								<label>Nombre a visualizar</label>
								<div class="right"><@spring.formInput  "user.displayName" "class='{validate:{required:true, messages:{required:\"Please enter your name\"}}}'"/></div>
							</div>
							
							<div class="row">
								<label>Roles</label>
								<div class="right"><@spring.formInput  "user.roles" "class='{validate:{required:true, messages:{required:\"Ingrese los roles del usuario\"}}}'"/></div>
							</div>
							

							<div class="row">
								<label></label>
								<div class="right">
									<@spring.formCheckbox  "user.account.enabled"/>
									<label for="account.enabled">Habilitado</label>
								</div>
							</div>
							
							<div class="row">
								<label></label>
								<div class="right">
									<@spring.formCheckbox  "user.account.locked"/>
									<label for="account.locked">Cuenta bloqueada</label>
								</div>
							</div>
							
							<div class="row">
								<label>Interno</label>
								<div class="right"><@spring.formInput  "user.account.phoneNumber"/></div>
							</div>
							
							<div class="row">
								<label></label>
								<div class="right">
									<button type="submit" class="green"><span>Guardar</span></button>
								</div>
							</div>
						</form>
					</div>
				</div>
			</div>
</#macro>