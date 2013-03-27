<@layout.main breadcrumbs=breadcrumbs menu=menu sidebar=sidebar body=body/>

<#macro breadcrumbs>
	<li><a href="<@spring.url '/'/>">Dashboard</a></li>
	<li><a href="<@spring.url '/group/list/'/>">Grupos</a></li>
</#macro>

<#macro menu>
	<@widget.menu "group"/>
</#macro>

<#macro sidebar>
	<@widget.statistics/>
</#macro>


<#macro body>
	<@page.section>
		<@page.box title="Lista de grupos">
			<a href="<@spring.url '/group/new'/>">Nuevo grupo...</a>
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
					<#list groups.list as group>
					<tr>
						<td>${group.id}</td>
						<td>${group.name}</td>
						<td>${group.description!}</td>
						<td>
							<@widget.link "/group/show/${group.id}" "view_doc.png" "Ver"/>
							<@widget.link "/group/edit/${group.id}" "edit.png" "Editar"/>
							<@widget.link "/group/delete/${group.id}" "delete.png" "Borrar" "class='deletion'"/>
						</td>
					</tr>
					</#list>
				</tbody>
			</table>
			<@widget.tablePaging groups "/group/list" />			
		</@page.box>
	</@page.section>
</#macro>