<@layout.main breadcrumbs=breadcrumbs menu=menu sidebar=sidebar body=body/>

<#macro breadcrumbs>
	<li><a href="<@spring.url '/'/>">Dashboard</a></li>
	<li><a href="<@spring.url '/workflow/list'/>">Workflow</a></li>
</#macro>

<#macro menu>
	<@widget.menu "admin"/>
</#macro>

<#macro sidebar>
	<@widget.statistics/>
</#macro>


<#macro body>
	<@page.section>
		<@page.box title="Workflows disponibles">
			<a href="<@spring.url '/workflow/new'/>">Nuevo workflow...</a>
			<table cellspacing="0" cellpadding="0" border="0"> 
				<thead> 
					<tr>
						<th>OID</th>
						<th>Version</th>
						<th>Activo</th>
						<th>Acciones</th>
					</tr>
				</thead>
				<tbody>
					<#list workflows.list as workflow>
					<tr>
						<td>${workflow.id}</td>
						<td>${workflow.version}</td>
						<td><#if workflow.active>Si<#else>No</#if></td>
						<td><@widget.link "/workflow/activate/${workflow.id}" "view_doc.png" "Activar"/></td>
					</tr>
					</#list>
				</tbody>
			</table>
			<@widget.tablePaging workflows "/workflow/list"/>			
		</@page.box>
	</@page.section>
</#macro>