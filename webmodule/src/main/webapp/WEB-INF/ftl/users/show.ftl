<@layout.main breadcrumbs=breadcrumbs menu=menu sidebar=sidebar body=body/>

<#macro breadcrumbs>
	<li><a href="<@spring.url '/'/>">Dashboard</a></li>
	<li><a href="<@spring.url '/admin/users'/>">Usuarios</a></li>
	<li>Detalle ${user.displayName}</li>
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
						Datos datos del usuario "${user.displayName}"
						<span class="hide"></span>
					</div>
					<div class="content">
							<div class="row">
								<label>Email</label>
								<div class="right">${user.username}</div>
							</div>							
							
							<div class="row">
								<label>Nombre a visualizar</label>
								<div class="right">${user.displayName}</div>
							</div>
							
							<div class="row">
								<label>Roles</label>
								<div class="right">${user.roles}</div>
							</div>

							<div class="row">
								<label></label>
								<label>Habilitado</label>
								<div class="right">${user.account.enabled?string("Si","No")}</div>
							</div>
							
							<div class="row">
								<label>Cuenta bloqueada</label>
								<div class="right">${user.account.locked?string("Si","No")}</div>
							</div>
							
							<div class="row">
								<label>Interno</label>
								<div class="right">${user.account.phoneNumber}</div>
							</div>
					</div>
				</div>
			</div>
</#macro>