<@layout.main breadcrumbs=breadcrumbs menu=menu sidebar=sidebar body=body script=script/>

<#macro script>
<style type="text/css">
		@import url("<@spring.url '/static/js/cloudmade/leaflet.css'/>");
</style>

<script type="text/javascript" src="<@spring.url '/static/js/cloudmade/leaflet-src.js'/>"></script>
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
		
		loadData();
	});
	
	function loadData() {
	
		$.ajax({
			url: '<@spring.url '/map/poi/'/>',
			dataType: 'json',
    		success: function(data) {
        		
				var l = data.length;
				for (var i = 0; i < l; i++) {
					var marker = new L.Marker(new L.LatLng(data[i].y, data[i].x));
					marker.bindPopup(data[i].name);
					map.addLayer(marker);
				}
		}});	
	
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
		<div class="box">
			<div class="title">
				Mapa de ejemplo para busquedas
				<span class="hide show"></span>
			</div>
			<div class="content show">
				<div id="map_canvas" style="width: 100%; height: 500px"></div>
			</div>
		</div>
	</div>


</#macro>