<@layout.main breadcrumbs=breadcrumbs menu=menu sidebar=sidebar body=body/>

<#macro breadcrumbs>
	<li><a href="<@spring.url '/'/>">Dashboard</a></li>
	<li>Tipos de recursos</li>
</#macro>

<#macro menu>
	<@widget.menu "resource"/>
</#macro>

<#macro sidebar>
	<@widget.statistics/>
</#macro>


<#macro body>
	<@page.section>
		<@page.box title="Tipo de recursos">
			<a href="<@spring.url '/resource/type/new'/>">Nuevo tipo de recurso...</a>
			<table cellspacing="0" cellpadding="0" border="0"> 
				<thead>
					<tr>
						<th>ID</th>
						<th>Nombre</th>
						<th>Acciones</th>
					</tr>
				</thead>
				<tbody>
					<#list types.list as type>
					<tr>
						<td>${type.id}</td>
						<td>${type.name}</td>
						<td>
							<@widget.link "/resource/type/show/${type.id}" "view_doc.png" "Ver"/>
							<@widget.link "/resource/type/edit/${type.id}" "edit.png" "Editar"/>
							<@widget.link "/resource/type/delete/${type.id}" "delete.png" "Eliminar" "class='deletion'"/>
						</td>
					</tr>
					</#list>
				</tbody>
			</table>
			<@widget.tablePaging types "/resource/type/list" />
		</@page.box>
	</@page.section>
</#macro>