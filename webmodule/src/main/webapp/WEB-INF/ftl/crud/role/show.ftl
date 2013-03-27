<@layout.main breadcrumbs=breadcrumbs menu=menu sidebar=sidebar body=body/>

<#macro breadcrumbs>
	<li><a href="<@spring.url '/'/>">Dashboard</a></li>
	<li><a href="<@spring.url '/role/list'/>">Roles</a></li>
	<li>${role.name}</li>
</#macro>

<#macro menu>
	<@widget.menu "admin"/>
</#macro>

<#macro sidebar>
	<@widget.statistics/>
</#macro>

<#macro body>

	<@page.section>
		<@page.box title="Rol ${role.name}">
			<@form.row label="Nombre">${role.name}</@form.row>
			<@form.row label="Descripci&oacute;n">${role.name}</@form.row>
		</@page.box>
		
		<@page.box title="Permisos del role ${role.name}">
			<table cellspacing="0" cellpadding="0" border="0"> 
				<thead> 
					<tr>
						<th>Nombre</th>
						<th>Descripci&oacute;n</th>
					</tr>
				</thead>
				<tbody>
					<#list role.permissions as permission>
					<tr>
	 					<td>${permission.name}</td>
	 					<td>${permission.description}</td>
	 				</tr>
					</#list>
				</tbody>
			</table>		
		</@page.box>	
	</@page.section>

</#macro>