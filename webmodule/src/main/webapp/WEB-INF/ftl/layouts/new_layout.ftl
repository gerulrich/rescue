<#macro main breadcrumbs menu sidebar body script=scriptEmpty>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<link rel="shortcut icon" href="<@spring.url '/img/favicon.ico'/>" type="image/x-icon" />
	<meta http-equiv="Content-Type" content="text/html;charset=utf-8"> 
	<title><@spring.message 'home.title'/></title>

	<meta name="apple-mobile-web-app-capable" content="no">
	<meta name="apple-mobile-web-app-status-bar-style" content="black">
	<meta name="viewport" content="width=device-width,initial-scale=0.69,user-scalable=yes,maximum-scale=1.00">
	<@widget.importCss/>
	<@widget.importJS/>
	<@script/>
	
</head>

<body>

<div id="wrapper">
	<div id="container">
	
		<div class="hide-btn top tip-s" original-title="Cerrar barra lateral"></div>
		<div class="hide-btn center tip-s" original-title="Cerrar barra lateral"></div>
		<div class="hide-btn bottom tip-s" original-title="Cerrar barra lateral"></div>
		
		<div id="top">

			<!-- COMIENZO DE LA BARRA SUPERIOR -->
			<h1 id="logo"><a href="<@spring.url '/' />"></a></h1>
			<div id="labels">
				<ul>
					<li><a href="#" class="user"><span class="bar"><@widget.welcome/></span></a></li>
					<!--li><a href="#" class="settings"></a></li>
					<li class="subnav">
						<a href="#" class="messages"></a>
						<ul>
							<li><a href="#">New message</a></li>
							<li><a href="#">Inbox</a></li>
							<li><a href="#">Outbox</a></li>
							<li><a href="#">Trash</a></li>
						</ul>
					</li-->
					<li><a href="<@spring.url '/account/signout.html' />" class="logout"></a></li>
				</ul>
			</div>
			<!-- FIN DE LA BARRA SUPERIOR -->
			
			<div id="menu">
				<@menu/>
			</div>
		</div>
		
		
		
		<!-- COMIENZO DE LA BARRA LATERAL -->
		<div id="left">
			<@sidebar/>
		</div>
		<!-- FIN DE LA BARRRA LATERAL -->
		
		
		<div id="right">
		
			<!-- COMIENZO DE LAS MIGAS DE PAN -->		
			<div id="breadcrumbs">
				<ul>
					<li></li>
					<@breadcrumbs/>
				</ul>
			</div>

			<div class="section">
				<!-- COMIENZO DEL CONTENIDO PRINCIPAL -->
				<@body/>
				<!-- FIN DEL CONTENIDO PRINCIPAL -->
			</div>
		</div>

		<div id="footer">
			<div class="split">Copyright your company.</div>
			<div class="split right">Powered by <a href="http://themeforest.net/item/mustache-admin/1614352" target="_blank" title="Admin Template">Mustache!</a></div>
		</div>
	</div>
</div>
</body>
</html>
</#macro>

<#macro scriptEmpty></#macro>