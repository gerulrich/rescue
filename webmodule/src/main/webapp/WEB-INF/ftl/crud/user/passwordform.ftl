<@layout.main breadcrumbs=breadcrumbs menu=menu sidebar=sidebar body=body/>

<#macro breadcrumbs>
	<li><a href="<@spring.url '/'/>">Dashboard</a></li>
	<li><a href="<@spring.url '/user/list'/>">Usuarios</a></li>
	<li>Cambiar contraseña de ${user.displayName}</li>
</#macro>

<#macro menu>
	<@widget.menu "admin"/>
</#macro>

<#macro sidebar>
	<@widget.statistics/>
</#macro>


<#macro body>

	<@page.section>
	
		<#if passwordForm??>
			<@spring.bind "passwordForm"/>
			<#if spring.status.error>
				<div class="message red">
					<span><@spring.showErrors "<br/>"/></span>
				</div>
			</#if>
		</#if>
		
		<#if "0" = result!>
			<div class="message green">
				<span><b>Succes</b>: La contrase&ntilde;a se ha cambiado con exito</span>
			</div>
		</#if>		
	
		<@page.box title="Cambiar la contrase&ntilde;a del usuario ${user.displayName}">
			<form  action="<@spring.url '/user/password/${user.id}'/>" method="post" class="valid">
				
				<@form.row label="Contraseña">
					<@spring.formPasswordInput "form.password" "class='{validate:{required:true, messages:{required:\"Ingrese una contraseña\"}}}'"/>
					<@spring.showErrors "<br/>"/>
				</@form.row>
			
				<@form.row label="Repita la contraseña">
					<@spring.formPasswordInput "form.passwordVerification" "class='{validate:{required:true, messages:{required:\"Repita la contraseña\"}}}'"/>
					<@spring.showErrors "<br/>"/>				
				</@form.row>			
				
				<@form.submit>Guardar</@form.submit>							
			</form>
		</@page.box>
		 
	</@page.section>
</#macro>