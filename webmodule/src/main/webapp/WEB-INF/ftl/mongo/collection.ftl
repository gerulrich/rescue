<@layout.main breadcrumbs=breadcrumbs menu=menu sidebar=sidebar body=body/>

<#macro breadcrumbs>
	<li><a href="<@spring.url '/'/>">Dashboard</a></li>
	<li><a href="<@spring.url '/admin/mongo/list'/>">MongoDB Collections</a></li>
	<li><a href="<@spring.url '/admin/mongo/show/${collection}'/>">${collection}</a></li>
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
			<div class="title">MongoDB ${collection} Collection<span class="hide"></span></div>
			<div class="content">
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
				<@widget.tablePaging objects "/admin/mongo/show/${collection}" />
				<#else>
					<h3>No hay datos en la colección</h3>
				</#if>
			</div>
		</div>
	</div>
</#macro>