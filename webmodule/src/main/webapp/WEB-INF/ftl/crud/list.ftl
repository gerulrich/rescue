<@layout.main breadcrumbs=breadcrumbs menu=menu sidebar=sidebar body=body/>

<#macro breadcrumbs>
	<#assign listAction=gridModel.getAction(5)/>
	<li><a href="<@spring.url '/'/>">Dashboard</a></li>
	<li><a href="<@spring.url listAction.url/>"><@spring.message "${gridModel.titleKey}"/></a></li>
</#macro>

<#macro menu>
	<@widget.menu "admin"/>
</#macro>

<#macro sidebar>
	<@widget.statistics/>
</#macro>

<#macro body>
	<@page.section>
		<#assign title><@spring.message "${gridModel.titleKey}"/></#assign>
		<#if gridModel.getAction(1)??>
			<#assign newAction=gridModel.getAction(1)/>
		</#if>
		<@page.box title=title>
			<#if newAction??>
			<a href="<@spring.url '${newAction.url}'/>"><@spring.message "${newAction.messageKey}"/></a>
			</#if>
			<table cellspacing="0" cellpadding="0" border="0"> 
				<thead>
					<tr>
						<th>OID</th>
						<#list gridModel.columns as column>
							<th><@spring.message "${column.titleKey}"/></th>
						</#list>
						<th>Acciones</th>
					</tr>
				</thead>
				<tbody>
					<#list entities.list as entity>
					<tr>
						<td>${entity.id}</td>
						<#list gridModel.columns as column>
							<td>${Get(entity,column.propertyPath)}</td>
						</#list>
						<td>
							<#list gridModel.actions as action>
								<#if !action.generic>
								<#assign helpLabel><@spring.message "${action.messageKey}"/></#assign>
								<@widget.link "${action.url}${entity.id}" "${action.icon}" helpLabel/>
								</#if>
							</#list>
						</td>
					</tr>
					</#list>
				</tbody>
			</table>
			<#assign listAction=gridModel.getAction(5)/>
			<@widget.tablePaging entities listAction.url/>
		</@page.box>
	</@page.section>
</#macro>