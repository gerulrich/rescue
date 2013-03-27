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

	<#if user.id??>
		<#assign title="Editar usuario."/>
		<#assign url="/user/${user.id}"/>
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
					<@spring.formInput  "user.username" "class='{validate:{required:true, email:true, messages:{email:\"Ingrese una direccion de E-mail valida.\", required:\"Ingrese una dirección de E-mail\"}}}'"/>
					<@spring.showErrors "<br/>"/>				
				</@form.row>
				
				<@form.row label="Nombre a visualizar">
					<@spring.formInput  "user.displayName" "class='{validate:{required:true, messages:{required:\"Ingrese el nombre del usuario.\"}}}'"/>
					<@spring.showErrors "<br/>"/>				
				</@form.row>			
						
				<@form.row label="Roles">
					<@spring.formInput  "user.roles" "class='{validate:{required:true, messages:{required:\"Ingrese los roles del usuario.\"}}}'"/>
					<@spring.showErrors "<br/>"/>				
				</@form.row>						
						
				<@form.row label="N&uacute;mero de agente">
					<@spring.formInput  "user.account.agentNumber"/>
					<@spring.showErrors "<br/>"/>				
				</@form.row>
						
				<@form.row label="Contrase&ntilde;a del agente">
					<@spring.formInput  "user.account.agentPassword"/>
					<@spring.showErrors "<br/>"/>				
				</@form.row>
				
				<#if user.id??>
					<@form.row label="">
						<@spring.formCheckbox  "user.account.enabled"/>
						<label for="account.enabled">Habilitado</label>
					</@form.row>
					
					<@form.row label="">
						<@spring.formCheckbox  "user.account.locked"/>
						<label for="account.locked">Cuenta bloqueada</label>
					</@form.row>
							
				<#else>
				
					<@form.row label="Contrase&ntilde;a">
						<@spring.formPasswordInput  "passwordForm.password" "class='{validate:{required:true, messages:{required:\"Ingrese una contraseña.\"}}}'"/>
						<@spring.showErrors "<br/>"/>					
					</@form.row>
					
					<@form.row label="Repetir contrase&ntilde;a">
						<@spring.formPasswordInput  "passwordForm.passwordVerification" "class='{validate:{required:true, messages:{required:\"Repita la contraseña.\"}}}'"/>
						<@spring.showErrors "<br/>"/>					
					</@form.row >
						
				</#if>
				
				<@form.submit>Guardar</@form.submit>
						
			</form>		
		</@page.box>
	</@page.section>
</#macro>