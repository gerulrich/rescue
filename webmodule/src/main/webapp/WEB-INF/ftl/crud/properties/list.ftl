<@layout.main breadcrumbs=breadcrumbs menu=menu sidebar=sidebar body=body/>

<#macro breadcrumbs>
	<li><a href="<@spring.url '/'/>">Dashboard</a></li>
	<li>Propiedades</li>
</#macro>

<#macro menu>
	<@widget.menu "admin"/>
</#macro>

<#macro sidebar>
	<@widget.statistics/>
</#macro>


<#macro body>
	<@page.section>
		<@page.box title="Lista de propiedades">
			<table cellspacing="0" cellpadding="0" border="0"> 
				<thead>
					<tr>
						<th>Nombre</th>
						<th>Valor</th>
						<th>CD</th>
						<th>Acciones</th>
					</tr>
				</thead>
				<tbody>
					<#list properties.list as property>
					<tr>
						<td>${property.key}</td>
						<td>${property.value}</td>
						<td><#if property.clientProperty>Si<#else>No</#if></td>
						<td>
							<@widget.edit "/config/edit/${property.id}"/>
						</td>
					</tr>
					</#list>
				</tbody>
			</table>
			<@widget.tablePaging properties "/config/list" />
		</@page.box>
	</@page.section>
</#macro>