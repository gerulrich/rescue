<@layout.main breadcrumbs=breadcrumbs menu=menu sidebar=sidebar body=body/>

<#macro breadcrumbs>
	<li><a href="<@spring.url '/'/>">Dashboard</a></li>
	<li><a href="<@spring.url '/resource/list/'/>">Recursos</a></li>
	<li><#if !entity.id??>Nuevo<#else>Editar</#if></li>
</#macro>

<#macro menu>
	<@widget.menu "resource"/>
</#macro>

<#macro sidebar>
	<@widget.statistics/>
</#macro>


<#macro body>

	<#if !entity.id??>
		<#assign title="Nuevo Recurso"/>
		<#assign url="/resource/new"/>
	<#else>
		<#assign title="Editar Recurso"/>
		<#assign url="/resource/edit/${entity.id}"/>
	</#if>

	<@page.section>
		<@page.box title=title>
			<form action="<@spring.url '${url}'/>" method="post" class="valid">
				<@form.row label="Nombre *">
					<@spring.formInput  "entity.name" "class='{validate:{required:true, messages:{required:\"Ingrese un nombre para el recurso\"}}}'"/>
					<@spring.showErrors "<br/>"/>
				</@form.row>
				
				<@form.row label="Tipo">
					<@widget.formSingleSelect "entity.type" types "id" "name"/>
					<@spring.showErrors "<br/>"/>
				</@form.row>

				<@form.row label="IMEI">
					<@spring.formInput  "entity.imei"/>
					<@spring.showErrors "<br/>"/>
				</@form.row>

				<@form.submit>Guardar</@form.submit>
			</form>
		</@page.box>
	</@page.section>
</#macro>