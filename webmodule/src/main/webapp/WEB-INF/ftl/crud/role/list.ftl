<@layout.main breadcrumbs=breadcrumbs menu=menu sidebar=sidebar body=body/>

<#macro breadcrumbs>
	<li><a href="<@spring.url '/'/>">Dashboard</a></li>
	<li><a href="<@spring.url '/role/list/'/>">Roles</a></li>
</#macro>

<#macro menu>
	<@widget.menu "admin"/>
</#macro>

<#macro sidebar>
	<@widget.statistics/>
</#macro>


<#macro body>
	<@page.section>
		<@page.box title="Lista de roles">
			<a href="<@spring.url '/role/new'/>">Nuevo rol...</a>
			<table cellspacing="0" cellpadding="0" border="0"> 
				<thead> 
					<tr>
						<th>OID</th>
						<th>Nombre</th>
						<th>Descripci&oacute;n</th>
						<th>Acciones</th>
					</tr>
				</thead>
				<tbody>
					<#list roles.list as role>
					<tr>
						<td>${role.id}</td>
						<td>${role.name}</td>
						<td>${role.description}</td>
						<td>
							<@widget.link "/role/show/${role.id}" "view_doc.png" "Ver"/>
							<@widget.link "/role/edit/${role.id}" "edit.png" "Editar"/>
							<@widget.link "/role/permissions/edit/${role.id}" "password.png" "Permisos"/>
							<@widget.link "/role/delete/${role.id}" "delete.png" "Borrar" "class='deletion'"/>
						</td>
					</tr>
					</#list>
				</tbody>
			</table>
			<@widget.tablePaging roles "/role/list" />			
		</@page.box>
	</@page.section>

	<div class="section">
		<div class="box">
			<div class="title"><span class="hide"></span></div>
			<div class="content">
			
				
			
				
			</div>
		</div>
	</div>
</#macro>