<@layout.main breadcrumbs=breadcrumbs menu=menu sidebar=sidebar body=body/>

<#macro breadcrumbs>
	<li><a href="<@spring.url '/'/>">Dashboard</a></li>
	<li><a href="<@spring.url '/sql/list'/>">SQL</a></li>
	<li><a href="<@spring.url '/sql/list'/>">${table.name}</a></li>
	<li>Estructura</li>	
</#macro>

<#macro menu>
	<@widget.menu "admin"/>
</#macro>

<#macro sidebar>
	<@widget.statistics/>
</#macro>


<#macro body>
	<@page.section>
		<@page.box title="Columnas de la tabla ${table.name}">
			<table cellspacing="0" cellpadding="0" border="0"> 
				<thead>
					<tr>
						<th>Nombre</th>
						<th>Tipo</th>
						<!--th>Size</th>
						<th>Storage</th>
						<th>Indices</th>
						<th>Index Size</th-->
						<th>Acciones</th>
					</tr>
				</thead>
				<tbody>
					<#list table.columns as column>
					<tr>
						<td>${column.name}</td>
						<td>${column.type}</td>
						<td></td>
					</tr>
					</#list>
				</tbody>
			</table>
		</@page.box>
	</@page.section>
</#macro>