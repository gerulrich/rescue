<#ftl strip_whitespace=true>

<#macro row label>
	<div class="row">
		<label>${label}</label>
		<div class="right">
			<#nested/>
		</div>
	</div>	
</#macro>

<#macro submit color="green">
	<div class="row">
		<label></label>
		<div class="right">
			<button id="boton" type="submit" class="green">
				<span><#nested/></span>
			</button>
		</div>
	</div>
</#macro>

<#macro simpleSelect name list>
	<select name="${name}">
		<#list list as elem>
			<option value="${elem}">${elem}</option>							
		</#list>
	</select>
</#macro>

<#macro pojoSelect name list id value>
	<select name="${name}">
		<#list list as elem>
			<option value="${elem[id]}">${elem[value]}</option>							
		</#list>
	</select>
</#macro>

<#macro mapSelect name map>
	<select name="${name}">
	<#list map?keys as key> 
   		<option value="${key}">${map[key]}</option> 
	</#list> 
	</select>
</#macro>