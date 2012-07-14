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
			<li><a href="#">Usuarios</a>
				<ul>
					<li><a href="<@spring.url '/admin/user/new'/>">Nuevo...</a></li>
					<li><a href="<@spring.url '/admin/users.html'/>">Ver</a></li>
				</ul>
			</li>
			<li><a href="<@spring.url '/admin/properties'/>">Configuración</a></li>
			<li><a href="#">Mapa</a>
				<ul>
					<li><a href="<@spring.url '/map/test.html'/>">Buscador de direcciones</a></li>
					<li><a href="<@spring.url '/map/zone/test.html'/>">Mapa de zonas</a></li>
					<li><a href="<@spring.url '/shp/upload'/>">Cargar capa desde archivo...</a></li>
				</ul>
			</li>
			<li><a href="#">Archivo</a>
				<ul>
					<li><a href="<@spring.url '/file/upload.html'/>">Subir archivo...</a></li>
					<li><a href="<@spring.url '/file/list.html'/>">Lista de archivos</a></li>
				</ul>
			</li>
		</ul>
	</li>
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
		<span><b>ERROR</b>: ${spring.status.errorMessages[0]}</span>
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
	<script type="text/javascript" src="<@spring.url '/static/js/jquery.ui.select.js'/>"></script>
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
	<script type="text/javascript" src="<@spring.url '/static/js/jquery.flot.js'/>"></script>
	<script type="text/javascript" src="<@spring.url '/static/js/jquery.flot.pie.min.js'/>"></script>
	<script type="text/javascript" src="<@spring.url '/static/js/jquery.flot.resize.min.js'/>"></script>
	<script type="text/javascript" src="<@spring.url '/static/js/jquery.graphtable-0.2.js'/>"></script>
	<script type="text/javascript" src="<@spring.url '/static/js/jquery.wysiwyg.js'/>"></script>
	<script type="text/javascript" src="<@spring.url '/static/js/controls/wysiwyg.image.js'/>"></script>
	<script type="text/javascript" src="<@spring.url '/static/js/controls/wysiwyg.link.js'/>"></script>
	<script type="text/javascript" src="<@spring.url '/static/js/controls/wysiwyg.table.js'/>"></script>
	<script type="text/javascript" src="<@spring.url '/static/js/plugins/wysiwyg.rmFormat.js'/>"></script>
	<script type="text/javascript" src="<@spring.url '/static/js/costum.js'/>"></script>
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

<#macro link url image="folder.png" tooltip="">
<a href="<@spring.url url/>"><img src="<@spring.url '/static/gfx/icons/small/${image}'/>" class="icon16 fl-space2" alt="" title="${tooltip}" /></a>
</#macro>


<#macro tablePaging paging  baseUrl>
<#assign previousPage = paging.pageNumber-1 >
<#assign nextPage = paging.pageNumber+1 >
<#assign currentPage = paging.pageNumber >
<#assign pageSize = paging.pageSize >
<#assign totalPages = paging.totalPages >
<div class="pager fr">
	
	<span class="nav">
		<a href="<@spring.url '${baseUrl}/1/${pageSize?c}'/>" class="first" title="first page">
			<span>Primera p&aacute;gina</span>
		</a>
		<#if ( previousPage >= 1 ) >
		<a href="<@spring.url '${baseUrl}/${previousPage?c}/${pageSize}'/>" class="previous" title="previous page">
			<span>Anterior</span>
		</a>
		</#if>
	</span>
	
	<span class="pages">
	<#assign start=paging.pageNumber-2>
	<#if (start <= 0)>
		<#assign start=1>
	</#if>
	<#assign end=start+5>
	<#if (end > paging.totalPages)>
		<#assign end=paging.totalPages>
	</#if>		
	<#assign seq=start..end>
	<#list seq as x>
		<a href="<@spring.url '${baseUrl}/${x?c}/${pageSize}'/>" title="page ${x}" <#if (currentPage == x)>class="active"</#if>><span>${x}</span></a>
	</#list>
	</span>
	<span class="nav">
		
		<#if (nextPage <= paging.totalPages) >
		<a href="<@spring.url '${baseUrl}/${nextPage?c}/${pageSize}'/>" class="next" title="next page">
			<span>Siguiente</span>
		</a>
		</#if>
		
		<a href="<@spring.url '${baseUrl}/${totalPages?c}/${pageSize}'/>" class="last" title="last page">
			<span>Ultima p&aacute;gina</span>
		</a>
	</span>
</div>
</#macro>