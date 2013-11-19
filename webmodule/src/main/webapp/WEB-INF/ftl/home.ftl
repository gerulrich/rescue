<@layout.main breadcrumbs=breadcrumbs menu=menu sidebar=sidebar body=body script=script/>


<#macro script>
	<script type="text/javascript" src="http://people.iola.dk/olau/flot/jquery.flot.js"></script>
	<script type="text/javascript" src="<@spring.url '/static/js/graphs/graphs.js'/>"></script>
	<!--script type="text/javascript" src="http://people.iola.dk/olau/flot/jquery.flot.selection.js"></script-->
<script type="text/javascript">

$(document).ready(function() {
	 
	 function updateTopMethodError() {
	 	$.ajax({
			url: "stats/topErrorMethods/",
			method: 'GET',
			dataType: 'json',
			success: function(data) {
				$("#topErrorMethod tbody").find("tr").remove();
				for(var i=0;i<data.length;i++) {
					var partsOfStr = data[i].x.split('.');
					var logUrl = "<@spring.url '/log/get/'/>"+partsOfStr[0]+"/"+partsOfStr[1];
					appendRow3("#topErrorMethod", data[i].x, data[i].y.toFixed(3)+"%", logUrl);
				}			
			}
		});		 
	 }
	 
	 function updateTopMethod() {
		$.ajax({
			url: "stats/topMethods/",
			method: 'GET',
			dataType: 'json',
			success: function(data) {
				$("#topMethod tbody").find("tr").remove();
				for(var i=0;i<data.length;i++) {
					appendRow("#topMethod", data[i].x, data[i].y.toFixed(1)+"%");
				}			
			}
		});	 
	 }
	 
	 function dropLog() {
		$.ajax({
			url: "log/drop/",
			method: 'GET',
			dataType: 'json',
			success: function(data) {
				$("#topMethod tbody").find("tr").remove();
				for(var i=0;i<data.length;i++) {
					//appendRow("#topMethod", data[i].x, data[i].y.toFixed(1)+"%");
				}			
			}
		});	 
	 }	 
	 
	 createGraph("#placeholder", "stats/request/PER_MINUTE");
	 createGraph("#placeholderavg", "stats/avg/PER_MINUTE");
	 updateTopMethod();
	 updateTopMethodError();
	 
	
	function appendRow(selector, col1, col2) {
		$(selector).find('tbody')
  			  .append($('<tr>')
				.append($('<td>')
					.text(col1)
    			)
    			.append($('<td>')
					.text(col2)
    			)
		);
	}
	
	function appendRow3(selector, col1, col2, col3) {
		$(selector).find('tbody')
  			  .append($('<tr>')
				.append($('<td>')
					.text(col1)
    			)
    			.append($('<td>')
					.text(col2)
    			)
    			.append($('<td>')
					.append($('<a href="' + col3 + '">Log</a>'))
    			)
		);
	}
	
	$("#logError").click(function(){
		saveLog("ERROR");
	});
	
	$("#logOk").click(function(){
		saveLog("OK");
	});
	
	$("#logDrop").click(function(){
		dropLog();
	});	
	
	$("#update").click(function(){
		updateGraph("#placeholder", "stats/request/PER_MINUTE");
		updateTopMethod();		
		updateTopMethodError();
	});
	
	function saveLog(status) {
	 $.ajax({
		url: "log/save/"+status,
		method: 'GET',
		dataType: 'json',
		success: function(data) {
						
		}
	});	
	}
	 
});

</script>
</#macro>

<#macro breadcrumbs>
	<li><a href="<@spring.url '/'/>">Dashboard</a></li>
</#macro>

<#macro menu>
	<@widget.menu "dashboard"/>
</#macro>

<#macro sidebar>
	<@widget.statistics/>
</#macro>

<#macro body>
	<div class="btn-box">
		<div class="content">
			<@widget.hasPermission "USER_MANAG">
			<a href="<@spring.url '/user/list'/>" class="item">
				<img src="<@spring.url '/static/gfx/icons/big/user.png'/>" alt="Usuarios">
				<span>Usuarios</span>
				Admin. de usuarios
			</a>
			<a href="<@spring.url '/role/list'/>" class="item">
				<img src="<@spring.url '/static/gfx/icons/big/users.png'/>" alt="Roles">
				<span>Roles</span>
				Admin. de Roles
			</a>			
			</@widget.hasPermission>
			
			<@widget.hasPermission "MONGO_CONSOLE">
			<a href="<@spring.url '/mongo/list'/>" class="item">
				<img src="<@spring.url '/static/gfx/icons/big/database.png'/>" alt="MongoDB">
				<span>MongoDB</span>
				Colecciones de MongoDB
			</a>
			</@widget.hasPermission>
			
			<@widget.hasPermission "SQL_CONSOLE">
			<a href="<@spring.url '/sql/list'/>" class="item">
				<img src="<@spring.url '/static/gfx/icons/big/database.png'/>" alt="SqlDB">
				<span>SqlDB</span>
				Tablas de Mysql
			</a>
			</@widget.hasPermission>			
			
			<@widget.hasPermission "CONFIG">
			<a href="<@spring.url '/config/list'/>" class="item">
				<img src="<@spring.url '/static/gfx/icons/big/settings.png'/>" alt="configuracion">
				<span>Configuraci&oacute;n</span>
				Par&aacute;metros de conf.
			</a>
			</@widget.hasPermission>
			
			<@widget.hasPermission "FILE_UPLOAD">
			<a href="<@spring.url '/file/list'/>" class="item">
				<img src="<@spring.url '/static/gfx/icons/big/file.png'/>" alt="archivos">
				<span>Archivos</span>
				Gesti&oacute;n de archivos
			</a>
			</@widget.hasPermission>			
		</div>
	</div>
	
	<div id="result"></div>
	<div id="msg"></div>	

	<@widget.hasPermission "STATISTICS">
		
		<@page.half>
			<@page.box title="Metodos m&aacute;s ejecutados">
			<table id="topMethod" cellspacing="0" cellpadding="0" border="0">
				<thead>
					<tr>
						<th>Nombre</th>
						<th>Porcentage</th>
					</tr>
				</thead>
				<tbody>
				</tbody>
			</table>
			</@page.box>		
		</@page.half>
		
		<@page.half>
			<@page.box title="Metodos con m&aacute;s errores">
			<table id="topErrorMethod" cellspacing="0" cellpadding="0" border="0">
				<thead>
					<tr>
						<th>Nombre</th>
						<th>Porcentage</th>
						<th>Log</th>
					</tr>
				</thead>
				<tbody>
				</tbody>
			</table>
			
			<#if debug>
			<div class="content">
				<div class="row">
					<div class="let">
						<button id="logError" type="submit" class="medium red linkopen"><span>Log error</span></button>
						<button id="logDrop" type="submit" class="medium red linkopen"><span>Borrar log</span></button>
					</div>
				</div>
				<div class="row">
					<div class="left">
						<button id="logOk" type="submit" class="medium green linkopen"><span>Log ok</span></button>
					</div>
				</div>
				<div class="row">
					<div class="left">
						<button id="update" type="submit" class="medium linkopen"><span>Actualizar</span></button>
					</div>
				</div>
			</div>
				
			</#if>
			</@page.box>		
		</@page.half>
		
		
		
		<@page.section>
			<@page.box title="Gr&aacute;fico - Request por Minuto">
				<div id="placeholder" style="width:100%;height:350px; text-align: left;"></div>
				<div class="dataTables_wrapper">
					<div>
						<div>
							<select>
								<option value="1" selected="selected">Ultima hora</option>
								<option value="2">Ultimas 24 horas</option>
								<option value="3">Ultima semana</option>
							</select>
						</div>
					</div>
				</div>
			</@page.box>
		</@page.section>
		
		<@page.section>
			<@page.box title="Gr&aacute;fico - Tiempo promedio request">
				<div id="placeholderavg" style="width:100%;height:350px; text-align: left;"></div>			
				<div class="dataTables_wrapper">
					<div>
						<div>
							<select>
								<option value="1" selected="selected">Ultima hora</option>
								<option value="2">Ultimas 24 horas</option>
								<option value="3">Ultima semana</option>
							</select>
						</div>
					</div>
				</div>
			</@page.box>
		</@page.section>		
	</@widget.hasPermission>		
	
</#macro>