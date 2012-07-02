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
	<div class="section">
		<div class="box">
			<div class="title">Lista de propiedades<span class="hide"></span></div>
			<div class="content">
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
								<@widget.edit "/admin/property/${property.id}"/>
							</td>
						</tr>
						</#list>
					</tbody>
				</table>
				<@widget.tablePaging properties "/admin/properties" />
			</div>
		</div>
	</div>
</#macro>