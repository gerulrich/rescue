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
			<a href="<@spring.url '/admin/users'/>" class="item">
				<img src="<@spring.url '/static/gfx/icons/big/user.png'/>" alt="Usuarios">
				<span>Usuarios</span>
				Admin. de usuarios
			</a>
			
			<a href="<@spring.url '/admin/mongo/list'/>" class="item">
				<img src="<@spring.url '/static/gfx/icons/big/database.png'/>" alt="MongoDB">
				<span>MongoDB</span>
				Colecciones de MongoDB
			</a>
			
			<a href="<@spring.url '/admin/properties'/>" class="item">
				<img src="<@spring.url '/static/gfx/icons/big/settings.png'/>" alt="configuracion">
				<span>Configuraci&oacute;n</span>
				Par&aacute;metros de conf.
			</a>
		</div>
	</div>
	<div class="plain">
		<h1>Panel de control</h1>
		<p> El panel de control es un lugar integrado para la administración de la aplicación. 
		Puedes administrar cuentas de usuario, roles, grupos, configurar la integración de telefonía 
		(CTI: Computer Telephony Integration), configurar el sistema de mapas,  y personalizar otros par&aacute;metros de 
		configuración de la aplicación según las necesidades.</p>
	</div>
</#macro>