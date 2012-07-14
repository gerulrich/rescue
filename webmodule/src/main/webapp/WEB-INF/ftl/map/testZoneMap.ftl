<@layout.main breadcrumbs=breadcrumbs menu=menu sidebar=sidebar body=body script=script/>

<#macro script>
<style type="text/css">
		@import url("<@spring.url '/static/js/cloudmade/leaflet.css'/>");
</style>

<script type="text/javascript" src="<@spring.url '/static/js/cloudmade/leaflet-src.js'/>"></script>
<script>

	var map = null;
	var geojsonLayer = null;
	$(document).ready(function() {

		$("form").submit(function() {
			geojsonLayer.clearLayers();
			loadZones($("#zoneName").val(), $("#zoneType").val());
			return false;
		});
		
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
		
		
		
		geojsonLayer = new L.GeoJSON();
		map.addLayer(geojsonLayer);
		
		var overlays = {
			"Zonas": geojsonLayer,
		};

		var layersControl = new L.Control.Layers(baseMaps, overlays);
	
		map.addControl(layersControl);
		//
		
		geojsonLayer.on("featureparse", function (e){
			var style = {
				weight: 2,
            	color: "#999",
            	opacity: 1,
            	fillColor: "#B0DE5C",
            	fillOpacity: 0.6            		
			};
			e.layer.setStyle(style);
    		
		});		
	});
	
	
	function loadZones(name, type) {
		var urlData = "<@spring.url '/zone?type='/>"+type+"&name="+name; 
		$.ajax({
			url: urlData,
			//dataType: 'text',
			success: function(zones) {
        		geojsonLayer.addGeoJSON(zones);
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
				Busqueda por zones
				<span class="hide show"></span>
			</div>


			<div class="content">
				<form method="post">
					<div class="row">
						<label>Zonas</label>
						<div class="right">
							<select name="zoneType" id="zoneType">
								<option value=""></option>
							<#list zones as zone>
								<option value="${zone}">${zone}</option>							
							</#list>
							</select>
						</div>
					</div>
					
					<div class="row">
						<label>Nombre</label>
						<div class="right"><input type="text" name="zoneName" id="zoneName"></div>
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