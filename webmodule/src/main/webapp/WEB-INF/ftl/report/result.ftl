<@layout.main script=script breadcrumbs=breadcrumbs menu=menu sidebar=sidebar body=body/>

<#macro script>
  <script src="http://localhost:8000/socket.io/socket.io.js"></script>
  <script>
	$(document).ready(function() {
		
		$("#resultOk").hide();
		$("#resultError").hide();
		$("#volverOk").click(function(){ window.location = "<@spring.url '/report/list/'/>";});
		$("#volverError").click(function(){ window.location = "<@spring.url '/report/list/'/>";});
		
		var socket = io.connect('http://localhost:8000', { rememberTransport: false, transports: ['WebSocket', 'Flash Socket', 'AJAX long-polling']});
		socket.on('news', function (data) {
			console.log(data);
			socket.emit('register', { data:'${report.id}'});
		});
		
		socket.on('disconnect', function () {
   			console.log('disconnect client event...');
		});		
		
		socket.on('updateui', function (data) {
			console.log(data);
			if (data.code == "STARTED") {
				started(data.uuid, data.message);
			} else if (data.code == "OK") {
				ok(data.uuid, data.progress);
			} else {
				error(data.uuid);
			}
		});
		
		function started(uuid, message) {
			$( "#events" ).append( "<ul>"+message+" <span id='"+uuid+"'>...</span></ul>")
		}
	
		function ok(uuid, progress) {
			$( "#"+uuid ).text( "OK" );
			setTimeout("setProgress("+progress+")",100+progress);
			if (progress == 100) {
				$("#resultOk").show();
				socket.disconnect();
			}
		}
		
		function error(uuid) {
			$( "#"+uuid ).text( "ERROR" );
			setTimeout("setProgress(0)",100);
			$("#resultError").show();
			socket.disconnect();
		}
		
	});
	
	function setProgress(progress) {
		$('#progress').progressbar({value: progress});
		$('#progress').prev(".percent").text(progress+"%");
	}
	
</script>

</#macro>

<#macro breadcrumbs>
	<li><a href="<@spring.url '/'/>">Dashboard</a></li>
	<li><a href="<@spring.url '/report/list/'/>">Reportes</a></li>
	<li>Ejecutar</li>
</#macro>

<#macro menu>
	<@widget.menu "reports"/>
</#macro>

<#macro sidebar>
	<@widget.statistics/>
</#macro>


<#macro body>
	<@page.section>

		<@page.box title="Generando reporte...">
			<blockquote>Aguarde unos instantes, el reporte est&aacute; siendo generado. Esta acci&oacute;n
				puede demorar algunos minutos.</blockquote>
		
			<div class="right">
				<span class="percent"></span>
				<div id="progress" class="progressbar-count" value="0%"></div>
			</div>
			<ul id="events" class="tick"></ul>
			
			<div id="resultOk">
				<blockquote id="fileLink">
					El reporte estar&aacute; disponible durante las pr&oacute;ximas 24 horas para descargado desde la opci&oacute;n "Mis reportes".
					Para descarlo ahora haga click <a href="<@spring.url '/report/download/${report.id}'/>">aqui</a></blockquote>
				<p>
					<button id="volverOk" type="submit" class="medium green linkopen"><span>Volver</span></button>
				</p>
			</div>
			
			<div id="resultError">
				<blockquote>Se produjo un error al generar el reporte. Espero unos minutos e intente nuevamente.
				Si el problema persiste contacte con el administrador.</blockquote>
				<p>
					<button id="volverError" type="submit" class="medium red linkopen"><span>Volver</span></button>
				</p>
			</div>			
			
			
		</@page.box>

			
	</@page.section>
</#macro>