<@layout.main breadcrumbs=breadcrumbs menu=menu sidebar=sidebar body=body/>


<#macro breadcrumbs>
	<li><a href="<@spring.url '/'/>">Dashboard</a></li>
</#macro>

<#macro menu>
	<@widget.menu "dashboard"/>
</#macro>

<#macro sidebar>
	<@widget.statistics/>
</#macro>

<#macro body>
	<div class="btn-box">
		<div class="content">
			<@widget.hasPermission "USER_MANAG">
			<a href="<@spring.url '/user/list'/>" class="item">
				<img src="<@spring.url '/static/gfx/icons/big/user.png'/>" alt="Usuarios">
				<span>Usuarios</span>
				Admin. de usuarios
			</a>
			<a href="<@spring.url '/role/list'/>" class="item">
				<img src="<@spring.url '/static/gfx/icons/big/users.png'/>" alt="Roles">
				<span>Roles</span>
				Admin. de Roles
			</a>			
			</@widget.hasPermission>
			
			<@widget.hasPermission "MONGO_CONSOLE">
			<a href="<@spring.url '/mongo/list'/>" class="item">
				<img src="<@spring.url '/static/gfx/icons/big/database.png'/>" alt="MongoDB">
				<span>MongoDB</span>
				Colecciones de MongoDB
			</a>
			</@widget.hasPermission>
			
			<@widget.hasPermission "SQL_CONSOLE">
			<a href="<@spring.url '/sql/list'/>" class="item">
				<img src="<@spring.url '/static/gfx/icons/big/database.png'/>" alt="SqlDB">
				<span>SqlDB</span>
				Tablas de Mysql
			</a>
			</@widget.hasPermission>			
			
			<@widget.hasPermission "CONFIG">
			<a href="<@spring.url '/config/list'/>" class="item">
				<img src="<@spring.url '/static/gfx/icons/big/settings.png'/>" alt="configuracion">
				<span>Configuraci&oacute;n</span>
				Par&aacute;metros de conf.
			</a>
			</@widget.hasPermission>
			
			<@widget.hasPermission "FILE_UPLOAD">
			<a href="<@spring.url '/file/list'/>" class="item">
				<img src="<@spring.url '/static/gfx/icons/big/file.png'/>" alt="archivos">
				<span>Archivos</span>
				Gesti&oacute;n de archivos
			</a>
			</@widget.hasPermission>			
		</div>
	</div>
	
	<div id="result"></div>
	<div id="msg"></div>

	<@widget.hasPermission "STATISTICS">
		
		<#list charts as chart>
			<@page.half>
				<@page.box title="${chart.name}">
					<iframe src="${chart.url}" width="100%" height="300" scrolling="no" frameborder="no"></iframe>
				</@page.box>
			</@page.half>
		</#list>

	</@widget.hasPermission>
	
	<#if debug>
		<@page.section>
		<@page.box title="Variables de ambiente">
			<table cellspacing="0" cellpadding="0" border="0"> 
				<thead>
					<tr>
						<th>Nombre</th>
						<th>Valor</th>
					</tr>
				</thead>
				<tbody>
					<#list env?keys as prop>
					<tr>
						<td>${prop}</td>
						<td>${env[prop]}</td>
					</tr>
					</#list>
				</tbody>
			</table>
		</@page.box>
		</@page.section>
	</#if>			
	
</#macro>