<@layout.main breadcrumbs=breadcrumbs menu=menu sidebar=sidebar body=body script=script/>

<#macro script>
<script>

	$(document).ready(function() {
    			
    	$(".message").hide();

		$("form").submit(function() {
			$(".message").hide();
			$('#boton').hide();
			$('#progress').progressbar({value: 0});
			$('#progress').prev(".percent").text("0%");
			setTimeout ( 'progress()', 500 );
			return true;
		});
    			    			
	});

    function progress() {
    	$.ajax({
			url: '<@spring.url '/file/uploadstatus/'/>',
			dataType: 'json',
    		success: function(data) {
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
</#macro>

<#macro menu>
	<@widget.menu "admin"/>
</#macro>

<#macro sidebar>
	<@widget.statistics/>
</#macro>


<#macro body>
	<div class="section">
		<div class="message green">
			<span><b>Succes</b>: El archivo se subio con exito</span>
		</div>
	</div>

	<div class="section">
		<div class="box">
			<div class="title">
			Subir archivo<span class="hide"></span>
			</div>
			<div class="content">
			
				<iframe id="uploadFrameID" name="uploadFrame" height="0" width="0" frameborder="0" scrolling="yes"></iframe>
				<form method="post" enctype="multipart/form-data" target="uploadFrame" class="valid">
					<div class="row">
						<label>Descripci&oacute;n</label>
						<div class="right"><input type="text" name="description"></div>
					</div>

					<div class="row">
						<label>Versi&oacute;n</label>
						<div class="right"><input type="text" name="version"></div>
					</div>
					
					<div class="row">
						<label>Tipo</label>
						<div class="right">
							<select name="type">
								<option value="shp" selected="selected">ShapeFile (comprimido en zip)</option>
								<option value="png">PNG</option>
								<option value="jpg">JPG</option>
								<option value="otro">Otro</option>
							</select>
						</div>
					</div>										
					
					<div class="row">
						<label>Archivo a subir</label>
						<div class="right"><input type="file" name="filename"></div>
					</div>
					
					<div class="row">
						<label>%</label>
						<div class="right">
							<span class="percent"></span>
							<div id="progress" class="progressbar-count" value="0%"></div>
						</div>
					</div>
						
					<div class="row">
						<label></label>
						<div class="right">
							<button id="boton" type="submit" class="green"><span>Subir</span></button>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>	
</#macro>