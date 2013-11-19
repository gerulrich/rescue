<@layout.main script=script breadcrumbs=breadcrumbs menu=menu sidebar=sidebar body=body/>

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

	<div id="dialog" class="modal" title="Dialog window" style="display:none">
		<blockquote>
			Generando reporte...<br/>
			Por favor espere.
		</blockquote>
	</div>

	<div class="section">
		<div class="box">
			<div class="title">
				Descargar reporte
				<span class="hide"></span>
			</div>
			<div class="content">
				<blockquote>
					Para descargar el reporte haga clic <a href="<@spring.url '/report/list/'/>">aqu&iacute;</a>
				</blockquote>
			</div>
			
		</div>
	</div>
</#macro>