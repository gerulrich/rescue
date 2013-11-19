<#ftl strip_whitespace=true>

<#macro box title padding=true>
	<div class="box">
		<div class="title">${title}<span class="hide"></span></div>
		<#if padding>
		<div class="content"><#nested/></div>
		<#else>
		<div class="content nopadding"><#nested/></div>
		</#if>
	</div>
</#macro>

<#macro half>
	<div class="half">
		<#nested/>
	</div>
</#macro>

<#macro section>
	<div class="section">
		<#nested/>
	</div>
</#macro>