<@layout.main template="admin">			

<!-- CONTENT BOX - DATATABLE -->
<div class="content-box">
	<div class="box-body">
		<div class="box-header clear">
			<ul class="tabs clear">
				<!--<li><a href="#data-table">JS plugin</a></li>-->
				<!--li><a href="#table">Table only</a></li-->
			</ul>
			
			<h2>Propiedades de configuración</h2>
		</div>

		<div class="box-wrap clear">
			<div id="table">
				<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed in porta lectus. Maecenas dignissim enim quis ipsum mattis aliquet. Maecenas id velit et elit gravida bibendum. Duis nec rutrum lorem.</p> 
				<table class="style1">
					<thead>
						<tr>
							<th>OID</th>
							<th>Nombre</th>
							<th>Valor</th>
							<th>Propiedad CD</th>
							<th>Acciones</th>
						</tr>
					</thead>
					<tbody>
						<#list properties.list as property>
						<tr>
							<td>${property.id}</td>
							<td>${property.key}</td>
							<td>${property.value}</td>
							<td><#if property.clientProperty>Si<#else>No</#if></td>
							<td>
								<@widget.edit "/admin/property/${property.id}"/>
							</td>
						</tr>
						</#list>
					</tbody>
				</table>
			<@widget.tablePaging properties "/admin/properties" />
		</div><!-- end of box-wrap -->
	</div> <!-- end of box-body -->
</div> <!-- end of content-box -->			
			    
</div><!-- end of page -->
</@layout.main>