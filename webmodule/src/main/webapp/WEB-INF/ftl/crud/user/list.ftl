<@layout.main breadcrumbs=breadcrumbs menu=menu sidebar=sidebar body=body/>

<#macro breadcrumbs>
	<li><a href="<@spring.url '/'/>">Dashboard</a></li>
	<li><a href="<@spring.url '/user/list'/>">Usuarios</a></li>
</#macro>

<#macro menu>
	<@widget.menu "admin"/>
</#macro>

<#macro sidebar>
	<@widget.statistics/>
</#macro>


<#macro body>
	<@page.section>
		<@page.box title="Lista de usuarios">
			<a href="<@spring.url '/user/new'/>">Nuevo usuario...</a>
			<table cellspacing="0" cellpadding="0" border="0"> 
				<thead> 
					<tr>
						<th>OID</th>
						<#if openfire_enabled = true>
						<th>Estado</th>
						</#if>
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
						<#if openfire_enabled = true>
						<td>
							<img src="http://192.168.0.104:9090/plugins/presence/status?jid=${user.displayName}@${openfire_domain}"/>
						</td>
						</#if>
						<td>${user.displayName}</td>
						<td>${user.username}</td>
						<td>${user.roles}</td>
						<td>${user.account.enabled?string("Si", "No")}</td>
						<td>
							<@widget.link "/user/show/${user.id}" "view_doc.png" "Ver"/>
							<@widget.link "/user/${user.id}" "edit.png" "Editar"/>
							<@widget.link "/delete/user/${user.id}" "delete.png" "Borrar" "class='deletion'"/>
							<@widget.link "/user/password/${user.id}" "password.png" "Cambiar contraseña"/>								
						</td>
					</tr>
					</#list>
				</tbody>
			</table>
			<@widget.tablePaging users "/user/list"/>			
		</@page.box>
	</@page.section>
</#macro>