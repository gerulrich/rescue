<@layout.main breadcrumbs=breadcrumbs menu=menu sidebar=sidebar body=body/>

<#macro breadcrumbs>
	<li><a href="<@spring.url '/'/>">Dashboard</a></li>
	<li><a href="<@spring.url '/role/list'/>">Roles</a></li>
	<li>Permisos del role ${role.name}</li>
</#macro>

<#macro menu>
	<@widget.menu "admin"/>
</#macro>

<#macro sidebar>
	<@widget.statistics/>
</#macro>

<#macro body>
		
	<@page.section>
		<@page.box title="Permisos del rol ${role.name}:  ${role.description}">
			<form  action="<@spring.url '/role/permissions/edit/${role.id}'/>" method="post">
				<table cellspacing="0" cellpadding="0" border="0">
					<thead> 
						<tr>
							<th></th>
							<th>Nombre</th>
							<th>Descripci&oacute;n</th>
						</tr>
					</thead>
					<#list permissions as permission>
			 		<tr>
			 			<td><input type="checkbox" name="permissionId" value="${permission.id}" <#if permission.selected>checked="checked"</#if>/></td>
			 			<td>${permission.name}</td>
			 			<td>${permission.description}</td>
			 		</tr>
					</#list>
				</table>
				<@form.submit>Guardar</@form.submit>
			</form>		
		</@page.box>
	</@page.section>
</#macro>