<#ftl strip_whitespace=true>

<#macro userinfo>
	<img src="<@spring.url '/avatar'/>" class="avatar" alt="" />
	<span class="user-detail">
		<span class="name"><@spring.message "home.welcome" /> <#if currentUser??>${currentUser.displayName}<#else>Anonimo</#if></span>
		<span class="text">Logged as admin</span>
		<span class="text">You have <a href="#">0 messages</a></span>
	</span>
</#macro>

<#macro logo>
	<a href="index.html" title="View dashboard">
		<img src="<@spring.url '/static/images/logo_earth.png'/>" alt="" class="picture" />
		<span class="textlogo">
			<span class="title">CLOUDENGINE</span>
			<span class="text">panel de control</span>
		</span>
	</a>
</#macro>

<#macro menu selected="">
<ul class="clear">
	<li<#if (selected="dashboard")> class="active"</#if>><a href="<@spring.url '/'/>">Dashboard</a></li>

	<li>
		<a href="#">Aplicación</a>
		<ul>
			<li><a href="${jnlpUrl}">Descargar</a></li>
		</ul>
	</li>	
	
	<li<#if (selected="admin")> class="active"</#if>>
		<a href="#">Administracion</a>
		<ul>
			<li><a href="#">Usuarios</a>
				<ul>
					<li><a href="<@spring.url '/admin/user/new'/>">Nuevo...</a></li>
					<li><a href="<@spring.url '/admin/users.html'/>">Usuarios</a></li>					
					<li><a href="columns2.html">Cambiar contraseña</a></li>
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

<#macro css>
<link rel="stylesheet" href="<@spring.url '/css/reset.css'/>" type="text/css"/>
<link rel="stylesheet" href="<@spring.url '/css/screen.css'/>" type="text/css"/>
<link rel="stylesheet" href="<@spring.url '/css/fancybox.css'/>" type="text/css"/>
<link rel="stylesheet" href="<@spring.url '/css/jquery.wysiwyg.css'/>" type="text/css"/>
<link rel="stylesheet" href="<@spring.url '/css/jquery.ui.css'/>" type="text/css"/>
<link rel="stylesheet" href="<@spring.url '/css/visualize.css'/>" type="text/css"/>
<link rel="stylesheet" href="<@spring.url '/css/visualize-light.css'/>" type="text/css"/>
<!--[if IE 7]><link rel="stylesheet" type="text/css" href="css/ie7.css" /><![endif]-->
</#macro>

<#macro detail url>
<a href="<@spring.url url/>"><img src="<@spring.url '/static/images/ico_detail_16.png'/>" class="icon16 fl-space2" alt="" title="<@spring.message 'accion.show'/>" /></a>
</#macro>

<#macro edit url>
<a href="<@spring.url url/>"><img src="<@spring.url '/static/images/ico_edit_16.png'/>" class="icon16 fl-space2" alt="" title="<@spring.message 'accion.edit'/>" /></a>
</#macro>

<#macro delete url>
<a href="<@spring.url url/>"><img src="<@spring.url '/static/images/ico_delete_16.png'/>" class="icon16 fl-space2" alt="" title="<@spring.message 'accion.delete'/>" /></a>
</#macro>


<#macro tablePaging paging  baseUrl>
<#assign previousPage = paging.pageNumber-1 >
<#assign nextPage = paging.pageNumber+1 >
<#assign currentPage = paging.pageNumber >
<#assign pageSize = paging.pageSize >
<#assign totalPages = paging.totalPages >
<div class="pager fr">
	
	<span class="nav">
		<a href="<@spring.url '${baseUrl}/1/${pageSize}'/>" class="first" title="first page">
			<span>First</span>
		</a>
		<#if ( previousPage >= 1 ) >
		<a href="<@spring.url '${baseUrl}/${previousPage}/${pageSize}'/>" class="previous" title="previous page">
			<span>Previous</span>
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
		<a href="<@spring.url '${baseUrl}/${x}/${pageSize}'/>" title="page ${x}" <#if (currentPage == x)>class="active"</#if>><span>${x}</span></a>
	</#list>
	</span>
	<span class="nav">
		
		<#if (nextPage <= paging.totalPages) >
		<a href="<@spring.url '${baseUrl}/${nextPage}/${pageSize}'/>" class="next" title="next page">
			<span>Next</span>
		</a>
		</#if>
		
		<a href="<@spring.url '${baseUrl}/${totalPages}/${pageSize}'/>" class="last" title="last page">
			<span>Last</span>
		</a>
	</span>
</div>
</#macro>