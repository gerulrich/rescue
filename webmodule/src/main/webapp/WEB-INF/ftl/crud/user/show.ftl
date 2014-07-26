<@layout.main breadcrumbs=breadcrumbs menu=menu sidebar=sidebar body=body/>

<#macro breadcrumbs>
	<li><a href="<@spring.url '/'/>">Dashboard</a></li>
	<li><a href="<@spring.url '/user/list'/>">Usuarios</a></li>
	<li>Detalle ${entity.displayName}</li>
</#macro>

<#macro menu>
	<@widget.menu "admin"/>
</#macro>

<#macro sidebar>
	<@widget.statistics/>
</#macro>

<#macro body>
	<@page.section>
		<@page.box title="Datos datos del usuario ${entity.displayName}">
			<@form.row label="Email">${entity.username}</@form.row>
			<@form.row label="Nombre a visualizar">${entity.displayName}</@form.row>
			<@form.row label="Roles">${entity.roles}</@form.row>
			<@form.row label="Habilitado">${entity.account.enabled?string("Si","No")}</@form.row>
			<@form.row label="Cuenta bloqueada">${entity.account.locked?string("Si","No")}</@form.row>
			<@form.row label="Interno">${entity.account.agentNumber!}&nbsp;</@form.row>
		</@page.box>
	</@page.section>
</#macro>