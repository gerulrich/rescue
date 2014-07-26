<@layout.main breadcrumbs=breadcrumbs menu=menu sidebar=sidebar body=body/>

<#macro breadcrumbs>
	<li><a href="<@spring.url '/'/>">Dashboard</a></li>
	<li><a href="<@spring.url '/resource/list'/>">Recursos</a></li>
	<li>Detalle ${entity.name}</li>
</#macro>

<#macro menu>
	<@widget.menu "resource"/>
</#macro>

<#macro sidebar>
	<@widget.statistics/>
</#macro>

<#macro body>
	<@page.section>
		<@page.box title="Recurso ${entity.name}">
			<@form.row label="Nombre">${entity.name}</@form.row>
			<@form.row label="Tipo">${entity.type.name}</@form.row>
			<@form.row label="IMEI">${entity.imei}</@form.row>
		</@page.box>
	</@page.section>
</#macro>