<@layout.main breadcrumbs=breadcrumbs menu=menu sidebar=sidebar body=body script=script/>

<#macro script>
<style type="text/css">
		@import url("<@spring.url '/static/css/leaflet.css'/>");
</style>
<script src="<@spring.url '/static/js/leaflet.js'/>"></script>

<script>

	var map = null;
	var geojsonLayer = null;
	var markerGroup = null;
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

		geojsonLayer = new L.GeoJSON();
		map.addLayer(geojsonLayer);
		
		var layersControl = new L.Control.Layers(baseMaps);
	
		map.addControl(layersControl);
		
		
		
		
		$("#form1").submit(function() {
			locateAddr($('#loc').val(), $('#nro').val());
			return false;
		});
		
		$("#form2").submit(function() {
			geojsonLayer.clearLayers();
			loadZones($("#zoneName").val(), $("#zoneType").val());
			return false;
		});		
		
	});
	
	function locateAddr(loc, nro) {
		geojsonLayer.clearLayers();
		if (markerGroup != null) {
			markerGroup.clearLayers();
		}
		$.ajax({
			url: '<@spring.url '/locate?loc='/>'+loc+"&nro="+nro,
			dataType: 'json',
			success: function(location) {
				$("#results").html("");
				array = new Array();
        		for (var i = 0; i < location.length; i++) {
    				var marker = new L.Marker(new L.LatLng(location[i].y, location[i].x));
        			map.setView(new L.LatLng(location[i].y, location[i].x), 15);
        			marker.bindPopup(location[i].name);
        			$("#results").append("<li><a href='#'>"+location[i].name+"</a></li>");
        			array.push(marker);
				}
				markerGroup = L.layerGroup(array);
				map.addLayer(markerGroup);
				map.fitBounds(markerGroup.getBounds());
			}
		});
	}
	
	function loadZones(name, type) {
		var urlData = "<@spring.url '/zone?type='/>"+type+"&name="+name; 
		$.ajax({
			url: urlData,
			//dataType: 'text',
			success: function(zones) {
				geojsonLayer = L.geoJson(zones, {
					style: style,
					onEachFeature: function (feature, layer) {
         				layer.bindPopup(feature.name);
         				layer.on({
							mouseover: highlightFeature,
							mouseout: resetHighlight,
						});
     				}
				}).addTo(map);
        		map.fitBounds(geojsonLayer.getBounds());
			}
		});
		
		function highlightFeature(e) {
			var layer = e.target;

			layer.setStyle({
				weight: 2,
				color: '#000',
				dashArray: '3',
				fillOpacity: 0.7
			});

			if (!L.Browser.ie && !L.Browser.opera) {
				layer.bringToFront();
			}
		}
		
		function style(feature) {
			return {
				weight: 2,
				opacity: 1,
				color: 'white',
				dashArray: '3',
				fillOpacity: 0.7,
				fillColor: '#FED976'
			};
		}
		
		function resetHighlight(e) {
			geojsonLayer.resetStyle(e.target);
			
		}				
		
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
	
	<@page.section>
		
		<@page.half>
		<@page.box title="Mapa">
			<div class="content show">
				<div id="map_canvas" style="width: 100%; height: 400px"></div>
			</div>
		</@page.box>
		</@page.half>
		
		<@page.half>
			

		<@page.box title="Buscar" padding=false>
			<div class="tabs">
				<div class="tabmenu">
					<ul> 
						<li><a href="#tabs-1">Direcci&oacute;n</a></li> 
						<li><a href="#tabs-2">Zonas</a></li> 
						<li><a href="#tabs-3">Puntos de inter&eacute;s</a></li> 
					</ul>
				</div>
							
				<div class="tab" id="tabs-1">
					<form id="form1" method="post">
						<@form.row label="Calle">
							<input type="text" name="loc" id="loc">						
						</@form.row>
						<@form.row label="Numero">
							<input type="text" name="nro" id="nro">						
						</@form.row>
					<@form.submit>Buscar</@form.submit>
					</form>
				</div>
								
				<div class="tab" id="tabs-2">
					<form id="form2" method="post">
						<@form.row label="Zonas">
							<select name="zoneType" id="zoneType">
								<option value=""></option>
								<#list zones as zone>
								<option value="${zone}">${zone}</option>							
								</#list>
							</select>
						</@form.row>
					
						<@form.row label="Nombre">
							<input type="text" name="zoneName" id="zoneName">
						</@form.row>
					
						<@form.submit>Ubicar</@form.submit>
					</form>
				</div>
								
				<div class="tab" id="tabs-3">
					<h2>The third tab</h2>
					<p>tab 3</p>
				</div>
			</div>
		</@page.box>			
		</@page.half>
		
		<@page.half>
			<@page.box title="Resultados">
				<ul id="results" class="comments">
					<li><b>Nombre:</b></li>
				<ul>
			</@page.box>
		</@page.half>

	</@page.section>
</#macro>