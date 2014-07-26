<@layout.main breadcrumbs=breadcrumbs menu=menu sidebar=sidebar body=body/>


<#macro breadcrumbs>
	<li><a href="<@spring.url '/'/>">Dashboard</a></li>
	<li><a href="<@spring.url '/user/list'/>">Usuarios</a></li>
	<li>Nuevo usuario</li>
</#macro>

<#macro menu>
	<@widget.menu "admin"/>
</#macro>

<#macro sidebar>
	<@widget.statistics/>
</#macro>


<#macro body>

	<#if entity.id??>
		<#assign title="Editar usuario."/>
		<#assign url="/user/edit/${entity.id}"/>
	<#else>
		<#assign title="Alta de usuario"/>
		<#assign url="/user/new"/>
	</#if>

	<@page.section>
	
		<#if passwordForm??>
			<@spring.bind "passwordForm"/>
			<#if spring.status.error>
				<div class="message red">
					<span><@spring.showErrors "<br/>"/></span>
				</div>
			</#if>
		</#if>
	
		<@page.box title=title>
			<form  action="<@spring.url '${url}'/>" method="post">
				<@form.row label="Email">
					<@spring.formInput  "entity.username" "class='{validate:{required:true, email:true, messages:{email:\"Ingrese una direccion de E-mail valida.\", required:\"Ingrese una dirección de E-mail\"}}}'"/>
					<@spring.showErrors "<br/>"/>				
				</@form.row>
				
				<@form.row label="Nombre a visualizar">
					<@spring.formInput  "entity.displayName" "class='{validate:{required:true, messages:{required:\"Ingrese el nombre del usuario.\"}}}'"/>
					<@spring.showErrors "<br/>"/>				
				</@form.row>			
						
				<@form.row label="Roles">
					<@spring.formInput  "entity.roles" "class='{validate:{required:true, messages:{required:\"Ingrese los roles del usuario.\"}}}'"/>
					<@spring.showErrors "<br/>"/>				
				</@form.row>						
						
				<@form.row label="N&uacute;mero de agente">
					<@spring.formInput  "entity.account.agentNumber"/>
					<@spring.showErrors "<br/>"/>				
				</@form.row>
						
				<@form.row label="Contrase&ntilde;a del agente">
					<@spring.formInput  "entity.account.agentPassword"/>
					<@spring.showErrors "<br/>"/>				
				</@form.row>
				
				<@form.row label="Grupo">
					<@form.pojoSelect "group" groups "id" "name"/>
					<@spring.showErrors "<br/>"/>
				</@form.row>
				
				<#if entity.id??>
				
					<@form.row label="">
						<@spring.formCheckbox  "entity.account.enabled"/>
						<label for="account.enabled">Habilitado</label>
					</@form.row>
					
					<@form.row label="">
						<@spring.formCheckbox  "entity.account.locked"/>
						<label for="account.locked">Cuenta bloqueada</label>
					</@form.row>

				<#else>
				
					<#-- @form.row label="Contrase&ntilde;a" -->
						<#-- @spring.formPasswordInput  "passwordForm.password" "class='{validate:{required:true, messages:{required:\"Ingrese una contraseña.\"}}}'"/-->
						<#--@spring.showErrors "<br/>"/-->
					<#--/@form.row-->
					
					<#--@form.row label="Repetir contrase&ntilde;a"-->
						<#--@spring.formPasswordInput  "passwordForm.passwordVerification" "class='{validate:{required:true, messages:{required:\"Repita la contraseña.\"}}}'"/-->
						<#--@spring.showErrors "<br/>"/-->					
					<#--/@form.row -->
						
				</#if>
				
				<@form.submit>Guardar</@form.submit>
						
			</form>		
		</@page.box>
	</@page.section>
</#macro>