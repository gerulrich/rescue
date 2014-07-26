<@layout.main script=script breadcrumbs=breadcrumbs menu=menu sidebar=sidebar body=body/>

<#macro script>

<script>
	$(document).ready(function() {
		// crear script que valide si se pudo conectar a la app de eventos.
	});	
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

	<div id="dialog" class="modal" title="Dialog window" style="display:none">
		<blockquote>
			Generando reporte...<br/>
			Por favor espere.
		</blockquote>
	</div>

	<div class="section">
		<div class="box">
			<div class="title">
				Ejecutar reporte: "${report.name}"
				<span class="hide"></span>
			</div>
			
			<div id="okmessage" class="message green" style="display:none">
				<span>Se ha generado el reporte correctamente. La descarga comenzar&aacute; en un momento.</span>
			</div>
			<div id="errormessage" class="message red" style="display:none"></div>
			<div id="waitmessage" class="message blue" style="display:none">
				<span>Generando el reporte. Por favor espere...</span>
			</div>
			
			<div class="content">
				<form action="<@spring.url '/report/metadata/generate/${report.id}'/>" method="post" class="valid">
					<#list report.parameters as param>
					<div class="row">
						<label>${param.label}</label>
						<div class="right">
							<#switch "${param.type}">
  								<#case "date">
  								<#assign classType="datepicker">
    							<#break>
  								<#case "number">
  								<#assign classType="onlynum">
								<#break>
  								<#default>
  								<#assign classType="">
							</#switch>
							
							<input type="text"
								   name="${param.name}"
								   value="${param.value}"
								   <#if param.required>
								   class='${classType} {validate:{required:true, messages:{required:"Ingrese una valor para el campo ${param.label}"}}}'
								   <#else>
								   class='${classType}'
								   </#if>
							/>
						</div>
					</div>
					</#list>
					
					<div class="row">
						<label></label>
						<div class="right">
							<button type="submit" class="green"><span>Ejecutar</span></button>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</#macro>