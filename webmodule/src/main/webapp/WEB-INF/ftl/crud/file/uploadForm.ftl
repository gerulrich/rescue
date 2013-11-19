<@layout.main breadcrumbs=breadcrumbs menu=menu sidebar=sidebar body=body script=script/>

<#macro script>
<script>

	$(document).ready(function() {
    			
    	$(".message").hide();

		$("#target").validate({
			meta: "validate",
			submitHandler: function(form) {
				$(".message").hide();
				$('#boton').hide();
				$('#progress').progressbar({value: 0});
				$('#progress').prev(".percent").text("0%");
				setTimeout ( 'progress()', 500 );
				form.submit();
			}
		});
    			    			
	});

    function progress() {
    	$.ajax({
			url: '<@spring.url '/file/uploadstatus/'/>',
			dataType: 'json',
    		success: function(data) {
    			if (data.total == 0) {
    				data.total = 100;
    			}
        		var porcentage = Math.floor(100 * parseInt(data.uploaded) / parseInt(data.total));
				if ( porcentage == 100 ) {
					$('#progress').progressbar({value: 100});
					$('#progress').prev(".percent").text("100%");
					$(".message").show();
					$('#boton').show();
					$('form').each (function() { this.reset(); });
				} else {
					$('#progress').progressbar({value: porcentage});
					$('#progress').prev(".percent").text(porcentage+"%");
					setTimeout ( 'progress()', 500 );
				}
			}
		});
	}
   	
</script>
</#macro>

<#macro breadcrumbs>
	<li><a href="<@spring.url '/'/>">Dashboard</a></li>
	<li><a href="<@spring.url '/file/list'/>">Archivos</a></li>
	<li>Nuevo</li>
</#macro>

<#macro menu>
	<@widget.menu "admin"/>
</#macro>

<#macro sidebar>
	<@widget.statistics/>
</#macro>


<#macro body>

	<@page.section>
		<@page.box title="Subir archivo">
			<iframe id="uploadFrameID" name="uploadFrame" height="0" width="0" frameborder="0" scrolling="yes"></iframe>
			<form id="target" enctype="multipart/form-data" method="post" target="uploadFrame">
				<div class="message inner green">
					<span><b>Succes</b>: El archivo se subio con exito</span>
				</div>
				<@form.row label="Descripci&oacute;n">
					<input type="text" name="description" class='{validate:{required:true, messages:{required:"Ingrese una descripción para el archivo"}}}'>				
				</@form.row>
				
				<@form.row label="Versi&oacute;n">
					<input type="text" name="version" class='{validate:{required:true, messages:{required:"Ingrese una versión para el archivo"}}}'>									
				</@form.row>
				
				<@form.row label="Tipo">
					<#assign mapValues = {
						"shp": "ShapeFile (comprimido en zip)", "png", "PNG",
						"jpg": "JPG", "rpt" : "Reporte", "wf": "Workflow","otro": "Otro"}>  
					<@form.mapSelect name="type" map=mapValues/>
				</@form.row>	

				<@form.row label="Archivo a subir">
					<input type="file" name="filename" class='{validate:{required:true, messages:{required:"Seleccione un archivo"}}}'>
				</@form.row>
				
				<@form.row label="%">
					<span class="percent"></span>
					<div id="progress" class="progressbar-count" value="0%"></div>
				</@form.row>

				<@form.submit>Subir</@form.submit>				
			</form>
		</@page.box>
	</@page.section>
</#macro>