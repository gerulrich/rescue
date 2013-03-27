<@layout.main breadcrumbs=breadcrumbs menu=menu sidebar=sidebar body=body/>

<#macro breadcrumbs>
	<li><a href="<@spring.url '/'/>">Dashboard</a></li>
	<li><a href="<@spring.url '/resource/type/list'/>">Tipos de Recursos</a></li>
	<li>Detalle ${type.name}</li>
</#macro>

<#macro menu>
	<@widget.menu "resource"/>
</#macro>

<#macro sidebar>
	<@widget.statistics/>
</#macro>

<#macro body>
	<@page.section>
		<@page.box title="Tipo de recurso ${type.name}">
			<@form.row label="Nombre">${type.name}</@form.row>
			<@form.row label="Imagen"><img src="<@spring.url '/resource/type/image/${type.id}'/>" /></@form.row>
		</@page.box>
	</@page.section>
</#macro>