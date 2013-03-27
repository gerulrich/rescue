<@layout.main breadcrumbs=breadcrumbs menu=menu sidebar=sidebar body=body/>

<#macro breadcrumbs>
	<li><a href="<@spring.url '/'/>">Dashboard</a></li>
	<li><a href="<@spring.url '/sql/list'/>">SQL</a></li>	
	<li><a href="<@spring.url '/sql/list'/>">${table}</a></li>
	<li>Datos</li>
</#macro>

<#macro menu>
	<@widget.menu "admin"/>
</#macro>

<#macro sidebar>
	<@widget.statistics/>
</#macro>


<#macro body>
	<@page.section>
		<@page.box title="Datos de la tabla ${table}">
			<#if rowsCount  &gt; 0 >
				<table cellspacing="0" cellpadding="0" border="0"> 
					<thead>
						<tr>
							<#list fields as f>
							<th>${f}</th>
							</#list>
						</tr>
					</thead>
					<tbody>
						<#list rows.list as row>
						<tr>
							<#list fields as f>
							<td>${row.values[f]!}</td>
							</#list>
						</tr>
						</#list>
					</tbody>
				</table>
			<#else>
				<h3>No hay datos en la tabla</h3>
			</#if>
		</@page.box>	
	</@page.section>
</#macro>