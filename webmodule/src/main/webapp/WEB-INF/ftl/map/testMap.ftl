<@layout.main breadcrumbs=breadcrumbs menu=menu sidebar=sidebar body=body script=script/>

<#macro script>
<style type="text/css">
		@import url("<@spring.url '/static/css/leaflet.css'/>");
</style>
<script src="<@spring.url '/static/js/leaflet.js'/>"></script>

<script>

	var map = null;
	$(document).ready(function() {

		map = new L.Map('map_canvas');
		
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