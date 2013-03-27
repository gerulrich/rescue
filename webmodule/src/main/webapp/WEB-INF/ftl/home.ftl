<@layout.main breadcrumbs=breadcrumbs menu=menu sidebar=sidebar body=body script=script/>


<#macro script>
	<script type="text/javascript" src="http://people.iola.dk/olau/flot/jquery.flot.js"></script>
	<!--script type="text/javascript" src="http://people.iola.dk/olau/flot/jquery.flot.selection.js"></script-->
<script type="text/javascript">

$(document).ready(function() {
	 
	var options = {
		xaxis: { mode: "time", tickLength: 1},
		yaxis: { ticks: 10, min: 0 },
		legend: { show: false },
		series:{
    		lines: { fill: true },
    		color: "#1E90FF",
    		shadowSize: 0
		},
		grid: {
			borderWidth: 2,
			minBorderMargin: 20,
			labelMargin: 10,
			backgroundColor: {
				colors: ["#fff", "#e4f4f4"]
			},			
		}         
    };
    var data = [];
    var placeholder = $("#placeholder");
    
    $.plot(placeholder, data, options);
    
    // fetch one series, adding to what we got
    var alreadyFetched = {};
    
	$.ajax({
		url: "stats/request",
		method: 'GET',
		dataType: 'json',
		success: onDataReceived
	});  
	 
	 // then fetch the data with jQuery
     function onDataReceived(series) {
		// extract the first coordinate pair so you can see that
		// data is now an ordinary Javascript object
		var firstcoordinate = '(' + series.data[0][0] + ', ' + series.data[0][1] + ')';

		// first correct the timestamps - they are recorded as the daily
		// midnights in UTC+0100, but Flot always displays dates in UTC
		// so we have to add one hour to hit the midnights in the plot
		for (var i = 0; i < series.data.length; ++i) {
			series.data[i][0] -= 60 * 60 * 3000;
			//series.data[i][1]++;
		}

		// let's add it to our current data
		if (!alreadyFetched[series.label]) {
			alreadyFetched[series.label] = true;
			data.push(series);
		} else {
			data = [];
			data.push(series);
		}

		// and plot all we got
		$.plot(placeholder, data, options);
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
		<@page.section>
			<@page.box title="Gr&aacute;fico - Request">
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
	</@widget.hasPermission>		
	
</#macro>