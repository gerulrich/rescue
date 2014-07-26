<@layout.main breadcrumbs=breadcrumbs menu=menu sidebar=sidebar body=body/>

<#macro breadcrumbs>
	<li><a href="<@spring.url '/'/>">Dashboard</a></li>
	<li><a href="<@spring.url '/group/list'/>">Grupos</a></li>
	<li>Detalle ${entity.name}</li>
</#macro>

<#macro menu>
	<@widget.menu "admin"/>
</#macro>

<#macro sidebar>
	<@widget.statistics/>
</#macro>

<#macro body>
	<@page.section>
		<@page.box title="Grupo ${entity.name}">
			<@form.row label="Nombre">${entity.name}</@form.row>
			<@form.row label="Descripci&oacute;n">${entity.description}</@form.row>
		</@page.box>
	</@page.section>
</#macro>