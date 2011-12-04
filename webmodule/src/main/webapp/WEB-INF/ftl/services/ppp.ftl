<@layout.main>

<h1>Configuración del sistema</h1>
			
<h2>The following services are bound to this application:</h2>
<ul>
	<#list services as service>
	<li><p>${service}</p></li>
	</#list>
</ul>
</div>

</@layout.main>