<@layout.main breadcrumbs=breadcrumbs menu=menu sidebar=sidebar body=body/>

<#macro breadcrumbs>
	<li><a href="<@spring.url '/'/>">Dashboard</a></li>
	<li>Recursos</li>
</#macro>

<#macro menu>
	<@widget.menu "resource"/>
</#macro>

<#macro sidebar>
	<@widget.statistics/>
</#macro>


<#macro body>
<@page.section>
		<@page.box title="Recursos">
			<a href="<@spring.url '/resource/new'/>">Nuevo recurso...</a>
			<table cellspacing="0" cellpadding="0" border="0">
				<thead>
					<tr>
						<th>ID</th>
						<th>Nombre</th>
						<th>IMEI</th>
						<th>Tipo</th>
						<th>Acciones</th>
					</tr>
				</thead>
				<tbody>
					<#list resources.list as resource>
					<tr>
						<td>${resource.id}</td>
						<td>${resource.name}</td>
						<td>${resource.imei}</td>
						<td>${resource.type.name}</td>
						<td>
							<@widget.link "/resource/show/${resource.id}" "view_doc.png" "Ver"/>
							<@widget.link "/resource/edit/${resource.id}" "edit.png" "Editar"/>
							<@widget.link "/resource/delete/${resource.id}" "delete.png" "Eliminar" "class='deletion'"/>
						</td>
					</tr>
					</#list>
				</tbody>
			</table>
			<@widget.tablePaging resources "/resource/list" />			
		</@page.box>
	</@page.section>
</#macro>