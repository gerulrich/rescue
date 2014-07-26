<@layout.main breadcrumbs=breadcrumbs menu=menu sidebar=sidebar body=body script=script/>

<#macro breadcrumbs>
	<li><a href="<@spring.url '/'/>">Dashboard</a></li>
	<li><a href="<@spring.url '/workflow/list'/>">Workflow</a></li>
	<li><a>Nuevo</a></li>
</#macro>

<#macro menu>
	<@widget.menu "admin"/>
</#macro>

<#macro sidebar>
	<@widget.statistics/>
</#macro>


<#macro body>

	<@page.section>
			<@page.box title="Cargar archivo como workflow">
				<form method="post" class="valid">
					<@form.row label="Archivo">
						<@form.pojoSelect name="fileId" list=files id="fileId" value="description"/>
					</@form.row>
				
					<@form.row label="">
						<input type="checkbox" id="append" name="overwrite"/>
						<label for="append">Workflow activo</label>
					</@form.row>
		
					<@form.submit>Aceptar</@form.submit>
				</form>
			</@page.box>		
	</@page.section>

</#macro>