<@layout.main template="admin">

<!-- CONTENT BOX - DATATABLE -->
<div class="content-box">
	<div class="box-body">
		<div class="box-header clear">
			<ul class="tabs clear">
				<!--<li><a href="#data-table">JS plugin</a></li>-->
				<!--li><a href="#table">Table only</a></li-->
			</ul>
			
			<h2>MongoDB Collections</h2>
		</div>

		<div class="box-wrap clear">
			<div id="table">
				<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed in porta lectus. Maecenas dignissim enim quis ipsum mattis aliquet. Maecenas id velit et elit gravida bibendum. Duis nec rutrum lorem.</p> 
				<table class="style1">
					<thead>
						<tr>
							<th>Nombre</th>
							<th>Nro documentos</th>
							<th>Acciones</th>
						</tr>
					</thead>
					<tbody>
						<#list collections as col>
						<tr>
							<td>${col.name}</td>
							<td>${col.size}</td>
							<td>
								<@widget.detail "/admin/mongo/show/${col.name}"/>
								<@widget.delete "/admin/mongo/drop/${col.name}"/>
							</td>
						</tr>
						</#list>
					</tbody>
				</table>
			
		</div><!-- end of box-wrap -->
	</div> <!-- end of box-body -->
</div> <!-- end of content-box -->			
			    
</div><!-- end of page -->
</ul>
</@layout.main>