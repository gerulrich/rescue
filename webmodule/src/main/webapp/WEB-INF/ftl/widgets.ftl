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

<#macro menu>
<ul class="clear">
	<li><a href="<@spring.url '/'/>">Dashboard</a></li>
	<li><a href="<@spring.url '/config.html'/>">Configuración</a><li>
	
	<li>
		<a href="#">Administracion</a>
		<ul>
			<li><a href="<@spring.url '/config.html'/>">Ver configuración</a></li>
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

<#macro footer>
	<div class="pagesize clear">
		<p class="bt-space15"><span class="copy"><strong>© 2010 Copyright by <a href="http://www.ait.sk/">Affinity Information Technology.</a></strong></span> Powered by <a href="#">TERMINATOR ADMIN.</a></p>
		<p><a href="<@spring.url '/home.html?locale=es'/>" />Español</p> </p>
		<img src="<@spring.url '/static/images/logo_earth_bw50.png'/>" alt="" class="block center" />
	</div>
</#macro>

<#macro loginError>
	<@spring.bind "loginForm" />
	<#if spring.status.error>
	<div id="login_error">
		<strong>ERROR</strong>: ${spring.status.errorMessages[0]}
	</div>					
	</#if>
</#macro>

<#macro scripts>
	<script type="text/javascript" src="<@spring.url '/static/js/jquery.js'/>"></script>
	<script type="text/javascript" src="<@spring.url '/static/js/jquery.visualize.js'/>"></script>
	<script type="text/javascript" src="<@spring.url '/static/js/jquery.wysiwyg.js'/>"></script>
	<script type="text/javascript" src="<@spring.url '/static/js/tiny_mce/jquery.tinymce.js'/>"></script>
	<script type="text/javascript" src="<@spring.url '/static/js/jquery.fancybox.js'/>"></script>
	<script type="text/javascript" src="<@spring.url '/static/js/jquery.idtabs.js'/>"></script>
	<script type="text/javascript" src="<@spring.url '/static/js/jquery.datatables.js'/>"></script>
	<script type="text/javascript" src="<@spring.url '/static/js/jquery.jeditable.js'/>"></script>
	<script type="text/javascript" src="<@spring.url '/static/js/jquery.ui.js'/>"></script>
	<script type="text/javascript" src="<@spring.url '/static/js/jquery.jcarousel.js'/>"></script>
	<script type="text/javascript" src="<@spring.url '/static/js/jquery.validate.js'/>"></script>
	<script type="text/javascript" src="<@spring.url '/static/js/excanvas.js'/>"></script>
	<script type="text/javascript" src="<@spring.url '/static/js/cufon.js'/>"></script>
	<script type="text/javascript" src="<@spring.url '/static/js/Zurich_Condensed_Lt_Bd.js'/>"></script>
	<script type="text/javascript" src="<@spring.url '/static/js/script.js'/>"></script>
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
<a href="<@spring.url url/>"><img src="<@spring.url '/static/images/ico_detail_16.png'/>" class="icon16 fl-space2" alt="" title="edit" /></a>
</#macro>

<#macro edit url>
<a href="<@spring.url url/>"><img src="<@spring.url '/static/images/ico_edit_16.png'/>" class="icon16 fl-space2" alt="" title="edit" /></a>
</#macro>

<#macro delete url>
<a href="<@spring.url url/>"><img src="<@spring.url '/static/images/ico_delete_16.png'/>" class="icon16 fl-space2" alt="" title="edit" /></a>
</#macro>