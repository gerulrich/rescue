<@layout.main breadcrumbs=breadcrumbs menu=menu sidebar=sidebar body=body/>


<#macro breadcrumbs>
	<li><a href="<@spring.url '/'/>">Dashboard</a></li>
	<li><a href="<@spring.url '/admin/users'/>">Usuarios</a></li>
	<li>Nuevo usuario</li>
</#macro>

<#macro menu>
	<@widget.menu "admin"/>
</#macro>

<#macro sidebar>
	<@widget.statistics/>
</#macro>


<#macro body>

		<div class="section">
		
			<@widget.showErrors "user"/>
		
			<div class="box">
				<div class="title">
					Alta de usuario.
					<span class="hide"></span>
				</div>
				<div class="content">
					<form  action="<@spring.url '/admin/user/new'/>" method="post" class="valid">
						<div class="row">
							<label>Email</label>
							<div class="right">
								<@spring.formInput  "user.email" "class='{validate:{required:true, email:true, messages:{email:\"Ingrese una direccion de E-mail valida.\", required:\"Ingrese una dirección de E-mail\"}}}'"/>
							</div>
						</div>							
							
						<div class="row">
							<label>Nombre a visualizar</label>
							<div class="right"><@spring.formInput  "user.displayName" "class='{validate:{required:true, messages:{required:\"Ingrese el nombre del usuario.\"}}}'"/></div>
						</div>
						
						<div class="row">
							<label>Roles</label>
							<div class="right"><@spring.formInput  "user.roles" "class='{validate:{required:true, messages:{required:\"Ingrese los roles del usuario.\"}}}'"/></div>
						</div>
							
						<div class="row">
							<label>Contrase&ntilde;a</label>
							<div class="right"><@spring.formPasswordInput  "user.password" "class='{validate:{required:true, messages:{required:\"Ingrese una contraseña.\"}}}'"/></div>
						</div>
							
						<div class="row">
							<label>Repetir contrase&ntilde;a</label>
							<div class="right"><@spring.formPasswordInput  "user.passwordVerification" "class='{validate:{required:true, messages:{required:\"Repita la contraseña.\"}}}'"/></div>
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