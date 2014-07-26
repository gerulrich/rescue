<@layout.main breadcrumbs=breadcrumbs menu=menu sidebar=sidebar body=body/>


<#macro breadcrumbs>
	<#assign group=entity/>
	<li><a href="<@spring.url '/'/>">Dashboard</a></li>
	<li><a href="<@spring.url '/chart/list'/>">Grupos</a></li>
	<li><#if !group.id??>Nuevo Gr&aacutefico/Tabla<#else>Editar Gr&aacutefico/Tabla</#if></li>
</#macro>

<#macro menu>
	<@widget.menu "admin"/>
</#macro>

<#macro sidebar>
	<@widget.statistics/>
</#macro>


<#macro body>
	<@page.section>
		
		<#if !entity.id??>
			<#assign title="Crear Gr&aacutefico/Tabla"/>
			<#assign url="/chart/new"/>
		<#else>
			<#assign title="Editar Gr&aacutefico/Tabla"/>
			<#assign url="/chart/edit/${entity.id}"/>
		</#if>
	
		<@page.box title=title>
			<form  action="<@spring.url '${url}'/>" method="post" class="valid">
				<@form.row label="Nombre">
					<@spring.formInput  "entity.name" "class='{validate:{required:true, messages:{required:\"Ingrese un nombre para el grupo\"}}}'"/>
					<@spring.showErrors "<br/>"/>
				</@form.row>
				
				<@form.row label="URL">
					<@spring.formInput  "entity.url"/>
					<@spring.showErrors "<br/>"/>
				</@form.row>
				
				<@form.submit>Guardar</@form.submit>
			</form>
		</@page.box>
		
	</@page.section>
</#macro>