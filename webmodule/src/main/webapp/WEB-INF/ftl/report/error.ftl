<@layout.main breadcrumbs=breadcrumbs menu=menu sidebar=sidebar body=body/>

<#macro breadcrumbs>
	<li><a href="<@spring.url '/'/>">Dashboard</a></li>
	<li><a href="<@spring.url '/report/list/'/>">Reportes</a></li>
	<li>Ejecutar</li>
</#macro>

<#macro menu>
	 <script>
	 $(document).ready(function() { 
	 	$("#volver").click(function(){
    		window.location = "<@spring.url '/report/list/'/>";
		});
     });
	 </script>
	<@widget.menu "admin"/>
</#macro>

<#macro sidebar>
	<@widget.statistics/>
</#macro>

<#macro body>
	<@page.section>
		<@page.box title="Error al Ejecutar reporte">
			<h2>Se produjo el siguiente error al compilar el manifiesto del reporte:</h2><br/>
			<div id="errors">
				<pre>${error}</pre>
			</div>
			<br/>
			<button id="volver" type="submit" class="medium red linkopen"><span>Volver</span></button>		
		</@page.box>
	</@page.section>
</#macro>