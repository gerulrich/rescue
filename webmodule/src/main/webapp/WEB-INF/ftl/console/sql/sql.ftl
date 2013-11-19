<@layout.main breadcrumbs=breadcrumbs menu=menu sidebar=sidebar body=body/>

<#macro breadcrumbs>
	<li><a href="<@spring.url '/'/>">Dashboard</a></li>
	<li><a href="<@spring.url '/sql/list'/>">SQL</a></li>	
</#macro>

<#macro menu>
	<@widget.menu "admin"/>
</#macro>

<#macro sidebar>
	<@widget.statistics/>
</#macro>


<#macro body>
	<@page.section>
		<@page.box title="Tablas de la base de datos">
			<table cellspacing="0" cellpadding="0" border="0"> 
				<thead>
					<tr>
						<th>Nombre</th>
						<th>Engine</th>
						<th>Filas estimadas</th>
						<th>Tamaño</th>
						<th>Index Size</th>
						<th>Acciones</th>
					</tr>
				</thead>
				<tbody>
					<#list tables as table>
					<tr>
						<td>${table.name}</td>
						<td>${table.engine}</td>
						<td>${table.rows}</td>
						<td>${table.sizeHumanReadable}</td>
						<td>${table.indexSizeHumanReadable}</td>
						<td>
							<@widget.link "/sql/table/list/${table.encodedName}/" "view_doc.png" "Ver"/>
							<@widget.link "/sql/table/${table.encodedName}" "detail.png" "Detalle"/>							
						</td>
					</tr>
					</#list>
				</tbody>
			</table>
		</@page.box>
	</@page.section>
</#macro>