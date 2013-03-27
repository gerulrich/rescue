<@layout.main breadcrumbs=breadcrumbs menu=menu sidebar=sidebar body=body/>


<#macro breadcrumbs>
	<li><a href="<@spring.url '/'/>">Dashboard</a></li>
	<li><a href="<@spring.url '/group/list'/>">Grupos</a></li>
	<li><#if !group.id??>Nuevo grupo<#else>Editar grupo</#if></li>
</#macro>

<#macro menu>
	<@widget.menu "admin"/>
</#macro>

<#macro sidebar>
	<@widget.statistics/>
</#macro>


<#macro body>
	<@page.section>
		
		<#if !group.id??>
			<#assign title="Crear grupo."/>
			<#assign url="/group/new"/>
		<#else>
			<#assign title="Editar grupo."/>
			<#assign url="/group/edit/${group.id}"/>
		</#if>
	
		<@page.box title=title>
			<form  action="<@spring.url '${url}'/>" method="post" class="valid">
				<@form.row label="Nombre">
					<@spring.formInput  "group.name" "class='{validate:{required:true, messages:{required:\"Ingrese un nombre para el grupo\"}}}'"/>
					<@spring.showErrors "<br/>"/>
				</@form.row>
				
				<@form.row label="Descripci&oacute;n">
					<@spring.formInput  "group.description"/>
					<@spring.showErrors "<br/>"/>
				</@form.row>
				
				<@form.submit>Guardar</@form.submit>
			</form>
		</@page.box>
		
	</@page.section>
</#macro>