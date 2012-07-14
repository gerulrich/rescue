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
	
		<div class="plain">
			<h3>A tener en cuenta</h3>
			<br/>
			<ul class="tick">
				<li>El archivo desde estar subido previamente desde la opci&oacute;n correspondiente.</li>
				<li>El archivo shapefile deben estar en la projecci&oacute;n GWS84.</li>
				<li>El archivo shapefile deber&aacute; estar comprimido en formato zip, el cual contendr&aacute;
				el archivo .shp y el archivo .dbf, ambos con el mismo nombre (sin tener en cuenta la extensi&oacute;n).</li>
				<li>Deber&aacute;n contar con los campos necesarios seg&uacute;n se trate de puntos de interes, calles o zonas.</li>
			</ul>
		</div>
	
		<div class="box">
			<div class="title">
			Cargar layer desde archivo<span class="hide"></span>
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
							<option value="zone">Zonas</option>
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