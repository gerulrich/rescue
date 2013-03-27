<@layout.main breadcrumbs=breadcrumbs menu=menu sidebar=sidebar body=body/>

<#macro breadcrumbs>
	<li><a href="<@spring.url '/'/>">Dashboard</a></li>
	<li><a href="<@spring.url '/mongo/list'/>">MongoDB Collections</a></li>	
</#macro>

<#macro menu>
	<@widget.menu "admin"/>
</#macro>

<#macro sidebar>
	<@widget.statistics/>
</#macro>


<#macro body>
	<@page.section>
		<@page.box title="MongoDB Collections">
			<table cellspacing="0" cellpadding="0" border="0"> 
				<thead>
					<tr>
						<th>Nombre</th>
						<th>Nro documentos</th>
						<th>Size</th>
						<th>Storage</th>
						<th>Indices</th>
						<th>Index Size</th>
						<th>Acciones</th>
					</tr>
				</thead>
				<tbody>
					<#list collections as col>
					<tr>
						<td>${col.name}</td>
						<td>${col.count}</td>
						<td>${col.size}</td>
						<td>${col.storage}</td>
						<td>${col.indexes}</td>
						<td>${col.indexSize}</td>
						<td>
							<@widget.link "/mongo/show/${col.name}" "view_doc.png" "Ver"/>
							<@widget.link "/mongo/drop/${col.name}" "delete.png" "Borrar"/>
						</td>
					</tr>
					</#list>
				</tbody>
			</table>
		</@page.box>
	</@page.section>			
</#macro>