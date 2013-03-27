<@layout.main breadcrumbs=breadcrumbs menu=menu sidebar=sidebar body=body/>

<#macro breadcrumbs>
	<li><a href="<@spring.url '/'/>">Dashboard</a></li>
	<li><a href="<@spring.url '/user/list'/>">Usuarios</a></li>
	<li>Detalle ${user.displayName}</li>
</#macro>

<#macro menu>
	<@widget.menu "admin"/>
</#macro>

<#macro sidebar>
	<@widget.statistics/>
</#macro>

<#macro body>
	<@page.section>
		<@page.box title="Datos datos del usuario ${user.displayName}">
			<@form.row label="Email">${user.username}</@form.row>
			<@form.row label="Nombre a visualizar">${user.displayName}</@form.row>
			<@form.row label="Roles">${user.roles}</@form.row>
			<@form.row label="Habilitado">${user.account.enabled?string("Si","No")}</@form.row>
			<@form.row label="Cuenta bloqueada">${user.account.locked?string("Si","No")}</@form.row>
			<@form.row label="Interno">${user.account.agentNumber!}&nbsp;</@form.row>
		</@page.box>
	</@page.section>
</#macro>