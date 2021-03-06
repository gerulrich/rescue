<@layout.main breadcrumbs=breadcrumbs menu=menu sidebar=sidebar body=body/>

<#macro breadcrumbs>
	<li><a href="<@spring.url '/'/>">Dashboard</a></li>
	<li><a href="<@spring.url '/config/list/'/>">Propiedades</a></li>
	<li>Editar ${entity.key}</li>
</#macro>

<#macro menu>
	<@widget.menu "admin"/>
</#macro>

<#macro sidebar>
	<@widget.statistics/>
</#macro>


<#macro body>
	<@page.section>
		<@page.box title="Editar la Propiedad ${entity.key}">
			<form action="<@spring.url '/config/edit/${entity.id}'/>" method="post" class="valid">
				<@form.row label="Valor de la propiedad">
					<@spring.formInput  "entity.value" "class='{validate:{required:true, messages:{required:\"Ingrese una valor para la propiedad\"}}}'"/>
				</@form.row>
					
				<@form.row label="">
					<@spring.formCheckbox  "entity.clientProperty"/>
					<label for="clientProperty">Propiedad cliente desktop</label>				
				</@form.row>
							
				<@form.submit>Guardar</@form.submit>
			</form>
		</@page.box>
	</@page.section>
</#macro>