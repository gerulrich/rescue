<#ftl strip_whitespace=true>

<#macro box title>
	<div class="box">
		<div class="title">${title}<span class="hide"></span></div>
		<div class="content"><#nested/></div>
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