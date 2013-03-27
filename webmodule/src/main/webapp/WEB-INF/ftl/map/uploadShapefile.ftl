<@layout.main breadcrumbs=breadcrumbs menu=menu sidebar=sidebar body=body/>

<#macro breadcrumbs>
	<li><a href="<@spring.url '/'/>">Dashboard</a></li>
	<li>Cargar capa desde archivo</li>
</#macro>

<#macro menu>
	<@widget.menu "admin"/>
</#macro>

<#macro sidebar>
	<@widget.statistics/>
</#macro>


<#macro body>

	<@page.section>
		
		<@page.half>
			
			<@page.box title="Cargar layer desde archivo">
				<iframe id="uploadFrameID" name="uploadFrame" height="0" width="0" frameborder="0" scrolling="yes"></iframe>
				<form method="post" class="valid">
					<@form.row label="Archivo">
						<@form.pojoSelect name="file" list=files id="id" value="filename"/>
					</@form.row>

					<@form.row label="Subir como">
						<@form.mapSelect name="type" map={"poi" : "Puntos de interes", "street": "Calles", "zone":"Zonas"}/>
					</@form.row>
					<@form.submit>Siguiente</@form.submit>

				</form>
			</@page.box>
		</@page.half>
	
		<@page.half>	
			<@page.box title="Ayuda">
				<ul class="comments">
					<li>El archivo debe estar subido previamente desde la opci&oacute;n correspondiente.</li>
					<li>El archivo shapefile deben estar en la projecci&oacute;n <b>GWS84</b>.</li>
					<li>El archivo shapefile deber&aacute; estar comprimido en formato zip, el cual contendr&aacute;
					el archivo .shp y el archivo .dbf, ambos con el mismo nombre (sin tener en cuenta la extensi&oacute;n).</li>
					<li>Deber&aacute;n contar con los campos necesarios seg&uacute;n se trate de puntos de interes, calles o zonas.</li>					
				</ul>
			</@page.box>
			
		</@page.half>
	</@page.section>
</#macro>