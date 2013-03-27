<@layout.main breadcrumbs=breadcrumbs menu=menu sidebar=sidebar body=body/>


<#macro breadcrumbs>
	<li><a href="<@spring.url '/'/>">Dashboard</a></li>
	<li><a href="<@spring.url '/role/list'/>">Roles</a></li>
	<li><#if !role.id??>Nuevo rol<#else>Editar rol</#if></li>
</#macro>

<#macro menu>
	<@widget.menu "admin"/>
</#macro>

<#macro sidebar>
	<@widget.statistics/>
</#macro>


<#macro body>

	<#if !role.id??>
		<#assign title="Crear rol."/>
		<#assign url="/role/new"/>
	<#else>
		<#assign title="Editar rol"/>
		<#assign url="/role/edit/${role.id}"/>
	</#if>

	<@page.section>
		<@page.box title=title>
			<form  action="<@spring.url '${url}'/>" method="post" class="valid">
				<@form.row label="Nombre">
					<@spring.formInput "role.name" "class='{validate:{required:true, messages:{required:\"Ingrese un nombre para el rol\"}}}'"/>
				</@form.row>

				<@form.row label="Descripci&oacute;n">
					<@spring.formInput "role.description" "class='{validate:{required:true, messages:{required:\"Ingrese una descripción del rol.\"}}}'"/>				
				</@form.row>
				
				<@form.submit>Guardar</@form.submit>
			</form>
		</@page.box>
	</@page.section>

</#macro>