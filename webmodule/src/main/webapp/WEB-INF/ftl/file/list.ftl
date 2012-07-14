<@layout.main breadcrumbs=breadcrumbs menu=menu sidebar=sidebar body=body/>

<#macro breadcrumbs>
	<li><a href="<@spring.url '/'/>">Dashboard</a></li>
	<li><a href="<@spring.url '/file/list'/>">Archivos</a></li>
</#macro>

<#macro menu>
	<@widget.menu "admin"/>
</#macro>

<#macro sidebar>
	<@widget.statistics/>
</#macro>


<#macro body>
	<div class="section">
		<div class="box">
			<div class="title">Lista de archivos<span class="hide"></span></div>
			<div class="content">
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
								<@widget.link "/file/delete/${file.id}" "trash_16x16.gif" "Borrar"/>
							</td>
						</tr>
						</#list>
					</tbody>
				</table>
				<@widget.tablePaging files "/file/list" />
			</div>
		</div>
	</div>
</#macro>