<@layout.main breadcrumbs=breadcrumbs menu=menu sidebar=sidebar body=body script=script/>

<#macro script>
<style type="text/css">
		@import url("<@spring.url '/static/js/cloudmade/leaflet.css'/>");
</style>

        <style type="text/css">
            #map{
            }
        </style>

<script type="text/javascript" src="<@spring.url '/static/js/cloudmade/leaflet-src.js'/>"></script>
<script type="text/javascript" src="<@spring.url '/static/js/cloudmade/heatcanvas-leaflet.js'/>"></script>
<script type="text/javascript" src="<@spring.url '/static/js/cloudmade/heatcanvas.js'/>"></script>
<script>

	var map = null;
	$(document).ready(function() {

		map = new L.Map('map_canvas');

		var heatmap = new L.TileLayer.HeatCanvas("Heat Canvas", map, {},
                        	{'step':0.3, 'degree':HeatCanvas.QUAD, 'opacity':0.7});
		heatmap.pushData(-34.6083, -58.3716, 14);
		heatmap.pushData(-34.6154, -58.3824, 14);
		
		
		
		heatmap.pushData(-34.608521,-58.37926836, 14);
		heatmap.pushData(-34.61435816,-58.37384214, 14);
		heatmap.pushData(-34.61196077,-58.37299148, 14);
		heatmap.pushData(-34.61833121,-58.38060644, 14);
		heatmap.pushData(-34.60195675,-58.38826424, 14);
		heatmap.pushData(-34.60554286,-58.36116262, 14);
		heatmap.pushData(-34.6159943,-58.3612528, 14);
		heatmap.pushData(-34.6036773,-58.3707297, 14);
		heatmap.pushData(-34.61063301,-58.37651722, 14);
		heatmap.pushData(-34.61582143,-58.38183973, 14);
		heatmap.pushData(-34.60764802,-58.38733613, 14);
		heatmap.pushData(-34.61116936,-58.36978046, 14);
		heatmap.pushData(-34.59871332,-58.36624681, 14);
		heatmap.pushData(-34.61251655,-58.37938385, 14);
		heatmap.pushData(-34.61321638,-58.36910873, 14);
		heatmap.pushData(-34.62031718,-58.36028722, 14);
		heatmap.pushData(-34.60578121,-58.38293967, 14);
		heatmap.pushData(-34.60637401,-58.37391255, 14);
		heatmap.pushData(-34.62004449,-58.37228507, 14);
		heatmap.pushData(-34.61807431,-58.38028622, 14);
		heatmap.pushData(-34.61174799,-58.36102502, 14);
		heatmap.pushData(-34.61375445,-58.37476942, 14);
		heatmap.pushData(-34.61343818,-58.37336673, 14);
		heatmap.pushData(-34.61945336,-58.37532429, 14);
		heatmap.pushData(-34.59966072,-58.37057619, 14);
		heatmap.pushData(-34.62012115,-58.3793699, 14);
		heatmap.pushData(-34.61285789,-58.38723216, 14);
		heatmap.pushData(-34.61004371,-58.36558989, 14);
		heatmap.pushData(-34.60111178,-58.36090742, 14);
		heatmap.pushData(-34.60921783,-58.37735305, 14);
		
		
		heatmap.pushData(-34.622631,-58.446449, 14);
		heatmap.pushData(-34.58924404,-58.42087162, 14);
		heatmap.pushData(-34.65692907,-58.45009373, 14);
		heatmap.pushData(-34.63333652,-58.44705957, 14);
		heatmap.pushData(-34.64415604,-58.47612251, 14);
		heatmap.pushData(-34.64526934,-58.41222102, 14);
		heatmap.pushData(-34.64726731,-58.46124061, 14);
		heatmap.pushData(-34.61140008,-58.46885926, 14);
		heatmap.pushData(-34.58674229,-58.44873783, 14);
		heatmap.pushData(-34.6218778,-58.4425375, 14);
		heatmap.pushData(-34.59307917,-58.41352942, 14);
		heatmap.pushData(-34.62163884,-58.43564559, 14);
		heatmap.pushData(-34.65885056,-58.42861737, 14);
		heatmap.pushData(-34.60901064,-58.41839489, 14);
		heatmap.pushData(-34.65204917,-58.41594197, 14);
		heatmap.pushData(-34.64563763,-58.42924777, 14);
		heatmap.pushData(-34.59306146,-58.42100404, 14);
		heatmap.pushData(-34.62861867,-58.48557068, 14);
		heatmap.pushData(-34.66008408,-58.46943992, 14);
		heatmap.pushData(-34.64048144,-58.44347168, 14);
		heatmap.pushData(-34.63241387,-58.49894688, 14);
		heatmap.pushData(-34.64547999,-58.42023882, 14);
		heatmap.pushData(-34.64709196,-58.47051731, 14);
		heatmap.pushData(-34.62377489,-58.48061859, 14);
		heatmap.pushData(-34.62436793,-58.42023359, 14);
		heatmap.pushData(-34.62423528,-58.39587291, 14);
		heatmap.pushData(-34.6543197,-58.45721078, 14);
		heatmap.pushData(-34.63979764,-58.43174734, 14);
		heatmap.pushData(-34.5963726,-58.48843255, 14);
		heatmap.pushData(-34.6176854,-58.44512906, 14);
		heatmap.pushData(-34.59889054,-58.43373544, 14);
		heatmap.pushData(-34.61086949,-58.49865702, 14);
		heatmap.pushData(-34.63845678,-58.48466336, 14);
		heatmap.pushData(-34.62404882,-58.48937841, 14);
		heatmap.pushData(-34.6310623,-58.48563433, 14);
		heatmap.pushData(-34.62420934,-58.39967092, 14);
		heatmap.pushData(-34.59675358,-58.41755975, 14);
		heatmap.pushData(-34.59275989,-58.46905916, 14);
		heatmap.pushData(-34.59738478,-58.47766179, 14);
		heatmap.pushData(-34.66135866,-58.44983992, 14);
		heatmap.pushData(-34.66160603,-58.43941722, 14);
		
		map.addLayer(heatmap);

		var googleMaps = new L.TileLayer('<@spring.url '/tiles/{z}/{x}/{y}/map.google.street/'/>', {
			maxZoom: 18
		});
			
		var osmMap = new L.TileLayer('<@spring.url '/tiles/{z}/{x}/{y}/map.osm/'/>', {
			maxZoom: 18
		});	
			

		map.addLayer(googleMaps).setView(new L.LatLng(-34.468861, -58.519218), 13);

		var baseMaps = {
			"Google": googleMaps,
			"Open Street Map": osmMap
		};

		var layersControl = new L.Control.Layers(baseMaps);
		map.addControl(layersControl);
		
		
		
		
		$("form").submit(function() {
			locateAddr($('#loc').val(), $('#nro').val());
			return false;
		});
		
	});
	
	function locateAddr(loc, nro) {
		$.ajax({
			url: '<@spring.url '/locate?loc='/>'+loc+"&nro="+nro,
			dataType: 'json',
			success: function(location) {
        		var marker = new L.Marker(new L.LatLng(location.y, location.x));
        		map.setView(new L.LatLng(location.y, location.x), 15);
        		marker.bindPopup(location.name);
				map.addLayer(marker);
        		
        		
			}
		});
	}
	
	/*function loadData() {
	
		$.ajax({
			url: '<@spring.url '/locate?loc=suip&nro=150'/>',
			dataType: 'json',
    		success: function(location) {
        		var marker = new L.Marker(new L.LatLng(location.y, location.x));
				
		}});	
	
	}*/
   	
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
		<div class="box">
			<div class="title">
				Mapa de ejemplo para busquedas
				<span class="hide show"></span>
			</div>
			<div class="content show">
				<div id="map_canvas" style="width: 100%; height: 400px"></div>
			</div>
		</div>

		<div class="box">
			<div class="title">
				Busqueda por calle y altura
				<span class="hide show"></span>
			</div>


			<div class="content">
				<form method="post">
					<div class="row">
						<label>Calle</label>
						<div class="right"><input type="text" name="loc" id="loc"></div>
					</div>
					
					<div class="row">
						<label>Numero</label>
						<div class="right"><input type="text" name="nro" id="nro"></div>
					</div>
					
					<div class="row">
						<label></label>
						<div class="right">
							<button id="boton" type="submit" class="green"><span>Ubicar</span></button>
						</div>
					</div>
				</form>
			</div>
		
		</div>
		
		
	</div>


</#macro>