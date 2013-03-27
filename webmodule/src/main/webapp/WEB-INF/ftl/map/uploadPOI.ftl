<@layout.main breadcrumbs=breadcrumbs menu=menu sidebar=sidebar body=body script=script/>

<#macro script>
<script>

	$(document).ready(function() {

		//$(".message").hide();

		$("#target").validate({
			meta: "validate",
			submitHandler: function(form) {
				d = "";
				try { d = $('form').serialize(); } catch(e){};
				$.ajax({
            		type: 'POST',
            		url: '<@spring.url '/shp/upload/poi/${file.id}'/>',
            		dataType: 'json',
            		data: d,
            		success: function(data) {
                		$("#waitmessage").hide();
                		if (data.code == 0) {
                			$("#okmessage").html("<span>La capa se ha cargado correctamente con "+data.data+" registros</span>");
                			$("#okmessage").show();
                		} else {
                			$("#errormessage").html("<span>"+data.data+"</span>");
                			$("#errormessage").show();
                		}
                		$('#boton').show();
            		}
        		});
        		$("#waitmessage").show();
				$("#okmessage").hide();
				$('#boton').hide();        		
				setTimeout ( 'progress()', 500 );
			}
		});
	});




    function progress() {
    	$.ajax({
			url: '<@spring.url '/shp/processstatus/'/>',
			dataType: 'json',
    		success: function(data) {
    			if (data.total == 0) {
    				data.total = 100;
    			}
        		var porcentage = Math.floor(100 * parseInt(data.uploaded) / parseInt(data.total));
				if ( porcentage == 100 ) {
					$('#progress').progressbar({value: 100});
					$('#progress').prev(".percent").text("100%");
					//$(".message").show();
					//$('#boton').show();
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
	<li><a href="<@spring.url '/shp/upload'/>">Cargar capa desde archivo</a></li>
	<li>Puntos de inter&eacute;s</li>	
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
			<@page.box title="Subir shapefile como capa de Puntos de Inter&eacute;s">
				<form id="target" method="post">
					<div id="okmessage" class="message inner green" style="display:none"></div>
					<div id="errormessage" class="message inner red" style="display:none"></div>
					<div id="waitmessage" class="message inner blue" style="display:none">
						<span>Procesando datos. Por favor espere...</span>
					</div>				
					<@form.row label="Campo Nombre">
						<@form.simpleSelect name="nameField"list=fields/>
					</@form.row>
					
					<@form.row label="Campo Tipo">
						<@form.simpleSelect name="typeField"list=fields/>
					</@form.row>
					
					<@form.row label="">
						<input type="checkbox" id="append" name="overwrite"/>
						<label for="append">Sobreescribir datos</label>
					</@form.row>
					
					<@form.submit>Finalizar</@form.submit> 		
						
				</form>
			</@page.box>
		</@page.half>

		<@page.half>
			<@page.box title="Informaci&oacute;n del archivo">
				<ul class="comments">
					<li><b>Nombre:</b> ${file.filename}</li>
					<li><b>Descripci&oacute;n:</b> ${file.description}</li>
					<li><b>Versi&oacute;n:</b> ${file.version}</li>
					<li><b>Tama&ntilde;o:</b> ${file.sizeReadable}</li>
					<li><b>Fecha:</b> ${file.date?datetime}</li>
					<li>
						<div class="right">
							<span class="percent"></span>
							<div id="progress" class="progressbar-count" value="0%"></div>
						</div>
					</li>
				</ul>
			</@page.box>
		</@page.half>
		
	</@page.section>
</#macro>