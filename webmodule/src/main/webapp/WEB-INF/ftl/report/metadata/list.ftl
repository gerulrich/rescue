<@layout.main breadcrumbs=breadcrumbs menu=menu sidebar=sidebar body=body/>

<#macro breadcrumbs>
	<li><a href="<@spring.url '/'/>">Dashboard</a></li>
	<li>Reportes</li>
</#macro>

<#macro menu>
	<@widget.menu "admin"/>
</#macro>

<#macro sidebar>
	<@widget.statistics/>
</#macro>


<#macro body>
	<@page.section>
		<@page.box title="Lista de reportes">
			<table cellspacing="0" cellpadding="0" border="0"> 
				<thead>
					<tr>
						<th>Nombre</th>
						<th>Categoria</th>
						<th>Acciones</th>
					</tr>
				</thead>
				<tbody>
					<#list reports.list as report>
					<tr>
						<td>${report.name}</td>
						<td>${report.category}</td>
						<td>
							<@widget.link "/report/metadata/form/${report.id}" "gears.png" "Ejecutar"/>
							<@widget.link "/report/metadata/delete/${report.id}" "delete.png" "Eliminar" "class='deletion'"/>
						</td>
					</tr>
					</#list>
				</tbody>
			</table>
			<@widget.tablePaging reports "/report/list" />
		</@page.box>
	</@page.section>
</#macro>