<@layout.main breadcrumbs=breadcrumbs menu=menu sidebar=sidebar body=body/>

<#macro breadcrumbs>
	<li><a href="<@spring.url '/'/>">Dashboard</a></li>
	<li><a href="<@spring.url '/resource/type/list/'/>">Tipo de Recurso</a></li>
	<li><#if !type.id??>Nuevo tipo de recurso<#else>Editar tipo de recurso</#if></li>
</#macro>

<#macro menu>
	<@widget.menu "admin"/>
</#macro>

<#macro sidebar>
	<@widget.statistics/>
</#macro>


<#macro body>
	<#if !type.id??>
		<#assign title="Nuevo tipo de Recurso"/>
		<#assign url="/resource/type/new/"/>
	<#else>
		<#assign title="Editar tipo de Recurso"/>
		<#assign url="/resource/type/edit/${type.id}"/>
	</#if>
	
	<@page.section>
		<@page.box title=title>
			<form action="<@spring.url '${url}'/>" method="post" class="valid">
				
				<@form.row label="Nombre *">
					<@spring.formInput  "type.name" "class='{validate:{required:true, messages:{required:\"Ingrese un nombre para el tipo\"}}}'"/>
					<@spring.showErrors "<br/>" />
				</@form.row>
				
				<@form.row label="Imagen">
					<@form.pojoSelect name="image" list=files id="id" value="filename"/>
				</@form.row>
						
				<@form.submit>Guardar</@form.submit>
			</form>		
		</@page.box>
	</@page.section>
</#macro>