<@layout.main breadcrumbs=breadcrumbs menu=menu sidebar=sidebar body=body/>

<#macro breadcrumbs>
	<li><a href="<@spring.url '/'/>">Dashboard</a></li>
	<li><a href="<@spring.url '/mongo/list'/>">MongoDB Collections</a></li>
	<li><a href="<@spring.url '/mongo/show/${collectionEncodedName}'/>">${collectionName}</a></li>
</#macro>

<#macro menu>
	<@widget.menu "admin"/>
</#macro>

<#macro sidebar>
	<@widget.statistics/>
</#macro>


<#macro body>
	<@page.section>
		<@page.box title="MongoDB ${collectionName} Collection">
			<#if objects.totalSize &gt; 0 > 
			<table cellspacing="0" cellpadding="0" border="0"> 
				<thead>
					<tr>
						<#list headers as h>
						<th>${h}</th>
						</#list>
					</tr>
				</thead>
				<tbody>
					<#list objects.list as obj>
					<tr>
						<#list headers as key>
						<td>${obj.values[key]!"-"}</td>
						</#list>
					</tr>								
					</#list>
				</tbody>
			</table>
			<@widget.tablePaging objects "/mongo/show/${collection}" />
			<#else>
				<h3>No hay datos en la colección</h3>
			</#if>			
		</@page.box>
	</@page.section>
</#macro>