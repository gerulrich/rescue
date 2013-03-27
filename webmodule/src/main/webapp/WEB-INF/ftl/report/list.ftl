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
						<th>Descripci&oacute;n</th>
						<th>Fecha</th>
						<th>Versi&oacute;n</th>
						<th>Acciones</th>
					</tr>
				</thead>
				<tbody>
					<#list reports.list as report>
					<tr>
						<td>${report.filename}</td>
						<td>${report.description}</td>
						<td>${report.date?datetime}</td>
						<td>${report.version!""}</td>
						<td>
							<@widget.link "/report/form/${report.id}" "gears.png" "Ejecutar"/>
						</td>
					</tr>
					</#list>
				</tbody>
			</table>
			<@widget.tablePaging reports "/report/list" />
		</@page.box>
	</@page.section>
</#macro>