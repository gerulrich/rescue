<@layout.main breadcrumbs=breadcrumbs menu=menu sidebar=sidebar body=body/>

<#macro breadcrumbs>
	<li><a href="<@spring.url '/'/>">Dashboard</a></li>
	<li><a href="<@spring.url '/resource/list'/>">Recursos</a></li>
	<li>Detalle ${resource.name}</li>
</#macro>

<#macro menu>
	<@widget.menu "resource"/>
</#macro>

<#macro sidebar>
	<@widget.statistics/>
</#macro>

<#macro body>
	<@page.section>
		<@page.box title="Recurso ${resource.name}">
			<@form.row label="Nombre">${resource.name}</@form.row>
			<@form.row label="Tipo">${resource.type.name}</@form.row>
			<@form.row label="IMEI">${resource.imei}</@form.row>
		</@page.box>
	</@page.section>
</#macro>