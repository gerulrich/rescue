<#macro main>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><@spring.message 'home.title'/></title>
<@widget.css/>
</head>
<body>
<#include "pagetop.ftl">
<div class="main pagesize"><!-- *** mainpage layout *** -->
	<div class="main-wrap">
		<div class="page clear">
        <#nested/>
		</div><!-- end of page -->
	</div>
</div>
<#include "footer.ftl">
</body>
</html>
</#macro>