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
			
				<iframe id="uploadFrameID" name="uploadFrame" height="0" width="0" frameborder="0" scrolling="yes"></iframe>
				<form method="post" class="valid">
					<div class="row">
						<label>Archivo</label>
						<div class="right">
							<select name="file">
							<#list files as f>
								<option value="${f.id}">${f.filename}</option>							
							</#list>
							</select>
						</div>
					</div>
					
					<div class="row">
						<label>Subir como</label>
						<div class="right">
							<select name="type">
							<option value="poi">Puntos de interes</option>
							<option value="street">Calles</option>
							<option value="">Zonas</option>
							</select>
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