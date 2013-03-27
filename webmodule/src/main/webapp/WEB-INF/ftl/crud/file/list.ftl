<@layout.main breadcrumbs=breadcrumbs menu=menu sidebar=sidebar body=body/>

<#macro breadcrumbs>
	<li><a href="<@spring.url '/'/>">Dashboard</a></li>
	<li>Archivos</li>
</#macro>

<#macro menu>
	<@widget.menu "admin"/>
</#macro>

<#macro sidebar>
	<@widget.statistics/>
</#macro>


<#macro body>
	<@page.section>
		<@page.box title="Lista de archivos">
			<a href="<@spring.url '/file/upload'/>">Subir archivo...</a>
			<table cellspacing="0" cellpadding="0" border="0">
				<thead>
					<tr>
						<th>Nombre</th>
						<th>Descripci&oacute;n</th>
						<th>Tama&ntilde;o</th>
						<th>Fecha</th>
						<th>Tipo</th>
						<th>Versi&oacute;n</th>
						<th>Acciones</th>
					</tr>
				</thead>
				<tbody>
					<#list files.list as file>
					<tr>
						<td>${file.filename}</td>
						<td>${file.description}</td>
						<td>${file.sizeReadable}</td>
						<td>${file.date?datetime}</td>
						<td>${file.type}</td>
						<td>${file.version!""}</td>
						<td>
							<@widget.link "/file/download/${file.id}" "download.png" "Descargar"/>
							<@widget.link "/file/delete/${file.id}" "delete.png" "Borrar" "class='deletion'"/>
						</td>
					</tr>
					</#list>
				</tbody>
			</table>
			<@widget.tablePaging files "/file/list" />			
		</@page.box>
	</@page.section>
</#macro>