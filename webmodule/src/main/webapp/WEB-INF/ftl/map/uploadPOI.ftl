<@layout.main breadcrumbs=breadcrumbs menu=menu sidebar=sidebar body=body/>

<#macro breadcrumbs>
	<li><a href="<@spring.url '/'/>">Dashboard</a></li>
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
			Subir shapefile<span class="hide"></span>
			</div>
			<div class="content">
			
				<form method="post" class="valid">
					<div class="row">
						<label>Campo Nombre</label>
						<div class="right">
							<select name="nameField">
							<#list fields as field>
								<option value="${field}">${field}</option>							
							</#list>
							</select>
						</div>
					</div>
					
					<div class="row">
						<label>Campo Tipo</label>
						<div class="right">
							<select name="typeField">
							<#list fields as field>
								<option value="${field}">${field}</option>							
							</#list>
							</select>
						</div>
					</div>
					
					<div class="row">
						<label></label>
							<div class="right">
							<input type="checkbox" id="append" name="overwrite"/>
							<label for="append">Sobreescribir datos</label>
						</div>
					</div>
					
						
					<div class="row">
						<label></label>
						<div class="right">
							<button id="boton" type="submit" class="green"><span>Siguiente</span></button>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>

</#macro>