<#ftl strip_whitespace=true>

<#--
  * Archivo con las macros de spring. En esta versión de las macros
  * son mocks con el unico proposito de utilizarse en test unitarios de los templates.
-->

<#macro message code>${code}</#macro>
<#macro messageText code, text>${code}</#macro>


<#macro theme code></#macro>
<#macro themeText code, text></#macro>
<#macro themeArgs code, args></#macro>
<#macro url relativeUrl extra...></#macro>
<#macro bind path></#macro>
<#macro bindEscaped path, htmlEscape></#macro>
<#macro formInput path attributes="" fieldType="text"></#macro>
<#macro formPasswordInput path attributes=""></#macro>
<#macro formHiddenInput path attributes=""></#macro>
<#macro formTextarea path attributes=""></#macro>
<#macro formSingleSelect path options attributes=""></#macro>
<#macro formMultiSelect path options attributes=""></#macro>
<#macro formRadioButtons path options separator attributes=""></#macro>
<#macro formCheckboxes path options separator attributes=""></#macro>
<#macro formCheckbox path attributes=""></#macro>
<#macro showErrors separator classOrStyle=""></#macro>

<#macro checkSelected value>
    <#if stringStatusValue?is_number && stringStatusValue == value?number>selected="selected"</#if>
    <#if stringStatusValue?is_string && stringStatusValue == value>selected="selected"</#if>
</#macro>

<#function contains list item>
    <#list list as nextInList>
    <#if nextInList == item><#return true></#if>
    </#list>
    <#return false>
</#function>

<#macro closeTag>
    <#if xhtmlCompliant?exists && xhtmlCompliant>/><#else>></#if>
</#macro>