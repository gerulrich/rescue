<@layout.main script=script breadcrumbs=breadcrumbs menu=menu sidebar=sidebar body=body/>

<#macro script>

<script>
	$(document).ready(function() {
		
		$("#target").validate({
			meta: "validate",
			submitHandler: function(form) {
				$.get('<@spring.url "/report/token"/>', function(data) {
  					$('#target').find('input[name="token"]').val(data);
  					$('#waitmessage').show();
    				setTimeout ( 'progress("'+data+'")', 500 );
  					form.submit();
				});
			}
		});
	});
	
	function progress(token) {
    	$.ajax({
			url: '<@spring.url '/report/progress?token='/>'+token,
    		success: function(data) {
    			if (data == token) {
    				setTimeout ( 'progress("'+token+'")', 500 );
				} else {
					$('#waitmessage').hide();
					$('#okmessage').show();
				}
			}
		});
	}
	
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
				Ejecutar reporte
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
				<iframe id="uploadFrameID" name="uploadFrame" height="0" width="0" frameborder="0" scrolling="yes"></iframe>
				<form id="target" action="<@spring.url '/report/generate/${report.id}'/>" method="get" target="uploadFrame">
					<input type="hidden" name="token" value=""/>
					<#list metadata.parameters as param>
					<div class="row">
						<label>${param.label}</label>
						<div class="right">
							<#if param.multiple = true>
							<select name="${param.name}">
  								<option value="aa">AAA</option>
							</select>
							<#else>
							<input type="text"
								   name="${param.name}"
								   <#if param.required>
								   class='{validate:{required:true, messages:{required:"Ingrese una valor para el campo ${param.label}"}}}'
								   </#if>
							/>
							</#if>
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