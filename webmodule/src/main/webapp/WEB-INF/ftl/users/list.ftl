<@layout.main breadcrumbs=breadcrumbs menu=menu sidebar=sidebar body=body/>

<#macro breadcrumbs>
	<li><a href="<@spring.url '/'/>">Dashboard</a></li>
	<li><a href="<@spring.url '/admin/users/'/>">Usuarios</a></li>
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
			<div class="title">Lista de usuarios<span class="hide"></span></div>
			<div class="content">
				<table cellspacing="0" cellpadding="0" border="0"> 
					<thead> 
						<tr>
							<th>OID</th>
							<th>Nombre</th>
							<th>E-Mail</th>
							<th>Roles</th>
							<th>Habilitado</th>
							<th>Acciones</th>
						</tr>
					</thead>
					<tbody>
						<#list users.list as user>
						<tr>
							<td>${user.id}</td>
							<td>${user.displayName}</td>
							<td>${user.username}</td>
							<td>${user.roles}</td>
							<td>${user.account.enabled?string("Si", "No")}</td>
							<td>
								<@widget.detail "/admin/user/show/${user.id}"/>
								<@widget.edit "/admin/user/${user.id}"/>
								<@widget.delete "/admin/user/${user.id}"/>
							</td>
						</tr>
						</#list>
					</tbody>
				</table>
				<@widget.tablePaging users "/admin/users" />
			</div>
		</div>
	</div>
</#macro>