<#ftl strip_whitespace=true>

<#macro welcome>
	<@spring.message "home.welcome" /> <#if currentUser??>${currentUser.displayName}<#else>Anonimo</#if>
</#macro>

<#macro statistics>
<div class="box statics">
		<div class="content">
			<ul>
				<li><h2>Informaci&oacute;n</h2></li>
				<li>SO: <div class="info red"><span>${osName!"-"}</span></div></li>
				<li>JVM: <div class="info blue"><span>${javaVersion!"-"}</span></div></li>
				<li>Build <div class="info green"><span>${buildNumber}</span></div></li>
				<li>Version <div class="info black"><span>${appVersion}</span></div></li>
			</ul>
		</div>
	</div>
</#macro>

<#macro menu selected="">

<ul> 
	<li<#if (selected="dashboard")> class="current"</#if>><a href="<@spring.url '/' />">Dashboard</a></li>

	<li>
		<a href="#">Aplicación</a>
		<ul>
			<li><a href="${jnlpUrl}">Descargar</a></li>
		</ul>
	</li>
	 
	<li<#if (selected="admin")> class="current"</#if>><a href="#">Administración</a>
		<ul>
			<@widget.hasPermission "USER_MANAG">
			<li><a href="<@spring.url '/user/list'/>">Usuarios</a></li>
			<li><a href="<@spring.url '/role/list'/>">Roles</a></li>
			<li><a href="<@spring.url '/group/list'/>">Grupos</a></li>
			</@widget.hasPermission>
			
			<@widget.hasPermission "CONFIG">
			<li><a href="<@spring.url '/config/list'/>">Configuración</a></li>
			</@widget.hasPermission>
			
			<@widget.hasPermission "GEO_MANAGER">
			<li><a href="#">Mapa</a>
				<ul>
					<li><a href="<@spring.url '/map/test.html'/>">Buscador de direcciones</a></li>
					<li><a href="<@spring.url '/map/zone/test.html'/>">Mapa de zonas</a></li>
					<li><a href="<@spring.url '/shp/upload'/>">Cargar capa desde archivo...</a></li>
				</ul>
			</li>
			</@widget.hasPermission>
			
			<@widget.hasPermission "FILE_UPLOAD">
			<li><a href="<@spring.url '/file/list.html'/>">Archivos</a></li>
			</@widget.hasPermission>
		</ul>
	</li>
	
	<@widget.hasPermission "RESOURCE">
	<li <#if (selected="resource")> class="current"</#if>><a href="#">Recursos</a>
		<ul>
			<li><a href="<@spring.url '/resource/type/list.html'/>">Tipos de recursos</a></li>
			<li><a href="<@spring.url '/resource/list.html'/>">Recursos</a></li>
		</ul>
	</li>
	</@widget.hasPermission>
	
	<@widget.hasPermission "EXEC_REPORT">
	<li <#if (selected="reports")> class="current"</#if>><a href="#">Reportes</a>
		<ul>
			<li><a href="<@spring.url '/report/list'/>">Lista de reportes</a></li>
		</ul>
	</li>
	</@widget.hasPermission>	
	
</ul>
</#macro>

<#macro loginError>
	<@spring.bind "loginForm" />
	<#if spring.status.error>
	<div id="login_error">
		<strong>ERROR</strong>: ${spring.status.errorMessages[0]}
	</div>
	</#if>
</#macro>

<#macro showErrors element="">
	<@spring.bind element />
	<#if spring.status.error>
	<div class="message red">
        There were problems with the data you entered:
        <ul>
            <#list spring.status.errorMessages as error>
                <li><span><b>ERROR</b>: ${error}</span></li>
            </#list>
        </ul>	
		
	</div>
	</#if>
</#macro>



<#macro importCss>
	<style type="text/css">
		@import url("<@spring.url '/static/css/style.css'/>");
		@import url("<@spring.url '/static/css/forms.css'/>");
		@import url("<@spring.url '/static/css/forms-btn.css'/>");
		@import url("<@spring.url '/static/css/menu.css'/>");
		@import url('<@spring.url '/static/css/style_text.css'/>');
		@import url("<@spring.url '/static/css/datatables.css'/>");
		@import url("<@spring.url '/static/css/fullcalendar.css'/>");
		@import url("<@spring.url '/static/css/pirebox.css'/>");
		@import url("<@spring.url '/static/css/modalwindow.css'/>");
		@import url("<@spring.url '/static/css/statics.css'/>");
		@import url("<@spring.url '/static/css/tabs-toggle.css'/>");
		@import url("<@spring.url '/static/css/system-message.css'/>");
		@import url("<@spring.url '/static/css/tooltip.css'/>");
		@import url("<@spring.url '/static/css/wizard.css'/>");
		@import url("<@spring.url '/static/css/wysiwyg.css'/>");
		@import url("<@spring.url '/static/css/wysiwyg.modal.css'/>");
		@import url("<@spring.url '/static/css/wysiwyg-editor.css'/>");
		@import url("<@spring.url '/static/css/handheld.css'/>");

		#wrapper {
			width : auto;
			min-width : 980px;
			max-width : 90%;
		}
	</style>
	
	<!--[if lte IE 8]><script type="text/javascript" src="<@spring.url '/static/js/excanvas.min.js'/>"></script><![endif]-->
</#macro>

<#macro importJS>
	<script type="text/javascript" src="<@spring.url '/static/js/jquery-1.7.1.min.js'/>"></script>
	<script type="text/javascript" src="<@spring.url '/static/js/jquery.backgroundPosition.js'/>"></script>
	<script type="text/javascript" src="<@spring.url '/static/js/jquery.placeholder.min.js'/>"></script>
	<script type="text/javascript" src="<@spring.url '/static/js/jquery.ui.1.8.17.js'/>"></script>
	<!--script type="text/javascript" src="<@spring.url '/static/js/jquery.ui.select.js'/>"></script-->
	<script type="text/javascript" src="<@spring.url '/static/js/jquery.ui.spinner.js'/>"></script>
	<script type="text/javascript" src="<@spring.url '/static/js/superfish.js'/>"></script>
	<script type="text/javascript" src="<@spring.url '/static/js/supersubs.js'/>"></script>
	<script type="text/javascript" src="<@spring.url '/static/js/jquery.datatables.js'/>"></script>
	<script type="text/javascript" src="<@spring.url '/static/js/fullcalendar.min.js'/>"></script>
	<script type="text/javascript" src="<@spring.url '/static/js/jquery.smartwizard-2.0.min.js'/>"></script>
	<script type="text/javascript" src="<@spring.url '/static/js/pirobox.extended.min.js'/>"></script>
	<script type="text/javascript" src="<@spring.url '/static/js/jquery.tipsy.js'/>"></script>
	<script type="text/javascript" src="<@spring.url '/static/js/jquery.elastic.source.js'/>"></script>
	<script type="text/javascript" src="<@spring.url '/static/js/jquery.jBreadCrumb.1.1.js'/>"></script>
	<script type="text/javascript" src="<@spring.url '/static/js/jquery.customInput.js'/>"></script>
	<script type="text/javascript" src="<@spring.url '/static/js/jquery.validate.min.js'/>"></script>
	<script type="text/javascript" src="<@spring.url '/static/js/jquery.metadata.js'/>"></script>
	<script type="text/javascript" src="<@spring.url '/static/js/jquery.filestyle.mini.js'/>"></script>
	<script type="text/javascript" src="<@spring.url '/static/js/jquery.filter.input.js'/>"></script>
	<!--script type="text/javascript" src="<@spring.url '/static/js/jquery.flot.js'/>"></script-->
	<!--script type="text/javascript" src="<@spring.url '/static/js/jquery.flot.pie.min.js'/>"></script-->
	<!--script type="text/javascript" src="<@spring.url '/static/js/jquery.flot.resize.min.js'/>"></script-->
	<!--script type="text/javascript" src="<@spring.url '/static/js/jquery.graphtable-0.2.js'/>"></script-->
	<script type="text/javascript" src="<@spring.url '/static/js/jquery.wysiwyg.js'/>"></script>
	<script type="text/javascript" src="<@spring.url '/static/js/controls/wysiwyg.image.js'/>"></script>
	<script type="text/javascript" src="<@spring.url '/static/js/controls/wysiwyg.link.js'/>"></script>
	<script type="text/javascript" src="<@spring.url '/static/js/controls/wysiwyg.table.js'/>"></script>
	<script type="text/javascript" src="<@spring.url '/static/js/plugins/wysiwyg.rmFormat.js'/>"></script>
	<script type="text/javascript" src="<@spring.url '/static/js/costum.js?version=01'/>"></script>
</#macro>

<#macro detail url>
<a href="<@spring.url url/>"><img src="<@spring.url '/static/gfx/icons/small/document.png'/>" class="icon16 fl-space2" alt="" title="<@spring.message 'accion.show'/>" /></a>
</#macro>

<#macro edit url>
<a href="<@spring.url url/>"><img src="<@spring.url '/static/gfx/icons/small/edit.png'/>" class="icon16 fl-space2" alt="" title="<@spring.message 'accion.edit'/>" /></a>
</#macro>

<#macro delete url>
<a href="<@spring.url url/>"><img src="<@spring.url '/static/gfx/icons/small/trash.png'/>" class="icon16 fl-space2" alt="" title="<@spring.message 'accion.delete'/>" /></a>
</#macro>

<#macro link url image="folder.png" tooltip="" attributes="">
<a ${attributes} href="<@spring.url url/>"><img src="<@spring.url '/static/gfx/icons/small/${image}'/>" class="icon16 fl-space2" alt="" title="${tooltip}" /></a>
</#macro>


<#macro tablePaging paging  baseUrl queryFilter="">

<#assign previousPage = paging.pageNumber-1 >
<#assign nextPage = paging.pageNumber+1 >
<#assign currentPage = paging.pageNumber >
<#assign pageSize = paging.pageSize >
<#assign totalPages = paging.totalPages >

<div class="dataTables_wrapper">
	<div>
		<div class="dataTables_paginate paging_full_numbers">
			<a href="<@spring.url '${baseUrl}/1/${pageSize?c}'/><#if (queryFilter != "")>?q=${queryFilter}</#if>" class="first" title="first page">
				<span class="first paginate_button">Primera p&aacute;gina</span>
			</a>
			
			<#if ( previousPage >= 1 ) >
				<a href="<@spring.url '${baseUrl}/${previousPage?c}/${pageSize}'/><#if (queryFilter != "")>?q=${queryFilter}</#if>" class="previous" title="previous page">
					<span class="previous paginate_button">Anterior</span>
				</a>
			<#else>
				<span class="previous paginate_button paginate_button_disabled">Anterior</span>
			</#if>
			
			
			<#assign start=paging.pageNumber-2>
			<#if (start <= 0)>
				<#assign start=1>
			</#if>
			<#assign end=start+5>
			<#if (end > paging.totalPages)>
				<#assign end=paging.totalPages>
			</#if>

			<#if (start > paging.totalPages)>
				<#assign start=1>
				<#assign start=1>
			</#if>
			
			<#assign seq=start..end>
			<span>
			<#list seq as x>
				<#if (currentPage == x)>
					<span class="paginate_active">${x}</span>
				<#else>
					<a href="<@spring.url '${baseUrl}/${x?c}/${pageSize}'/><#if (queryFilter != "")>?q=${queryFilter}</#if>" title="página ${x}">
						<span class="paginate_button">${x}</span>
					</a>					
				</#if>
			</#list>
			</span>
			
			<#if (nextPage <= paging.totalPages) >
				<a href="<@spring.url '${baseUrl}/${nextPage?c}/${pageSize}'/><#if (queryFilter != "")>?q=${queryFilter}</#if>">
					<span class="next paginate_button">Siguiente</span>
				</a>
			<#else>
				<span class="next paginate_button paginate_button_disabled">Siguiente</span>
			</#if>
		
			<a href="<@spring.url '${baseUrl}/${totalPages?c}/${pageSize}'/><#if (queryFilter != "")>?q=${queryFilter}</#if>">
				<span class="last paginate_button">ultima p&aacute;gina</span>
			</a>
		</div>
		<div class="dataTables_length">
			<!--label-->
				<select id="pageSizeList" size="1">
				
					
				
					<option value="<@spring.url '${baseUrl}/1/5'/><#if (queryFilter != "")>?q=${queryFilter}</#if>" <#if (pageSize == 5)>selected="selected"</#if>>5</option>
					<option value="<@spring.url '${baseUrl}/1/10'/><#if (queryFilter != "")>?q=${queryFilter}</#if>" <#if (pageSize == 10)>selected="selected"</#if>>10</option>
					<option value="<@spring.url '${baseUrl}/1/25'/><#if (queryFilter != "")>?q=${queryFilter}</#if>" <#if (pageSize == 25)>selected="selected"</#if>>25</option>
					<option value="<@spring.url '${baseUrl}/1/50'/><#if (queryFilter != "")>?q=${queryFilter}</#if>" <#if (pageSize == 50)>selected="selected"</#if>>50</option>
					<option value="<@spring.url '${baseUrl}/1/100'/><#if (queryFilter != "")>?q=${queryFilter}</#if>" <#if (pageSize == 100)>selected="selected"</#if>>100</option>
				</select>
				<!--a class="ui-selectmenu ui-widget ui-state-default ui-corner-all entries ui-selectmenu-dropdown" id="undefined-button" role="button" href="#" aria-haspopup="true" aria-owns="undefined-menu">
					<span class="ui-selectmenu-status">10</span>
					<span class="ui-selectmenu-icon ui-icon ui-icon-triangle-1-s"></span>
				</a>
			</label-->
		</div>
	</div>
</div>

</#macro>

<#macro formSingleSelect path options key value attributes="">
	<@spring.bind path/>
	<select name="${spring.status.expression}" ${attributes}>
		<#list options as option>
		<option value="${option[key]}" <#if spring.status.value?default("") == value>selected="true"</#if>>${option[value]}</option>
		</#list>
	</select>
</#macro>

<#macro hasPermission permission>
	<#if ( userUtil.currentUser()?? && userUtil.currentUser().hasPermission(permission))>
		<#nested>
	</#if>
</#macro>