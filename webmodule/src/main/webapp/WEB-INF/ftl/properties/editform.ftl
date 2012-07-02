<@layout.main breadcrumbs=breadcrumbs menu=menu sidebar=sidebar body=body/>

<#macro breadcrumbs>
	<li><a href="<@spring.url '/'/>">Dashboard</a></li>
	<li><a href="<@spring.url '/admin/properties/'/>">Propiedades</a></li>
	<li>Editar ${property.key}</li>
</#macro>

<#macro menu>
	<@widget.menu "admin"/>
</#macro>

<#macro sidebar>
	<@widget.statistics/>
</#macro>


<#macro body>
	<div class="section">
		<div class="box">
			<div class="title">
				Editar la Propiedad "${property.key}"
				<span class="hide"></span>
			</div>
			<div class="content">
				<form action="<@spring.url '/admin/property/${property.id}'/>" method="post" class="valid">
					<div class="row">
						<label>Valor de la propiedad</label>
						<div class="right">
							<@spring.formInput  "property.value" "class='{validate:{required:true, messages:{required:\"Ingrese una valor para la propiedad\"}}}'"/>
						</div>
					</div>
						
					<div class="row">
						<label></label>
						<div class="right">
							<@spring.formCheckbox  "property.clientProperty"/>
							<label for="clientProperty">Propiedad cliente desktop</label>
						</div>
					</div>
							
					<div class="row">
						<label></label>
						<div class="right">
							<button type="submit" class="green"><span>Guardar</span></button>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</#macro>